package by.offvanhooijdonk.plaincalendar.widget.ui.configure.preview;

import android.app.Fragment;

public class PreviewWidgetFragment extends Fragment {
    /*private static final int DEFAULT_COLUMNS = 4;
    private static final int DEFAULT_ROWS = 2;

    private Context ctx;
    private WidgetModel widgetSettings;

    private ImageView imgBack;
    private TextView txtDate;
    private TextView txtDay;
    private ListView lstEvents;
    private ImageView viewDivider;
    private PreviewEventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.widget_calendars, container, false);
        }

        ctx = getActivity();
        adapter = new PreviewEventsAdapter(ctx);

        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        imgBack = v.findViewById(R.id.widgetBack);
        txtDate = v.findViewById(R.id.txtWidgetDate);
        txtDay = v.findViewById(R.id.txtWidgetDay);
        lstEvents = v.findViewById(R.id.listEvents);
        viewDivider = v.findViewById(R.id.viewDivider);

        lstEvents.setAdapter(adapter);

        Date now = new Date();
        txtDate.setText(DateHelperKt.formatDateOnly(now, false));
        txtDay.setText(DateHelperKt.formatDay(now));
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().getLayoutParams().width = calcWidthPx();
        getView().getLayoutParams().height = calcHeightPx();


    }

    public void attachWidgetSettings(WidgetModel initialSettings) {
        this.widgetSettings = initialSettings;
        adapter.attachWidgetSettings(widgetSettings);
        updatePreview();
    }

    public void updatePreview() {
        updateBackColor(widgetSettings.getBackgroundColor());
        updateOpacity(widgetSettings.getOpacity());
        updateTextColor(widgetSettings.getTextColor());
        updateCorners();
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

    private void updateCorners() {
        if (imgBack != null) {
            imgBack.setImageDrawable(AppCompatResources.getDrawable(ctx, R.drawable.widget_back_no_corner));
        }
    }

    *//*private void updateTextSize(int sizeDelta) {

    }*//*

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
            txtDate.setText(DateHelperKt.formatDateOnly(now, true));
        } else {
            txtDate.setText(DateHelperKt.formatDateOnly(now, false));
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
    }*/
}
