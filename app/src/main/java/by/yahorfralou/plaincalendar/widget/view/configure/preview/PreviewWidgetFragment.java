package by.yahorfralou.plaincalendar.widget.view.configure.preview;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.helper.DateHelper;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public class PreviewWidgetFragment extends Fragment {
    private static final int DEFAULT_COLUMNS = 4;
    private static final int DEFAULT_ROWS = 2;

    private Context ctx;
    private WidgetBean widgetSettings;

    private ImageView imgBack;
    private TextView txtDate;
    private TextView txtDay;
    private ListView lstEvents;
    private ImageView viewDivider;
    private PreviewEventsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.widget_calendars, container, false);
        }

        ctx = getActivity();
        adapter = new PreviewEventsAdapter(ctx);

        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        imgBack = v.findViewById(R.id.widgetBack);
        txtDate = v.findViewById(R.id.txtWidgetDate);
        txtDay = v.findViewById(R.id.txtWidgetDay);
        lstEvents = v.findViewById(R.id.listEvents);
        viewDivider = v.findViewById(R.id.viewDivider);

        lstEvents.setAdapter(adapter);

        Date now = new Date();
        txtDate.setText(DateHelper.formatDateOnly(now, false));
        txtDay.setText(DateHelper.formatDay(now));
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().getLayoutParams().width = calcWidthPx();
        getView().getLayoutParams().height = calcHeightPx();


    }

    public void attachWidgetSettings(WidgetBean initialSettings) {
        this.widgetSettings = initialSettings;
        adapter.attachWidgetSettings(widgetSettings);
        updatePreview();
    }

    public void updatePreview() {
        updateBackColor(widgetSettings.getBackgroundColor());
        updateOpacity(widgetSettings.getOpacity());
        updateTextColor(widgetSettings.getTextColor());
        updateCorners(widgetSettings.getCorners());
        updateShowTodayDate(widgetSettings.getShowTodayDate());
        updateShowDayOfWeek(widgetSettings.getShowTodayDayOfWeek() && widgetSettings.getShowTodayDate());
        updateShowDivider(widgetSettings.getShowDateDivider() && widgetSettings.getShowTodayDate());
        updateShowTodayLeadingZeroChange(widgetSettings.getShowTodayLeadingZero());

        adapter.notifyDataSetChanged();
    }


    private void updateBackColor(int color) {
        if (imgBack != null) {
            imgBack.setColorFilter(color);
        }
    }

    private void updateOpacity(int perCent) {
        if (imgBack != null) {
            imgBack.setImageAlpha((perCent * 0xFF) / 100);
        }
    }

    private void updateCorners(WidgetBean.Corners corners) {
        if (imgBack != null) {
            int resId = WidgetHelper.getBackgroundRes(corners);
            imgBack.setImageDrawable(ctx.getDrawable(resId));
        }
    }

    /*private void updateTextSize(int sizeDelta) {

    }*/

    private void updateTextColor(int color) {
        txtDate.setTextColor(color);
        txtDay.setTextColor(color);
        viewDivider.setColorFilter(color);
    }

    private void updateShowTodayDate(boolean isShow) {
        if (isShow) {
            txtDate.setVisibility(View.VISIBLE);
            txtDay.setVisibility(View.VISIBLE);
            viewDivider.setVisibility(View.VISIBLE);
        } else {
            txtDate.setVisibility(View.GONE);
            txtDay.setVisibility(View.GONE);
            viewDivider.setVisibility(View.GONE);
        }
    }

    private void updateShowDayOfWeek(boolean isShow) {
        txtDay.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void updateShowTodayLeadingZeroChange(boolean isShow) {
        Date now = new Date();
        if (isShow) {
            txtDate.setText(DateHelper.formatDateOnly(now, true));
        } else {
            txtDate.setText(DateHelper.formatDateOnly(now, false));
        }
    }

    private void updateShowDivider(boolean isShow) {
        viewDivider.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private int calcWidthPx() {
        int widthDp = 74 * DEFAULT_COLUMNS - 2;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, getResources().getDisplayMetrics());
    }

    private int calcHeightPx() {
        int heightDp = 84 * DEFAULT_ROWS;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, getResources().getDisplayMetrics());
    }
}
