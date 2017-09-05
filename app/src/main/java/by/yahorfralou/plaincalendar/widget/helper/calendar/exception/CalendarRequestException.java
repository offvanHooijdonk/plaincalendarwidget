package by.yahorfralou.plaincalendar.widget.helper.calendar.exception;

/**
 * Created by Yahor_Fralou on 9/5/2017 3:50 PM.
 */

public class CalendarRequestException extends RuntimeException {

    public CalendarRequestException(String message) {
        super(message);
    }

    public CalendarRequestException(Throwable cause) {
        super(cause);
    }
}
