package com.example.shahaf.moviecatcher;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shahaf on 18/05/2016.
 */
public class DBHelper {

    Context cont;

    public DBHelper(Context context) {
        this.cont = context;
    }


    public ArrayList<Movie2> fillList() {
        ArrayList<Movie2> allMovies = new ArrayList<Movie2>();
        SqlHelper helper = new SqlHelper(cont);
        //Cursor c = helper.getReadableDatabase().rawQuery("SELECT * FROM "+DBConstants.TABLENAME,null);
        Cursor c = helper.getReadableDatabase().query(DBConstants.TABLENAME, null, null, null, null, null, null);

        while (c.moveToNext()) {
            Movie2 tempMovie = new Movie2(c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getFloat(7));
            tempMovie.setId(c.getInt(0));
            tempMovie.setSeen(c.getInt(8));
            allMovies.add(tempMovie);
        }
        return allMovies;
    }

    public void addMovie(Movie2 m) {
        SqlHelper helper = new SqlHelper(cont);

        String sql = "SELECT * " +
                " FROM " + DBConstants.TABLENAME;


        Cursor cur = helper.getReadableDatabase().rawQuery(sql, null);

        //Cursor c = helper.getReadableDatabase().query(DBConstants.TABLENAME, null, null, null, null, null, null);

        int check = 0;
        String title = "";
        while (cur.moveToNext()) {
            title = cur.getString(1);
            if (title.equals(m.getTitle())) {
                check = 1;
            }
        }

        if (check != 1) {

            //SqlHelper helper = new SqlHelper(cont);
            ContentValues cv = new ContentValues();
            cv.put(DBConstants.MOVIETITLE, m.getTitle());
            cv.put(DBConstants.PLOT, m.getPlot());
            cv.put(DBConstants.GENRE, m.getGenre());
            cv.put(DBConstants.RELEASED, m.getReleased());
            cv.put(DBConstants.RUNTIME, m.getRuntime());
            cv.put(DBConstants.POSTERURL, m.getPosterUrl());
            cv.put(DBConstants.RATING, m.getRating());
            cv.put(DBConstants.SEEN,m.getSeen());

            helper.getWritableDatabase().insert(DBConstants.TABLENAME, null, cv);


        } else {
            /*
            String Setsql = "UPDATE " + DBConstants.TABLENAME +
                    " SET " + DBConstants.MOVIETITLE + "=" + m.getTitle() + ", " +
                    DBConstants.PLOT + "=" + m.getPlot() + ", " +
                    DBConstants.GENRE + "=" + m.getGenre();
                    +", " +
                    DBConstants.RELEASED + "=" + m.getReleased() + ", " +
                    DBConstants.RUNTIME + "=" + m.getRuntime() + ", " +
                    DBConstants.POSTERURL + "=" + m.getRating() +
                    "WHERE " + DBConstants.MOVIETITLE + "=" + m.getTitle();
                    */


            ContentValues cv = new ContentValues();
            cv.put(DBConstants.MOVIETITLE, m.getTitle());
            cv.put(DBConstants.PLOT, m.getPlot());
            cv.put(DBConstants.GENRE, m.getGenre());
            cv.put(DBConstants.RELEASED, m.getReleased());
            cv.put(DBConstants.RUNTIME, m.getRuntime());
            cv.put(DBConstants.POSTERURL, m.getPosterUrl());
            cv.put(DBConstants.RATING, m.getRating());
            cv.put(DBConstants.SEEN, m.getSeen());

            helper.getWritableDatabase().update(DBConstants.TABLENAME, cv, DBConstants.MOVIETITLE + "='" + m.getTitle() +"'", null);


        }


    }

    public void deleteMovie(int id) {

        SqlHelper helper = new SqlHelper(cont);
        helper.getWritableDatabase().delete(DBConstants.TABLENAME, DBConstants.ID + "=" + id, null);
    }


    public Cursor getAllMoviesAsCursor() {

        SqlHelper mySQLHelper = new SqlHelper(cont);
        Cursor cursor = mySQLHelper.getReadableDatabase().query(DBConstants.TABLENAME, null, null, null, null, null, null);


        return cursor;
    }


    public void deleteAll() {

        SqlHelper helper = new SqlHelper(cont);
        helper.getWritableDatabase().delete(DBConstants.TABLENAME, null , null);

    }
}
