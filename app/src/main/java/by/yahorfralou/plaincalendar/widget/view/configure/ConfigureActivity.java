package by.yahorfralou.plaincalendar.widget.view.configure;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.helper.PermissionHelper;
import by.yahorfralou.plaincalendar.widget.helper.PrefHelper;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.view.configure.preview.PreviewWidgetFragment;
import by.yahorfralou.plaincalendar.widget.view.configure.settings.ColorsSettingsFragment;
import by.yahorfralou.plaincalendar.widget.view.configure.settings.SeekBarSettingsFragment;
import by.yahorfralou.plaincalendar.widget.view.customviews.CalendarIconView;
import by.yahorfralou.plaincalendar.widget.widget.CalendarWidgetProvider;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ConfigureActivity extends AppCompatActivity implements IConfigureView, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private ConfigurePresenter presenter;
    private List<CalendarBean> calendarSettings;
    private WidgetBean widgetBean = null;
    private Integer widgetId;
    private boolean isCreateMode = false;

    private ProgressDialog dialogProgress;
    private TextView txtCalendarsNumber;
    private Button btnPickCalendars;
    private ViewGroup blockCalIcons;
    private FloatingActionButton fabCreateWidget;
    private View viewNoWidgets;
    private RadioGroup groupSettings;
    private PreviewWidgetFragment fragPreviewWidget;
    private ImageView imgSettings;
    private View blockBottomSettings;
    private View blockExpandableSettings;

    private SettingsSelection settingsOpened;
    private AlertDialog pickCalendarsDialog;
    private BaseAdapter calSettingsAdapter;
    private SettingsListener settingsListener = new SettingsListener();
    private boolean isSettingsExpanded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);

        presenter = new ConfigurePresenter(getApplicationContext(), this);

        calendarSettings = new ArrayList<>();

        viewNoWidgets = findViewById(R.id.viewNoWidgets);
        dialogProgress = new ProgressDialog(this);
        dialogProgress.setMessage(getString(R.string.dialog_load_calendars_msg));
        txtCalendarsNumber = findViewById(R.id.txtCalendarsNumber);
        btnPickCalendars = findViewById(R.id.btnPickCalendars);
        blockCalIcons = findViewById(R.id.blockCalendarsIcons);
        fabCreateWidget = findViewById(R.id.fabCreateWidget);
        groupSettings = findViewById(R.id.groupSettings);
        imgSettings = findViewById(R.id.imgSettings);
        blockBottomSettings = findViewById(R.id.blockBottomSettings);
        blockExpandableSettings = findViewById(R.id.blockExpandableSettings);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            widgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            widgetBean = new WidgetBean();
            isCreateMode = true;

            initWidgetWithDefaults();

            widgetBean.setId(widgetId);
            getSupportActionBar().setTitle(getString(R.string.title_add_widget));
        } else {
            Log.d(LOGCAT, "No Widget ID found in Extras");
            int[] widgetIds = WidgetHelper.getWidgetIds(this, CalendarWidgetProvider.class);
            if (widgetIds != null && widgetIds.length > 0) {
                // FIXME and add a list to pick
                widgetId = widgetIds[0];
                presenter.loadWidgetSettings(widgetId);

                getSupportActionBar().setTitle(getString(R.string.title_edit_widget, String.valueOf(widgetId)));
            } else {
                widgetId = null;
                presenter.onNoWidgets();
                return;
            }
        }

        btnPickCalendars.setOnClickListener(view -> pickCalendars());

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
        if (pickCalendarsDialog.getListView() != null) {
            TextView txtEmptyList = new TextView(this);
            txtEmptyList.setText(R.string.empty_cal_list);
            pickCalendarsDialog.getListView().setEmptyView(txtEmptyList);
        }

        if (!PermissionHelper.hasCalendarPermissions(ConfigureActivity.this)) {
            fabCreateWidget.setEnabled(false);
            askForPermissions();
        }
        fabCreateWidget.setOnClickListener(view -> applySettings());

        groupSettings.setOnCheckedChangeListener((group, checkedId) -> displaySettings(checkedId));
        if (isCreateMode) {
            openDefaultSettings();
            initPreview();
        }

        imgSettings.setOnClickListener(v -> toggleExpandableSettings());
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
        // TODO handle if no calendars
        pickCalendarsDialog.show();
    }

    @Override
    public void onWidgetSettingsLoaded(WidgetBean widgetBean) {
        this.widgetBean = widgetBean;
        calendarSettings.clear();
        calendarSettings.addAll(widgetBean.getCalendars());

        if (calendarSettings.isEmpty()) {
            fabCreateWidget.setEnabled(false);
        } else {
            fabCreateWidget.setEnabled(true);
        }

        openDefaultSettings();
        initPreview();

        txtCalendarsNumber.setText(String.valueOf(calendarSettings.size()));
        updateCalIcons();
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
        fabCreateWidget.setEnabled(true);

        if (calendarSettings.isEmpty()) {
            pickCalendars();
        }
    }

    @Override
    public void showCalendarsLoadProgress(boolean isShow) {
        if (isShow) {
            dialogProgress.show();
        } else {
            dialogProgress.dismiss();
        }
    }

    @Override
    public void showNoWidgets(boolean isShow) {
        if (isShow) {
            viewNoWidgets.setVisibility(View.VISIBLE);
        } else {
            viewNoWidgets.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyChangesAndFinish() {
        Intent intentBr = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, CalendarWidgetProvider.class);
        intentBr.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetId});
        sendBroadcast(intentBr);

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onSelectionPicked() {
        Iterator<CalendarBean> itCalendarSettings = calendarSettings.iterator();
        while (itCalendarSettings.hasNext()) {
            if (!itCalendarSettings.next().isSelected()) {
                itCalendarSettings.remove();
            }
        }
        if (calendarSettings.isEmpty()) {
            fabCreateWidget.setEnabled(false);
        } else {
            fabCreateWidget.setEnabled(true);
        }

        txtCalendarsNumber.setText(String.valueOf(calendarSettings.size()));
        updateCalIcons();
    }

    private void pickCalendars() {
        if (PermissionHelper.hasCalendarPermissions(this)) {
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

    private void initPreview() {
        fragPreviewWidget = (PreviewWidgetFragment) getFragmentManager().findFragmentById(R.id.fragPreviewWidget);
        fragPreviewWidget.setInitialParametersFromWidgetBean(widgetBean);
    }

    private void openDefaultSettings() {
        View settingFirst = groupSettings.getChildAt(0);
        if (settingFirst != null && settingFirst instanceof RadioButton) {
            ((RadioButton) settingFirst).setChecked(true);
        }
    }

    private void displaySettings(int buttonId) {
        Fragment fr;
        switch (buttonId) {
            case R.id.radioBack: {
                int[] colorsBackground = getResources().getIntArray(R.array.settings_back_colors);
                ColorsSettingsFragment fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetBean.getBackgroundColor());
                fragment.setSettingsListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.BACKGROUND;
            }
            break;
            case R.id.radioOpac: {
                // TODO values from Widget Bean and preferences
                SeekBarSettingsFragment fragment = SeekBarSettingsFragment.newInstance(0,
                        100,
                        widgetBean.getOpacity(),
                        5, // TODO constant
                        getString(R.string.per_cent_sign));
                fragment.setListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.OPACITY;
            }
            break;
            case R.id.radioTextColor: {
                int[] colorsBackground = getResources().getIntArray(R.array.settings_text_colors);
                ColorsSettingsFragment fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetBean.getTextColor());
                fragment.setSettingsListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.TEXT_COLOR;
            }
            break;

            default:
                fr = null;
        }

        if (fr != null) {
            getFragmentManager().beginTransaction().replace(R.id.placeholderSettings, fr).commit();
        }
    }

    private void applySettings() {
        widgetBean.setCalendars(calendarSettings);
        presenter.onApplySettings(widgetBean);
    }

    private void updateCalIcons() {
        blockCalIcons.removeAllViews();

        for (CalendarBean bean : calendarSettings) {
            CalendarIconView iconView = (CalendarIconView) LayoutInflater.from(ConfigureActivity.this)
                    .inflate(R.layout.inc_cal_icon, blockCalIcons, false);

            if (bean.getColor() != null) {
                iconView.setBackColor(bean.getColor());
            }
            iconView.setSymbol(bean.getDisplayName().charAt(0));

            blockCalIcons.addView(iconView);
        }
    }

    private void toggleExpandableSettings() { // TODO move to some helper?
        int startHeight = isSettingsExpanded ? blockBottomSettings.getTop() - blockExpandableSettings.getTop() : 0;
        int endHeight = !isSettingsExpanded ? blockBottomSettings.getTop() - blockExpandableSettings.getTop() : 0;

        ValueAnimator heightAnim = ValueAnimator.ofInt(startHeight, endHeight);
        heightAnim.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = blockExpandableSettings.getLayoutParams();
            layoutParams.height = value;
            blockExpandableSettings.setLayoutParams(layoutParams);
        });
        heightAnim.setDuration(isSettingsExpanded ? 350 : 500);
        heightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        heightAnim.start();

        if (isSettingsExpanded) {
            fabCreateWidget.show();
        } else {
            fabCreateWidget.hide();
        }
        isSettingsExpanded = !isSettingsExpanded;
    }

    private void initWidgetWithDefaults() {
        if (widgetBean != null) {
            widgetBean.setBackgroundColor(PrefHelper.getDefaultBackColor(this));
            widgetBean.setOpacity(PrefHelper.getDefaultOpacityPerCent(this));
            widgetBean.setTextColor(PrefHelper.getDefaultTextColor(this));
        }
    }

    private class SettingsListener implements ColorsSettingsFragment.SettingClickListener, SeekBarSettingsFragment.OnValueChangeListener {

        @Override
        public void onColorClick(int colorValue) {
            if (settingsOpened == SettingsSelection.BACKGROUND) {
                widgetBean.setBackgroundColor(colorValue);
                fragPreviewWidget.updateBackColor(colorValue);
            } else if (settingsOpened == SettingsSelection.TEXT_COLOR) {
                widgetBean.setTextColor(colorValue);
                fragPreviewWidget.updateTextColor(colorValue);
            }
        }

        @Override
        public void onSeekValueChanged(int value) {
            widgetBean.setOpacity(value);
            fragPreviewWidget.updateOpacity(value);
        }
    }

    private enum SettingsSelection {
        BACKGROUND, OPACITY, TEXT_COLOR
    }
}
