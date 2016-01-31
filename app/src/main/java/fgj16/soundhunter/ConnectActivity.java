package fgj16.soundhunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Connect to server
 */
public class ConnectActivity extends BaseActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_connect);

        /* Create message handler */
        MessageHandler.instantiate ();
        MessageHandler.setActivity (this);

        /* Install listener on connect button */
        final Button btn = (Button) findViewById (R.id.button_connect);
        btn.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {

                    /* Reset connection */
                    MessageHandler.resetConnection ();

                } catch (Exception e) {
                    /* FIXME: */
                }
            }
        });
    }

}
