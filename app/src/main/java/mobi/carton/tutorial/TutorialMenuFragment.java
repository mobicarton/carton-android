package mobi.carton.tutorial;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.carton.CartonFragment;
import mobi.carton.R;
import mobi.carton.library.HeadRecognition;

public class TutorialMenuFragment extends CartonFragment
        implements
        HeadRecognition.OnHeadGestureListener {


    private HeadRecognition mHeadRecognition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHeadRecognition = new HeadRecognition(getContext());
        mHeadRecognition.setOnHeadGestureListener(this);

        return inflater.inflate(R.layout.fragment_tutorial_menu, container, false);
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
    public void onTilt(int direction) {

    }


    @Override
    public void onNod(int direction) {
        if (getUserVisibleHint()) {
            actionDirection(direction);
        }
    }


    @Override
    public void movingDirection(int direction) {
        actionDirection(direction);
    }


    private void actionDirection(int direction) {
        if (direction == HeadRecognition.NOD_DOWN) {
            startActivity(new Intent(getContext(), TutorialActivity.class));
        }
    }
}