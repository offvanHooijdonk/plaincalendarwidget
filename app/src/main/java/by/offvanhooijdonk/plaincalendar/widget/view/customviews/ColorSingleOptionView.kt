package by.offvanhooijdonk.plaincalendar.widget.view.customviews

import android.content.Context
import android.widget.FrameLayout
import by.offvanhooijdonk.plaincalendar.widget.view.customviews.ColorSingleOptionView
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.app.App
import java.lang.Exception

class ColorSingleOptionView : FrameLayout {
    private var checkSize = DIMENSION_UNSET
    private var color = COLOR_UNSET
    private var checked = false
    private lateinit var root: View
    private lateinit var viewSelected: View

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.ColorSingleOptionView, 0, 0)
        try {
            checkSize = ta.getDimensionPixelSize(R.styleable.ColorSingleOptionView_checkSize, DIMENSION_UNSET)
            color = ta.getColor(R.styleable.CalendarIconView_calendarColor, COLOR_UNSET)
            checked = ta.getBoolean(R.styleable.CalendarIconView_calendarColor, false)
        } catch (e: Exception) {
            Log.e(App.LOGCAT, "Error reading attributes for Calendar Icon", e)
        } finally {
            ta.recycle()
        }
        init()
    }

    private fun init() {
        inflate(context, R.layout.view_color_single_option, this)
        root = findViewById(R.id.root)
        viewSelected = findViewById(R.id.imgSelected)
        updateBackColor()
        updateCheckMarkSize()
        updateChecked()
    }

    private fun updateBackColor() {
        if (color != COLOR_UNSET) {
            setBackgroundColor(color)
        }
    }

    private fun updateCheckMarkSize() {
        if (checkSize != DIMENSION_UNSET) {
            viewSelected.layoutParams.width = checkSize
            viewSelected.layoutParams.height = checkSize
        }
    }

    private fun updateChecked() {
        if (checked) {
            viewSelected.visibility = VISIBLE
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.color_option_border_selected, null)
        } else {
            viewSelected.visibility = GONE
            root.background = ResourcesCompat.getDrawable(resources, R.drawable.color_option_border, null)
        }
    }

    fun getCheckSize(): Int {
        return checkSize
    }

    fun setCheckSize(checkSize: Int) {
        this.checkSize = checkSize
        updateCheckMarkSize()
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
        updateBackColor()
    }

    fun isChecked(): Boolean {
        return checked
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
        updateChecked()
    }

    companion object {
        private const val DIMENSION_UNSET = -1
        private const val COLOR_UNSET = Int.MIN_VALUE
    }
}
