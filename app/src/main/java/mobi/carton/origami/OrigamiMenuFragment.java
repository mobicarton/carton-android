package mobi.carton.origami;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.carton.PageFragment;
import mobi.carton.R;


public class OrigamiMenuFragment extends PageFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_origami_menu, container, false);
    }
}
