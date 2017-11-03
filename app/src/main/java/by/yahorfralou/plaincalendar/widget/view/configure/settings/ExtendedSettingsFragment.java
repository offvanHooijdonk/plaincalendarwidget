package by.yahorfralou.plaincalendar.widget.view.configure.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ExtendedSettingsFragment extends Fragment {
    //private Context ctx;
    private ExtendedOptionsListener listener;

    private Switch swShowTodayDate;
    private Switch swShowDayOfWeek;
    private Switch swShowTodayLeadingZero;
    private Switch swShowDivider;
    private Spinner spShowEventEnd;
    private Switch swShowEventColor;
    private Switch swShowDateAsLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.inc_exp_settings, container, false);
        }
        //ctx = getActivity();

        swShowTodayDate = v.findViewById(R.id.switchShowTodayDate);
        swShowDayOfWeek = v.findViewById(R.id.switchShowDayOfWeek);
        swShowDivider = v.findViewById(R.id.switchShowDivider);
        swShowEventColor = v.findViewById(R.id.switchShowEventColor);
        swShowTodayLeadingZero = v.findViewById(R.id.switchShowTodayLeadingZero);
        swShowDateAsLabel = v.findViewById(R.id.switchShowDateAsLabel);
        spShowEventEnd = v.findViewById(R.id.spShowEventEnd);

        swShowTodayDate.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        swShowDayOfWeek.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        swShowDivider.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        swShowEventColor.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        swShowTodayLeadingZero.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        swShowDateAsLabel.setOnCheckedChangeListener((sw, isChecked) -> onCheckChange(sw.getId(), isChecked));
        spShowEventEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    Log.i(LOGCAT, "Event End option selected:" + position + ", id=" + id);
                    listener.onShowEventEndChange(WidgetBean.ShowEndDate.fromCode(position));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return v;
    }

    public void setWidgetOptions(WidgetBean widgetBean) {
        if (widgetBean != null) {
            swShowTodayDate.setChecked(widgetBean.getShowTodayDate());
            swShowDayOfWeek.setChecked(widgetBean.getShowTodayDayOfWeek());
            swShowTodayLeadingZero.setChecked(widgetBean.getShowTodayLeadingZero());
            swShowEventColor.setChecked(widgetBean.getShowEventColor());
            swShowDivider.setChecked(widgetBean.getShowDateDivider());
            swShowDateAsLabel.setChecked(widgetBean.getShowDateTextLabel());
            spShowEventEnd.setSelection(widgetBean.getShowEndDate().getCode());
        }
    }

    public void setListener(ExtendedOptionsListener listener) {
        this.listener = listener;
    }

    private void onCheckChange(int resId, boolean isChecked) {
        if (listener != null) {
            switch (resId) {
                case R.id.switchShowTodayDate: {
                    swShowDayOfWeek.setEnabled(isChecked);
                    swShowDivider.setEnabled(isChecked);
                    swShowTodayLeadingZero.setEnabled(isChecked);
                    listener.onShowTodayDateChange(isChecked);
                }
                break;
                case R.id.switchShowDayOfWeek: listener.onShowDayOfWeekChange(isChecked);
                break;
                case R.id.switchShowTodayLeadingZero: listener.onShowTodayLeadingZeroChange(isChecked);
                break;
                case R.id.switchShowEventColor: listener.onShowEventColorChange(isChecked);
                break;
                case R.id.switchShowDivider: listener.onShowDividerChange(isChecked);
                break;
                case R.id.switchShowDateAsLabel: listener.onShowDateAsLabelChange(isChecked);
                break;

            }
        }
    }

    public interface ExtendedOptionsListener {
        void onShowTodayDateChange(boolean isShow);

        void onShowDayOfWeekChange(boolean isShow);

        void onShowTodayLeadingZeroChange(boolean isShow);

        void onShowEventColorChange(boolean isShow);

        void onShowDividerChange(boolean isShow);

        void onShowDateAsLabelChange(boolean isShow);

        void onShowEventEndChange(WidgetBean.ShowEndDate endDateOption);
    }
}
