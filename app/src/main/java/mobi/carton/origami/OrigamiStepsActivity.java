package mobi.carton.origami;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import mobi.carton.R;
import mobi.carton.library.CartonActivity;


public class OrigamiStepsActivity extends CartonActivity{


    public final static String EXTRA_NAME = "extra_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_origami_steps);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);

        TextView textViewName = (TextView) findViewById(R.id.textView_origamiName);
        textViewName.setText(name);
    }
}