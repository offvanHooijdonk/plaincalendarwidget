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
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel: ConfigureViewModel by viewModel()
    private lateinit var bind: ConfActivityBinding

    private var dialogPickCalendars: AlertDialog? = null

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

    private fun checkPermissionAndPickCalendars() {
        when {
            checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED -> {
                startDialogPickCalendars()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) -> {
                // todo snack showing need permission
            }
            else -> {
                requestCalendarPermission()
            }
        }
    }

    private fun startDialogPickCalendars() {

    }

    private fun requestCalendarPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startDialogPickCalendars()
            } else {
                // todo snack showing need permission
            }
        }
    }

    private fun closeDialogPickCalendars() {
        dialogPickCalendars?.dismiss()
        dialogPickCalendars = null
    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
}
