package mobi.carton.origami;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.carton.PageFragment;
import mobi.carton.R;
import mobi.carton.library.HeadRecognition;


public class OrigamiMenuFragment extends PageFragment
        implements
        HeadRecognition.OnHeadGestureListener {


    private HeadRecognition mHeadRecognition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHeadRecognition = new HeadRecognition(getContext());
        mHeadRecognition.setOnHeadGestureListener(this);

        return inflater.inflate(R.layout.fragment_origami_menu, container, false);
    }


    @Override
    public void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }


    @Override
    public void onResume() {
        super.onResume();
        mHeadRecognition.start();
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
            startActivity(new Intent(getContext(), OrigamiActivity.class));
        }
    }
}
