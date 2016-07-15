package mobi.carton.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Utilities and constants related to preference
 */
public class CartonPrefs {


    /**
     * Boolean indicating when the app has been launched without CARTON mode
     * (usually it means no horizontal reverse)
     */
    private static final String PREF_WITHOUT_CARTON = "pref_without_carton";



    /**
     * Set the WithoutCarton mode preference indicating whether the app has been launched with CARTON or not.
     *
     * @param context  Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param withoutCarton New value that will be set.
     */
    public static void setWithoutCarton(final Context context, final boolean withoutCarton) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PREF_WITHOUT_CARTON, withoutCarton).commit();
    }


    /**
     * Return true if user refused to sign in, false if they haven't refused (yet).
     *
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     */
    public static boolean getWithoutCarton(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_WITHOUT_CARTON, false);
    }
}
