package hu.fallen.eyedroptracker.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static hu.fallen.eyedroptracker.data.DroptimeContract.*;

/**
 * Created by tibi on 2017-08-26.
 */

public class DroptimeDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "droptime.db";
    public static final int DATABASE_VERSION = 1;

    public DroptimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DroptimeEntry.TABLE_NAME + " (" +
                DroptimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DroptimeEntry.COLUMN_DROPTYPE + " TEXT NOT NULL, " +
                DroptimeEntry.COLUMN_DATETIME + " INTEGER NOT NULL" +
                ");";
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // v1
    }
}
