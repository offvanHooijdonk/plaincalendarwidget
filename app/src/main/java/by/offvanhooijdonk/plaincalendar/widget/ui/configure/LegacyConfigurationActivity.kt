package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.Manifest
import android.animation.ValueAnimator
import android.app.Fragment
import android.app.ProgressDialog
import android.appwidget.AppWidgetManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.app.App
import by.offvanhooijdonk.plaincalendar.widget.databinding.ConfActivityBinding
import by.offvanhooijdonk.plaincalendar.widget.helper.PrefHelper
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel.ShowEndDate
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.preview.PreviewWidgetFragment
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.ColorsSettingsFragment
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.ColorsSettingsFragment.SettingClickListener
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.ExtendedSettingsFragment
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.ExtendedSettingsFragment.ExtendedOptionsListener
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.SeekBarSettingsFragment
import by.offvanhooijdonk.plaincalendar.widget.ui.customviews.CalendarIconView
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import java.util.ArrayList

@Deprecated(message = "Old implementation, replaced with new refactored Activity")
class LegacyConfigurationActivity : AppCompatActivity(), IConfigureView, PermissionCallbacks {
    private var presenter: ConfigurePresenter? = null
    private var calendarSettings: MutableList<CalendarModel>? = null
    private var widgetModel: WidgetModel? = null
    private var widgetId: Int? = null
    private var isCreateMode = false
    private var dialogProgress: ProgressDialog? = null
    private var txtNoCalSelected: TextView? = null
    private var btnPickCalendars: Button? = null
    private var blockCalIcons: ViewGroup? = null
    private var inputDaysForEvents: EditText? = null
    private var fabCreateWidget: FloatingActionButton? = null
    private var viewNoWidgets: View? = null
    private var groupSettings: RadioGroup? = null
    private var fragPreviewWidget: PreviewWidgetFragment? = null
    private var fragExtendedSettings: ExtendedSettingsFragment? = null
    private var imgSettings: ImageView? = null
    private var blockBottomSettings: View? = null
    private var blockExpandableSettings: View? = null
    private var settingsOpened: SettingsSelection? = null
    private var pickCalendarsDialog: AlertDialog? = null
    private var calSettingsAdapter: BaseAdapter? = null
    private val settingsListener = SettingsListener()
    private var isSettingsExpanded = false

    //
    private val viewModel: ConfigureViewModel by viewModel()
    private lateinit var bind: ConfActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_widget)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_configure_widget)

        //presenter = ConfigurePresenter(applicationContext, this)
        calendarSettings = ArrayList()
        /*viewNoWidgets = findViewById(R.id.viewNoWidgets)*/
        dialogProgress = ProgressDialog(this)
        dialogProgress?.setMessage(getString(R.string.dialog_load_calendars_msg))
        /*txtNoCalSelected = findViewById(R.id.txtNoCalSelected)
        btnPickCalendars = findViewById(R.id.btnPickCalendars)
        blockCalIcons = findViewById(R.id.blockCalendarsIcons)
        inputDaysForEvents = findViewById(R.id.inputDaysForEvents)
        fabCreateWidget = findViewById(R.id.fabCreateWidget)
        groupSettings = findViewById(R.id.groupSettings)
        imgSettings = findViewById(R.id.imgSettings)
        blockBottomSettings = findViewById(R.id.blockBottomSettings)
        blockExpandableSettings = findViewById(R.id.blockExpandableSettings)
        fragExtendedSettings = fragmentManager.findFragmentById(R.id.fragExtendedSettings) as ExtendedSettingsFragment*/

        /*if (intent.extras != null && intent.extras!!.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            widgetId = intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
            widgetModel = WidgetModel()
            isCreateMode = true
            initWidgetWithDefaults()
            fillOptions()
            widgetModel.setId(widgetId)
            supportActionBar!!.title = getString(R.string.title_add_widget)
        } else {
            Log.d(App.LOGCAT, "No Widget ID found in Extras")
            val widgetIds: IntArray = WidgetHelper.getWidgetIds(this, CalendarWidgetProvider::class.java)
            if (widgetIds != null && widgetIds.size > 0) {
                // FIXME and add a list to pick
                widgetId = widgetIds[0]
                presenter!!.loadWidgetSettings(widgetId!!.toLong())
                supportActionBar!!.setTitle(getString(R.string.title_edit_widget, widgetId.toString()))
            } else {
                widgetId = null
                presenter!!.onNoWidgets()
                return
            }
        }
        btnPickCalendars.setOnClickListener(View.OnClickListener { view: View? -> pickCalendars() })*/
        calSettingsAdapter = CalendarsChoiceAdapter(this, calendarSettings) { index: Int, isSelected: Boolean -> calendarSettings.get(index).isSelected = isSelected }
        pickCalendarsDialog = AlertDialog.Builder(this)
            .setAdapter(calSettingsAdapter, null)
            .setTitle("Pick Calendars")
            .setCancelable(false)
            .setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                onSelectionPicked()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .create()
        if (pickCalendarsDialog!!.listView != null) {
            val txtEmptyList = TextView(this)
            txtEmptyList.setText(R.string.empty_cal_list)
            pickCalendarsDialog!!.listView.emptyView = txtEmptyList
        }
        inputDaysForEvents.setOnFocusChangeListener(OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                if (inputDaysForEvents.getText().length == 0 || !isDaysValid(inputDaysForEvents.getText().toString().toInt())) {
                    inputDaysForEvents.setText(widgetModel!!.days.toString())
                } else {
                    widgetModel.setDays(inputDaysForEvents.getText().toString().toInt())
                }
            }
        })
        if (!PermissionHelper.hasCalendarPermissions(this@LegacyConfigurationActivity)) {
            fabCreateWidget.setEnabled(false)
            askForPermissions()
        }
        fabCreateWidget.setOnClickListener(View.OnClickListener { view: View? -> applySettings() })
        groupSettings.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, checkedId: Int -> displaySettings(checkedId) })
        if (isCreateMode) {
            openDefaultSettings()
            initPreview()
        }
        imgSettings.setOnClickListener(View.OnClickListener { v: View? -> toggleExpandableSettings(v) })
    }

    private fun fillOptions() {
        inputDaysForEvents!!.setText(widgetModel!!.days.toString())
        fragExtendedSettings!!.setWidgetOptions(widgetModel)
    }

    override fun displayCalendarsDialog(@NonNull calendars: List<CalendarModel>) {
        for (bean in calendars) {
            for (setting in calendarSettings!!) {
                if (bean.equals(setting)) {
                    bean.isSelected = setting.isSelected
                }
            }
        }
        calendarSettings!!.clear()
        calendarSettings!!.addAll(calendars)
        calSettingsAdapter!!.notifyDataSetChanged()
        // TODO handle if no calendars
        pickCalendarsDialog!!.show()
    }

    override fun onWidgetSettingsLoaded(widgetModel: WidgetModel) {
        this.widgetModel = widgetModel
        calendarSettings!!.clear()
        calendarSettings!!.addAll(widgetModel.calendars)
        for (c in calendarSettings!!) {
            c.isSelected = true
        }
        if (calendarSettings!!.isEmpty()) {
            fabCreateWidget!!.isEnabled = false
        } else {
            fabCreateWidget!!.isEnabled = true
        }
        fillOptions()
        openDefaultSettings()
        initPreview()
        updateCalIcons()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(App.LOGCAT, "onPermissionsGranted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(App.LOGCAT, "onPermissionsDenied")
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun afterPermissionGranted() {
        Log.i(App.LOGCAT, "afterPermissionGranted")
        fabCreateWidget!!.isEnabled = true
        if (calendarSettings!!.isEmpty()) {
            pickCalendars()
        }
    }

    override fun showCalendarsLoadProgress(isShow: Boolean) {
        if (isShow) {
            dialogProgress!!.show()
        } else {
            dialogProgress!!.dismiss()
        }
    }

    override fun showNoWidgets(isShow: Boolean) {
        if (isShow) {
            viewNoWidgets!!.visibility = View.VISIBLE
        } else {
            viewNoWidgets!!.visibility = View.GONE
        }
    }

    override fun notifyChangesAndFinish() {
        val intentBr = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, CalendarWidgetProvider::class.java)
        intentBr.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId!!))
        sendBroadcast(intentBr)
        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onSelectionPicked() {
        val itCalendarSettings = calendarSettings!!.iterator()
        while (itCalendarSettings.hasNext()) {
            if (!itCalendarSettings.next().isSelected) {
                itCalendarSettings.remove()
            }
        }
        if (calendarSettings!!.isEmpty()) {
            fabCreateWidget!!.isEnabled = false
        } else {
            fabCreateWidget!!.isEnabled = true
        }
        updateCalIcons()
    }

    private fun pickCalendars() {
        if (PermissionHelper.hasCalendarPermissions(this)) {
            presenter!!.onPickCalendarsRequested()
        } else {
            askForPermissions()
        }
    }

    private fun askForPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "This app needs to access Calendars on your device.",
            REQUEST_PERMISSION_GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR)
    }

    private fun initPreview() {
        fragPreviewWidget = fragmentManager.findFragmentById(R.id.fragPreviewWidget) as PreviewWidgetFragment
        fragPreviewWidget!!.attachWidgetSettings(widgetModel)
        fragExtendedSettings!!.setListener(settingsListener)
    }

    private fun openDefaultSettings() {
        val settingFirst = groupSettings!!.getChildAt(0)
        if (settingFirst != null && settingFirst is RadioButton) {
            settingFirst.isChecked = true
        }
    }

    private fun displaySettings(buttonId: Int) {
        val fr: Fragment?
        when (buttonId) {
            R.id.radioBack -> {
                val colorsBackground = resources.getIntArray(R.array.settings_back_colors)
                val fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetModel!!.backgroundColor!!)
                fragment.setSettingsListener(settingsListener)
                fr = fragment
                settingsOpened = SettingsSelection.BACKGROUND
            }
            R.id.radioOpac -> {

                // TODO values from Widget Bean and preferences
                val fragment = SeekBarSettingsFragment.newInstance(0,
                    100,
                    widgetModel!!.opacity!!,
                    5,  // TODO constant
                    getString(R.string.per_cent_sign), null)
                fragment.setListener(settingsListener)
                fr = fragment
                settingsOpened = SettingsSelection.OPACITY
            }
            R.id.radioTextColor -> {
                val colorsBackground = resources.getIntArray(R.array.settings_text_colors)
                val fragment = ColorsSettingsFragment.getNewInstance(colorsBackground, widgetModel!!.textColor!!)
                fragment.setSettingsListener(settingsListener)
                fr = fragment
                settingsOpened = SettingsSelection.TEXT_COLOR
            }
            R.id.radioTextSize -> {
                val fragment = SeekBarSettingsFragment.newInstance(0,
                    5,
                    widgetModel!!.textSizeDelta!!,
                    1,
                    "+%s", null)
                fragment.setListener(settingsListener)
                fr = fragment
                settingsOpened = SettingsSelection.TEXT_SIZE
            }
            else -> fr = null
        }
        if (fr != null) {
            fragmentManager.beginTransaction().replace(R.id.placeholderSettings, fr).commit()
        }
    }

    private fun applySettings() {
        if (widgetModel!!.calendars != null) {
            widgetModel!!.calendars.clear()
        } else {
            widgetModel.setCalendars(ArrayList<E>())
        }
        for (c in calendarSettings!!) {
            if (c.isSelected) {
                widgetModel!!.calendars.add(c)
            }
        }
        presenter!!.onApplySettings(widgetModel)
    }

    private fun updateCalIcons() {
        blockCalIcons!!.removeAllViews()
        if (calendarSettings!!.isEmpty()) {
            blockCalIcons!!.visibility = View.GONE
            txtNoCalSelected!!.visibility = View.VISIBLE
        } else {
            blockCalIcons!!.visibility = View.VISIBLE
            txtNoCalSelected!!.visibility = View.GONE
        }
        for ((_, displayName, _, color) in calendarSettings!!) {
            val iconView = LayoutInflater.from(this@LegacyConfigurationActivity)
                .inflate(R.layout.inc_cal_icon, blockCalIcons, false) as CalendarIconView
            if (color != null) {
                iconView.setCalendarColor(color)
            }
            iconView.setSymbol(displayName[0])
            blockCalIcons!!.addView(iconView)
        }
    }

    fun toggleExpandableSettings(v: View?) { // TODO move to some helper?
        val startHeight = if (isSettingsExpanded) blockBottomSettings!!.top - blockExpandableSettings!!.top else 0
        val endHeight = if (!isSettingsExpanded) blockBottomSettings!!.top - blockExpandableSettings!!.top else 0
        val heightAnim = ValueAnimator.ofInt(startHeight, endHeight)
        heightAnim.addUpdateListener { animation: ValueAnimator ->
            val value = animation.animatedValue as Int
            val layoutParams = blockExpandableSettings!!.layoutParams
            layoutParams.height = value
            blockExpandableSettings!!.layoutParams = layoutParams
        }
        heightAnim.duration = if (isSettingsExpanded) 200 else 350.toLong()
        heightAnim.interpolator = AccelerateDecelerateInterpolator()
        heightAnim.start()
        if (isSettingsExpanded) {
            fabCreateWidget!!.show()
        } else {
            fabCreateWidget!!.hide()
        }
        isSettingsExpanded = !isSettingsExpanded
    }

    private fun initWidgetWithDefaults() {
        if (widgetModel != null) {
            // TODO add Preferences
            widgetModel.setDays(14)
            widgetModel.setBackgroundColor(PrefHelper.getDefaultBackColor(this))
            widgetModel.setOpacity(PrefHelper.getDefaultOpacityPerCent(this))
            widgetModel.setTextColor(PrefHelper.getDefaultTextColor(this))
            // TODO add Preferences
            widgetModel.setTextSizeDelta(0)
            widgetModel.setShowTodayDate(true)
            widgetModel.setShowTodayDayOfWeek(true)
            widgetModel.setShowDateDivider(true)
            widgetModel.setShowTodayLeadingZero(false)
            widgetModel.setShowEndDate(ShowEndDate.NEVER)
            widgetModel.setShowDateTextLabel(true)
            widgetModel.setShowEventColor(true)
        }
    }

    fun decrementDays(view: View?) {
        updateDaysView(-1)
    }

    fun incrementDays(view: View?) {
        updateDaysView(+1)
    }

    private fun updateDaysView(byDays: Int) {
        var value = inputDaysForEvents!!.text.toString().toInt()
        value += byDays
        if (isDaysValid(value)) {
            inputDaysForEvents!!.setText(value.toString())
            widgetModel.setDays(value)
        }
    }

    private fun isDaysValid(value: Int): Boolean {
        // TODO store this values
        return value >= 1 && value <= 1000
    }

    private inner class SettingsListener : SettingClickListener, SeekBarSettingsFragment.OnValueChangeListener, ExtendedOptionsListener {
        override fun onColorClick(colorValue: Int) {
            if (settingsOpened == SettingsSelection.BACKGROUND) {
                widgetModel.setBackgroundColor(colorValue)
                fragPreviewWidget!!.updatePreview()
            } else if (settingsOpened == SettingsSelection.TEXT_COLOR) {
                widgetModel.setTextColor(colorValue)
                fragPreviewWidget!!.updatePreview()
            }
        }

        override fun onSeekValueChanged(value: Int) {
            when (settingsOpened) {
                SettingsSelection.OPACITY -> {
                    widgetModel.setOpacity(value)
                    fragPreviewWidget!!.updatePreview()
                }
                SettingsSelection.TEXT_SIZE -> {
                    widgetModel.setTextSizeDelta(value)
                    fragPreviewWidget!!.updatePreview()
                }
            }
        }

        override fun onShowTodayDateChange(isShow: Boolean) {
            widgetModel.setShowTodayDate(isShow)
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowDayOfWeekChange(isShow: Boolean) {
            widgetModel.setShowTodayDayOfWeek(isShow)
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowTodayLeadingZeroChange(isShow: Boolean) {
            widgetModel.setShowTodayLeadingZero(isShow)
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowEventColorChange(isShow: Boolean) {
            widgetModel.setShowEventColor(isShow)
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowDividerChange(isShow: Boolean) {
            widgetModel.setShowDateDivider(isShow)
            fragPreviewWidget!!.updatePreview()
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowDateAsLabelChange(isShow: Boolean) {
            widgetModel.setShowDateTextLabel(isShow)
            fragPreviewWidget!!.updatePreview()
        }

        override fun onShowEventEndChange(endDateOption: ShowEndDate) {
            widgetModel.setShowEndDate(endDateOption)
            fragPreviewWidget!!.updatePreview()
        }
    }

    private enum class SettingsSelection {
        BACKGROUND, OPACITY, TEXT_COLOR, TEXT_SIZE
    }

    companion object {
        private const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    }
}
