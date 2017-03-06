package ru.anrad.moonday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class HistoryDataSource {
    private final static int DEFAULT_STATISTIC_COUNTER = 5;
    //Singletone functionality
    private static HistoryDataSource instance;

    public static HistoryDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryDataSource(context.getApplicationContext());
        }
        return instance;
    }

    private Context context;
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    private final static String STAT_COUNTER_PREF_NAME = "statcounter";
    private final static int STAT_COUNTER_DEFAULT = 6;

    private ArrayList<MoonDay> items = new ArrayList<>();
    private MoonDayStatistic statistic;

    private final String[] ALL_COLUMNS = {
            MySQLiteHelper.HISTORY_ID,
            MySQLiteHelper.HISTORY_BEGIN,
            MySQLiteHelper.HISTORY_END,
    };

    private HistoryDataSource(Context context) {
        this.context = context;
        this.dbHelper = new MySQLiteHelper(context);

        open();
        loadItemsFromDatabase();
        close();
    }

    private void open() throws SQLException { this.db = this.dbHelper.getWritableDatabase();}

    private void close() { this.dbHelper.close(); }

    public MoonDayStatistic getStatistic() {
        return getStatistic(this.items);
    }

    public ArrayList<MoonDay> getItems() {
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
        open();
        ContentValues values = buildContentValuesForDatabase(begin, end);
        db.insert(MySQLiteHelper.HISTORY_TABLE_NAME, null, values);
        close();
        //Log.v(getClass().getName(), "Put new item in history:" +" begin="+begin.toString()+", end="+end);
    }

    private void deleteItemFromDatabase(MoonDay item) {
        open();
        db.delete(MySQLiteHelper.HISTORY_TABLE_NAME, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(item.getId())});
        close();
        //Log.v(getClass().getName(), "Delete duty in db:" + item.toString());
    }

    private void updateItemInDatabase(MoonDay item) {
        ContentValues values = buildContentValuesForDatabase(item);
        open();
        db.update(MySQLiteHelper.HISTORY_TABLE_NAME, values, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(item.getId())});
        close();
        //Log.v(DutyDataSource.class.getName(), "Update duty in db");
    }

    private MoonDay getItemFromDatabase(long id) {
        MoonDay d = null;
        open();
        Cursor cursor = db.query(MySQLiteHelper.HISTORY_TABLE_NAME, ALL_COLUMNS, MySQLiteHelper.HISTORY_ID + " = ?", new String[]{Long.toString(id)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            d = cursorToItem(cursor);
            //Log.v(getClass().getName(), "getItemFromDatabase, id="+ id+ ", item= " + d.toString());
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return d;
    }

    private void loadItemsFromDatabase() {
        items.clear();
        //Log.v(getClass().getName(), "loadItemsFromDatabase...");
        open();
        Cursor cursor = db.query(MySQLiteHelper.HISTORY_TABLE_NAME, ALL_COLUMNS, null, null, null, null, MySQLiteHelper.HISTORY_BEGIN);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MoonDay d = cursorToItem(cursor);
            items.add(d);
            //Log.v(getClass().getName(), "loadItemsFromDatabase: item: " + d.toString());
            cursor.moveToNext();
        }
        cursor.close();
        close();
    }

    private MoonDayStatistic getStatistic(ArrayList<MoonDay> items) {
        int itemsSize = items.size();
        if (itemsSize < 1) return null;

        //int statCounter = getStatCounter();
        int statCounter = DEFAULT_STATISTIC_COUNTER < itemsSize ? DEFAULT_STATISTIC_COUNTER : itemsSize ;
        //if (statCounter > itemsSize) statCounter = items.size();


        ArrayList<MoonDay> statItems = new ArrayList<>();
        /**
        for (int i = 0; i < statCounter; i++) {
            statItems.add(items.get(i));
        }
         **/
        for (int i = itemsSize - statCounter; i < itemsSize; i++) {
            statItems.add(items.get(i));
        }
        return new MoonDayStatistic(statItems);
    }

    private int getStatCounter() {
        int statCounter;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
        String statCounterString = sp.getString(STAT_COUNTER_PREF_NAME,"0");
        try {
            statCounter = Integer.valueOf(statCounterString);
        } catch (NumberFormatException e) {
            statCounter = STAT_COUNTER_DEFAULT;
        }
        return statCounter;
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
