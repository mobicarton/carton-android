package mobi.carton.tutorial;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mobi.carton.CartonFragment;
import mobi.carton.R;
import mobi.carton.library.HeadRecognition;


/**
 * A fragment to show the Tutorial feature in the main menu
 */
public class TutorialMenuFragment extends CartonFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tutorial_menu, container, false);
    }


    @Override
    public void movingDirection(int direction) {
        if (direction == HeadRecognition.NOD_DOWN) {
            startActivity(new Intent(getContext(), TutorialActivity.class));
        }
    }
}