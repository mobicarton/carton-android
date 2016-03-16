package mobi.carton;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.carton.glass.CompassFragment;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;
import mobi.carton.origami.OrigamiMenuFragment;
import mobi.carton.subtitle.SubtitleFragment;

public class MainActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        CustomViewPager.OnScrollListener {


    private HeadRecognition mHeadRecognition;

    private CustomViewPager mViewPager;

    private MenuPagerAdapter mMenuPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDefaultLauncher();

        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, OrigamiMenuFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, CompassFragment.class.getName()));

        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(CartonSdk.CATEGORY);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(packageManager));
        for(ResolveInfo info : resolveInfos) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            fragments.add(AppCompatibleFragment.newInstance(applicationInfo));
        }

        fragments.add(Fragment.instantiate(this, SubtitleFragment.class.getName()));

        mMenuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mMenuPagerAdapter);
        mViewPager.setOnScrollListener(this);

        ViewPagerIndicators viewPagerIndicators = (ViewPagerIndicators) findViewById(R.id.viewPagerIndicators);
        viewPagerIndicators.setViewPager(mViewPager);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHeadRecognition.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mHeadRecognition.stop();
    }


    @Override
    public void onTilt(int direction) {
        switch (direction) {
            case HeadRecognition.TILT_RIGHT:
                mViewPager.nextPage();
                break;
            case HeadRecognition.TILT_LEFT:
                mViewPager.previousPage();
                break;
        }
    }


    @Override
    public void onNod(int direction) {

    }


    @Override
    public void onScroll(int direction) {
        CartonFragment cartonFragment = (CartonFragment) mMenuPagerAdapter.getItem(mViewPager.getCurrentItem());
        cartonFragment.movingDirection(direction);
    }
}
