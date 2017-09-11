package by.yahorfralou.plaincalendar.widget.data.calendars.observer;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class EventsContentObserver extends ContentObserver {
    private EventsChangeListener listener;

    public EventsContentObserver(Handler handler, EventsChangeListener l) {
        super(handler);

        this.listener = l;

        Log.i(LOGCAT, "Content Observer created");
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(LOGCAT, "onChange");

        if (listener != null) {
            listener.onDataChanged();
        }
    }

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    public interface EventsChangeListener {
        void onDataChanged();
    }
}
