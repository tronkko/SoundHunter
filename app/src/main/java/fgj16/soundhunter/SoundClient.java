package fgj16.soundhunter;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by tronkko on 30.1.2016.
 */
public class SoundClient {

    /* Host name and port of the SoundHunter server */
    public static final String SERVER_NAME = "192.168.1.101";
    public static final int SERVER_PORT = 8000;

    /* Message handler */
    private OnMessageReceived mListener;

    /* Output buffer */
    private PrintWriter mBufferOut;

    /* Input buffer */
    private BufferedReader mBufferIn;

    /* Whether listener is still active */
    private boolean mDone = false;


    /* Create client */
    public SoundClient (OnMessageReceived listener) {
        mListener = listener;
        mBufferOut = null;
        mBufferIn = null;
        mDone = false;
    }

    /* Send message to server */
    public void send (String msg) throws Exception {
        if (mBufferOut != null) {

            /* Output message to buffer */
            mBufferOut.println (msg);

            /* Flush buffer to server */
            mBufferOut.flush ();

        } else {

            /* Output buffer not created?!?!? */
            throw new Exception ("No output buffer");

        }
    }

    /* Close connection */
    public void disconnect () {
        /* Exit from loop */
        mDone = true;

        /* Close output buffer */
        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close ();
            mBufferOut = null;
        }

        /* Close input buffer */
        mBufferIn = null;

        /* Detach listener */
        mListener = null;
    }

    /* Receive messages from server */
    public void run () {
        try {

            /* Resolve server's IP address */
            InetAddress addr = InetAddress.getByName (SERVER_NAME);

            /* Connect to server */
            Socket socket = new Socket (addr, SERVER_PORT);

            /* Allow inactivity for 60 minutes before breaking out */
            socket.setSoTimeout (60*60*1000);

            try {

                /* Create output buffer */
                mBufferOut = new PrintWriter(
                        new BufferedWriter (new OutputStreamWriter (socket.getOutputStream ())));

                /* Create input buffer */
                mBufferIn = new BufferedReader(
                        new InputStreamReader (socket.getInputStream ()));

                /* Listen to messages until disconnect */
                mDone = false;
                while (!mDone) {

                    /* Read message from server */
                    String msg = mBufferIn.readLine ();

                    /* Pass message to async thread */
                    if (msg != null  &&  mListener != null) {
                        mListener.messageReceived(msg);
                    }

                }

            }
            catch (Exception e) {

                /* Timeout or server terminated unexpectedly */
                Log.e ("TcpClient", "Error", e);

            }
            finally {

                /* Close socket due to error */
                socket.close ();

            }

        }
        catch (Exception e) {

            /* Cannot resolve server address? */
            Log.e ("TcpClient", "Error", e);

        }
    }

    /* Declare interface for listener */
    public interface OnMessageReceived {
        public void messageReceived (String msg);
    }
}
