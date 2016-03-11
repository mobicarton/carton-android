package mobi.carton.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class CartonPrefs {


    private static final String PREF_WITHOUT_CARTON = "pref_without_carton";


    public static void setWithoutCarton(final Context context, final boolean withoutCarton) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(PREF_WITHOUT_CARTON, withoutCarton).commit();
    }


    public static boolean getWithoutCarton(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(PREF_WITHOUT_CARTON, false);
    }
}
