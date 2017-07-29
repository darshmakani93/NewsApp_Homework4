package com.example.darshmakani.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.darshmakani.newsapp.model.SQLiteHelper;
import com.example.darshmakani.newsapp.model.SQLiteUtilities;
import com.example.darshmakani.newsapp.model.NewsItem;
import com.example.darshmakani.newsapp.utilities.NetworkUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Darsh Makani on 7/24/2017.
 */

public class NewsServices extends JobService {

    AsyncTask mgTask;

   /* // here this NewsServices class extends JobService and override methods
     onStartJob method which pre execute text from database and then refresh news data and run refresh news in background
    */

    @Override
    public boolean onStartJob(final JobParameters job) {
        mgTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {

                Toast.makeText(NewsServices.this, "News refreshed", Toast.LENGTH_SHORT).show();

                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {

                refreshNews(NewsServices.this);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {


                    jobFinished(job, false);
                super.onPostExecute(o);

            }
        };

        mgTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if (mgTask != null) mgTask.cancel(false);

        return true;
    }
    //here refreshNews refresh periodically news context from database

    public static void refreshNews(Context context) {

        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();

        try {
            SQLiteUtilities.deleteAll(db);

                String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);

            SQLiteUtilities.dataInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }

}
