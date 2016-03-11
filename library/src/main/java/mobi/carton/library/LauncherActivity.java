package mobi.carton.library;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class LauncherActivity extends Activity
        implements
        HeadRecognition.OnHeadTrackingListener {


    public static final int CODE_LAUNCHER = 32767;

    public static final String EXTRA_WITHOUT = "extra_without";


    private HeadRecognition mHeadRecognition;


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


    public void clickVisit(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(getString(R.string.carton_website)));
        startActivity(intent);
    }


    public void clickWithout(View v) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WITHOUT, true);
        setResult(CODE_LAUNCHER, intent);
        finish();
    }


    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        if ((pitch < 10 && pitch > -10) && (roll < 10 && roll > - 10)){
            Intent intent = new Intent();
            intent.putExtra(EXTRA_WITHOUT, false);
            setResult(CODE_LAUNCHER, intent);
            finish();
        }
    }
}