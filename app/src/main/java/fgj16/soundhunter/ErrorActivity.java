package fgj16.soundhunter;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_error);

        /* Get error message */
        Intent i = getIntent ();
        String error = i.getStringExtra ("msg");

        /* Save error message to screen */
        TextView v = (TextView) findViewById (R.id.text_error);
        v.setText (error);

        /* Silence audio */
        MessageHandler.silence ();

        /* Install listener on OK button */
        final Button button1 = (Button) findViewById (R.id.button_error);
        button1.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {

                    /* Show connect screen */
                    Intent i = new Intent (ErrorActivity.this, ConnectActivity.class);
                    startActivity (i);

                    /* Re-connect to server */
                    MessageHandler.resetConnection ();

                } catch (Exception e) {
                    /* FIXME: */
                }
            }
        });
    }
}
