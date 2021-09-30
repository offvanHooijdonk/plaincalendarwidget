package by.offvanhooijdonk.plaincalendar.widget.helper

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

fun hasCalendarPermissions(ctx: Context): Boolean { //todo move where used
    return EasyPermissions.hasPermissions(ctx, Manifest.permission.READ_CALENDAR)
}

