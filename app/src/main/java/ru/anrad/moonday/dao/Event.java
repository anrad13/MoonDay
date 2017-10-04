package ru.anrad.moonday.dao;

import java.util.Date;


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

}