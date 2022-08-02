package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import by.offvanhooijdonk.plaincalendarv2.widget.app.App.Companion.LOGCAT

@Composable
fun permissionCheck(ifGranted: () -> Unit, ifDenied: () -> Unit, ifShowRationale: () -> Unit) {
    val permission = Manifest.permission.READ_CALENDAR

    val isAlreadyGranted = LocalContext.current.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (!isAlreadyGranted) {
        Log.d(LOGCAT, "Not granted")
        if ((LocalContext.current as? Activity)?.shouldShowRequestPermissionRationale(permission) == true) {
            Log.d(LOGCAT, "Show rationale")
            ifShowRationale()
        } else {
            Log.d(LOGCAT, "Not need rationale, showing request")
            val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) ifGranted() else ifDenied()
            }
            SideEffect {
                launcherPermission.launch(permission)
            }
        }
    } else {
        Log.d(LOGCAT, "GRANTED")
        ifGranted()
    }
}
