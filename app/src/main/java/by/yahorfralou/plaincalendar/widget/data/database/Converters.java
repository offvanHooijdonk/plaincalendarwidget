package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.TypeConverter;

import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public class Converters {

    @TypeConverter
    public static int cornersToInt(WidgetBean.Corners corners) {
        return corners.getCode();
    }

    @TypeConverter
    public static WidgetBean.Corners cornersFromInt(int value) {
        return WidgetBean.Corners.fromInt(value);
    }
}
