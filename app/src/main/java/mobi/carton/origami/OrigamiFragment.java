package mobi.carton.origami;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mobi.carton.R;

public class OrigamiFragment extends Fragment {


    private static final String ARG_NAME = "arg_name";
    private static final String ARG_NB_STEPS = "arg_nb_steps";
    private static final String ARG_AUTHOR = "arg_author";


    public static OrigamiFragment newInstance(Origami origami) {
        OrigamiFragment fragment = new OrigamiFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, origami.getName());
        args.putString(ARG_AUTHOR, origami.getAuthor());
        args.putInt(ARG_NB_STEPS, origami.getNbSteps());

        fragment.setArguments(args);

        return fragment;
    }


    public OrigamiFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_origami, container, false);

        Bundle bundle = getArguments();
        String name = bundle.getString(ARG_NAME);
        TextView textViewName = (TextView) rootView.findViewById(R.id.textView_origamiName);
        textViewName.setText(name);

        TextView textViewAuthor = (TextView) rootView.findViewById(R.id.textView_origamiAuthor);
        textViewAuthor.setText(bundle.getString(ARG_AUTHOR));

        TextView textViewNbSteps = (TextView) rootView.findViewById(R.id.textView_origamiNbSteps);
        textViewNbSteps.setText(String.format("%d %s", bundle.getInt(ARG_NB_STEPS), getString(R.string.origami_steps)));

        if (name != null) {
            int resourceId = getResources().getIdentifier(name.toLowerCase().concat("_finished"), "drawable", getActivity().getPackageName());
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_origamiFinished);
            imageView.setImageResource(resourceId);
        }

        return rootView;
    }
}
