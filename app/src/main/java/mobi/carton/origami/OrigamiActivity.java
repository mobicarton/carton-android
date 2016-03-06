package mobi.carton.origami;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.library.CartonActivity;


public class OrigamiActivity extends CartonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDebug();
        setContentView(R.layout.activity_origami);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(OrigamiFragment.newInstance("Frog", "Andrew Hudson", 17));
        fragments.add(OrigamiFragment.newInstance("Cranevar", "Andrew Hudson", 26));
        fragments.add(OrigamiFragment.newInstance("Tulip", "Andrew Hudson", 23));
        fragments.add(OrigamiFragment.newInstance("Mouse", "Tavin", 14));
        fragments.add(OrigamiFragment.newInstance("Lily", "Tavin", 25));

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        CustomViewPager viewPager = (CustomViewPager) super.findViewById(R.id.viewPager_Origami);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        viewPager.setPageMargin((int) -(40 * scale + 0.5f));
    }
}
