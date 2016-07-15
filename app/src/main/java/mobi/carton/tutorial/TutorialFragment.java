package mobi.carton.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.carton.R;


/**
 * Simple Fragment with some text to explain what to do in the tutorial
 */
public class TutorialFragment extends Fragment {


    private static final String ARG_DESCRIPTION = "arg_description";
    private static final String ARG_IMG_ID = "arg_img_id";


    public static TutorialFragment newInstance(String description, int resId) {
        TutorialFragment fragment = new TutorialFragment();

        Bundle args = new Bundle();
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMG_ID, resId);

        fragment.setArguments(args);

        return fragment;
    }


    public TutorialFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);

        Bundle b = getArguments();

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_tutorial);
        imageView.setImageResource(b.getInt(ARG_IMG_ID));

        TextView textView = (TextView) rootView.findViewById(R.id.textView_tutorial);
        textView.setText(b.getString(ARG_DESCRIPTION));

        return rootView;
    }
}
