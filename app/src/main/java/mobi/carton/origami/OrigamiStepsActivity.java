package mobi.carton.origami;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.csr.ContinuousSpeechRecognition;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonViewPager;
import mobi.carton.library.HeadRecognition;


/**
 * Activity to handle each step of the origami
 */
public class OrigamiStepsActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        ViewPager.OnPageChangeListener,
        CartonViewPager.OnScrollListener,
        ContinuousSpeechRecognition.OnTextListener {


    public final static String EXTRA_NAME = "extra_name";
    public final static String EXTRA_NB_STEPS = "extra_nb_steps";


    private CartonViewPager mViewPager;
    private int mNbSteps;
    private TextView mTextViewStepPosition;
    private ImageView mImageViewStepPosition;

    private HeadRecognition mHeadRecognition;

    private ContinuousSpeechRecognition mContinuousSpeechRecognition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            fragments.add(OrigamiStepFragment.newInstance(i, resourceId));
        }

        fragments.add(OrigamiStepFragment.newInstance(0, getResources().getIdentifier(name.toLowerCase().concat("_finished"), "drawable", getPackageName())));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mTextViewStepPosition = (TextView) findViewById(R.id.textView_stepPosition);
        mTextViewStepPosition.setText("1");
        mImageViewStepPosition = (ImageView) findViewById(R.id.imageView_stepPosition);

        mViewPager = (CartonViewPager) findViewById(R.id.viewPager_OrigamiSteps);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(160 * scale + 0.5f));
        mViewPager.addOnPageChangeListener(this);
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
    public void onShake() {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        if (position < mNbSteps) {
            mTextViewStepPosition.setText(String.format("%d", position+1));
            mImageViewStepPosition.setImageDrawable(null);
        } else {
            mTextViewStepPosition.setText("");
            mImageViewStepPosition.setImageResource(R.drawable.ic_action_accept);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onScroll(int direction) {
        actionDirection(direction);
    }

    private void actionDirection(int direction) {
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


    @Override
    public void onError(int error) {

    }
}