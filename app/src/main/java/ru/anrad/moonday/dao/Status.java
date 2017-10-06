package ru.anrad.moonday.dao;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Radoselskiy on 05.10.2017.
 */

public class Status {

    private Date begin;
    private StatusType type;
    private Date forecast;

    public Status(Date begin, StatusType type, Date forecast) {
        this.begin = begin;
        this.type = type;
        this.forecast = forecast;
        //Log.v(getClass().getName(), " Create Status = " + this.toString());
    }

    public Date getBegin() {
        return begin;
    }

    public StatusType getType() {
        return type;
    }

    public Date getForecast() {
        return forecast;
    }

    private static final long DAY_IN_MSEC = 86_400_000;
    public long getForecastLeftDays() {
        long res, forecastTime, todayTime;
        forecastTime = getForecast().getTime();
        todayTime = today().getTime();

        res = (forecastTime - todayTime) / DAY_IN_MSEC;
        return res;
    }

    private Date today() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }

    @Override
    public String toString() {
        return "Status{" +
                "begin=" + begin +
                ", type=" + type +
                ", forecast=" + forecast +
                '}';
    }
}
