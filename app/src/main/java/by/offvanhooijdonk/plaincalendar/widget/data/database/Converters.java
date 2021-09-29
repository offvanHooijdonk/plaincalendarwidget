package by.offvanhooijdonk.plaincalendar.widget.data.database;

import android.arch.persistence.room.TypeConverter;

import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel;

public class Converters {

    @TypeConverter
    public static String cornersToInt(WidgetModel.Corners corners) {
        return corners.name();
    }

    @TypeConverter
    public static WidgetModel.Corners cornersFromInt(String value) {
        return WidgetModel.Corners.valueOf(value);
    }

    @TypeConverter
    public static String showEndDateToString(WidgetModel.ShowEndDate showEndDate) {
        return showEndDate.name();
    }

    @TypeConverter
    public static WidgetModel.ShowEndDate showEndDateFromString(String s) {
        return WidgetModel.ShowEndDate.valueOf(s);
    }
}
