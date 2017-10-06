package ru.anrad.moonday.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        Statistic s = getStatistic();
        Date forecast = null;
        switch (e.getType()) {
            case GREEN:
                forecast = forecastDate(e.getDate(), s.getRedDays());
                break;
            case RED:
                forecast = forecastDate(e.getDate(), s.getGreenDays());
                break;
            case UNKNOWN:
            default:
                 forecast = null;
        }
        return new Status(e.getDate(), e.getType(), forecast);
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

    public List<Interval> getHistory() {
        List<Interval> l = new ArrayList<>();
        Date begin = null;
        Date end = null;
        Interval interval = null;
        List<Event> events = getEventDataSource().getAll();
        for (Event e:events) {
            if (e.getType().equals(StatusType.RED)) { //Start new red interval
                begin = e.getDate();
                end = null;
                interval = null;
            }
            if (e.getType().equals(StatusType.GREEN)) { //Close & save current red interval
                end = e.getDate();
                interval = new Interval(begin, end);
                l.add(interval);
            }
        }
        return l;
    }

    public Statistic getStatistic() {
        return new Statistic(getHistory());
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
    private Date forecastDate(Date from, long days) {
            Calendar c = Calendar.getInstance();
            c.setTime(from);
            c.add(Calendar.DATE,(int)days);
            return c.getTime();
    }


}
