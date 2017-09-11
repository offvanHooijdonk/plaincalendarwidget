package by.yahorfralou.plaincalendar.widget.data.database;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import io.reactivex.Maybe;

//@Dao
public interface WidgetDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<WidgetBean> widgets);

    //@Query("SELECT * FROM widgets")
    Maybe<List<WidgetBean>> getAll();
}
