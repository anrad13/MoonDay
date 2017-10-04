package ru.anrad.moonday.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

/**
 * Created by Game on 04.10.2017.
 */

public class EventDataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private static final String[] EVENT_TABLE_ALL_COLUMNS = {
            MySQLiteHelper.EVENT_BEGIN,
            MySQLiteHelper.EVENT_TYPE,
    };

    private static final int GREEN_TYPE = 0;
    private static final int RED_TYPE = 1;

    public EventDataSource(Context context){
       dbHelper = new MySQLiteHelper(context);
    }

    private SQLiteDatabase openDB() {
       return this.dbHelper.getWritableDatabase();
    }

    private void close() {
        this.dbHelper.close();
    }

        /**
         * Создает новое событие в таблице
         *
         * @param event
         */
        public void put(Event event){

        }

        /**
         * Возвращает последнее событие в таблице.
         * Последнее = самая поздняя дата
         */
        public Event getLast(){
            Event e;
            Cursor cursor =
                    dbHelper.
                    getWritableDatabase().
                    query(
                            MySQLiteHelper.EVENT_TABLE_NAME,
                            EVENT_TABLE_ALL_COLUMNS,
                            null,
                            null,
                            null,
                            null,
                            MySQLiteHelper.EVENT_BEGIN
                    );
            if (cursor.moveToLast()) {
                // В таблице есть значения
                //begin
                Date begin = new Date(cursor.getLong(0));
                //type
                StatusType type;
                if (cursor.getInt(1) == RED_TYPE) {  type = StatusType.RED;} else { type = StatusType.GREEN;}
                e = new Event(begin, type);
            }
            else {
                // Значений нет. Используем по умолчанию пустое событие
                e = new Event();
            }
            cursor.close();

            return e;
        }

        /**
         * Возвращает все события из таблицы отсортированные по "возрасту" начиная с
         * самого старшего
         */
        public List<Event> getAll(){
            return null;
        }

        /**
         * Удаляет последнее событие из таблицы событий
         */
        public void delete(){

        }

}
