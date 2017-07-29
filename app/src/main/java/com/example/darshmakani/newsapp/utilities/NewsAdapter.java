package com.example.darshmakani.newsapp.utilities;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darshmakani.newsapp.R;

import com.example.darshmakani.newsapp.Contract;
import com.example.darshmakani.newsapp.model.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Darsh Makani on 6/26/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {


   // private ArrayList<NewsItem> data;


    private static final String TAG = NewsAdapter.class.getSimpleName();

    final private ItemClickListener listener;
    private Context context;
    private Cursor Cursor;

    //cursor was added in argument instead of arraylist, here cursor will return news from database, here onListItemClick takes cursor as argument
    // here i updated  methods according to cursor  and image stored in recycler view ....

    public NewsAdapter(Cursor cursor, ItemClickListener listener) {

        this.Cursor = cursor;

        this.listener = listener;
    }


    public interface ItemClickListener {

        void onListItemClick(Cursor cursor, int clickedItemIndex);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachToParent);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {

        return Cursor.getCount();
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTitle;

            public final TextView mDescription;

        public final TextView mTime;


            public final ImageView imgage;

        public NewsAdapterViewHolder(View itemView) {

            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDescription = (TextView) itemView.findViewById(R.id.description);
            mTime = (TextView) itemView.findViewById(R.id.time);
            imgage = (ImageView)itemView.findViewById(R.id.imgage);



            itemView.setOnClickListener(this);
        }


        //Used Picasso to load a thumbnail for each news item in the recycler view


        public void bind(int pos) {

            Cursor.moveToPosition(pos);

            mTitle.setText(Cursor.getString(Cursor.getColumnIndex(Contract.NewsItem.TITLE)));
            mDescription.setText(Cursor.getString(Cursor.getColumnIndex(Contract.NewsItem.DESCRIPTION)));
            mTime.setText(Cursor.getString(Cursor.getColumnIndex(Contract.NewsItem.PUBLISHED_AT)));

            String urlToImage = Cursor.getString(Cursor.getColumnIndex(Contract.NewsItem.URL_TO_IMAGE));

            Log.d(TAG, urlToImage);

            if(urlToImage != null){

                Picasso.with(context)
                        .load(urlToImage)
                        .into(imgage);
            }
        }

        @Override
        public void onClick(View v) {

            listener.onListItemClick(Cursor, getAdapterPosition());
        }
    }
}
