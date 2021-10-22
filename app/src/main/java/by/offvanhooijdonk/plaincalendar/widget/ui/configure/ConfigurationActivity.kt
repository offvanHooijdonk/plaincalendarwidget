package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.databinding.ConfActivityBinding
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel: ConfigureViewModel by viewModel()
    private lateinit var bind: ConfActivityBinding

    private var dialogPickCalendars: AlertDialog? = null
    private val calendarsList = mutableListOf<CalendarModel>()
    private val calendarsAdapter = CalendarsChoiceAdapter(this, calendarsList) { index, isChecked ->
        onCalendarToggle(index, isChecked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_configure_widget)

        setupCallbacks()

        viewModel.passWidgetId(extractWidgetId())

    }

    private fun setupCallbacks() {
        viewModel.widgetId.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                val id = viewModel.widgetId.get()
                supportActionBar?.title =
                    when {
                        viewModel.isCreationMode.get() -> getString(R.string.title_add_widget)
                        id != null -> getString(R.string.title_edit_widget, id.toString())
                        else -> getString(R.string.title_no_widget)
                    }
            }
        })

        viewModel.isShowCalendarsPick.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (viewModel.isShowCalendarsPick.get()) {
                    checkPermissionAndPickCalendars()
                } else {
                    closeDialogPickCalendars()
                }
            }
        })
    }

    private fun onCalendarToggle(index: Int, checked: Boolean) {
        TODO("Not yet implemented")
    }

    private fun checkPermissionAndPickCalendars() {
        when {
            checkSelfPermission(CALENDAR_PERMISSION) == PackageManager.PERMISSION_GRANTED -> {
                startDialogPickCalendars()
            }
            shouldShowRequestPermissionRationale(CALENDAR_PERMISSION) -> {
                showPermissionRationale()
            }
            else -> {
                requestCalendarPermission()
            }
        }
    }

    private fun startDialogPickCalendars() {
        dialogPickCalendars = AlertDialog.Builder(this)
            .setAdapter(calendarsAdapter, null)
            .setTitle(R.string.app_name)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                // todo save selection
                viewModel.onCalendarsPicked()
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                viewModel.onCalendarPickCancel()
            }
            .create()
    }

    private fun requestCalendarPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startDialogPickCalendars()
            } else {
                showPermissionRationale()
            }
        }.launch(CALENDAR_PERMISSION)
    }

    private fun showPermissionRationale() {
        Snackbar.make(bind.blockBottomSettings, R.string.permission_rationale_calendar, Snackbar.LENGTH_LONG)
            .setAction(R.string.permission_rationale_calendar_action) { requestCalendarPermission() }
            .show()
    }

    private fun closeDialogPickCalendars() {
        dialogPickCalendars?.dismiss()
        dialogPickCalendars = null
    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

    companion object {
        private const val CALENDAR_PERMISSION = Manifest.permission.READ_CALENDAR
    }
}
