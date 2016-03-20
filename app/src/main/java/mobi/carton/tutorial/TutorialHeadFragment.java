package mobi.carton.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobi.carton.R;
import mobi.carton.library.HeadRecognition;

public class TutorialHeadFragment extends Fragment
        implements
        HeadRecognition.OnHeadTrackingListener {


    private static final String ARG_DESCRIPTION = "arg_description";
    private static final String ARG_IS_TILT = "arg_is_titl";


    private HeadRecognition mHeadRecognition;
    private TextView mTextViewTracking;
    private NeedleView mNeedleView;
    private boolean isTitl;


    public static TutorialHeadFragment newInstance(String description, boolean isTitl) {
        TutorialHeadFragment fragment = new TutorialHeadFragment();

        Bundle args = new Bundle();
        args.putString(ARG_DESCRIPTION, description);
        args.putBoolean(ARG_IS_TILT, isTitl);

        fragment.setArguments(args);
        return fragment;
    }


    public TutorialHeadFragment() {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        Bundle bundle = getArguments();
        isTitl = bundle.getBoolean(ARG_IS_TILT);
        if (isTitl) {
            rootView = inflater.inflate(R.layout.fragment_tutorial_tilt, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_tutorial_nod, container, false);
        }


        TextView textView = (TextView) rootView.findViewById(R.id.textView_tutorial);
        textView.setText(getArguments().getString(ARG_DESCRIPTION));

        mTextViewTracking = (TextView) rootView.findViewById(R.id.textView_tracking);
        mNeedleView = (NeedleView) rootView.findViewById(R.id.needleView);

        mHeadRecognition = new HeadRecognition(getContext());
        mHeadRecognition.setOnHeadTrackingListener(this);

        return rootView;
    }


    @Override
    public void onDirectionChanged(int azimuth, int pitch, int roll) {
        if (getUserVisibleHint()) {
            if (isTitl) {
                mNeedleView.setAngle(pitch * -1 + 90);
                mTextViewTracking.setText(String.format("%d", (int) Math.sqrt(pitch * pitch)));
            } else {
                mNeedleView.setAngle(roll + 90);
                mTextViewTracking.setText(String.format("%d", (int) Math.sqrt(roll * roll)));
            }
        }
    }
}
