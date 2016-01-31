package fgj16.soundhunter;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_game);

        /* Prevent screen saver from activating */
        getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Install listeners */
        final ImageButton button1 = (ImageButton) findViewById (R.id.button_1);
        button1.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {
                    MessageHandler.send ("click 1");
                    Animation shake = AnimationUtils.loadAnimation (GameActivity.this, R.anim.shake);
                    v.startAnimation (shake);
                } catch (Exception e) {
                    /* FIXME: */
                }
            }
        });

        final ImageButton button2 = (ImageButton) findViewById (R.id.button_2);
        button2.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {
                    MessageHandler.send ("click 2");
                    Animation shake = AnimationUtils.loadAnimation (GameActivity.this, R.anim.shake);
                    v.startAnimation (shake);
                } catch (Exception e) {
                    /* FIXME: */
                }
            }
        });

        final ImageButton button3 = (ImageButton) findViewById (R.id.button_3);
        button3.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {
                    MessageHandler.send ("click 3");
                    Animation shake = AnimationUtils.loadAnimation (GameActivity.this, R.anim.shake);
                    v.startAnimation (shake);
                } catch (Exception e) {
                    /* FIXME: */
                }
            }
        });

        final ImageButton button4 = (ImageButton) findViewById (R.id.button_4);
        button4.setOnClickListener (new View.OnClickListener () {
            public void onClick (View v) {
                try {
                    MessageHandler.send ("click 4");
                    Animation shake = AnimationUtils.loadAnimation (GameActivity.this, R.anim.shake);
                    v.startAnimation (shake);
                }
                catch (Exception e) {
                    /* FIXME: */
                }
            }
        });

        /* Get teams */
        Team[] teams = MessageHandler.getTeams ();

        /* Show team names on screen */
        final TextView tv1 = (TextView) findViewById (R.id.text_team1);
        tv1.setText (teams[1].getName ());

        final TextView tv2 = (TextView) findViewById (R.id.text_team2);
        tv2.setText (teams[2].getName ());

        final TextView tv3 = (TextView) findViewById (R.id.text_team3);
        tv3.setText (teams[3].getName ());

        final TextView tv4 = (TextView) findViewById (R.id.text_team4);
        tv4.setText (teams[4].getName ());

        /* Set current activity */
        MessageHandler.setActivity (this);
    }
}
