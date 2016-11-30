package mobi.carton;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A fragment that shows the time (clock) in the main menu
 */
public class TimeFragment extends CartonFragment {


    /**
     * Use the TextClock widget (which is created from the xml) to provide a simple clock
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time, container, false);
    }
}