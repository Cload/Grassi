package com.beccari.grassi.gui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beccari.grassi.BuildConfig;
import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Michele on 11/10/2017.
 */

public class FilesArrayAdapter extends ArrayAdapter {
    View fragview;
    Context context;
    ListView listView;
    TextView nameview;
    ArrayList<File> llist;
    ArrayList<String> flist;
    String[] files;
    File[] links;
    int count;


    public FilesArrayAdapter(@NonNull Context context, @LayoutRes int resource, View fragview, ListView listView, int count) {
        super(context, resource);
        this.fragview = fragview;
        this.context = context;
        this.listView = listView;
        this.count = count;
    }

    @Override
    public void notifyDataSetChanged() {
        listView.setAdapter(this);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (flist == null) {
            return count;
        } else {
            return flist.size();
        }
    }
    @Override
    public void remove(@Nullable Object object) {
        super.remove(object);
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        File directory = Utils.DOWNLOADDIRECTORY;
        files = directory.list();
        links = directory.listFiles();
        llist = new ArrayList<>(Arrays.asList(links));
        flist = new ArrayList<>(Arrays.asList(files));
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.savedcomlist_line, parent, false);
        }
        final Intent open = new Intent(Intent.ACTION_VIEW);
        nameview = (TextView) convertView.findViewById(R.id.sctitlebox);
        nameview.setText(flist.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", llist.get(position)), "application/pdf");
                open.setFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                open.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(open);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Eliminare il File?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        llist.get(position).delete();
                        flist.remove(position);
                        llist.remove(position);
                        remove(v);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }


        });


        return convertView;
    }

}
