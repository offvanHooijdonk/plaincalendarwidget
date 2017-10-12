package by.yahorfralou.plaincalendar.widget.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {
    private static final String PREF_OPACITY = "pref_opacity";
    private static final int DEFAULT_OPACITY = 100;

    public static int getDefaultOpacity(Context ctx) {
        return getSharedPreference(ctx).getInt(PREF_OPACITY, DEFAULT_OPACITY);
    }

    public static void setDefaultOpacity(Context ctx, int opacity) {
        getSharedPreference(ctx).edit().putInt(PREF_OPACITY, opacity).apply();
    }

    private static SharedPreferences getSharedPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
