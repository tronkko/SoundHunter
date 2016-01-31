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

/**
 * Gameplay
 */
public class GameActivity extends BaseActivity {

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

        /* Hide score text */
        TextView t = (TextView) findViewById(R.id.text_score);
        t.setVisibility (View.GONE);

        /* Set current activity */
        MessageHandler.setActivity (this);
    }

    /* Show scoring team on screen */
    public void score (int teamid) {
        /* Find hidden text field */
        TextView t = (TextView) findViewById(R.id.text_score);

        /* Get name of scoring team */
        Team[] teams = MessageHandler.getTeams ();
        String name = teams[teamid].getName ();

        /* Write team name to hidden text view */
        t.setText (name + " scores");

        /* Make the text view visible */
        t.setVisibility (View.VISIBLE);

        /* Animate text */
        Animation anim = AnimationUtils.loadAnimation (this, R.anim.flash);
        anim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart (Animation arg0) {
            }
            @Override
            public void onAnimationRepeat (Animation arg0) {
            }
            @Override
            public void onAnimationEnd (Animation arg0) {
                /* Hide text view when animation completes */
                TextView t = (TextView) findViewById(R.id.text_score);
                t.setVisibility (View.GONE);
            }
        });
        t.clearAnimation ();
        t.startAnimation (anim);

        /* Find team button */
        ImageButton btn = null;
        switch (teamid) {
            case 1:
                btn = (ImageButton) findViewById (R.id.button_1);
                break;

            case 2:
                btn = (ImageButton) findViewById (R.id.button_2);
                break;

            case 3:
                btn = (ImageButton) findViewById (R.id.button_3);
                break;

            case 4:
                btn = (ImageButton) findViewById (R.id.button_4);
                break;
        }

        /* Animate team button */
        if (btn != null) {
            Animation a = AnimationUtils.loadAnimation (GameActivity.this, R.anim.vanish);
            btn.startAnimation (a);
        }
    }
}
