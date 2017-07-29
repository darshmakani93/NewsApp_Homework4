package com.example.darshmakani.newsapp;

import android.provider.BaseColumns;

/**
 * Created by Darsh Makani on 7/24/2017.
 */

public class Contract {


    public static final class NewsItem implements BaseColumns {

        //here NewsItem class initiate columns name and implemented BaseColumns interface.....

        public static final String TABLE_NAME = "news_items";

        public static final String SOURCE = "source";

        public static final String AUTHOR = "author";

        public static final String TITLE = "title";

        public static final String DESCRIPTION = "description";

        public static final String URL = "url";

        public static final String URL_TO_IMAGE = "urlToImage";

        public static final String PUBLISHED_AT = "published_at";
    }



}
