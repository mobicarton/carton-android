package mobi.carton.library;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class MirrorRelativeLayout extends RelativeLayout {


    public MirrorRelativeLayout(Context context) {
        this(context, null, 0);
    }


    public MirrorRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MirrorRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth(), 0);
        canvas.scale(-1, 1);
        super.onDraw(canvas);
    }
}