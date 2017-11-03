package ru.anrad.moonday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Game on 04.10.2017.
 */

public class EventDataSource {

    //private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private static final String[] EVENT_TABLE_ALL_COLUMNS = {
            MySQLiteHelper.EVENT_ID,
            MySQLiteHelper.EVENT_BEGIN,
            MySQLiteHelper.EVENT_TYPE,
    };

    private static final int EVENT_COL_ID = 0;
    private static final int EVENT_COL_BEGIN = 1;
    private static final int EVENT_COL_TYPE = 2;

    private static final int GREEN_TYPE = 0;
    private static final int RED_TYPE = 1;

    public EventDataSource(Context context){
       dbHelper = new MySQLiteHelper(context);
    }

    /*
    private SQLiteDatabase openDB() {
       return this.dbHelper.getWritableDatabase();
    }
    */


    private void closeDB() {
        this.dbHelper.close();
    }


        /**
         * Создает новое событие в таблице
         */
        public void put(Event event){
            ContentValues values = eventToContentValues(event);
            getDB().insert(MySQLiteHelper.EVENT_TABLE_NAME, null, values);
            closeDB();
        }

        /**
         * Возвращает последнее событие в таблице.
         * Последнее = самая поздняя дата
         */
        public Event getLast(){
            Event e;
            Cursor cursor = getCursor();
            if (cursor.moveToLast()) {
                // Есть значение
                e = cursorToEvent(cursor);
            }
            else {
                // Значений нет. Используем по умолчанию пустое событие
                e = new Event();
            }
            cursor.close();
            closeDB();
            return e;
        }

        /**
         * Возвращает все события из таблицы отсортированные по "возрасту" начиная с
         * самого первого (старшего)
         */
        public List<Event> getAll(){
            List<Event> l = new ArrayList<>();
            Cursor cursor = getCursor();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Event e = cursorToEvent(cursor);
                l.add(e);
                //Log.v(getClass().getName(), "loadItemsFromDatabase: item: " + d.toString());
                cursor.moveToNext();
            }
            cursor.close();
            closeDB();
            return l;
        }

        /**
         * Удаляет последнее событие из таблицы событий
         */
        public void delete(){
            Cursor cursor = getCursor();
            if (cursor.moveToLast()) {
                // Есть значение
                long id = cursor.getLong(EVENT_COL_ID);
                //@TODO for delete record
                //db.execSQL("DELETE FROM " + MySQLiteHelper.EVENT_TABLE_NAME + " WHERE " + MySQLiteHelper.EVENT_ID + " = " + id + "");
                getDB().delete(MySQLiteHelper.EVENT_TABLE_NAME, MySQLiteHelper.EVENT_ID + " = " + id, null);
            }
            cursor.close();
            closeDB();
        }

    private Event cursorToEvent (Cursor cursor){
        Date begin;
        StatusType type;
        begin = new Date(cursor.getLong(EVENT_COL_BEGIN));
        if (cursor.getInt(EVENT_COL_TYPE) == RED_TYPE) {  type = StatusType.RED;} else { type = StatusType.GREEN;}
        return new Event(begin, type);
    }

    private Cursor getCursor() {
        return getDB().
                        query(
                                MySQLiteHelper.EVENT_TABLE_NAME,
                                EVENT_TABLE_ALL_COLUMNS,
                                null,
                                null,
                                null,
                                null,
                                MySQLiteHelper.EVENT_BEGIN
                        );
    }

    private SQLiteDatabase getDB() {
        return dbHelper.getWritableDatabase();
    }

    private ContentValues eventToContentValues(Event event) {
        ContentValues values = new ContentValues();
        int type;
        if (event.getType() == StatusType.GREEN) {  type = GREEN_TYPE;} else { type = RED_TYPE;}
        values.put(MySQLiteHelper.EVENT_TYPE, type);
        values.put(MySQLiteHelper.EVENT_BEGIN, event.getDate().getTime());
        return values;
    }


}
