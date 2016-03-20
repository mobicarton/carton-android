package mobi.carton.tutorial;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import mobi.carton.R;


public class NeedleView extends View {


    private Paint mPaint;


    private int mPosX;
    private int mPosY;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mAngle;

    private boolean mOrientationVertical;


    public NeedleView(Context context) {
        super(context);
    }


    public NeedleView(Context context, AttributeSet attrs) {
        super(context, attrs);

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


    public NeedleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    public void setAngle(int angle) {
        mAngle = angle;

        angle = angle * -1;
        if (mOrientationVertical)
            angle += 90;

        double radian = (double) angle * Math.PI / 180.0;

        mPosX = mCenterX + (int) ((double) mRadius * Math.cos(radian));
        mPosY = mCenterY + (int) ((double) mRadius * Math.sin(radian));
    }


    public void setNeedleColor(int color) {
        mPaint.setColor(color);
    }


    public void setNeedleWidth(float width) {
        mPaint.setStrokeWidth(width);
    }
}
