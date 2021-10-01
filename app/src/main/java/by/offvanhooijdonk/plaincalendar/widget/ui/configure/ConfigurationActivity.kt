package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.databinding.ConfActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel: ConfigureViewModel by viewModel()
    private lateinit var bind: ConfActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_configure_widget)

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

        viewModel.passWidgetId(extractWidgetId())

    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
}
