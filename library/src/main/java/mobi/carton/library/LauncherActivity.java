package mobi.carton.library;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * A default launcher to provide information: how to put mobile phone into a CARTON
 * device, a link to the official website and auto-launch when the phone is horizontal
 */
public class LauncherActivity extends Activity
        implements
        HeadRecognition.OnHeadTrackingListener {


    /**
     * Code to check where the result come from
     */
    public static final int CODE_LAUNCHER = 32767;

    /**
     * return true if the app is launch without the CARTON device
     */
    public static final String EXTRA_WITHOUT = "extra_without";


    /**
     * Use the built-in Head Gesture Recognition API to track the roll angle of the mobile
     */
    private HeadRecognition mHeadRecognition;


    /*
    LIFECYCLE
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadTrackingListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }


    /**
     * When user click on the link to the official website
     * then launch implicit intent
     * @param v The view that was clicked.
     */
    public void clickVisit(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.carton_website)));
        startActivity(intent);
    }


    /**
     * When user click on the button to launch app without using CARTON device
     * then set extra to return app without using any CARTON device and remove the launcher
     * @param v The view that was clicked.
     */
    public void clickWithout(View v) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WITHOUT, true);
        setResult(RESULT_OK, intent);
        finish();
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadTrackingListener
    /**
     * Check if the mobile phone is horizontal then it consider CARTON is used,
     * therefore the launcher is close
     */
    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        // if roll and pitch are 0 (+/- 10°)
        if ((pitch < 10 && pitch > -10) && (roll < 10 && roll > - 10)){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_WITHOUT, false);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}