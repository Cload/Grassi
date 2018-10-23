package com.beccari.grassi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Topic;
import com.beccari.grassi.gui.TopicsAdapter;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Michele on 24/10/2017.
 */

public class TopicsActivity extends Activity {



    public TopicsActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int position = getIntent().getIntExtra("POSITION", 0);
        ArrayList<Topic> stopics ;
        String json = PreferenceManager.getDefaultSharedPreferences(this).getString("Topics", "");
        Gson gson = new Gson();
        stopics = Utils.linkedTreeMapToArraylist(gson.fromJson(json, ArrayList.class));
        stopics = Topic.getTopicsFromSubject(stopics, Utils.SUBJECTS.get(position));
        if (stopics.size()==0){
            Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(this).getString("username", ""));
            Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(this).getString("password", ""));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ListView topic = (ListView) findViewById(R.id.topic_list);
        topic.setAdapter(new TopicsAdapter(TopicsActivity.this, stopics));

    }
}
