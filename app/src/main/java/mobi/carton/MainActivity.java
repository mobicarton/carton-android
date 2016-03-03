package mobi.carton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;

public class MainActivity extends CartonActivity
        implements HeadRecognition.OnHeadGestureListener {


    private HeadRecognition mHeadRecognition;

    private CustomViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));

        PagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager);
        mViewPager.setAdapter(pagerAdapter);


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
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                mViewPager.nextPage();
                break;
            case HeadRecognition.NOD_UP:
                mViewPager.previousPage();
                break;
        }
    }
}
