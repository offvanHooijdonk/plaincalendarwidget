package by.yahorfralou.plaincalendar.widget.configure;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static by.yahorfralou.plaincalendar.widget.PlainCalendarWidgetApp.LOGCAT;

public class ConfigureActivity extends Activity implements IConfigureView, EasyPermissions.PermissionCallbacks {


    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private ConfigurePresenter presenter;

    private ProgressDialog dialogProgress;
    private TextView txtOutput;
    private Button btnGetCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);

        presenter = new ConfigurePresenter(getApplicationContext(), this);

        dialogProgress = new ProgressDialog(this);
        dialogProgress.setMessage("Loading Calendars…");

        txtOutput = findViewById(R.id.txtOutput);

        btnGetCalendar = findViewById(R.id.btnGetCalendar);

        if (hasPermissions()) {
            loadCalendars();
        } else {
            askForPermissions();
        }
    }

    @Override
    public void displayCalendars(String[] calendars) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMultiChoiceItems(calendars, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                    }
                })
                .create();
        dialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i(LOGCAT, "onPermissionsGranted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.w(LOGCAT, "onPermissionsDenied");
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void afterPermissionGranted() {
        Log.i(LOGCAT, "afterPermissionGranted");

        loadCalendars();
    }

    private void loadCalendars() {
        presenter.onCalendarsListRequested();
    }

    private void askForPermissions() {
        EasyPermissions.requestPermissions(
                this,
                "This app needs to access Calendars on your device.",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.READ_CALENDAR);
    }

    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(ConfigureActivity.this, Manifest.permission.READ_CALENDAR);
    }
}
