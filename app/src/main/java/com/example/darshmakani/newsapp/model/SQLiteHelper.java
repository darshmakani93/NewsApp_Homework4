package com.example.darshmakani.newsapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.darshmakani.newsapp.Contract;

/**
 * Created by Darsh Makani on 7/24/2017.
 */

// created SQLiteOpenHelper class for network call which stores the data for your news stories in the database

public class SQLiteHelper extends SQLiteOpenHelper {



    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "news.db";
    private static final String TAG = "SQLiteHelper";

    // created database news.db, assign database version=1 and also assign tag and assign into SQLiteHelper..

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String NEWS_TABLE = "CREATE TABLE " + Contract.NewsItem.TABLE_NAME + " (" +

                Contract.NewsItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.NewsItem.SOURCE + " TEXT NOT NULL, " +
                Contract.NewsItem.AUTHOR + " TEXT NOT NULL, " +
                Contract.NewsItem.TITLE + " TEXT NOT NULL, " +
                Contract.NewsItem.DESCRIPTION + " TEXT NOT NULL, " +
                Contract.NewsItem.URL + " TEXT NOT NULL, " +
                Contract.NewsItem.URL_TO_IMAGE + " TEXT NOT NULL, " +
                Contract.NewsItem.PUBLISHED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +

                "); ";

        Log.d(TAG, "SQL: " + NEWS_TABLE);

        db.execSQL(NEWS_TABLE);
    }


    //If db version changes from 1 to another version so, table will be dropped

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.NewsItem.TABLE_NAME);
        onCreate(db);
    }
}
