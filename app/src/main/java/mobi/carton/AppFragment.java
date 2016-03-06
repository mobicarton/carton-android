package mobi.carton;


import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AppFragment extends PageFragment {


    private static final String ARG_APP_INFO = "arg_app_info";


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

        ApplicationInfo applicationInfo = getArguments().getParcelable(ARG_APP_INFO);

        TextView textView = (TextView) rootView.findViewById(R.id.textView_appName);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_appIcon);

        if (applicationInfo != null) {
            imageView.setImageDrawable(applicationInfo.loadIcon(getActivity().getPackageManager()));
            textView.setText(applicationInfo.loadLabel(getActivity().getPackageManager()));
        }

        return rootView;
    }
}
