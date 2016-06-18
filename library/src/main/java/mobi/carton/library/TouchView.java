package mobi.carton.library;


import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View
        implements
        GestureDetector.OnGestureListener {


    private GestureDetector mDetector;


    public TouchView(Context context) {
        super(context);
        mDetector = new GestureDetector(context, this);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d("TouchView", "ACTION_UP");
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TouchView", "ACTION_DOWN");
        }

        // handle long press with multiple touch only
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN: {
                // when the second finger touch
                //if (event.getPointerCount() == 2) {
                    Log.d("TouchView", "ACTION_POINTER_DOWN + " + event.getPointerCount());
                //}
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                // when the last multiple touch is released
                //if (event.getPointerCount() == 2) {
                    Log.d("TouchView", "ACTION_POINTER_UP + " + event.getPointerCount());
                //}
                break;
            }
        }

        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("onFling", "direction " + getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
        return false;
    }


    private int getDirection(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

        int direction = 0;

        // Right
        if (45 >= angle && angle > -45) {
            direction = 1;
        } else

            // Down (back == up with head)
            if (-45 >= angle && angle > -135) {
                direction = HeadRecognition.NOD_UP;
            } else

                // Left
                if (-135 >= angle && angle >= -180 || 180 >= angle && angle > 135) {
                    direction = 0;
                } else

                    // Up (go == down with head)
                    if (135 >= angle && angle > 45) {
                        direction = HeadRecognition.NOD_DOWN;
                    }

        return direction;
    }
}
