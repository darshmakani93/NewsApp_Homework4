package com.example.darshmakani.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.EditText;

import android.widget.ProgressBar;

import com.example.darshmakani.newsapp.model.NewsItem;
import com.example.darshmakani.newsapp.model.SQLiteHelper;
import com.example.darshmakani.newsapp.model.SQLiteUtilities;


import com.example.darshmakani.newsapp.utilities.NetworkUtils;
import com.example.darshmakani.newsapp.utilities.NewsAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

// Code by Darsh Makani

//implemented all  callbacks to convert AsyncTask into AsyncTaskLoader

//loadInBackground method runs  in background for load news from database..

//Modified onListItemClick methods for cursor for get news from database instead of internet API news ....


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener {

    static final String TAG = "mainactivity";
    /* private TextView newsTextView;
    private TextView newsUrlView;
    private ProgressBar progressBar;*/


    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    ///private EditText mSearchBoxEditText;
    private ProgressBar progressBar;
    private SQLiteDatabase mDataBase;
    private Cursor cursor;
    private static final int NEWS_LOADER = 1;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        //  mSearchBoxEditText = (EditText) findViewById(R.id.search);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


       // load whats in database in recycler view and schedule services which is defined in scheduleUtilities class(coming from firebase)


        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isFirst = preference.getBoolean("isfirst", true);

        if (isFirst) {

            LoaderManager loaderManager = getSupportLoaderManager();

                loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

            SharedPreferences.Editor editor = preference.edit();
                editor.putBoolean("isfirst", false);
            editor.commit();

        }

        ScheduleUtilities.scheduleRefresh(this);
    }
    //  this methods will modified apps to get news data from database and shown in recycler view

    @Override
    protected void onStart() {

        super.onStart();

        mDataBase = new SQLiteHelper(MainActivity.this).getReadableDatabase();
        cursor = SQLiteUtilities.getAll(mDataBase);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.newsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        if (menu_id == R.id.search) {



            /*String s = mSearchBoxEditText.getText().toString();

            NewsData task = new NewsData(s);

            task.execute();*/

                LoaderManager loaderManager = getSupportLoaderManager();

                    loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {

                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {

                NewsServices.refreshNews(MainActivity.this);
                return null;
            }
        };
    }


    @Override
    public void onLoaderReset(Loader<Void> loader) {


    }


    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {

        progressBar.setVisibility(View.INVISIBLE);

        mDataBase = new SQLiteHelper(MainActivity.this).getReadableDatabase();

        cursor = SQLiteUtilities.getAll(mDataBase);

        mNewsAdapter = new NewsAdapter(cursor, this);

        mRecyclerView.setAdapter(mNewsAdapter);

        mNewsAdapter.notifyDataSetChanged();
    }

/*
    class NewsData extends AsyncTask<URL, Void, ArrayList<NewsItem>> implements NewsAdapter.ItemClickListener {
        String query;
        ArrayList<NewsItem> data;

        NewsData(String s) {

            query = s;
        }
        //  set visibility of spinning progress bars
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {

            ArrayList<NewsItem> result = null;

            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> newsData) {
            this.data = newsData;
            super.onPostExecute(data);

            progressBar.setVisibility(View.INVISIBLE);

            if (data != null) {
                NewsAdapter adapter = new NewsAdapter(data, this);
                mRecyclerView.setAdapter(adapter);
            }
        }
        @Override
        public void onListItemClick(int clickedItemIndex) {

            openWebPage(data.get(clickedItemIndex).getUrl());
        }

        public void openWebPage(String url) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
    */

    @Override
    public void onListItemClick(Cursor cursor, int clickedItemIndex) {

        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.NewsItem.URL));

        Log.d(TAG, String.format("Url %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivity(intent);
        }
    }





}

