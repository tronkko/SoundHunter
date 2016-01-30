package fgj16.soundhunter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_connect);

        /* Create message handler */
        MessageHandler.instantiate ();
        MessageHandler.setActivity (this);
    }
}
