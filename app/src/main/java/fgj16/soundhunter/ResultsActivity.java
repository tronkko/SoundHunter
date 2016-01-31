package fgj16.soundhunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Game over screen
 */
public class ResultsActivity extends BaseActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_results);

        /* Install listener on new game button */
        final Button button = (Button) findViewById (R.id.button_new);
        button.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                /* Show team setup screen */
                Intent i = new Intent (ResultsActivity.this, SetupActivity.class);
                startActivity (i);
            }
        });

        /* Set current activity */
        MessageHandler.setActivity (this);

        /* Play end tune */
        MessageHandler.play (R.raw.tulosruutu, true);

        /* Find game over text */
        TextView t = (TextView) findViewById(R.id.text_over);

        /* Get name and logo of winning team */
        Team[] teams = MessageHandler.getTeams ();
        int max = -1;
        int logo = 0;
        String name = "";
        for (int i = 1; i <= 4; i++) {
            Log.d ("ResultsActivity", teams[i].getName () + " score " + teams[i].getScore ());
            if (teams[i].getScore () > max) {
                name = teams[i].getName ();
                max = teams[i].getScore ();
                switch (i) {
                    case 1:
                        logo = R.drawable.hiisi;
                        break;

                    case 2:
                        logo = R.drawable.joukahainen;
                        break;

                    case 3:
                        logo = R.drawable.ukko;
                        break;

                    case 4:
                        logo = R.drawable.sampo;
                        break;
                }
            }
        }

        /* Write team name to text view */
        t.setText (name.toUpperCase () + " WINS");

        /* Show team logo as a background */
        if (logo != 0) {
            ImageView iv = (ImageView) findViewById (R.id.image_over);
            iv.setImageResource (logo);
        }

    }
}
