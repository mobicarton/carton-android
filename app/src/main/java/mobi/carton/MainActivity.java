package mobi.carton;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import mobi.carton.compatible.CompatibleAppsMenuFragment;
import mobi.carton.glass.CompassFragment;
import mobi.carton.library.CartonActivity;
import mobi.carton.library.CartonSdk;
import mobi.carton.library.CartonViewPager;
import mobi.carton.library.HeadRecognition;
import mobi.carton.origami.OrigamiMenuFragment;
import mobi.carton.tutorial.TutorialActivity;
import mobi.carton.tutorial.TutorialMenuFragment;
import mobi.carton.utils.PrefUtils;


/**
 * MainActivity which is the first launched activity handle fragments for the main menu
 */
public class MainActivity extends CartonActivity
        implements
        HeadRecognition.OnHeadGestureListener,
        CartonViewPager.OnScrollListener {


    /**
     * Use built-in Head Gesture Recognition to go through the menu (Right/Left with tilting)
     */
    private HeadRecognition mHeadRecognition;


    /**
     * Use CustomViewPager (implementing gesture listener) to handle the sliding inside the menu
     */
    private CartonViewPager mViewPager;


    // TODO: update comment here
    /**
     * FragmentPageAdapter used to retrieve the Fragment when scrolling
     */
    private MenuPagerAdapter mMenuPagerAdapter;


    /*
    LIFECYCLE
     */


    /**
     * Start the Default Launcher.
     * Add each part of the menu (Clock, Origami Helper, Compass, Compatible Apps,
     * OnLive Subtitle and Tutorial.
     * Add indicators to the menu (which look like small dots)
     * Make sure to launch the Tutorial first if it's the first launch of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDefaultLauncher();

        setContentView(R.layout.activity_main);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(Fragment.instantiate(this, TimeFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, OrigamiMenuFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, CompassFragment.class.getName()));

        // Get the list of apps which are compatible with Carton Viewer
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(CartonSdk.CATEGORY);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        if (resolveInfos.size() > 0) {
            fragments.add(Fragment.instantiate(this, CompatibleAppsMenuFragment.class.getName()));
        }

        // fragments.add(Fragment.instantiate(this, SubtitleFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, TutorialMenuFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, CalibrateFragment.class.getName()));

        mMenuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager = (CartonViewPager) super.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mMenuPagerAdapter);
        mViewPager.setOnScrollListener(this);

        // add indicators (which look like small dot) in the main menu
        ViewPagerIndicators viewPagerIndicators = (ViewPagerIndicators) findViewById(R.id.viewPagerIndicators);
        viewPagerIndicators.setViewPager(mViewPager);

        mHeadRecognition = new HeadRecognition(this);
        mHeadRecognition.setOnHeadGestureListener(this);

        if (getIntent().getBooleanExtra(EXTRA_NO_LAUNCHER, false)) {
            if (!PrefUtils.isTutorialDone(this)) {
                Intent intentTutorial = new Intent(this, TutorialActivity.class);
                startActivity(intentTutorial);
            }
        }
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


    private void sendDirectionToCurrentFragment(int direction) {
        CartonFragment cartonFragment = (CartonFragment) mMenuPagerAdapter.getItem(mViewPager.getCurrentItem());
        cartonFragment.movingDirection(direction);
    }


    /*
    IMPLEMENTS
     */


    // HeadRecognition.OnHeadGestureListener
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


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onNod(int direction) {
        sendDirectionToCurrentFragment(direction);
    }


    // HeadRecognition.OnHeadGestureListener
    @Override
    public void onShake() {

    }


    // CustomViewPager.OnScrollListener
    @Override
    public void onScroll(int direction) {
        sendDirectionToCurrentFragment(direction);
    }
}
