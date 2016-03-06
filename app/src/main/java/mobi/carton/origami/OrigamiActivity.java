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

        for (Origami origami : getListOrigami()) {
            fragments.add(OrigamiFragment.newInstance(origami));
        }

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        CustomViewPager viewPager = (CustomViewPager) super.findViewById(R.id.viewPager_Origami);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        viewPager.setPageMargin((int) -(40 * scale + 0.5f));
    }


    private ArrayList<Origami> getListOrigami() {
        ArrayList<Origami> origamis = new ArrayList<>();

        origamis.add(new Origami("Cranevar", "Andrew Hudson", 26));
        origamis.add(new Origami("Frog", "Andrew Hudson", 17));
        origamis.add(new Origami("Lily", "Tavin", 25));
        origamis.add(new Origami("Mouse", "Tavin", 14));
        origamis.add(new Origami("Tulip", "Andrew Hudson", 23));

        return origamis;
    }
}
