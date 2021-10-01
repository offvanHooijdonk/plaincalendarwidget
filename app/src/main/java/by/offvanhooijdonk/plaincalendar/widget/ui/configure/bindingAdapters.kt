package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setVisibility(isVisible: Boolean = true) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
