package mobi.carton.tutorial;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Utilities and constants related to tutorial preference
 */
public class Utils {


    /**
     * Boolean indicating if the tutorial has been done or not
     * If not, it will be launch automatically (should be at least once).
     */
    private static final String PREF_TUTORIAL_DONE = "pref_tutorial_done";


    /**
     * Set the value defining whether the tutorial has been done or not.
     *
     * @param context  Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param tutorialDone New value that will be set.
     */
    public static void setTutorialDone(final Context context, final boolean tutorialDone) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PREF_TUTORIAL_DONE, tutorialDone).commit();
    }


    /**
     * Return true if user has already done the tutorial.
     *
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     */
    public static boolean isTutorialDone(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_TUTORIAL_DONE, false);
    }
}
