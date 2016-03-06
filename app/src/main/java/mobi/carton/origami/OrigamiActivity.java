package mobi.carton.origami;

import android.os.Bundle;

import mobi.carton.R;
import mobi.carton.library.CartonActivity;


public class OrigamiActivity extends CartonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_origami);
    }
}
