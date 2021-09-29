package by.offvanhooijdonk.plaincalendar.widget.view.configure;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import by.offvanhooijdonk.plaincalendar.widget.R;
import by.offvanhooijdonk.plaincalendar.widget.helper.PermissionHelper;
import by.offvanhooijdonk.plaincalendar.widget.helper.PrefHelper;
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper;
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel;
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel;
import by.offvanhooijdonk.plaincalendar.widget.view.configure.preview.PreviewWidgetFragment;
import by.offvanhooijdonk.plaincalendar.widget.view.configure.settings.ColorsSettingsFragment;
import by.offvanhooijdonk.plaincalendar.widget.view.configure.settings.ExtendedSettingsFragment;
import by.offvanhooijdonk.plaincalendar.widget.view.configure.settings.SeekBarSettingsFragment;
import by.offvanhooijdonk.plaincalendar.widget.view.customviews.CalendarIconView;
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static by.offvanhooijdonk.plaincalendar.widget.app.App.LOGCAT;

public class ConfigureActivity extends AppCompatActivity implements IConfigureView, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private ConfigurePresenter presenter;
    private List<CalendarModel> calendarSettings;
    private WidgetModel widgetModel = null;
    private Integer widgetId;
    private boolean isCreateMode = false;

    private ProgressDialog dialogProgress;
    private TextView txtNoCalSelected;
    private Button btnPickCalendars;
    private ViewGroup blockCalIcons;
    private EditText inputDaysForEvents;
    private FloatingActionButton fabCreateWidget;
    private View viewNoWidgets;
    private RadioGroup groupSettings;
    private PreviewWidgetFragment fragPreviewWidget;
    private ExtendedSettingsFragment fragExtendedSettings;
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
        txtNoCalSelected = findViewById(R.id.txtNoCalSelected);
        btnPickCalendars = findViewById(R.id.btnPickCalendars);
        blockCalIcons = findViewById(R.id.blockCalendarsIcons);
        inputDaysForEvents = findViewById(R.id.inputDaysForEvents);
        fabCreateWidget = findViewById(R.id.fabCreateWidget);
        groupSettings = findViewById(R.id.groupSettings);
        imgSettings = findViewById(R.id.imgSettings);
        blockBottomSettings = findViewById(R.id.blockBottomSettings);
        blockExpandableSettings = findViewById(R.id.blockExpandableSettings);
        fragExtendedSettings = (ExtendedSettingsFragment) getFragmentManager().findFragmentById(R.id.fragExtendedSettings);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            widgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            widgetModel = new WidgetModel();
            isCreateMode = true;

            initWidgetWithDefaults();
            fillOptions();

            widgetModel.setId(widgetId);
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

        inputDaysForEvents.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (inputDaysForEvents.getText().length() == 0 || !isDaysValid(Integer.parseInt(inputDaysForEvents.getText().toString()))) {
                    inputDaysForEvents.setText(String.valueOf(widgetModel.getDays()));
                } else {
                    widgetModel.setDays(Integer.parseInt(inputDaysForEvents.getText().toString()));
                }
            }
        });

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

        imgSettings.setOnClickListener(this::toggleExpandableSettings);
    }

    private void fillOptions() {
        inputDaysForEvents.setText(String.valueOf(widgetModel.getDays()));
        fragExtendedSettings.setWidgetOptions(widgetModel);
    }

    @Override
    public void displayCalendarsDialog(@NonNull List<CalendarModel> calendars) {
        for (CalendarModel bean : calendars) {
            for (CalendarModel setting : calendarSettings) {
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
    public void onWidgetSettingsLoaded(WidgetModel widgetModel) {
        this.widgetModel = widgetModel;
        calendarSettings.clear();
        calendarSettings.addAll(widgetModel.getCalendars());
        for (CalendarModel c : calendarSettings) {
            c.setSelected(true);
        }

        if (calendarSettings.isEmpty()) {
            fabCreateWidget.setEnabled(false);
        } else {
            fabCreateWidget.setEnabled(true);
        }

        fillOptions();

        openDefaultSettings();
        initPreview();

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
        Iterator<CalendarModel> itCalendarSettings = calendarSettings.iterator();
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
        fragPreviewWidget.attachWidgetSettings(widgetModel);

        fragExtendedSettings.setListener(settingsListener);
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
                ColorsSettingsFragment fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetModel.getBackgroundColor());
                fragment.setSettingsListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.BACKGROUND;
            }
            break;
            case R.id.radioOpac: {
                // TODO values from Widget Bean and preferences
                SeekBarSettingsFragment fragment = SeekBarSettingsFragment.newInstance(0,
                        100,
                        widgetModel.getOpacity(),
                        5, // TODO constant
                        getString(R.string.per_cent_sign), null);
                fragment.setListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.OPACITY;
            }
            break;
            case R.id.radioCorners : {
                SeekBarSettingsFragment fragment = SeekBarSettingsFragment.newInstance(WidgetModel.Corners.NO_CORNER.getCode(),
                        WidgetModel.Corners.XLARGE.getCode(),
                        widgetModel.getCorners().getCode(),
                        1,
                        null, getResources().getStringArray(R.array.corner_sizes));
                fragment.setListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.CORNERS;
            }
            break;
            case R.id.radioTextColor: {
                int[] colorsBackground = getResources().getIntArray(R.array.settings_text_colors);
                ColorsSettingsFragment fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetModel.getTextColor());
                fragment.setSettingsListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.TEXT_COLOR;
            }
            break;
            case R.id.radioTextSize : {
                SeekBarSettingsFragment fragment = SeekBarSettingsFragment.newInstance(0,
                        5,
                        widgetModel.getTextSizeDelta(),
                        1,
                        "+%s", null);
                fragment.setListener(settingsListener);
                fr = fragment;
                settingsOpened = SettingsSelection.TEXT_SIZE;
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
        if (widgetModel.getCalendars() != null) {
            widgetModel.getCalendars().clear();
        } else {
            widgetModel.setCalendars(new ArrayList<>());
        }
        for (CalendarModel c : calendarSettings) {
            if (c.isSelected()) {
                widgetModel.getCalendars().add(c);
            }
        }

        presenter.onApplySettings(widgetModel);
    }

    private void updateCalIcons() {
        blockCalIcons.removeAllViews();
        if (calendarSettings.isEmpty()) {
            blockCalIcons.setVisibility(View.GONE);
            txtNoCalSelected.setVisibility(View.VISIBLE);
        } else {
            blockCalIcons.setVisibility(View.VISIBLE);
            txtNoCalSelected.setVisibility(View.GONE);
        }

        for (CalendarModel bean : calendarSettings) {
            CalendarIconView iconView = (CalendarIconView) LayoutInflater.from(ConfigureActivity.this)
                    .inflate(R.layout.inc_cal_icon, blockCalIcons, false);

            if (bean.getColor() != null) {
                iconView.setCalendarColor(bean.getColor());
            }
            iconView.setSymbol(bean.getDisplayName().charAt(0));

            blockCalIcons.addView(iconView);
        }
    }

    public void toggleExpandableSettings(View v) { // TODO move to some helper?
        int startHeight = isSettingsExpanded ? blockBottomSettings.getTop() - blockExpandableSettings.getTop() : 0;
        int endHeight = !isSettingsExpanded ? blockBottomSettings.getTop() - blockExpandableSettings.getTop() : 0;

        ValueAnimator heightAnim = ValueAnimator.ofInt(startHeight, endHeight);
        heightAnim.addUpdateListener(animation -> {
            int value = (Integer) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = blockExpandableSettings.getLayoutParams();
            layoutParams.height = value;
            blockExpandableSettings.setLayoutParams(layoutParams);
        });
        heightAnim.setDuration(isSettingsExpanded ? 200 : 350);
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
        if (widgetModel != null) {
            // TODO add Preferences
            widgetModel.setDays(14);
            widgetModel.setBackgroundColor(PrefHelper.getDefaultBackColor(this));
            widgetModel.setOpacity(PrefHelper.getDefaultOpacityPerCent(this));
            widgetModel.setTextColor(PrefHelper.getDefaultTextColor(this));
            // TODO add Preferences
            widgetModel.setCorners(WidgetModel.Corners.getDefault());
            // TODO add Preferences
            widgetModel.setTextSizeDelta(0);
            widgetModel.setShowTodayDate(true);
            widgetModel.setShowTodayDayOfWeek(true);
            widgetModel.setShowDateDivider(true);
            widgetModel.setShowTodayLeadingZero(false);
            widgetModel.setShowEndDate(WidgetModel.ShowEndDate.NEVER);
            widgetModel.setShowDateTextLabel(true);
            widgetModel.setShowEventColor(true);
        }
    }

    public void decrementDays(View view) {
        updateDaysView(-1);
    }

    public void incrementDays(View view) {
        updateDaysView(+1);
    }

    private void updateDaysView(int byDays) {
        int value = Integer.parseInt(inputDaysForEvents.getText().toString());
        value += byDays;
        if (isDaysValid(value)) {
            inputDaysForEvents.setText(String.valueOf(value));
            widgetModel.setDays(value);
        }
    }

    private boolean isDaysValid(int value) {
        // TODO store this values
        return value >= 1 && value <= 1000;
    }

    private class SettingsListener implements ColorsSettingsFragment.SettingClickListener,
            SeekBarSettingsFragment.OnValueChangeListener, ExtendedSettingsFragment.ExtendedOptionsListener {

        @Override
        public void onColorClick(int colorValue) {
            if (settingsOpened == SettingsSelection.BACKGROUND) {
                widgetModel.setBackgroundColor(colorValue);
                fragPreviewWidget.updatePreview();
            } else if (settingsOpened == SettingsSelection.TEXT_COLOR) {
                widgetModel.setTextColor(colorValue);
                fragPreviewWidget.updatePreview();
            }
        }

        @Override
        public void onSeekValueChanged(int value) {
            switch (settingsOpened) {
                case OPACITY:
                    widgetModel.setOpacity(value);
                    fragPreviewWidget.updatePreview();
                    break;
                case CORNERS:
                    WidgetModel.Corners corners = WidgetModel.Corners.fromInt(value);
                    widgetModel.setCorners(corners);
                    fragPreviewWidget.updatePreview();
                    break;
                case TEXT_SIZE:
                    widgetModel.setTextSizeDelta(value);
                    fragPreviewWidget.updatePreview();
                    break;
            }
        }

        @Override
        public void onShowTodayDateChange(boolean isShow) {
            widgetModel.setShowTodayDate(isShow);
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowDayOfWeekChange(boolean isShow) {
            widgetModel.setShowTodayDayOfWeek(isShow);
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowTodayLeadingZeroChange(boolean isShow) {
            widgetModel.setShowTodayLeadingZero(isShow);
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowEventColorChange(boolean isShow) {
            widgetModel.setShowEventColor(isShow);
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowDividerChange(boolean isShow) {
            widgetModel.setShowDateDivider(isShow);
            fragPreviewWidget.updatePreview();
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowDateAsLabelChange(boolean isShow) {
            widgetModel.setShowDateTextLabel(isShow);
            fragPreviewWidget.updatePreview();
        }

        @Override
        public void onShowEventEndChange(WidgetModel.ShowEndDate endDateOption) {
            widgetModel.setShowEndDate(endDateOption);
            fragPreviewWidget.updatePreview();
        }
    }

    private enum SettingsSelection {
        BACKGROUND, OPACITY, CORNERS, TEXT_COLOR, TEXT_SIZE
    }
}
