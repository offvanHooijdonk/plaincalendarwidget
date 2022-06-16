package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import android.view.View

/*@BindingAdapter("visible")*/
fun View.setVisibility(isVisible: Boolean = true) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}
