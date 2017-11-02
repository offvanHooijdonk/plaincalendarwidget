package by.yahorfralou.plaincalendar.widget.view.configure.preview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import by.yahorfralou.plaincalendar.widget.model.EventBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.widget.EventsRemoteViewsFactory;

public class PreviewEventsAdapter extends BaseAdapter {

    private Context ctx;
    private List<EventBean> eventList;
    private WidgetBean widgetSettings;

    public PreviewEventsAdapter(Context context) {
        this.ctx = context;

        initStubEvents();
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eventList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(ctx).inflate(R.layout.item_event_widget, parent, false);
        }
        EventBean eventBean = eventList.get(position);

        TextView txtDateRange = v.findViewById(R.id.txtDateRange);
        TextView txtEventTitle = v.findViewById(R.id.txtEventTitle);
        ImageView imgColor = v.findViewById(R.id.imgColor);

        txtEventTitle.setText(eventBean.getTitle());

        // TODO use PrefHelper for defaults
        // TODO implement 'Show Event End' logic
        String eventDateText = EventsRemoteViewsFactory.formatDateRange(ctx, eventBean.getDateStart(), eventBean.getDateEnd(), eventBean.isAllDay(),
                widgetSettings != null ? widgetSettings.getShowDateTextLabel() : true);
        txtDateRange.setText(eventDateText);

        if (widgetSettings != null) {
            float textSizeDate = WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_date_text, widgetSettings.getTextSizeDelta());
            float textSizeEvent = WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetSettings.getTextSizeDelta());

            txtEventTitle.setTextSize(textSizeEvent);
            txtEventTitle.setTextColor(widgetSettings.getTextColor());

            txtDateRange.setTextSize(textSizeDate);
            txtDateRange.setTextColor(widgetSettings.getTextColor());

            if (widgetSettings.getShowEventColor()) {
                if (eventBean.getEventColor() != null) {
                    imgColor.setColorFilter(eventBean.getEventColor());
                }
                imgColor.setVisibility(View.VISIBLE);
            } else {
                imgColor.setVisibility(View.GONE);
            }
        }

        return v;
    }

    public void attachWidgetSettings(WidgetBean widgetBean) {
        this.widgetSettings = widgetBean;
        notifyDataSetChanged();
    }

    private void initStubEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        eventList = new ArrayList<>();

        EventBean eventBean = new EventBean();
        eventBean.setId(1);
        eventBean.setAllDay(false);
        eventBean.setTitle(ctx.getString(R.string.fish_event_title_1));
        eventBean.setEventColor(ctx.getResources().getColor(R.color.colorPrimary));
        eventBean.setDateStart(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 3);
        eventBean.setDateEnd(calendar.getTime());
        eventList.add(eventBean);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        eventBean = new EventBean();
        eventBean.setId(2);
        eventBean.setAllDay(true);
        eventBean.setTitle(ctx.getString(R.string.fish_event_title_2));
        eventBean.setEventColor(ctx.getResources().getColor(R.color.colorAccent));
        eventBean.setDateStart(calendar.getTime());
        eventList.add(eventBean);
    }

}
