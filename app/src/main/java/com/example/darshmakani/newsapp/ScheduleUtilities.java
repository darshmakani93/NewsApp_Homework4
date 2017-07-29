package com.example.darshmakani.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Darsh Makani on 7/24/2017.
 */


//here i used JobDispatcher of firebase  which loads new news information in every minute


public class ScheduleUtilities {



    private static final int SCHEDULE_INTERVAL_SECONDS = 60;

    private static final int SYNC_FLEXTIME_SECONDS = 0;

    private static final String NEWS_TAG = "news_job_tag";

    private static boolean Init;

    synchronized public static void scheduleRefresh(@NonNull final Context context){

        if(Init) return;

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher sq = new FirebaseJobDispatcher(driver);

        Job constraintRefreshJob = sq.newJobBuilder()
                .setService(NewsServices.class)
                .setTag(NEWS_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_SECONDS,
                        SCHEDULE_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        sq.schedule(constraintRefreshJob);

        Init = true;
    }

}
