package mobi.carton.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import mobi.carton.R;


/**
 * A view which provide a vertical or horizontal needle
 * The needle can rotate to an half circle
 */
public class NeedleView extends View {


    private Paint mPaint;


    /*
    PosX, PosY, CenterX, CenterY and Radius are related to the size of the view
     */
    private int mPosX;
    private int mPosY;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;


    /**
     * Angle of the current needle
     */
    private int mAngle;


    /**
     * Use to define if the orientation of the needle is vertical (true) or horizontal (false)
     */
    private boolean mOrientationVertical;


    public NeedleView(Context context) {
        this(context, null);
    }


    public NeedleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /**
     * Constructor which retrieve the attributes: orientation (vertical/horizontal) and
     * the color, width, angle of the needle
     */
    public NeedleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NeedleView, 0, 0);

        int color;
        float needleWidth;
        try {
            mOrientationVertical = a.getBoolean(R.styleable.NeedleView_orientationVertical, false);
            color = a.getColor(R.styleable.NeedleView_needleColor, Color.RED);
            needleWidth = a.getDimension(R.styleable.NeedleView_needleWidth, 5f);
            mAngle = a.getInteger(R.styleable.NeedleView_angle, 90);
        } finally {
            a.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(needleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(color);

        setAngle(mAngle);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        if (mOrientationVertical) {
            mCenterX = 0;
            mCenterY = getHeight() / 2;
            mRadius = Math.min(w, h / 2);
        } else {
            mCenterX = getWidth() / 2;
            mCenterY = getHeight();
            mRadius = Math.min(w / 2, h);
        }

        setAngle(mAngle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(mCenterX, mCenterY, mPosX, mPosY, mPaint);
    }


    /**
     * Set the rotation of the needle by defining an angle
     * Value should be between 0 and 180°
     * For the horizontal style, 0° point to right and 180° to left
     * For the vertical style, 0° point to down and 180° to up
     * @param angle the angle of the needle to rotate
     */
    public void setAngle(int angle) {
        mAngle = angle;

        angle = angle * -1;
        if (mOrientationVertical)
            angle += 90;

        double radian = (double) angle * Math.PI / 180.0;

        mPosX = mCenterX + (int) ((double) mRadius * Math.cos(radian));
        mPosY = mCenterY + (int) ((double) mRadius * Math.sin(radian));
        invalidate();
    }


    /**
     * Set the color of the needle.
     * @param color the color of the needle
     */
    public void setNeedleColor(int color) {
        mPaint.setColor(color);
    }


    /**
     * Set the width of the needle.
     * @param width the width of the needle
     */
    public void setNeedleWidth(float width) {
        mPaint.setStrokeWidth(width);
    }
}
