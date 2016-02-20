package mobi.carton.library;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CartonActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // force landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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
    }
}
