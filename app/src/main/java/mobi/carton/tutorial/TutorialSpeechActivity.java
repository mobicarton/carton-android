package mobi.carton.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;


public class TutorialSpeechActivity extends CartonActivity implements
        HeadRecognition.OnHeadGestureListener,
        ContinuousSpeechRecognition.OnTextListener,
        CustomViewPager.OnScrollListener {


    private CustomViewPager mViewPager;

    private HeadRecognition mHeadRecognition;

    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial_sr);

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

        mContinuousSpeechRecognition = new ContinuousSpeechRecognition(this);
        mContinuousSpeechRecognition.setOnTextListener(this);
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


    @Override
    public void onNod(int direction) {
        actionDirection(direction);
    }


    @Override
    public void onScroll(int direction) {
        actionDirection(direction);
    }


    private void actionDirection(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }


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
            }
        }
    }


    @Override
    public void onError(int error) {

    }
}