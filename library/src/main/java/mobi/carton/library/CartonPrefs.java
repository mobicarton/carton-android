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
     * Integer value to save around all the application the delta nod related to head calibration
     */
    private static final String PREF_DELTA_NOD = "pref_delta_nod";



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
     * Return true if the user choose to launch without Carton Viewer.
     *
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return boolean, true without Carton Viewer
     */
    public static boolean getWithoutCarton(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_WITHOUT_CARTON, false);
    }



    /**
     * Set the deltaNod value related to the degree to adapt horizontal mobile phone.
     *
     * @param context  Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param deltaNod New value that will be set.
     */
    public static void setDeltaNod(final Context context, final int deltaNod) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(PREF_DELTA_NOD, deltaNod).commit();
    }


    /**
     * Return the deltaNod (in degree) or 0 if there isn't any.
     *
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return deltaNod in degree
     */
    public static int getDeltaNod(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_DELTA_NOD, 0);
    }
}
