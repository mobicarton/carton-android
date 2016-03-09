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


public class AppCompatibleFragment extends PageFragment
        implements
        HeadRecognition.OnHeadGestureListener {


    private static final String ARG_APP_INFO = "arg_app_info";

    private HeadRecognition mHeadRecognition;
    private Intent mIntent;


    public static AppCompatibleFragment newInstance(ApplicationInfo applicationInfo) {
        AppCompatibleFragment fragment = new AppCompatibleFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_APP_INFO, applicationInfo);

        fragment.setArguments(args);

        return fragment;
    }


    public AppCompatibleFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_compatible, container, false);

        PackageManager packageManager = getActivity().getPackageManager();
        ApplicationInfo applicationInfo = getArguments().getParcelable(ARG_APP_INFO);

        TextView textViewName = (TextView) rootView.findViewById(R.id.textView_appName);
        TextView textViewDescription = (TextView) rootView.findViewById(R.id.textView_appDescription);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_appIcon);

        if (applicationInfo != null) {
            imageView.setImageDrawable(applicationInfo.loadIcon(packageManager));
            textViewName.setText(applicationInfo.loadLabel(packageManager));
            textViewDescription.setText(applicationInfo.loadDescription(packageManager));
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
