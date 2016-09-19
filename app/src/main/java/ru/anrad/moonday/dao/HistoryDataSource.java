package ru.anrad.moonday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class HistoryDataSource {

    //Singletone functionality
    private static HistoryDataSource instance;

    public static HistoryDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryDataSource(context.getApplicationContext());
        }
        return instance;
    }

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private ArrayList<MoonDay> items = new ArrayList<>();
    private MoonDayStatistic statistic;

    private final String[] ALL_COLUMNS = {
            MySQLiteHelper.HISTORY_ID,
            MySQLiteHelper.HISTORY_BEGIN,
            MySQLiteHelper.HISTORY_END,
    };

    private HistoryDataSource(Context context) {
        this.dbHelper = new MySQLiteHelper(context);
        this.db = this.dbHelper.getWritableDatabase();
        loadItemsFromDatabase();
    }

    private void open() throws SQLException { this.db = this.dbHelper.getWritableDatabase();
    }
    public void close() {
        this.dbHelper.close();
    }

    public MoonDayStatistic getStatistic() {
        return statistic;
    }

    public ArrayList<MoonDay> getItems() {
        //loadItemsFromDatabase();
        //Collections.sort(out, new DutyComparator());
        return items;
    }

    public MoonDay getItem(long _id){
        for (MoonDay d : items) {
            if (d.getId() == _id) return d;
        }
        return null;
    }

    public void putItem(Date begin, Date end) {
        putItemInDatabase(begin, end);
        loadItemsFromDatabase();
    }

    public void updateItem(MoonDay _item) {
        MoonDay item = getItemFromDatabase(_item.getId());
        if (item == null) return;
        updateItemInDatabase(item);
        loadItemsFromDatabase();
    }

    public void deleteItem(MoonDay _item) {
        deleteItemFromDatabase(_item);
        loadItemsFromDatabase();
    }

    private ContentValues buildContentValuesForDatabase(MoonDay item) {
        ContentValues values = buildContentValuesForDatabase(item.getBegin(),item.getEnd());
        //values.put(MySQLiteHelper.HISTORY_ID, item.getId());
        return values;
    }
    private ContentValues buildContentValuesForDatabase(Date begin, Date end) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.HISTORY_BEGIN, begin.getTime());
        values.put(MySQLiteHelper.HISTORY_END, end.getTime());
        return values;
    }

    private void putItemInDatabase(Date begin, Date end) {
        ContentValues values = buildContentValuesForDatabase(begin, end);
        db.insert(MySQLiteHelper.HISTORY_TABLE_NAME, null, values);
        Log.v(getClass().getName(), "Put new item in history:" +" begin="+begin.toString()+", end="+end);
    }

    private void deleteItemFromDatabase(MoonDay item) {
        db.delete(MySQLiteHelper.HISTORY_TABLE_NAME, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(item.getId())});
        Log.v(getClass().getName(), "Delete duty in db:" + item.toString());
    }

    private void updateItemInDatabase(MoonDay item) {
        ContentValues values = buildContentValuesForDatabase(item);
        db.update(MySQLiteHelper.HISTORY_TABLE_NAME, values, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(item.getId())});
        //Log.v(DutyDataSource.class.getName(), "Update duty in db");
    }

    private MoonDay getItemFromDatabase(long id) {
        MoonDay d = null;
        Cursor cursor = db.query(MySQLiteHelper.HISTORY_TABLE_NAME, ALL_COLUMNS, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            d = cursorToItem(cursor);
            Log.v(getClass().getName(), "getItemFromDatabase, id="+ id+ ", item= " + d.toString());
            cursor.moveToNext();
        }
        cursor.close();
        return d;
    }

    private void loadItemsFromDatabase() {
        items.clear();
        Log.v(getClass().getName(), "loadItemsFromDatabase...");
        Cursor cursor = db.query(MySQLiteHelper.HISTORY_TABLE_NAME, ALL_COLUMNS, null, null, null, null, MySQLiteHelper.HISTORY_BEGIN);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MoonDay d = cursorToItem(cursor);
            items.add(d);
            Log.v(getClass().getName(), "loadItemsFromDatabase: item: " + d.toString());
            cursor.moveToNext();
        }
        cursor.close();

        if (items.size()>1) {
            statistic = new MoonDayStatistic(items);
        } else {
            statistic = null;
        }
    }

    private MoonDay cursorToItem (Cursor cursor){
        long id;
        Date begin;
        Date end;

        id = cursor.getLong(0);
        begin = new Date(cursor.getLong(1));
        end = new Date(cursor.getLong(2));
        return new MoonDay(id, begin, end);
    }

}
