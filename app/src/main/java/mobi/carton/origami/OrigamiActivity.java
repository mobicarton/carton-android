package mobi.carton.origami;

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


public class OrigamiActivity extends CartonActivity
        implements HeadRecognition.OnHeadGestureListener {


    private ArrayList<Origami> mOrigamis;
    private CustomViewPager mViewPager;

    private HeadRecognition mHeadRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_origami);

        List<Fragment> fragments = new ArrayList<>();

        mOrigamis = getListOrigami();
        for (Origami origami : mOrigamis) {
            fragments.add(OrigamiFragment.newInstance(origami));
        }

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager_Origami);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(40 * scale + 0.5f));

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


    private ArrayList<Origami> getListOrigami() {
        ArrayList<Origami> origamis = new ArrayList<>();

        origamis.add(new Origami("Cranevar", "Andrew Hudson", 26));
        origamis.add(new Origami("Frog", "Andrew Hudson", 17));
        origamis.add(new Origami("Lily", "Tavin", 25));
        origamis.add(new Origami("Mouse", "Tavin", 14));
        origamis.add(new Origami("Tulip", "Andrew Hudson", 23));

        return origamis;
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
                Intent intent = new Intent(this, OrigamiStepsActivity.class);
                intent.putExtra(OrigamiStepsActivity.EXTRA_NAME, mOrigamis.get(mViewPager.getCurrentItem()).getName());
                startActivity(intent);
                break;
            case HeadRecognition.NOD_UP:
                break;
        }
    }
}
