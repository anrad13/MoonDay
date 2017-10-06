package ru.anrad.moonday.dao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Radoselskiy on 06.10.2017.
 */

public class Interval {

    private Date begin;
    private Date end;
    long days;


    public Interval(Date begin, Date end) {
        this.begin = begin;
        this.end = end;
        days = calcIntervalLength(begin, end);
    }

    public Date getBegin() {
        return this.begin;
    }

    public Date getEnd() {
        return this.end;
    }

    public long getDays() {
        return days;
    }

    private static final long DAY_IN_MSEC = 86_400_000;
    private static long calcIntervalLength(Date begin, Date end) {
        //return TimeUnit.DAYS.convert(end.getTime() - begin.getTime(), TimeUnit.MICROSECONDS);
        return (end.getTime() - begin.getTime()) / DAY_IN_MSEC;
    }
}
