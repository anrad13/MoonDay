package ru.anrad.moonday.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ru.anrad.moonday.db";
    private static final int DATABASE_VERSION = 2;

    public static final String HISTORY_TABLE_NAME = "day_history";
    public static final String HISTORY_ID = "id";
    public static final String HISTORY_BEGIN = "begin";
    public static final String HISTORY_END = "end";

    public static final String CURRENT_TABLE_NAME = "day_current";
    public static final String CURRENT_ID = "id";
    public static final String CURRENT_IS_ACTIVE = "is_active";
    public static final String CURRENT_BEGIN = "begin";
    public static final String CURRENT_END = "end";

    public static final String DATE_NULL_VALUE = "0";
    //public static final String STRING_NULL_VALUE = "";

    // Database creation sql statement
    private static final String CURRENT_TABLE_CREATE =
            "create table "
                    + CURRENT_TABLE_NAME + "("
                    + CURRENT_ID + " integer primary key autoincrement, "
                    + CURRENT_IS_ACTIVE + " boolean, "
                    + CURRENT_BEGIN + " long, "
                    + CURRENT_END + " long "
                    + ");";
    private static final String HISTORY_TABLE_CREATE =
            "create table "
                    + HISTORY_TABLE_NAME + "("
                    + HISTORY_ID + " integer primary key autoincrement, "
                    + HISTORY_BEGIN + " long, "
                    + HISTORY_END + " long "
                    + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(HISTORY_TABLE_CREATE);
        database.execSQL(CURRENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.v(MySQLiteHelper.class.getName()," Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + CURRENT_TABLE_NAME);
        onCreate(database);
    }

}
