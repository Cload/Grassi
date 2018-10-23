package com.beccari.grassi.gui;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.data.Communication;
import com.beccari.grassi.data.FileDownloader;

import java.util.ArrayList;

/**
 * Created by Michele on 02/10/2017.
 */

public class FilesAdapter extends BaseAdapter {

    Activity activity;
    final int count;
    Communication comms = new Communication();
    FileDownloader task = new FileDownloader();
    AsyncTask.Status status;
    int which;

    public void setWhich(int which) {
        this.which = which;
    }

    public FilesAdapter(Activity activity, Communication comms, int count, FileDownloader task) {
        this.task = task;
        this.activity = activity;
        this.comms = comms;
        this.count = count;
        this.status = null;
    }


    public void setTask(FileDownloader task) {
        this.task = task;
        status = task.getStatus();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        Log.i("GETCOUNT", String.valueOf(comms.getFilesnames().size()));
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.i("DATA", "DATASETCAMBIATO");
        status = task.getStatus();
        Log.i("STATO", String.valueOf(task.getStatus()));

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("GETVIEW", "chiamata");
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.files_line, parent, false);
        Log.i("STATOVISTA", String.valueOf(parent.getChildCount()));
        TextView textView = (TextView) convertView.findViewById(R.id.cdfilebox);
        LinearLayout mprogressbar = (LinearLayout) convertView.findViewById(R.id.proglayout);
        if (position == which) {
            if (task.getStatus() == AsyncTask.Status.RUNNING) {
                Log.i("STATO", "AGGIORNATO");
                textView.setVisibility(View.INVISIBLE);
                mprogressbar.setVisibility(View.VISIBLE);

            } else {
                textView.setVisibility(View.VISIBLE);
                mprogressbar.setVisibility(View.INVISIBLE);
            }
        }
        textView.setText(comms.getFilesnames().get(position));


        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
