package fgj16.soundhunter;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

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
                    MessageHandler.play (R.raw.klikkaus2);
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
                    MessageHandler.play (R.raw.klikkaus2);
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
                    MessageHandler.play (R.raw.klikkaus2);
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
                    MessageHandler.play (R.raw.klikkaus2);
                    MessageHandler.send ("click 4");
                }
                catch (Exception e) {
                    /* FIXME: */
                }
            }
        });

        /* Set current activity */
        MessageHandler.setActivity (this);
    }
}
