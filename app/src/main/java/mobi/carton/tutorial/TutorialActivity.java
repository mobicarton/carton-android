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
 * First Activity of the tutorial to handle the part related to Finger gesture
 */
public class TutorialActivity extends CartonActivity
        implements
        CustomViewPager.OnScrollListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tutorial);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TutorialFragment.newInstance(getString(R.string.tutorial_step_1), 0));
        fragments.add(TutorialFragment.newInstance(getString(R.string.tutorial_step_2), 0));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        CustomViewPager viewPager = (CustomViewPager) super.findViewById(R.id.viewPager_tutorial);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        viewPager.setPageMargin((int) -(40 * scale + 0.5f));

        viewPager.setOnScrollListener(this);

        Utils.setTutorialDone(this, true);
    }


    @Override
    public void onScroll(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                Intent intent = new Intent(this, TutorialHeadActivity.class);
                startActivity(intent);
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }
}