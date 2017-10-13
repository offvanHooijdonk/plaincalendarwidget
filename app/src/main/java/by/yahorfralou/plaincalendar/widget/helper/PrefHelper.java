package by.yahorfralou.plaincalendar.widget.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.yahorfralou.plaincalendar.widget.R;

public class PrefHelper {
    private static final String PREF_OPACITY_PER_CENT = "pref_opacity_per_cent";
    private static final String PREF_DEFAULT_BACK_COLOR = "pref_default_back_color";

    private static final int DEFAULT_OPACITY_PERCENT = 80;
    private static final int DEFAULT_BACK_COLOR_RESOURCE = R.color.md_blue_400;

    public static int getDefaultOpacityPerCent(Context ctx) {
        return getSharedPreference(ctx).getInt(PREF_OPACITY_PER_CENT, DEFAULT_OPACITY_PERCENT);
    }

    public static void setDefaultOpacityPerCent(Context ctx, int opacity) {
        getSharedPreference(ctx).edit().putInt(PREF_OPACITY_PER_CENT, opacity).apply();
    }

    public static int getDefaultBackColor(Context ctx) {
        return getSharedPreference(ctx).getInt(PREF_DEFAULT_BACK_COLOR, ctx.getResources().getColor(DEFAULT_BACK_COLOR_RESOURCE));
    }

    public static void setDefaultBackColor(Context ctx, int color) {
        getSharedPreference(ctx).edit().putInt(PREF_DEFAULT_BACK_COLOR, color).apply();
    }

    private static SharedPreferences getSharedPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
