package mobi.carton.compatible;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.carton.CartonFragment;
import mobi.carton.R;


/**
 * Displays the details (title, description and icon) about an application compatible with
 * Carton (which is already installed on the device).
 */
public class CompatibleAppsFragment extends CartonFragment {


    private static final String ARG_APP_INFO = "arg_app_info";


    public static CompatibleAppsFragment newInstance(ApplicationInfo applicationInfo) {
        CompatibleAppsFragment fragment = new CompatibleAppsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_APP_INFO, applicationInfo);

        fragment.setArguments(args);

        return fragment;
    }


    public CompatibleAppsFragment() {

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
        }

        return rootView;
    }
}
