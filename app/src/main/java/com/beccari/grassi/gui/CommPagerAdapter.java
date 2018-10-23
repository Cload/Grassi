package com.beccari.grassi.gui;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.activities.CommunicationFragment;
import com.beccari.grassi.data.CFetcher;
import com.beccari.grassi.data.FileScanner;

/**
 * Created by Michele on 05/10/2017.
 */

public class CommPagerAdapter extends PagerAdapter{
    Context context;
    CommunicationFragment activity;

    public CommPagerAdapter(CommunicationFragment activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = PreferenceManager.getDefaultSharedPreferences(context).getString("CommURL", "");
        String archivedurl = url.replace("IN_PUBBLICAZIONE","ARCHIVIATI");
        container.getRootView();
        View view = activity.getLayoutInflater().inflate(R.layout.viewpager, container, false);
        CFetcher cFetcher;
        switch (position){
            case 0:
                cFetcher = new CFetcher(context, view, url, activity);
                cFetcher.execute();
                container.addView(view);
                break;
            case 1:
                cFetcher = new CFetcher(context, view, archivedurl, activity);
                cFetcher.execute();
                container.addView(view);
                break;
            case 2:
                FileScanner scanner = new FileScanner(context, activity, container.getRootView());
                container.addView(scanner.getSavedFiles(view));
        }
        return  view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}

