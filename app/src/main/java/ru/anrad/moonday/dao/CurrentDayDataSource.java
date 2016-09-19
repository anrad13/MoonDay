package ru.anrad.moonday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class CurrentDayDataSource {


    //Singletone functionality
    private static CurrentDayDataSource instance;

    public static CurrentDayDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new CurrentDayDataSource(context.getApplicationContext());
        }
        return instance;
    }

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private boolean isEmpty = true;
    private boolean isActive;
    private Date begin;
    private Date end;

    private final String[] ALL_COLUMNS = {
            MySQLiteHelper.CURRENT_IS_ACTIVE,
            MySQLiteHelper.CURRENT_BEGIN,
            MySQLiteHelper.CURRENT_END,
    };

    private CurrentDayDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        open();
        loadCurrentDayFromDatabase();
        close();
    }

    private void open() throws SQLException {
        this.db = this.dbHelper.getWritableDatabase();
    }

    private void close() {
        this.dbHelper.close();
    }

    private void loadCurrentDayFromDatabase() {
        //Log.v("DutyDataSource", "loadItemsFromDatabase: state: ");
        Cursor cursor = db.query(MySQLiteHelper.CURRENT_TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            // В таблице есть значения текущего дня
            isEmpty = false;
            //isActive
            if (cursor.getLong(0) != 0) { isActive = true;} else { isActive = false;}
            //begin
            if (cursor.getLong(1) != 0) { begin = new Date(cursor.getLong(1));} else { begin = null;}
            //end
            if (cursor.getLong(2) != 0) { end = new Date(cursor.getLong(2));} else { end = null;}
        }
        else {
            // Значений нет. Используем по умолчанию пустые значения
            isEmpty = true;
            isActive = false;
            begin = null;
            end = null;
        }
        cursor.close();
    }

    public boolean isEmpty() {return isEmpty;}
    public boolean isActive() {return isActive;}
    public Date getBegin() { return begin; }
    public Date getEnd() { return end; }

    public void beginDay(Date _begin) throws IllegalArgumentException {
        if (isActive) throw new RuntimeException("Error: Can't do beginDay for active day");
        //@TODO Сделать проверку что введенное значение не меньше предыдущего значения и не более текущего дня
        if (_begin.after(today()))
            throw new IllegalArgumentException("Дата начала не может быть больше сегодня");
        if (end != null) {
            if (_begin.before(end))
                throw new IllegalArgumentException("Дата начала не может быть ранее даты окончания предыдущего дня");
        }

        isEmpty = false;
        isActive = true;
        begin = _begin;
        updateCurrentDayInDatabase();
    }

    public void finishDay(Date _end) throws IllegalArgumentException {
        if (!isActive) throw new RuntimeException("Error: Can't do finishDay for not active day");
        //@TODO Сделать проверку что введенное значение не меньше значения begin и не более текущего дня
        if (_end.after(today()))
            throw new IllegalArgumentException("Дата завершения не может быть больше сегодня");
        if (begin != null) {
            if (_end.before(begin))
                throw new IllegalArgumentException("Дата окончания не может быть ранее даты начала текущего дня");
        }

        isEmpty = false;
        isActive = false;
        end = _end;
        begin = null;
        updateCurrentDayInDatabase();
    }

    public void undoBeginDay() {
        if (!isActive) throw new RuntimeException("Error: Can't undo beginDay for not active day");
        isActive = false;
        begin = null;
        updateCurrentDayInDatabase();
    }

    public void clear() {
        open();
        db.delete(MySQLiteHelper.CURRENT_TABLE_NAME, null, null);
        loadCurrentDayFromDatabase();
        close();
    }

    private void updateCurrentDayInDatabase() {
        //@TODO Проверка консистентности данных
        ContentValues values = buildContentValuesForDatabase();
        open();
        //db.beginTransaction();
        db.delete(MySQLiteHelper.CURRENT_TABLE_NAME, null, null);
        db.insert(MySQLiteHelper.CURRENT_TABLE_NAME, null, values);
        //db.endTransaction();
        loadCurrentDayFromDatabase();
        close();
    }

    private ContentValues buildContentValuesForDatabase() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.CURRENT_IS_ACTIVE, isActive);
        //insert begin
        if (begin != null) {
            values.put(MySQLiteHelper.CURRENT_BEGIN, begin.getTime());
            //Log.v("DutyDataSource", "buildDutyContentValuesForDatabase: Put When");
        }
        else {
            values.put(MySQLiteHelper.CURRENT_BEGIN, MySQLiteHelper.DATE_NULL_VALUE);
        }
        //insert end
        if (end != null) {
            values.put(MySQLiteHelper.CURRENT_END, end.getTime());
            //Log.v("DutyDataSource", "buildDutyContentValuesForDatabase: Put When");
        }
        else {
            values.put(MySQLiteHelper.CURRENT_END, MySQLiteHelper.DATE_NULL_VALUE);
        }

        return values;
    }


    private Date today() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }
 }
