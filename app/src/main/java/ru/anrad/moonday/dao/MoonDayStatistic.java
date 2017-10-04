package ru.anrad.moonday.dao;

import android.app.Application;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MoonDayStatistic {

    private long hotDurationMSec, hotDurationDay;
    private long restDurationMSec, restDurationDay;
    private MoonDay lastDay;

    private boolean hasHot = false;
    private boolean hasRest = false;

    private static final long DAY_IN_MSEC = 86400000;

    public MoonDayStatistic(ArrayList<MoonDay> days) {
        if (days.size()>0) {
            hotDurationMSec = calcHotDuration(days);
            hotDurationDay = Math.round(hotDurationMSec / DAY_IN_MSEC);
            hasHot = true;
        }
        if (days.size()>1) {
            restDurationMSec = calcRestDuration(days);
            restDurationDay = Math.round(restDurationMSec / DAY_IN_MSEC);
            hasRest = true;
        }
    }

    public boolean hasHot() { return hasHot; }
    public boolean hasRest() { return hasRest; }

    public long getRestDurationDay() {
        return restDurationDay;
    }

    public long getHotDurationDay() {
        return hotDurationDay;
    }

    public MoonDay getLastDay() { return lastDay;}

    public Date getBeginForecast(Date end) {
        if (hasRest)
            //return new Date(end.getTime() + + restDurationDay*DAY_IN_MSEC);
            return addDaysToDate(end, restDurationDay);
        return null;
    }

    public long getBeginForecastLeftDays(Date end) {
        if (hasRest) {
            long res, beginForecastTime, todayTime;
            beginForecastTime = getBeginForecast(end).getTime();
            todayTime = today().getTime();
            res = (beginForecastTime - todayTime) / DAY_IN_MSEC;
            //if (((beginForecastTime - todayTime) % DAY_IN_MSEC) != 0) res += 1;
            return res;
        }
        return 0;
    }

    public Date getEndForecast(Date begin) {
        if (hasHot)
            //return new Date(begin.getTime() + hotDurationDay*DAY_IN_MSEC);
            return addDaysToDate(begin, hotDurationDay);
        return null;
    }

    public long getEndForecastLeftDays(Date begin) {
        if (hasHot) {
            long res, endForecastTime, todayTime;
            endForecastTime = getEndForecast(begin).getTime();
            todayTime = today().getTime();
            res = (endForecastTime - todayTime) / DAY_IN_MSEC;
            //if (((endForecastTime - todayTime) % DAY_IN_MSEC) != 0) res += 1;
            return res;
        }
        return 0;
    }

    private long calcHotDuration(ArrayList<MoonDay> days) {
        long v = 0;
        lastDay = null;
        for (MoonDay day : days) {
            v += day.getEnd().getTime() - day.getBegin().getTime();
            lastDay = day;
        }
        return v / days.size();
    }

    private long calcRestDuration(ArrayList<MoonDay> days) {
        long v = 0;
        for (int i = 1; i < days.size(); i++) {
            v += (days.get(i).getBegin().getTime() - days.get(i-1).getEnd().getTime());
        }
        return v/(days.size() - 1);
    }
    private Date today() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }

    private Date addDaysToDate(Date date, long days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,(int)days);
        return c.getTime();
    }
}
