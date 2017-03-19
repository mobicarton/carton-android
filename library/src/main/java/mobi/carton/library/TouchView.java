package mobi.carton.library;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class TouchView extends View
        implements
        GestureDetector.OnGestureListener {


    /**
     * Used for receiving notifications from the Finger Touch Recognition when
     * gesture such as swipe, or simple tap have been detected.
     */
    public interface OnFingerTouchGestureListener {

        void onSwipe(int direction);
    }


    public interface OnFingerTapListener {

        void onTap(boolean isLong);
    }


    private OnFingerTouchGestureListener mCallback;
    private GestureDetector mDetector;

    private OnFingerTapListener mOnFingerTapListener;


    public TouchView(Context context) {
        super(context);
        mDetector = new GestureDetector(context, this);
    }


    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mDetector = new GestureDetector(context, this);
    }


    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDetector = new GestureDetector(context, this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mOnFingerTapListener != null)
                mOnFingerTapListener.onTap(false);
        }

        return mDetector.onTouchEvent(event);
    }


    public void setOnFingerTouchGestureListener(OnFingerTouchGestureListener callback) {
        mCallback = callback;
    }


    public void setOnFingerTapListener(OnFingerTapListener l) {
        mOnFingerTapListener = l;
    }


    /*
    IMPLEMENTS
     */


    // GestureDetector.OnGestureListener
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
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
        if (mOnFingerTapListener != null)
            mOnFingerTapListener.onTap(true);
    }


    // GestureDetector.OnGestureListener
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mCallback != null)
            mCallback.onSwipe(getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY()));
        return false;
    }


    private int getDirection(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

        int direction = 0;

        // Right
        if (45 >= angle && angle > -45) {
            if (CartonPrefs.getWithoutCarton(getContext()))
                direction = CartonSdk.RIGHT;
            else
                direction = CartonSdk.LEFT;
        } else

            // Down (back == up with head)
            if (-45 >= angle && angle > -135) {
                direction = CartonSdk.DOWN;
            } else

                // Left
                if (-135 >= angle && angle >= -180 || 180 >= angle && angle > 135) {
                    if (CartonPrefs.getWithoutCarton(getContext()))
                        direction = CartonSdk.LEFT;
                    else
                        direction = CartonSdk.RIGHT;
                } else

                    // Up (go == down with head)
                    if (135 >= angle && angle > 45) {
                        direction = CartonSdk.UP;
                    }

        return direction;
    }
}
