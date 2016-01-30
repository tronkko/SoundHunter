package fgj16.soundhunter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResultsActivity extends AppCompatActivity {

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
    }
}
