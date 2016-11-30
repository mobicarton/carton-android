package mobi.carton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


/**
 * Indicators (as little circle/dot) to the menu's ViewPager
 */
public class ViewPagerIndicators extends View
        implements
        ViewPager.OnPageChangeListener {


    private Paint mPaint = new Paint();
    private Paint mPaintInactive = new Paint();
    private float mRadius;
    private float mRadiusInactive;

    private int mCount = 0;
    private int mActive = 0;


    /**
     * Constructor to initiate some variable and get radius and color from xml (attributes)
     */
    public ViewPagerIndicators(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicators, 0, 0);

        int color;
        try {
            mRadius = a.getDimension(R.styleable.ViewPagerIndicators_circleRadius, 8f);
            color = a.getColor(R.styleable.ViewPagerIndicators_circleColor, Color.WHITE);
        } finally {
            a.recycle();
        }

        mRadiusInactive = mRadius - mRadius / 4;

        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        mPaintInactive.setAntiAlias(true);
        mPaintInactive.setColor(color);
        mPaintInactive.setStyle(Paint.Style.STROKE);
        mPaintInactive.setStrokeWidth(mRadius / 4);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        float x;
        for (int i = 0; i < mCount; i++) {
            x = mRadius + i * (mRadius * 4);
            if (i == mActive)
                canvas.drawCircle(x, mRadius, mRadius, mPaint);
            else
                canvas.drawCircle(x, mRadius, mRadiusInactive, mPaintInactive);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = (int) ((mRadius * 2 + (mCount - 1) * (mRadius * 4)));
        int measuredHeight = (int) ((mRadius * 2));

        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    /**
     *
     * @param viewPager ViewPager to add theses indicators
     */
    public void setViewPager(ViewPager viewPager) {
        mCount = viewPager.getAdapter().getCount();
        setActiveItem(viewPager.getCurrentItem());
        viewPager.addOnPageChangeListener(this);
    }


    private void setActiveItem(int activeItem) {
        mActive = activeItem;
        invalidate();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        setActiveItem(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}