package by.offvanhooijdonk.plaincalendar.widget.view.customviews

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import by.offvanhooijdonk.plaincalendar.widget.view.customviews.CalendarIconView
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.app.App
import android.util.TypedValue
import android.widget.ImageView
import java.lang.Exception

class CalendarIconView : FrameLayout {
    private lateinit var imgBack: ImageView
    private lateinit var imgColor: ImageView
    private lateinit var txtSymbol: TextView
    private var imageSize = 0
    private var textSize = 0
    private var symbol = DEFAULT_CHAR
    private var calendarColor = 0
    private var borderColor = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.CalendarIconView, 0, 0)
        try {
            imageSize = ta.getDimensionPixelSize(R.styleable.CalendarIconView_imageSize, DIMENSION_UNSET)
            textSize = ta.getDimensionPixelSize(R.styleable.CalendarIconView_textSize, DIMENSION_UNSET)
            val symbolString = ta.getString(R.styleable.CalendarIconView_symbol)
            if (symbolString != null && symbolString.isNotEmpty()) {
                symbol = symbolString[0]
            }
            calendarColor = ta.getColor(R.styleable.CalendarIconView_calendarColor, getContext().resources.getColor(R.color.md_blue_400))
            borderColor = ta.getColor(R.styleable.CalendarIconView_borderColor, getContext().resources.getColor(R.color.md_white_1000))
        } catch (e: Exception) {
            Log.e(App.LOGCAT, "Error reading attributes for Calendar Icon", e)
        } finally {
            ta.recycle()
        }
        init()
    }

    private fun init() {
        inflate(context, R.layout.view_calendar_icon, this)
        imgBack = findViewById(R.id.imgBack)
        imgColor = findViewById(R.id.imgColor)
        txtSymbol = findViewById(R.id.txtSymbol)
        updateImageSize()
        updateTextSize()
        updateSymbol()
        updateColor()
        updateBorderColor()
    }

    private fun updateImageSize() {
        if (imageSize != DIMENSION_UNSET && imageSize > 0) {
            imgBack.layoutParams.height = imageSize
            imgBack.layoutParams.width = imageSize
        }
    }

    private fun updateTextSize() {
        if (textSize != DIMENSION_UNSET && textSize > 0) {
            txtSymbol.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }
    }

    private fun updateSymbol() {
        txtSymbol.text = symbol.toString()
    }

    private fun updateColor() {
        imgColor.setColorFilter(calendarColor)
    }

    private fun updateBorderColor() {
        imgBack.setColorFilter(borderColor)
    }

    fun getImageSize(): Int {
        return imageSize
    }

    fun setImageSize(imageSize: Int) {
        this.imageSize = imageSize
        updateImageSize()
    }

    fun getTextSize(): Int {
        return textSize
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        updateTextSize()
    }

    fun getSymbol(): Char {
        return symbol
    }

    fun setSymbol(symbol: Char) {
        this.symbol = symbol
        updateSymbol()
    }

    fun getCalendarColor(): Int {
        return calendarColor
    }

    fun setCalendarColor(calendarColor: Int) {
        this.calendarColor = calendarColor
        updateColor()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(borderColor: Int) {
        this.borderColor = borderColor
        updateBorderColor()
    }

    companion object {
        private const val DEFAULT_CHAR = 'o'
        private const val DIMENSION_UNSET = -1
    }
}
