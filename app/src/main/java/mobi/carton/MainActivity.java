package mobi.carton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;

public class MainActivity extends CartonActivity {


    private HeadRecognition mHeadRecognition;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));

        mPagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (ViewPager) super.findViewById(R.id.viewPager);
        mViewPager.setAdapter(this.mPagerAdapter);


        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(new HeadRecognition.OnHeadGestureListener() {
            @Override
            public void onTilt(int direction) {
                if (direction == 0)
                    if (mViewPager.getCurrentItem() < (mPagerAdapter.getCount()-1))
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                if (direction == 1)
                    if (mViewPager.getCurrentItem() > 0)
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
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
}
