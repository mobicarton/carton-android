package mobi.carton.tutorial;


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

public class TutorialActivity extends CartonActivity implements
        HeadRecognition.OnHeadGestureListener,
        CustomViewPager.OnScrollListener {


    private CustomViewPager mViewPager;

    private HeadRecognition mHeadRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TutorialFragment.newInstance("A simple tutorial to explain how interact. Make a quick tilt with your head on the right to navigate", 0));
        fragments.add(TutorialFragment.newInstance("You can tilt on the other side too. Actually you can even nod your head down to navigate forward. You can also continue to the right :)", 0));
        fragments.add(TutorialFragment.newInstance("Almost done. In fact, you can touch the screen with your finger easily, and swipe left, right, forward or backward, just try.", 0));
        fragments.add(TutorialFragment.newInstance("Finish! Nothing to do here. You can go to the menu with head or finger interactions you've just learned. Congrats!", 0));

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
                //Intent intent = new Intent(this, TutorialSpeechRecognitionActivity.class);
                //startActivity(intent);
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }
}