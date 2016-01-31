package fgj16.soundhunter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Keep network connection fresh.
 */
public class KeepAlive extends Handler {
   @Override
   public void handleMessage (Message msg) {
       /* Schedule next message after 60 seconds */
       sendEmptyMessageDelayed (0, 60*1000);

       /* Send tick message to server to let it know the client is alive */
       try {
           MessageHandler.send ("tick");
       }
       catch (Exception e) {
           /* FIXME: */
           Log.d ("KeepAlive", "Error", e);
       }
    }
}
