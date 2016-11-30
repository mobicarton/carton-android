package mobi.carton.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MainActivity;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;


/**
 * Activity of the tutorial to handle the part related to Speech Recognition
 */
public class TutorialSpeechActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        CustomViewPager.OnScrollListener,
        ContinuousSpeechRecognition.OnTextListener,
        ContinuousSpeechRecognition.OnRmsListener {


    private CustomViewPager mViewPager;

    /**
     * Use built-in Head Gesture Recognition API to provide multimodal interactions
     */
    private HeadRecognition mHeadRecognition;


    /**
     * ProgressBar which show the volume of the listening sounds
     */
    private ProgressBar mProgressBarRms;
    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial_speech);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TutorialFragment.newInstance(getString(R.string.tutorial_step_5), 0));
        fragments.add(TutorialFragment.newInstance(getString(R.string.tutorial_step_6), 0));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager_tutorial);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(40 * scale + 0.5f));

        mViewPager.setOnScrollListener(this);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);

        mProgressBarRms = (ProgressBar) findViewById(R.id.progressbar_rms);

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(this);
        mContinuousSpeechRecognition.setOnTextListener(this);
        mContinuousSpeechRecognition.setOnRmsListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
        mContinuousSpeechRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
        mContinuousSpeechRecognition.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContinuousSpeechRecognition.destroy();
    }


    private void actionDirection(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
            case HeadRecognition.NOD_DOWN:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CartonActivity.EXTRA_NO_LAUNCHER, true);
                startActivity(intent);
                break;
        }
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onTilt(int direction) {
        switch (direction) {
            case HeadRecognition.TILT_RIGHT:
                mViewPager.nextPage();
                break;
            case HeadRecognition.TILT_LEFT:
                mViewPager.previousPage();
                break;
        }
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        actionDirection(direction);
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onShake() {

    }


    // CustomViewPager.OnScrollListener
    @Override
    public void onScroll(int direction) {
        actionDirection(direction);
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onTextMatched(ArrayList<String> matchedText) {
        Log.d("onTextMatched", matchedText.toString());
        for (String s : matchedText) {
            switch (s) {
                case "next":
                    onTilt(HeadRecognition.TILT_RIGHT);
                    return;
                case "previous":
                    onTilt(HeadRecognition.TILT_LEFT);
                    return;
                case "cancel":
                    actionDirection(HeadRecognition.NOD_UP);
                    return;
                case "ok":
                    actionDirection(HeadRecognition.NOD_DOWN);
                    return;
            }
        }
    }


    // ContinuousSpeechRecognition.OnTextListener
    @Override
    public void onError(int error) {

    }


    // ContinuousSpeechRecognition.OnRmsListener
    @Override
    public void onRmsChanged(float rms) {
        mProgressBarRms.setProgress((int) (rms * 10));
    }
}