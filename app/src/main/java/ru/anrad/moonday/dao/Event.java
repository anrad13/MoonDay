package ru.anrad.moonday.dao;

import java.util.Date;
import java.util.Objects;


/**
 * Created by Game on 04.10.2017.
 */

public class Event {

    private Date date;
    private StatusType type;

    public Event(Date _date, StatusType _type){
        this.date = _date;
        this.type = _type;
    }

    public Event(){
        this.date = null;
        this.type = StatusType.UNKNOWN;
    }

    public Date getDate(){
        return date;
    }

    public StatusType getType(){
        return type;
    }

    @Override
    public String toString() {
        return "Event: begin=" + getDate().toString()+" type = " + getType().toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o == null || ! (o instanceof Event) ) return false;
        Event e = (Event) o;
        return e.getDate().equals(date) && e.getType().equals(type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type);
    }

}