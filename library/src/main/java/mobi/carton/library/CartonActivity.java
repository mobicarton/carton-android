package mobi.carton.library;


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

public class CartonActivity extends FragmentActivity {


    public static final String EXTRA_WITHOUT_CARTON = "extra_without_carton";

    private static final String EXTRA_NO_LAUNCHER = "extra_no_launcher";

    private boolean mDebug = false;

    private boolean mNoLauncher;


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

        // set 10 mm margin from LEFT / TOP
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, dm);
        layoutParams.y = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, dm);

        window.setAttributes(layoutParams);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_WITHOUT_CARTON)) {
            mDebug = intent.getBooleanExtra(EXTRA_WITHOUT_CARTON, false);
            CartonPrefs.setWithoutCarton(getApplicationContext(), mDebug);
            mNoLauncher = true;
        } else {
            mNoLauncher = intent.getBooleanExtra(EXTRA_NO_LAUNCHER, false);
            mDebug = CartonPrefs.getWithoutCarton(getApplicationContext());
        }
    }


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


    @Override
    public void setContentView(int layoutResID) {
        this.setContentView(
                getLayoutInflater().inflate(layoutResID, null)
        );
    }


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


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mDebug) {
            super.setContentView(view, params);
            return;
        }
        MirrorRelativeLayout relativeLayout = new MirrorRelativeLayout(this);
        relativeLayout.addView(view);
        super.setContentView(relativeLayout, params);
    }



    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        if (mDebug) {
            super.addContentView(view, params);
            return;
        }
        MirrorRelativeLayout relativeLayout = new MirrorRelativeLayout(this);
        relativeLayout.addView(view);
        super.addContentView(relativeLayout, params);
    }
}