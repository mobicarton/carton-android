package mobi.carton;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CustomViewPager extends ViewPager
        implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener {


    public interface ViewPagerLifecycle {

        void onResumePage();
        void onPausePage();
    }


    private GestureDetector mDetector;


    public CustomViewPager(Context context) {
        super(context);
    }


    public CustomViewPager(Context context, AttributeSet attrs) {
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


    private int getDirection(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));

        int direction = 0;

        // Right
        if (45 >= angle && angle > -45) {
            direction = -2;
        } else

            // Down
            if (-45 >= angle && angle > -135) {
                direction = -1;
            } else

                // Left
                if (-135 >= angle && angle >= -180 || 180 >= angle && angle > 135) {
                    direction = 2;
                } else

                    // Up
                    if (135 >= angle && angle > 45) {
                        direction = 1;
                    }

        return direction;
    }

    /*
    IMPLEMENTS
     */


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
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
        int direction = getDirection(e1.getX(), e1.getY(), e2.getX(), e2.getY());
        Log.d("DIRECTION", "onFling > " + direction);
        return false;
    }
}
