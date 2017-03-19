package mobi.carton;


import android.os.Bundle;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.TouchView;
import mobi.carton.utils.PrefUtils;

public class CalibrateActivity extends CartonActivity
        implements
        TouchView.OnFingerTapListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calibrate);
        TouchView touchView = (TouchView) findViewById(R.id.touchView);
        touchView.setOnFingerTapListener(this);
    }


    @Override
    public void onTap(boolean isLong) {
        if (isLong) {
            PrefUtils.setCalibrationDone(this, true);
            onBackPressed();
        }
    }
}
