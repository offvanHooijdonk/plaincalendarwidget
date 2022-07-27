package by.offvanhooijdonk.plaincalendar.widget.ui.configure.preview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@Deprecated
public class PreviewEventsAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
/*

    private Context ctx;
    private List<EventModel> eventList;
    private WidgetModel widgetSettings;

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
        EventModel eventModel = eventList.get(position);

        TextView txtDateRange = v.findViewById(R.id.txtDateRange);
        TextView txtEventTitle = v.findViewById(R.id.txtEventTitle);
        ImageView imgColor = v.findViewById(R.id.imgColor);

        txtEventTitle.setText(eventModel.getTitle());

        // TODO use PrefHelper for defaults
        // TODO implement 'Show Event End' logic
        String eventDateText = DateHelper.formatEventDateRange(ctx, eventModel.getDateStart(), eventModel.getDateEnd(), eventModel.isAllDay(),
                widgetSettings != null ? widgetSettings.getShowDateTextLabel() : true,
                widgetSettings != null ? widgetSettings.getShowEndDate() : WidgetModel.ShowEndDate.NEVER);
        txtDateRange.setText(eventDateText);

        if (widgetSettings != null) {
            float textSizeDate = WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_date_text, widgetSettings.getTextSizeDelta());
            float textSizeEvent = WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetSettings.getTextSizeDelta());

            txtEventTitle.setTextSize(textSizeEvent);
            txtEventTitle.setTextColor(widgetSettings.getTextColor());

            txtDateRange.setTextSize(textSizeDate);
            txtDateRange.setTextColor(widgetSettings.getTextColor());

            if (widgetSettings.getShowEventColor()) {
                if (eventModel.getEventColor() != null) {
                    imgColor.setColorFilter(eventModel.getEventColor());
                }
                imgColor.setVisibility(View.VISIBLE);
            } else {
                imgColor.setVisibility(View.GONE);
            }
        }

        return v;
    }

    public void attachWidgetSettings(WidgetModel widgetModel) {
        this.widgetSettings = widgetModel;
        notifyDataSetChanged();
    }

    private void initStubEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        eventList = new ArrayList<>();

        EventModel eventModel = new EventModel();
        eventModel.setId(1);
        eventModel.setAllDay(false);
        eventModel.setTitle(ctx.getString(R.string.fish_event_title_1));
        eventModel.setEventColor(ctx.getResources().getColor(R.color.colorPrimary));
        eventModel.setDateStart(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 3);
        eventModel.setDateEnd(calendar.getTime());
        eventList.add(eventModel);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        eventModel = new EventModel();
        eventModel.setId(2);
        eventModel.setAllDay(true);
        eventModel.setTitle(ctx.getString(R.string.fish_event_title_2));
        eventModel.setEventColor(ctx.getResources().getColor(R.color.colorAccent));
        eventModel.setDateStart(calendar.getTime());
        eventList.add(eventModel);
    }
*/

}
