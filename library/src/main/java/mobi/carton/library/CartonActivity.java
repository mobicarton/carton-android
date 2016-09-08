package mobi.carton.library;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


/**
 * A base activity that handles common functionality in the app. This includes screen
 * auto-configuration (size, border, brightness), default launcher, among others
 */
public class CartonActivity extends FragmentActivity {


    public static final String EXTRA_WITHOUT_CARTON = "extra_without_carton";

    public static final String EXTRA_NO_LAUNCHER = "extra_no_launcher";

    private boolean mDebug = false;

    private boolean mNoLauncher;

    //private TouchView mTouchView;
    //private boolean mTouchAdded;


    /**
     * Here we ensure to:
     * - hide title, status bar, navigation
     * - set screen's size to 60 x 35 mm
     * - set margin to 10 mm from top/left
     * - set screen's brightness to maximum
     * - keep the screen ON
     */
    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();

        window.getDecorView().setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        DisplayMetrics dm = getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        // set brightness to maximum
        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;

        // set width to 60 mm and height to 35 mm
        layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 60, dm);
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 35, dm);

        // set 10 mm margin from TOP/LEFT
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, dm);
        layoutParams.y = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, dm);

        window.setAttributes(layoutParams);

        // we keep the screen ON because we could use the app without touch the screen
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_WITHOUT_CARTON)) {
            // just finish the launcher
            mDebug = intent.getBooleanExtra(EXTRA_WITHOUT_CARTON, false);
            CartonPrefs.setWithoutCarton(getApplicationContext(), mDebug);
            // to avoid launch it again
            mNoLauncher = true;
        } else {
            mNoLauncher = intent.getBooleanExtra(EXTRA_NO_LAUNCHER, false);
            mDebug = CartonPrefs.getWithoutCarton(getApplicationContext());
        }

        //mTouchView = new TouchView(this);
        //mTouchAdded = false;
    }


    /**
     * Called to start default launcher, usually once at the beginning (during onCreate)
     */
    public void startDefaultLauncher() {
        if (!mNoLauncher) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivityForResult(intent, LauncherActivity.CODE_LAUNCHER);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LauncherActivity.CODE_LAUNCHER) {
            if (resultCode == RESULT_OK) {
                boolean without = data.getBooleanExtra(LauncherActivity.EXTRA_WITHOUT, false);
                CartonPrefs.setWithoutCarton(getApplicationContext(), without);
                // "recreate" activity because setContentView (onCreate) already called
                Intent intent = getIntent();
                intent.putExtra(EXTRA_NO_LAUNCHER, true);
                finish();
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Set the activity content from a layout resource. The resource will be
     * inflated and call {@link #setContentView(View, ViewGroup.LayoutParams)} to handle
     * horizontal reverse
     *
     * @param layoutResID Resource ID to be inflated.
     *
     * @see #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(
                getLayoutInflater().inflate(layoutResID, null)
        );
    }


    /**
     * Set the activity content to an explicit view. When calling this method, the layout
     * parameters of the specified view are ignored. Both the width and the height of the
     * view are set by default to {@link ViewGroup.LayoutParams#MATCH_PARENT}. It then call
     * {@link #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)}
     *
     * @param view The desired content to display.
     *
     * @see #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view) {
        this.setContentView(
                view,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
    }


    /**
     * Add a {@link MirrorFrameLayout} as parent of the {@param view} before setting
     * the activity content to this explicit view
     *
     * @param view The desired content to display.
     * @param params Layout parameters for the view.
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mDebug) {
            // if
            super.setContentView(view, params);
        } else {
            MirrorFrameLayout frameLayout = new MirrorFrameLayout(this);
            frameLayout.addView(view);
            super.setContentView(frameLayout, params);
        }

        //addTouchView();
    }


    /**
     * Nest the additional content view into a {@link MirrorFrameLayout} before calling
     * the original {@link android.app.Activity#addContentView(View, ViewGroup.LayoutParams)}
     *
     * @param view The desired content to display.
     * @param params Layout parameters for the view.
     */
    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mDebug) {
            super.addContentView(view, params);
            return;
        }
        MirrorFrameLayout frameLayout = new MirrorFrameLayout(this);
        frameLayout.addView(view);
        super.addContentView(frameLayout, params);
    }


    /*public void addTouchView() {
        if (!mTouchAdded) {
            mTouchAdded = true;
            super.addContentView(mTouchView,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
        }
    }*/
}