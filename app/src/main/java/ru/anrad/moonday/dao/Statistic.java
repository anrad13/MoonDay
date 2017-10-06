package ru.anrad.moonday.dao;

import java.util.List;

/**
 * Created by Radoselskiy on 06.10.2017.
 */

public class Statistic {

    private long redLengthMsec;
    private long greenLengthMsec;

    private long redDays;
    private long greenDays;

    private boolean hasRed = false;
    private boolean hasGreen = false;

    private static final long DAY_IN_MSEC = 86400000;
    private static final long N = 5;

    public Statistic(List<Interval> days) {
        if (days.size()>0) {
            redLengthMsec = calcHotDuration(days);
            redDays = redLengthMsec / DAY_IN_MSEC;
            hasRed = true;
        }
        if (days.size()>1) {
            greenLengthMsec = calcRestDuration(days);
            greenDays = Math.round(greenLengthMsec / DAY_IN_MSEC);
            hasGreen = true;
        }
    }

    private long calcHotDuration(List<Interval> days) {
        long v = 0;
        for (Interval day : days) {
            v += day.getEnd().getTime() - day.getBegin().getTime();
            }
        return v / days.size();
    }

    private long calcRestDuration(List<Interval> days) {
        long v = 0;
        for (int i = 1; i < days.size(); i++) {
            v += (days.get(i).getBegin().getTime() - days.get(i-1).getEnd().getTime());
        }
        return v/(days.size() - 1);
    }

    public long getRedLengthMsec() {
        return redLengthMsec;
    }

    public long getGreenLengthMsec() {
        return greenLengthMsec;
    }

    public long getRedDays() {
        return redDays;
    }

    public long getGreenDays() {
        return greenDays;
    }

    public boolean isHasRed() {
        return hasRed;
    }

    public boolean isHasGreen() {
        return hasGreen;
    }
}
