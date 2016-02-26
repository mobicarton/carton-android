package mobi.carton;

import android.os.Bundle;
import android.widget.TextView;

import mobi.carton.library.CartonActivity;
import mobi.carton.library.HeadRecognition;

public class MainActivity extends CartonActivity {


    private HeadRecognition mHeadRecognition;

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(new HeadRecognition.OnHeadGestureListener() {
            @Override
            public void onTilt(int direction) {
                mTextView.setText("TILT : " + direction);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }
}
