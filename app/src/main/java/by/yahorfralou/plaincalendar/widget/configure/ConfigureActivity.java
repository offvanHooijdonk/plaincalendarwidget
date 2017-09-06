package by.yahorfralou.plaincalendar.widget.configure;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.views.CalendarIconView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ConfigureActivity extends Activity implements IConfigureView, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private ConfigurePresenter presenter;
    List<CalendarBean> calendarSettings;

    private ProgressDialog dialogProgress;
    private TextView txtCalendarsNumber;
    private Button btnPickCalendars;
    private ViewGroup blockCalIcons;

    private AlertDialog pickCalendarsDialog;
    private BaseAdapter calSettingsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);

        presenter = new ConfigurePresenter(getApplicationContext(), this);
        calendarSettings = new ArrayList<>();

        dialogProgress = new ProgressDialog(this);
        dialogProgress.setMessage(getString(R.string.dialog_load_calendars_msg));

        txtCalendarsNumber = findViewById(R.id.txtCalendarsNumber);
        btnPickCalendars = findViewById(R.id.btnPickCalendars);
        blockCalIcons = findViewById(R.id.blockCalendarsIcons);

        btnPickCalendars.setOnClickListener(view -> loadCalendars());

        calSettingsAdapter = new CalendarsChoiceAdapter(this, calendarSettings, (index, isSelected) -> calendarSettings.get(index).setSelected(isSelected));
        pickCalendarsDialog = new AlertDialog.Builder(this)
                .setAdapter(calSettingsAdapter, null)
                .setTitle("Pick Calendars")
                .setCancelable(false)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onSelectionPicked();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        presenter.loadCalendarsSettings();

    }

    @Override
    public void displayCalendarsDialog(@NonNull List<CalendarBean> calendars) {
        for (CalendarBean bean : calendars) {
            for (CalendarBean setting : calendarSettings) {
                if (bean.equals(setting)) {
                    bean.setSelected(setting.isSelected());
                }
            }
        }
        calendarSettings.clear();
        calendarSettings.addAll(calendars);

        calSettingsAdapter.notifyDataSetChanged();
        pickCalendarsDialog.show();
    }

    @Override
    public void onCalendarSettingsLoaded(List<CalendarBean> list) {
        calendarSettings.clear();
        calendarSettings.addAll(list);

        txtCalendarsNumber.setText(String.valueOf(calendarSettings.size()));

        blockCalIcons.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(-8);
        for (CalendarBean bean : calendarSettings) {
            CalendarIconView iconView = new CalendarIconView(ConfigureActivity.this);

            if (bean.getColor() != null) {
                iconView.setBackColor(bean.getColor());
            }
            iconView.setSymbol(bean.getDisplayName().charAt(0));
            iconView.setLayoutParams(params);

            blockCalIcons.addView(iconView);
        }
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
        Log.d(LOGCAT, "onPermissionsGranted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(LOGCAT, "onPermissionsDenied");
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void afterPermissionGranted() {
        Log.i(LOGCAT, "afterPermissionGranted");

        loadCalendars();
    }

    @Override
    public void showCalendarsLoadProgress(boolean isShow) {
        if (isShow) {
            dialogProgress.show();
        } else {
            dialogProgress.dismiss();
        }
    }

    private void onSelectionPicked() {
        presenter.updateCalendarsSettings(calendarSettings);
    }

    private void loadCalendars() {
        if (hasPermissions()) {
            presenter.onPickCalendarsRequested();
        } else {
            askForPermissions();
        }
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
