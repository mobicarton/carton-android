package mobi.carton.compatible;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.carton.CustomViewPager;
import mobi.carton.MenuPagerAdapter;
import mobi.carton.R;
import mobi.carton.ZoomOutPageTransformer;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonPrefs;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.HeadRecognition;

/**
 * Handle the sub-menu of applications (list) compatible with Carton
 */

public class CompatibleAppsActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        CustomViewPager.OnScrollListener {


    private CustomViewPager mViewPager;

    private HeadRecognition mHeadRecognition;

    private ArrayList<ApplicationInfo> mApplicationInfos;

    private PackageManager mPackageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_origami);

        List<Fragment> fragments = new ArrayList<>();

        mPackageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(CartonSdk.CATEGORY);
        mApplicationInfos = new ArrayList<>();
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackageManager));
        for(ResolveInfo info : resolveInfos) {
            ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
            mApplicationInfos.add(applicationInfo);
            fragments.add(CompatibleAppsFragment.newInstance(applicationInfo));
        }

        MenuPagerAdapter pagerAdapter = new MenuPagerAdapter(super.getSupportFragmentManager(), fragments);

        mViewPager = (CustomViewPager) super.findViewById(R.id.viewPager_Origami);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(3);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) -(40 * scale + 0.5f));

        mViewPager.setOnScrollListener(this);

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
        action(direction);
    }


    @Override
    public void onShake() {

    }


    @Override
    public void onScroll(int direction) {
        action(direction);
    }


    private void action(int direction) {
        switch (direction) {
            case HeadRecognition.NOD_DOWN:
                ApplicationInfo applicationInfo = mApplicationInfos.get(mViewPager.getCurrentItem());
                Intent intent = mPackageManager.getLaunchIntentForPackage(applicationInfo.packageName);
                intent.putExtra(CartonActivity.EXTRA_WITHOUT_CARTON, CartonPrefs.getWithoutCarton(getApplicationContext()));
                startActivity(intent);
                break;
            case HeadRecognition.NOD_UP:
                onBackPressed();
                break;
        }
    }
}