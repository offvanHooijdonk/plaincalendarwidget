package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.TypeConverter;

import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public class Converters {

    @TypeConverter
    public static String cornersToInt(WidgetBean.Corners corners) {
        return corners.name();
    }

    @TypeConverter
    public static WidgetBean.Corners cornersFromInt(String value) {
        return WidgetBean.Corners.valueOf(value);
    }

    @TypeConverter
    public static String showEndDateToString(WidgetBean.ShowEndDate showEndDate) {
        return showEndDate.name();
    }

    @TypeConverter
    public static WidgetBean.ShowEndDate showEndDateFromString(String s) {
        return WidgetBean.ShowEndDate.valueOf(s);
    }
}
