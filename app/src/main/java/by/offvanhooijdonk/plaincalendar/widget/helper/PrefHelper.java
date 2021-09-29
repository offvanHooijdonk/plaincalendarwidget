package by.offvanhooijdonk.plaincalendar.widget.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.offvanhooijdonk.plaincalendar.widget.R;

public class PrefHelper {
    private static final String PREF_OPACITY_PER_CENT = "pref_opacity_per_cent";
    private static final String PREF_DEFAULT_BACK_COLOR = "pref_default_back_color";
    private static final String PREF_DEFAULT_TEXT_COLOR = "pref_default_text_color";

    private static final int DEFAULT_OPACITY_PERCENT = 80;
    private static final int DEFAULT_BACK_COLOR_RESOURCE = R.color.widget_default_back;
    private static final int DEFAULT_TEXT_COLOR_RESOURCE = R.color.widget_default_text;

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

    public static int getDefaultTextColor(Context ctx) {
        return getSharedPreference(ctx).getInt(PREF_DEFAULT_TEXT_COLOR, ctx.getResources().getColor(DEFAULT_TEXT_COLOR_RESOURCE));
    }

    public static void setDefaultTextColor(Context ctx, int color) {
        getSharedPreference(ctx).edit().putInt(PREF_DEFAULT_TEXT_COLOR, color).apply();
    }

    private static SharedPreferences getSharedPreference(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
