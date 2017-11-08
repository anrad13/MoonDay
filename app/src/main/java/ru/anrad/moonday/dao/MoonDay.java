package ru.anrad.moonday.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Radoselskiy on 07.09.2016.
 */
@Deprecated
public class MoonDay {

    private static final String DATE_FORMAT_STRING = "EEE d MMMM y";
    private static  SimpleDateFormat DF = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());

    private long id;
    private Date begin;
    private Date end;

    public MoonDay(long _id, Date _begin, Date _end) {
        id = _id;
        begin = _begin;
        end = _end;
    }

    public long getId() { return id; }
    public Date getBegin() {return  begin;}
    public Date getEnd() {return  end;}
    public long getDays() { return (end.getTime() - begin.getTime())/86400000; }

    public void setBegin(Date _begin) { begin = _begin;}
    public void setEnd(Date _begin) { begin = _begin;}

    public String toFormatString() {
        StringBuilder s = new StringBuilder();
        s.append(DF.format(begin));
        s.append(" - ");
        s.append(DF.format(end));
        s.append(" (" + getDays() + " дней)");
        return s.toString();
    }

    public String toString() {return "MoonDay: id="+id+", begin="+begin.toString()+"end="+end;}
}
