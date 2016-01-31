var net = require('net');

/* Listen on port */
var port = 8000;

/* List of active clients by id */
var clients = {};
var clientid = 0;

/* List of teams */
var teams = {};

/* Identifier of currently flashing client */
var activeclient = 0;

/* Number of round in game */
var gameround = 0;

/* Send message to all clients */
function broadcast (msg) {
    for (var id in clients) {
        clients[id].send (msg);
    }
}

/* Construct team */
function Team (name, logo) {
    this.name = name;
    this.logo = logo;
    this.score = 0;
}
Team.prototype = Object.create (Object.prototype);
Team.prototype.constructor = Team;

/* Construct client object */
function Client (id, socket) {
    /* Initialize member variables */
    this.id = id;
    this.socket = socket;
    this.buffer = '';
    this.port = socket.remotePort;
    this.address = socket.remoteAddress;
    this.time = new Date ().getTime ();

    /* Debug */
    console.log ('Connect ' + this.address + ' port ' + this.port);

    /* Acknowledge client */
    this.send ('hello');
}
Client.prototype = Object.create (Object.prototype);
Client.prototype.constructor = Client;

/* Receive data from client */
Client.prototype.receive = function (data) {
    /* Split data into commands separated by line-feeds */
    var i = 0;
    var n = data.length;
    while (i < n) {
        /* Find next line feed */
        var pos = data.indexOf ('\n', i);
        if (pos >= 0) {

            /* Extract string up to next line-feed */
            var row = data.substring (i, pos);

            /* Append command to buffer and execute */
            this.execute (this.buffer + row);

            /* Reset buffer and prepare for next command */
            this.buffer = '';
            i = pos + 1;

        } else {

            /* Extract remaining string */
            var row = data.substring (i);

            /* Append string to buffer */
            this.buffer += row;

            /* Skip rest of the line */
            i = n;

        }
    }
};

/* Execute single command */
Client.prototype.execute = function (cmd) {
    /* Debug */
    console.log ('< ' + cmd.trim ()
            + ' from ' + this.address + ' port ' + this.port);

    /* Extract command and arguments */
    var arg = cmd.trim ().split (' ');

    /* Remember time of last message from client */
    this.time = new Date ().getTime ();

    /* Execute command */
    try {
        switch (arg[0]) {
        case '':
            /* No command */
            /*NOP*/;
            break;

        case 'hello':
            /* Test connection */
            this.send ('hello');
            break;

        case 'tick':
            /* Keep alive */
            this.send ('tack');
            break;

        case 'tack':
            /* Keep alive */
            /*NOP*/;
            break;

        case 'start':
            /* Start new game */
            start ();
            break;

        case 'stop':
            /* End game */
            broadcast ('stop');
            break;

        case 'q':
        case 'quit':
            /* Close connection */
            this.socket.end ();
            break;

        case 'click':
            /* Click number */
            var button;
            if (arg.length > 1) {

                /* Is the client active one */
                if (this.id == activeclient) {

                    /* Yes, get button number */
                    button = parseInt (arg[1], 10) || 0;
                    if (1 <= button  &&  button <= 4) {

                        /* Let client know the answer was correct */
                        this.send ('good ' + button);

                        /* Score team 1-4 */
                        teams[button].score++;
                        broadcast ('score ' + button);

                        /* Determine whether to end the game */
                        var done;
                        gameround++;
                        if (gameround < 5) {
                            /* Less than 5 rounds played => continue */
                            done = false;
                        } else {
                            /* 
                             * 5 or more rounds played.  Inspect teams and
                             * play additional round if two teams are even.
                             */
                            var max = 0;
                            done = true;
                            for (var id in teams) {
                                if (teams[id].score > max) {
                                    /* New maximum score => may end */
                                    max = teams[id].score;
                                    done = true;
                                } else if (teams[id].score == max) {
                                    /* Even score => must continue */
                                    done = false;
                                }
                            }
                        }

                        /* Next round */
                        if (!done) {
                            /* Choose next active team */
                            ring ();
                        } else {
                            /* Game ends */
                            broadcast ('stop');
                            activeclient = 0;
                        }

                    } else {
                        throw new Error ("Invalid click " + arg[1]);
                    }

                } else {

                    /* Click from non-active client => play boo */
                    this.send ('boo');

                }

            } else {
                throw new Error ("Missing click");
            }
            break;

        case 'team':
            /* Get team number */
            var id = parseInt (arg[1]) || 0;
            if (id < 1  ||  id > 4) {
                throw new Error ("Invalid team number " + arg[1]);
            }

            /* Get team name */
            var name;
            if (arg.length > 2  &&  arg[2].trim () != "") {
                /* Plus sign marks space in team name */
                name = arg[2].replace ('+', ' ');
            } else {
                name = "Team " + id;
            }

            /* Get logo */
            var logo;
            if (arg.length > 3) {
                logo = arg[3];
            } else {
                logo = "default";
            }

            /* Set up team */
            teams[id] = new Team (name, logo);
            break;

        case 'debug':
            /* Send debug message to server log */
            console.log (cmd.trim ()
                + ' from ' + this.address + ' port ' + this.port);
            break;

        default:
            /* Invalid command */
            console.log ('Invalid client command ' + cmd.trim ()
                + ' from ' + this.address + ' port ' + this.port);
        }
    }
    catch (e) {
        if (e instanceof Error) {
            console.log (e.message
                + ' while processing ' + cmd.trim ()
                + ' from ' + this.address + ' port ' + this.port);
        } else {
            console.log (e);
        }
    }
};

/* Start game */
function start () {
    gameround = 0;
    activeclient = 0;

    /* Reset team score (just in case) */
    for (var id in teams) {
        teams[id].score = 0;
    }

    /* Broadcast teams to clients */
    for (var id in teams) {
        broadcast ('team ' 
                + id + ' ' 
                + teams[id].name + ' '
                + teams[id].logo);
    }

    /* Start game in each client */
    broadcast ('start');

    /* Play random client */
    ring ();
}

/* Ring random client */
function ring () {
    /* Get list of active clients */
    var arr = [];
    for (var id in clients) {
        arr[arr.length] = id;
    }

    /* Any clients left? */
    if (arr.length > 0) {

        /* Choose random client but avoid choosing the same client */
        var done = false;
        var count = 100;
        var i = 0;
        while (count-- > 0) {
            i = Math.floor (Math.random () * arr.length);
            if (activeclient != arr[i]) {
                break;
            }
        }
        activeclient = arr[i];

        /* Choose random sound */
        var sound = Math.floor (Math.random () * 10);

        /* Send play command to chosen client */
        clients[activeclient].send ('play ' + sound);

    } else {

        /* End game as no clients exist */
        activeclient = 0;
        gameround = 0;

    }
}

/* Send data to client */
Client.prototype.send = function (data) {
    console.log ('> ' + data
        + ' to ' + this.address + ' port ' + this.port);
    this.socket.write (data + '\n');
};

/* Close connection */
Client.prototype.disconnect = function (data) {
    /* Debug */
    console.log ('Disconnect ' + this.address + ' port ' + this.port);
};

/* Create server */
net.createServer (function (socket) {

    /* Generate unique identifier for connection */
    var id = ++clientid;

    /* Create client object */
    var client = new Client (id, socket);

    /* Client sends data */
    socket.on ('data', function (data) {
        client.receive (data.toString ());
    });

    /* Client closes connection */
    socket.on ('end', function () {
        client.disconnect ();
        delete clients[id];
    });

    /* Remember client for broadcasting */
    clients[id] = client;

}).listen (port);
console.log ('Listening on port ' + port);
 
/* Read debug commands from keyboard */
var stdin = process.openStdin ();
stdin.on ('data', function (line) {
    /* Remove excess line-feed */
    var cmd = line.toString ().trim ();

    /* Split command from spaces */
    var arg = cmd.split (' ');

    /* Parse command */
    switch (arg[0]) {
    case 'list':
        /* List clients */
        for (var id in clients) {
            console.log (id + '\t' + clients[id].address);
        }
        break;

    case 'start':
        /* Start new game */
        start ();
        break;

    case 'stop':
    case 'silence':
        /* Broadcast commands to all clients */
        broadcast (cmd);
        break;

    case 'q':
    case 'quit':
        /* Stop server */
        process.exit ();
        break;

    case '':
        /* Empty command */
        /*NOP*/;
        break;

    default:
        /* Send client */
        if (typeof clients[arg[0]] != 'undefined') {
            clients[arg[0]].send (arg.slice (1).join (' '));
        } else {
            /* Invalid server command */
            console.log ('Invalid server command ' + cmd);
        }
    }
});
console.log ('Reading commands from keyboard');

/* Check connections once a minute */
setInterval (function () {
    var skip = false;

    /* Go through clients and remove stale connections */
    var now = new Date ().getTime ();
    for (var id in clients) {

        /* Has client send anything for a while? */
        if (clients[id].time < now -100*1000) {
            /* No, connection is stale */
            if (id == activeclient) {
                /* If active client is stale then choose new client */
                skip = true;
            }

            /* Invalidate client */
            console.log ("Purging stale client");
            clients[id].disconnect ();
            delete clients[id];
        }
        
    }

    /* Make another client active if the currently active client was stale */
    if (skip) {
        console.log ("Choosing new active client");
        ring ();
    }
}, 60 * 1000);

