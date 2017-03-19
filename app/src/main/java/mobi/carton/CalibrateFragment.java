package mobi.carton;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import mobi.carton.library.HeadRecognition;
import mobi.carton.utils.NeedleView;


/**
 * A fragment that allows user to recalibrate head alignment
 */
public class CalibrateFragment extends CartonFragment
        implements
        HeadRecognition.OnHeadTrackingListener,
        Animator.AnimatorListener {


    private HeadRecognition mHeadRecognition;

    private TextView mTextViewTracking;
    private NeedleView mNeedleView;


    /*
    LIFECYCLE
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calibrate, container, false);

        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_calibrate);
        mNeedleView = (NeedleView) rootView.findViewById(R.id.needleView);
        mTextViewTracking = (TextView) rootView.findViewById(R.id.textView_tracking);

        mHeadRecognition = new HeadRecognition(getContext());
        mHeadRecognition.setOnHeadTrackingListener(this);

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        objectAnimator.setDuration(5000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.addListener(this);
        objectAnimator.start();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadTrackingListener
    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        if (getUserVisibleHint()) {
            mNeedleView.setAngle(roll + 90);
            int value = (int) Math.sqrt(roll * roll);
            mTextViewTracking.setText(String.format("%d°", value));
        }
    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationStart(Animator animator) {

    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationEnd(Animator animator) {

    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationCancel(Animator animator) {
        //mProgressBar.setProgress(100);
    }


    // Animator.AnimatorListener
    @Override
    public void onAnimationRepeat(Animator animator) {
        mHeadRecognition.autoCalibrateDeltaNod();
    }
}