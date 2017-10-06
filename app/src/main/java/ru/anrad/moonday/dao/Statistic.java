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
            redLengthMsec = calcRedDuration(days);
            redDays = Math.round((double)redLengthMsec / (double)DAY_IN_MSEC);
            hasRed = true;
        }
        if (days.size()>1) {
            greenLengthMsec = calcGreenDuration(days);
            greenDays = Math.round( (double)greenLengthMsec / (double)DAY_IN_MSEC);
            hasGreen = true;
        }
    }

    private long calcRedDuration(List<Interval> days) {
        long v = 0;
        for (Interval day : days) {
            v += day.getEnd().getTime() - day.getBegin().getTime();
            }
        return Math.round((double)v / (double)days.size());
    }

    private long calcGreenDuration(List<Interval> days) {
        long v = 0;
        for (int i = 1; i < days.size(); i++) {
            v += (days.get(i).getBegin().getTime() - days.get(i-1).getEnd().getTime());
        }
        return Math.round((double)v/(double)(days.size() - 1));
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

    @Override
    public String toString() {
        return "Statistic{" +
                "redLengthMsec=" + redLengthMsec +
                ", greenLengthMsec=" + greenLengthMsec +
                ", redDays=" + redDays +
                ", greenDays=" + greenDays +
                ", hasRed=" + hasRed +
                ", hasGreen=" + hasGreen +
                '}';
    }
}
