package fgj16.soundhunter;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Set;

/**
 * Created by tronkko on 30.1.2016.
 */
public class MessageHandler extends Handler {
    /* Pointer to sole message handler */
    static private MessageHandler mHandler = null;

    /* Asynchronous thread */
    private AsyncClient mThread = null;

    /* Current activity */
    static private AppCompatActivity mActivity = null;

    /* Current media player */
    static MediaPlayer mp;

    /* Keep network connection alive */
    private KeepAlive mKeepAlive;

    /* Teams in current game */
    private Team[] mTeams;


    /* Create message handler */
    MessageHandler () {
        /* Start background thread */
        mThread = new AsyncClient ();
        mThread.execute ("");

        /* Create timer to keep the network connection fresh */
        mKeepAlive = new KeepAlive ();
        mKeepAlive.sendEmptyMessageDelayed (0, 60 * 1000);

        /* Reset teams */
        mTeams = new Team[5];
        mTeams[1] = new Team ("Hiisi");
        mTeams[2] = new Team ("Joukahainen");
        mTeams[3] = new Team ("Ukko");
        mTeams[4] = new Team ("Sampo");
    }

    /* Reconnect to server */
    static public void resetConnection () {
        if (mHandler != null) {
            mHandler._resetConnection ();
        }
    }
    public void _resetConnection () {
        Log.d ("MessageHandler", "resetConnection");
        /* Terminate old thread */
        if (mThread != null) {
            try {
                mThread.stop ();
            }
            catch (Exception e) {
                /* FIXME: */
                Log.d ("MessageHandler", "Error", e);
            }
        }

        /* Create new background thread */
        mThread = new AsyncClient ();
        mThread.execute ("");
    }

    /* Play audio file */
    static public void play (int id) {
        MessageHandler.play (id, false);
    }
    static public void play (int id, boolean looping) {
        /* Silence previous clip */
        MessageHandler.silence ();

        /* Create new audio */
        mp = MediaPlayer.create (mActivity, id);

        /* Release audio after it is played through */
        mp.setOnCompletionListener (new MediaPlayer.OnCompletionListener () {
            @Override
            public void onCompletion (MediaPlayer mp) {
                Log.d ("MessageHandler", "oncomplete");
                mp.release ();
                MessageHandler.mp = null;
            }
        });

        /* Loop audio if needed */
        mp.setLooping (looping);

        /* Start playing */
        mp.start ();
    }

    /* Silence audio */
    static public void silence () {
        if (mp != null) {
            mp.stop ();
            mp.release ();
            mp = null;
        }
    }

    /* Receive message from server */
    public void receive (String msg) {
        /* Debug */
        Log.d ("MainThread", "got '" + msg + "'");

        /* Extract command and arguments */
        String[] arr = msg.split (" ");

        /* Parse command */
        if (arr[0].equals ("start")) {

            /* Silence background music from welcome screen */
            MessageHandler.silence ();

            /* Show game screen */
            Intent i = new Intent (mActivity.getBaseContext(), GameActivity.class);
            mActivity.startActivity (i);

            /* Close current activity */
            mActivity.finish ();

        } else if (arr[0].equals ("team")) {

            /* Get team number */
            int teamid = Integer.parseInt (arr[1], 10);

            /* Get team name */
            String name = arr[2].replace ("+", " ");

            /* Create new team */
            mTeams[teamid] = new Team (name);

        } else if (arr[0].equals ("score")) {

            /* Get team number */
            int teamid = Integer.parseInt (arr[1], 10);

            /* Add score */
            mTeams[teamid].addScore ();

        } else if (arr[0].equals ("good")) {

            /* Player made a correct choice */
            MessageHandler.play (R.raw.oikein);

        } else if (arr[0].equals ("boo")) {

            /* 1GPlayer pressed button on non-active client */
            MessageHandler.play (R.raw.vaarin);

        } else if (arr[0].equals ("stop")) {

            /* Silence audio */
            MessageHandler.silence ();

            /* Stop game and show results */
            Intent i = new Intent (mActivity.getBaseContext(), ResultsActivity.class);
            mActivity.startActivity (i);

            /* Close current activity */
            mActivity.finish ();

        } else if (arr[0].equals ("play")) {

            /* Play looping sound */
            int resid;
            int soundid = Integer.parseInt (arr[1], 10);
            switch (soundid) {
                case 0:
                    resid = R.raw.kutsu1;
                    break;

                case 1:
                    resid = R.raw.kutsu2;
                    break;

                case 2:
                    resid = R.raw.kutsu3;
                    break;

                case 3:
                    resid = R.raw.kutsu4;
                    break;

                case 4:
                    resid = R.raw.kutsu1;
                    break;

                case 5:
                    resid = R.raw.kutsu2;
                    break;

                case 6:
                    resid = R.raw.kutsu3;
                    break;

                case 7:
                    resid = R.raw.kutsu4;
                    break;

                case 8:
                    resid = R.raw.kutsu1;
                    break;

                case 9:
                    resid = R.raw.kutsu2;
                    break;

                default:
                    Log.d ("MessageHandler", "Invalid soundid " + soundid);
                    resid = R.raw.kutsu1;
            }
            this.play (resid, true);

        } else if (arr[0].equals ("silence")) {

            /* Silence audio */
            this.silence ();

        } else if (arr[0].equals ("hello")) {

            /* Show team setup */
            Intent i = new Intent (mActivity.getBaseContext(), SetupActivity.class);
            mActivity.startActivity (i);

            /* Close current activity */
            mActivity.finish ();

        } else if (arr[0].equals ("tick")  ||  arr[0].equals ("tack")) {

            /* Keep alive */
            /*NOP*/;

        } else if (arr[0].equals ("error")) {

            /* Show error screen  */
            Intent i = new Intent (mActivity.getBaseContext(), ErrorActivity.class);
            i.putExtra ("msg", msg);
            mActivity.startActivity (i);

            /* Close current activity */
            mActivity.finish ();

        } else {

            /* Invalid command */
            Log.e ("MessageHandler", "Invalid message " + msg);

        }
    }

    /* Handle message from background thread */
    @Override
    public void handleMessage (Message msg) {
        /* Extract command from server */
        String cmd = msg.obj.toString ();

        /* Forward message to receive function */
        this.receive (cmd);
    }

    /* Get instance to sole message handler */
    static public MessageHandler getInstance () {
        /* Create first message handler */
        instantiate ();

        /* Return reference to previously created handler */
        return mHandler;
    }

    /* Ensure that message handler exists */
    static public void instantiate () {
        if (mHandler == null) {
            mHandler = new MessageHandler ();
        }
    }

    /* Set current activity */
    static public void setActivity (AppCompatActivity app) {
        mActivity = app;
    }

    /* Send message to server */
    static public void send (String msg) throws Exception {
        MessageHandler.getInstance ()._send (msg);
    }
    public void _send (String msg) throws Exception {
        if (mThread != null) {

            /* Pass message to background thread */
            mThread.send (msg);

        } else {
            throw new Exception ("Thread not started yet");
        }
    }

    /* Get list of teams */
    static public Team[] getTeams () {
        return MessageHandler.getInstance ()._getTeams ();
    }
    public Team[] _getTeams () {
        return mTeams;
    }

    /* Asynchronous background thread */
    private class AsyncClient extends AsyncTask<String, String, SoundClient> {
        private SoundClient mClient = null;

        @Override
        protected SoundClient doInBackground (String... message) {

            /* Create client */
            mClient = new SoundClient (new SoundClient.OnMessageReceived () {
                @Override
                public void messageReceived (String message) {
                    /* Obtain new message from global message pool */
                    Message msg = mHandler.obtainMessage ();

                    /* Pass command from server alongside message */
                    msg.obj = message;

                    /* Send message to main thread */
                    mHandler.sendMessage (msg);
                }
            });

            /* Start reading messages from server */
            mClient.run ();
            return null;
        }

        /* Send message to client */
        public void send (String msg) throws Exception {
            if (mClient != null) {
                mClient.send (msg);
            } else {
                throw new Exception ("Client not ready");
            }
        }

        public void stop () throws Exception {
            /* Tell SoundClient to exit from main loop */
            mClient.disconnect ();

            /* Close connection */
            mClient.send ("quit");
        }
    }
}
