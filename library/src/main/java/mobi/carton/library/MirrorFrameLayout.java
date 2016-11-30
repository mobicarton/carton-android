package mobi.carton.library;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * MirrorFrameLayout is similar to FrameLayout except that it is designed to reverse
 * horizontally the view, which is mandatory if the app is used with a CARTON device
 * because of the Pepper's ghost effect.
 */
public class MirrorFrameLayout extends FrameLayout {


    public MirrorFrameLayout(Context context) {
        this(context, null, 0);
    }


    public MirrorFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MirrorFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // set to false this option in order to make onDraw() of his children being called
        setWillNotDraw(false);
    }


    /**
     * Implemented to reverse horizontally the drawing.
     *
     * @param canvas the canvas manipulated on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // little trick to reverse horizontally
        canvas.translate(getWidth(), 0);
        canvas.scale(-1, 1);
        super.onDraw(canvas);
    }
}