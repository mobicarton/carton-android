package mobi.carton.tutorial;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {


    private static final String PREF_TUTORIAL_DONE = "pref_tutorial_done";


    public static void setTutorialDone(final Context context, final boolean tutorialDone) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PREF_TUTORIAL_DONE, tutorialDone).commit();
    }


    public static boolean isTutorialDone(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_TUTORIAL_DONE, false);
    }
}
