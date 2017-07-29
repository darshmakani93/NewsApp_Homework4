package com.example.darshmakani.newsapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.darshmakani.newsapp.Contract.NewsItem.*;


/**
 * Created by Darsh Makani on 7/24/2017.
 */

public class SQLiteUtilities {

    /*cursor will return news  from db and store news data from internet into arraylist(here database)
    *//* if data stores successfuly inn database so it set transaction successful
*/
    public static Cursor getAll(SQLiteDatabase db) {

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PUBLISHED_AT + " DESC"
        );

        return cursor;
    }

    public static void dataInsert(SQLiteDatabase db, ArrayList<NewsItem> newsItems) {
        db.beginTransaction();
        try {
            for (NewsItem a : newsItems) {
                ContentValues cv = new ContentValues();

                cv.put(SOURCE, a.getSource());

                cv.put(AUTHOR, a.getAuthor());

                cv.put(TITLE, a.getTitle());

                cv.put(DESCRIPTION, a.getDescription());
                cv.put(URL, a.getUrl());

                cv.put(URL_TO_IMAGE, a.getUrlToImage());
                cv.put(PUBLISHED_AT, a.getPublishedAt());

                db.insert(TABLE_NAME, null, cv);

            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();

            db.close();
        }
    }


    public static void deleteAll(SQLiteDatabase db) {

        db.delete(TABLE_NAME, null, null);
    }
}



