package by.yahorfralou.plaincalendar.widget.view.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import by.yahorfralou.plaincalendar.widget.R;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ColorSingleOptionView extends FrameLayout {
    private static final int DIMENSION_UNSET = -1;
    private static final int COLOR_UNSET = Integer.MIN_VALUE;

    private int checkSize = DIMENSION_UNSET;
    private int color = COLOR_UNSET;
    private boolean checked = false;

    private View root;
    private View viewSelected;

    public ColorSingleOptionView(@NonNull Context context) {
        super(context);
        init();
    }

    public ColorSingleOptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorSingleOptionView, 0, 0);
        try {
            checkSize = ta.getDimensionPixelSize(R.styleable.ColorSingleOptionView_checkSize, DIMENSION_UNSET);
            color = ta.getColor(R.styleable.CalendarIconView_calendarColor, COLOR_UNSET);
            checked = ta.getBoolean(R.styleable.CalendarIconView_calendarColor, false);
        } catch (Exception e) {
            Log.e(LOGCAT, "Error reading attributes for Calendar Icon", e);
        } finally {
            ta.recycle();
        }

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_color_single_option, this);

        root = findViewById(R.id.root);
        viewSelected = findViewById(R.id.imgSelected);

        updateBackColor();
        updateCheckMarkSize();
        updateChecked();
    }

    private void updateBackColor() {
        if (color != COLOR_UNSET) {
            setBackgroundColor(color);
        }
    }

    private void updateCheckMarkSize() {
        if (checkSize != DIMENSION_UNSET) {
            viewSelected.getLayoutParams().width = checkSize;
            viewSelected.getLayoutParams().height = checkSize;
        }
    }

    private void updateChecked() {
        if (checked) {
            viewSelected.setVisibility(VISIBLE);
            root.setBackground(getResources().getDrawable(R.drawable.color_option_border_selected, null));
        } else {
            viewSelected.setVisibility(GONE);
            root.setBackground(getResources().getDrawable(R.drawable.color_option_border, null));
        }
    }

    public int getCheckSize() {
        return checkSize;
    }

    public void setCheckSize(int checkSize) {
        this.checkSize = checkSize;
        updateCheckMarkSize();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        updateBackColor();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        updateChecked();
    }
}
