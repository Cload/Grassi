package com.beccari.grassi.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.beccari.grassi.BuildConfig;
import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.gui.FilesAdapter;
import com.beccari.grassi.gui.FilesArrayAdapter;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

/**
 * Created by Michele on 09/10/2017.
 */

public class FileScanner {

    View fragview;
    Activity activity;
    Context context;

    public FileScanner(Context context, Activity activity, View fragview) {
        this.fragview = fragview;
        this.context = context;
        this.activity = activity;
    }
    public View getSavedFiles(View view){
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ListView listView = (ListView) view.findViewById(R.id.com_list);
        File directory = Utils.DOWNLOADDIRECTORY;
        final String[] files = directory.list();
        if (files!=null) {
            FilesArrayAdapter arrayAdapter = new FilesArrayAdapter(context, R.layout.savedcomlist_line, fragview, listView, files.length);
            listView.setAdapter(arrayAdapter);
        }
        return view;
    }


}
