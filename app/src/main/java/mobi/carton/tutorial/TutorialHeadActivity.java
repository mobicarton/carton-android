package mobi.carton.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;


/**
 * Activity of the tutorial to handle the part related to Head gesture Recognition
 */
public class TutorialHeadActivity extends CartonActivity implements
        HeadRecognition.OnHeadGestureListener,
        CustomViewPager.OnScrollListener {


    private CustomViewPager mViewPager;

    private HeadRecognition mHeadRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TutorialHeadFragment.newInstance(getString(R.string.tutorial_step_3), true));
        fragments.add(TutorialHeadFragment.newInstance(getString(R.string.tutorial_step_4), false));

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

        Utils.setTutorialDone(this, true);
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
            case HeadRecognition.NOD_DOWN:
                Intent intent = new Intent(this, TutorialSpeechActivity.class);
                startActivity(intent);
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }
}