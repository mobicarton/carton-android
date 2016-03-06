package mobi.carton;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.carton.library.HeadRecognition;


public class AppFragment extends PageFragment
        implements
        HeadRecognition.OnHeadGestureListener {


    private static final String ARG_APP_INFO = "arg_app_info";

    private HeadRecognition mHeadRecognition;
    private Intent mIntent;


    public static AppFragment newInstance(ApplicationInfo applicationInfo) {
        AppFragment fragment = new AppFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_APP_INFO, applicationInfo);

        fragment.setArguments(args);

        return fragment;
    }


    public AppFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app, container, false);

        PackageManager packageManager = getActivity().getPackageManager();
        ApplicationInfo applicationInfo = getArguments().getParcelable(ARG_APP_INFO);

        TextView textView = (TextView) rootView.findViewById(R.id.textView_appName);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_appIcon);

        if (applicationInfo != null) {
            imageView.setImageDrawable(applicationInfo.loadIcon(packageManager));
            textView.setText(applicationInfo.loadLabel(packageManager));
            mIntent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
        }

        mHeadRecognition = new HeadRecognition(getContext());
        mHeadRecognition.setOnHeadGestureListener(this);

        return rootView;
    }


    @Override
    public void onResumePage() {
        mHeadRecognition.start();
    }


    @Override
    public void onPausePage() {
        mHeadRecognition.stop();
    }


    @Override
    public void onTilt(int direction) {

    }


    @Override
    public void onNod(int direction) {
        if (direction == HeadRecognition.NOD_DOWN) {
            startActivity(mIntent);
        }
    }
}
