package mobi.carton.origami;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;


public class OrigamiStepsActivity extends CartonActivity
        implements HeadRecognition.OnHeadGestureListener {


    public final static String EXTRA_NAME = "extra_name";
    public final static String EXTRA_NB_STEPS = "extra_nb_steps";


    private CustomViewPager mViewPager;
    private int mNbSteps;

    private HeadRecognition mHeadRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_origami_steps);

        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);

        TextView textViewName = (TextView) findViewById(R.id.textView_origamiName);
        textViewName.setText(name);

        List<Fragment> fragments = new ArrayList<>();

        mNbSteps = intent.getIntExtra(EXTRA_NB_STEPS, 1);
        int resourceId;
        for (int i = 1; i <= mNbSteps; i++) {
            resourceId = getResources().getIdentifier(name.toLowerCase().concat("_step_").concat(Integer.toString(i)), "drawable", getPackageName());
            fragments.add(StepFragment.newInstance(i, resourceId));
        }

        fragments.add(StepFragment.newInstance(0, getResources().getIdentifier(name.toLowerCase().concat("_finished"), "drawable", getPackageName())));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager_OrigamiSteps);
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
                if (mViewPager.getCurrentItem() == mNbSteps)
                    onBackPressed();
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }
}