package by.yahorfralou.plaincalendar.widget.configure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.model.CalendarBean;

public class CalendarsChoiceAdapter extends BaseAdapter {
    private Context ctx;
    private List<CalendarBean> calendars;
    private SelectionListener listener;

    public CalendarsChoiceAdapter(@NonNull Context context, @NonNull List<CalendarBean> calendars, SelectionListener l) {
        this.ctx = context;
        this.calendars = calendars;
        this.listener = l;
    }

    @Override
    public int getCount() {
        return calendars.size();
    }

    @Override
    public Object getItem(int i) {
        return calendars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return calendars.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.item_calendar_choice, parent, false);
        }
        CalendarBean bean = calendars.get(i);

        final CheckBox chbSelect = view.findViewById(R.id.chbSelect);
        chbSelect.setChecked(bean.isSelected());

        TextView txtCalendarName = view.findViewById(R.id.txtCalendarName);
        txtCalendarName.setText(bean.getDisplayName());

        TextView txtAccountName = view.findViewById(R.id.txtAccountName);
        txtAccountName.setText(bean.getAccountName());

        if (bean.getColor() != null) {
            ImageView imgColor = view.findViewById(R.id.imgColor);
            imgColor.setColorFilter(bean.getColor());
        }

        view.setOnClickListener(view1 -> {
            chbSelect.toggle();
            if (listener != null) {
                listener.onItemToggle(i, chbSelect.isChecked());
            }
        });

        return view;
    }

    public interface SelectionListener {
        void onItemToggle(int index, boolean isSelected);
    }
}
