package com.beccari.grassi.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.beccari.grassi.Connection.TopicsFetcher;
import com.beccari.grassi.R;

import java.net.CookieManager;
import java.util.prefs.PreferenceChangeEvent;


public class SubjectActivity extends Activity {

    public SubjectActivity() {
    }

    @Override
    public void onBackPressed() {
        fetcher.cancel(true);
        super.onBackPressed();
    }

    TopicsFetcher fetcher;


    @Override
    protected void onDestroy() {
        fetcher.cancel(true);
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String cookie = PreferenceManager.getDefaultSharedPreferences(this).getString("COOKIE", "");
        setContentView(R.layout.activity_subjects);
        fetcher = new TopicsFetcher(getApplicationContext(), cookie, SubjectActivity.this);
        fetcher.execute();
    }
}
