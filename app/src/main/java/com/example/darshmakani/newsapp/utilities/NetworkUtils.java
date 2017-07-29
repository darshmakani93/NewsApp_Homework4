package com.example.darshmakani.newsapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.darshmakani.newsapp.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Darsh Makani on 6/18/2017.
 */

// Created a new class called NetworkUtils & Defined the appropriate base_url and query_parameter constants.
public class NetworkUtils {
    public static final String TAG = "NetworkUtils";

    final static String BASE_URL = "https://newsapi.org/v1/articles";
    final static String SORT_PARAM = "sortBy";

    final static String SOURCE_PARAM = "source";

    final static String APIKEY_PARAM = "apiKey";

    private final static String src = "the-next-web";
    private final static String sort = "latest";

    //put key here

    private final static String key = "367a6904ef8b4984b47686c5864b0dfc";

    // Created a static method in NetworkUtils that uses Uri.Builder to build the appropriate url

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, src)
                .appendQueryParameter(SORT_PARAM, sort)
                .appendQueryParameter(APIKEY_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        Log.v(TAG, "Built URI " + url);


        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {

        ArrayList<NewsItem> result = new ArrayList<>();

        JSONObject main = new JSONObject(json);

        JSONArray items = main.getJSONArray("articles");


        String source = main.getString("source");
        String author;
        String title;
        String description;
        String url;
        String urlImage;
        String publishedAt;

        for(int i = 0; i < items.length(); i++) {


            JSONObject item = items.getJSONObject(i);

            author = item.getString("author");
            title = item.getString("title");
            description = item.getString("description");
            url = item.getString("url");
            urlImage = item.getString("urlToImage");
            publishedAt = item.getString("publishedAt");


            NewsItem newsItem = new NewsItem(source, author, title, description, url, urlImage, publishedAt);
            result.add(newsItem);
        }

        return result;
    }
}
