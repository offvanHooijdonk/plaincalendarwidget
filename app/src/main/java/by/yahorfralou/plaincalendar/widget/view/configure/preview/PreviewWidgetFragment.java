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
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public class PreviewWidgetFragment extends Fragment {
    private static final int DEFAULT_COLUMNS = 4;
    private static final int DEFAULT_ROWS = 2;

    private Context ctx;

    private ImageView imgBack;
    private TextView txtDate;
    private TextView txtDay;
    private ListView lstEvents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.widget_calendars, container, false);
        }

        ctx = getActivity();

        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        imgBack = v.findViewById(R.id.widgetBack);
        txtDate = v.findViewById(R.id.txtWidgetDate);
        txtDay = v.findViewById(R.id.txtWidgetDay);
        lstEvents = v.findViewById(R.id.listEvents);

        lstEvents.setAdapter(new PreviewEventsAdapter(ctx));
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().getLayoutParams().width = calcWidthPx();
        getView().getLayoutParams().height = calcHeightPx();

        Date now = new Date();
        txtDate.setText(DateHelper.formatDateOnly(now));
        txtDay.setText(DateHelper.formatDay(now));
    }

    public void setInitialParametersFromWidgetBean(WidgetBean initialParams) {
        updateBackColor(initialParams.getBackgroundColor());
        updateOpacity(initialParams.getOpacity());
    }

    public void updateBackColor(int color) {
        if (imgBack != null) {
            imgBack.setColorFilter(color);
        }
    }

    public void updateOpacity(int perCent) {
        if (imgBack != null) {
            imgBack.setImageAlpha((perCent * 0xFF) / 100);
        }
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
