package by.offvanhooijdonk.plaincalendar.widget.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetCalendarModel

@Database(entities = [CalendarModel::class, WidgetModel::class, WidgetCalendarModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
    abstract fun widgetDao(): WidgetDao

    companion object {
        private const val DB_NAME = "plain-calendar-widget-v0.6"

        /*private static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE widgets ADD COLUMN opacity INTEGER;");
        }
    };*/
        fun buildDatabase(ctx: Context?): AppDatabase {
            return Room.databaseBuilder(ctx!!, AppDatabase::class.java, DB_NAME) /*.addMigrations(MIGRATION_1_2)*/
                .build()
        }
    }
}
