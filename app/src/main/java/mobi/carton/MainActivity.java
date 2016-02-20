package mobi.carton;

import android.os.Bundle;

import mobi.carton.library.CartonActivity;

public class MainActivity extends CartonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
