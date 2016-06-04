package com.example.shahaf.moviecatcher;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shahaf on 16/05/2016.
 */
public class SqlHelper extends SQLiteOpenHelper {

    public SqlHelper(Context context) {
        super(context, "movies_database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String command = "CREATE TABLE " + DBConstants.TABLENAME + "(" + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " " + DBConstants.MOVIETITLE + " TEXT, " +
                " " + DBConstants.PLOT + " TEXT, " +
                " " + DBConstants.GENRE + " TEXT, " +
                " " + DBConstants.RELEASED + " TEXT, " +
                " " + DBConstants.RUNTIME + " TEXT, " +
                " " + DBConstants.POSTERURL + " TEXT, " +
                " " + DBConstants.RATING + " REAL, " +
                " " + DBConstants.SEEN + " REAL ) ";

        db.execSQL(command);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
