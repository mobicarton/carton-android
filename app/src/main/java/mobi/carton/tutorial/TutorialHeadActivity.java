package mobi.carton.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonViewPager;
import mobi.carton.library.HeadRecognition;
import mobi.carton.utils.PrefUtils;


/**
 * Activity of the tutorial to handle the part related to Head gesture Recognition
 */
public class TutorialHeadActivity extends CartonActivity implements
        HeadRecognition.OnHeadGestureListener,
        CartonViewPager.OnScrollListener {


    private CartonViewPager mViewPager;

    /**
     * Use built-in Head Gesture Recognition API to provide multimodal interactions
     */
    private HeadRecognition mHeadRecognition;


    /*
    LIFECYCLE
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TutorialHeadFragment.newInstance(getString(R.string.tutorial_step_3), true));
        fragments.add(TutorialHeadFragment.newInstance(getString(R.string.tutorial_step_4), false));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CartonViewPager) super.findViewById(R.id.viewPager_tutorial);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(40 * scale + 0.5f));

        mViewPager.setOnScrollListener(this);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);

        PrefUtils.setTutorialDone(this, true);
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


    private void actionDirection(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                /*
                Intent intent = new Intent(this, TutorialSpeechActivity.class);
                startActivity(intent);
                */
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
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
}