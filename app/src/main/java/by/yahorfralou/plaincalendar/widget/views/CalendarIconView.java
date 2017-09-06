package by.yahorfralou.plaincalendar.widget.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import by.yahorfralou.plaincalendar.widget.R;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarIconView extends FrameLayout {
    private static final char DEFAULT_CHAR = 'o';
    private static final int DIMENSION_UNSET = -1;

    private ImageView imgBack;
    private ImageView imgColor;
    private TextView txtSymbol;

    private int imageSize;
    private int textSize;
    private char symbol = DEFAULT_CHAR;
    private int backColor;

    public CalendarIconView(@NonNull Context context) {
        super(context);

        init();
    }

    public CalendarIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarIconView, 0, 0);
        try {
            imageSize = ta.getDimensionPixelSize(R.styleable.CalendarIconView_imageSize, DIMENSION_UNSET);
            textSize = ta.getDimensionPixelSize(R.styleable.CalendarIconView_textSize, DIMENSION_UNSET);

            String symbolString = ta.getString(R.styleable.CalendarIconView_symbol);
            if (symbolString != null && !"".equals(symbolString)) {
                symbol = symbolString.charAt(0);
            }

            backColor = ta.getColor(R.styleable.CalendarIconView_backgroundColor, getContext().getResources().getColor(R.color.md_blue_400));
        } catch (Exception e) {
            Log.e(LOGCAT, "Error reading attributes for Calendar Icon", e);
        } finally {
            ta.recycle();
        }

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_icon, this);

        imgBack = findViewById(R.id.imgBack);
        imgColor = findViewById(R.id.imgColor);
        txtSymbol = findViewById(R.id.txtSymbol);

        updateImageSize();
        updateTextSize();
        updateSymbol();
        updateBackColor();
    }

    private void updateImageSize() {
        if (imageSize != DIMENSION_UNSET && imageSize > 0) {
            imgBack.getLayoutParams().height = imageSize;
            imgBack.getLayoutParams().width = imageSize;
        }
    }

    private void updateTextSize() {
        if (textSize != DIMENSION_UNSET && textSize > 0) {
            txtSymbol.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private void updateSymbol() {
        txtSymbol.setText(String.valueOf(symbol));
    }

    private void updateBackColor() {
        imgColor.setColorFilter(backColor);
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
        updateImageSize();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updateTextSize();
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        updateSymbol();
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backgroundColor) {
        this.backColor = backgroundColor;
        updateBackColor();
    }
}
