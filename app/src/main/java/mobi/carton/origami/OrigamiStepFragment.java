package mobi.carton.origami;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mobi.carton.R;


/**
 * A fragment to show a step (image with its position)
 */
public class OrigamiStepFragment extends Fragment {


    private static final String ARG_POSITION = "arg_position";
    private static final String ARG_IMG_ID = "arg_img_id";


    public static OrigamiStepFragment newInstance(int position, int resId) {
        OrigamiStepFragment fragment = new OrigamiStepFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_IMG_ID, resId);

        fragment.setArguments(args);

        return fragment;
    }


    public OrigamiStepFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_step);
        imageView.setImageResource(getArguments().getInt(ARG_IMG_ID));

        return rootView;
    }
}
