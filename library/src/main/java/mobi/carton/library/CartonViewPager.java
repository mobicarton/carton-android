package mobi.carton.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by damienbrun on 18/12/2016.
 */

public class CartonViewPager extends android.support.v4.view.ViewPager
        implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener {


public interface OnScrollListener {
    void onScroll(int direction);
}


    /**
     * Use GestureDetector to detect the swipe with onFling event
     */
    private GestureDetector mDetector;


    /**
     *
     */
    private OnScrollListener mOnScrollListener;


    public CartonViewPager(Context context) {
        super(context);
    }


    public CartonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(context, this);
        setOnTouchListener(this);
    }


    public void nextPage() {
        if (getCurrentItem() < getAdapter().getCount()-1) {
            setCurrentItem(getCurrentItem() + 1);
        }
    }


    public void previousPage() {
        if (getCurrentItem() > 0) {
            setCurrentItem(getCurrentItem() - 1);
        }
    }


    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }


    /**
     * Retrieve the angle of the movement and define in which direction the finger motion occurred
     * @param x1 x coordinate of first down motion event that started the fling
     * @param y1 y coordinate of first down motion event that started the fling
     * @param x2 x coordinate of move motion event that triggered the current fling
     * @param y2 y coordinate of move motion event that triggered the current fling
     * @return It return the direction (up or down)
     */
    private int getDirection(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

        int direction = 0;

        // Right
        if (45 >= angle && angle > -45) {
            direction = CartonSdk.RIGHT;
        } else

            // Down (back == up with head)
            if (-45 >= angle && angle > -135) {
                direction = CartonSdk.UP;
            } else

                // Left
                if (-135 >= angle && angle >= -180 || 180 >= angle && angle > 135) {
                    direction = CartonSdk.LEFT;
                } else

                    // Up (go == down with head)
                    if (135 >= angle && angle > 45) {
                        direction = CartonSdk.DOWN;
                    }

        return direction;
    }

    /*
    IMPLEMENTS
     */


    // View.OnTouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!CartonPrefs.getWithoutCarton(getContext())) {
            event.setLocation(event.getX()*-1, event.getY());
        }
        return mDetector.onTouchEvent(event);
    }


    // GestureDetector.OnGestureListener
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }


    // GestureDetector.OnGestureListener
    @Override
    public void onShowPress(MotionEvent e) {

    }


    // GestureDetector.OnGestureListener
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    // GestureDetector.OnGestureListener
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }


    // GestureDetector.OnGestureListener
    @Override
    public void onLongPress(MotionEvent e) {

    }


    // GestureDetector.OnGestureListener
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
        }
        return false;
    }
}
