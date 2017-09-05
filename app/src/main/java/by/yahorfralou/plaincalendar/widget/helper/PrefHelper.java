package by.yahorfralou.plaincalendar.widget.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {
    private static final String PREF_ACCOUNT_NAME = "account_name";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getAccountName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_ACCOUNT_NAME, null);
    }

    public static void setAccountName(Context ctx, String accountName) {
        getSharedPreferences(ctx).edit().putString(PREF_ACCOUNT_NAME, accountName).apply();
    }


}
