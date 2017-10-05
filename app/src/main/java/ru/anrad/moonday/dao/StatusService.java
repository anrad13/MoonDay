package ru.anrad.moonday.dao;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Radoselskiy on 05.10.2017.
 */

public class StatusService {

    private final Context context;
    EventDataSource eventDataSource;

    public StatusService(Context context) {
        this.context = context;
        eventDataSource = new EventDataSource(context);
    }

    public Status getCurrentStatus() {
        Event e = getEventDataSource().getLast();
        //@TODO Statistic s = getStatistic(eventDataSource.getAll());
        return new Status(e.getDate(), e.getType(), null);
    }

    public Status changeStatus(Date finishDate) {
        Date eventDate = setMidnight(finishDate);
        StatusType eventType;
        Status currentStatus = getCurrentStatus();
        switch (currentStatus.getType()) {
            //Если нет текущего статуса то начинаем новый Красный статус
            case RED:
                eventType = StatusType.GREEN;
                break;
            case UNKNOWN: //Если нет текущего статуса то начинаем новый Красный статус
            case GREEN: //Если статус Зеленый то начинаем новый Красный статус
            default: //По умолчанию начинаем новый Красный статус
                eventType = StatusType.RED;
                break;
        }
        Event e = new Event(eventDate, eventType);
        getEventDataSource().put(e);
        return getCurrentStatus();
    }

    public Status undoCurrentStatus() {
        getEventDataSource().delete();
        return getCurrentStatus();
    }

    private EventDataSource getEventDataSource() {
        return eventDataSource;
    }

    private Date today() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }
    private Date setMidnight(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }


}
