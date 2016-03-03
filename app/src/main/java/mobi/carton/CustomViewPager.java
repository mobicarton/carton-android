package mobi.carton;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {


    public CustomViewPager(Context context) {
        super(context);
    }


    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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
}
