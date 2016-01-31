package fgj16.soundhunter;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends BaseActivity {

    /* Create activity */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_setup);

        /* Install listener on start button */
        final Button button = (Button) findViewById (R.id.button_start);
        button.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {

                    /* Get team names */
                    EditText e1 = (EditText) findViewById (R.id.edit_team1);
                    String t1 = e1.getText ().toString ().replace (" ", "+");

                    EditText e2 = (EditText) findViewById (R.id.edit_team2);
                    String t2 = e2.getText ().toString ().replace (" ", "+");

                    EditText e3 = (EditText) findViewById (R.id.edit_team3);
                    String t3 = e3.getText ().toString ().replace (" ", "+");

                    EditText e4 = (EditText) findViewById (R.id.edit_team4);
                    String t4 = e4.getText ().toString ().replace (" ", "+");

                    /* Send team names to server */
                    MessageHandler.send ("team 1 " + t1);
                    MessageHandler.send ("team 2 " + t2);
                    MessageHandler.send ("team 3 " + t3);
                    MessageHandler.send ("team 4 " + t4);

                    /* Start game */
                    MessageHandler.send ("start");

                } catch (Exception e) {

                    /* FIXME: */

                }
            }
        });

        /* Prevent screen saver from activating */
        getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Get teams */
        Team[] teams = MessageHandler.getTeams ();

        /* Show team names on screen */
        final EditText t1 = (EditText) findViewById (R.id.edit_team1);
        t1.setText (teams[1].getName ());

        final EditText t2 = (EditText) findViewById (R.id.edit_team2);
        t2.setText (teams[2].getName ());

        final EditText t3 = (EditText) findViewById (R.id.edit_team3);
        t3.setText (teams[3].getName ());

        final EditText t4 = (EditText) findViewById (R.id.edit_team4);
        t4.setText (teams[4].getName ());

        /* Set current activity */
        MessageHandler.setActivity (this);

        /* Play background music */
        MessageHandler.play (R.raw.aloitusruutu, true);
    }
}
