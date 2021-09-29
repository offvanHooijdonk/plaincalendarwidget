package by.offvanhooijdonk.plaincalendar.widget.helper;

import android.Manifest;
import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionHelper {

    public static boolean hasCalendarPermissions(Context ctx) {
        return EasyPermissions.hasPermissions(ctx, Manifest.permission.READ_CALENDAR);
    }
}
