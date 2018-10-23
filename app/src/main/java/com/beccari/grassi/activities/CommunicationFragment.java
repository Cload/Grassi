package com.beccari.grassi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beccari.grassi.R;
import com.beccari.grassi.gui.CommPagerAdapter;
import com.beccari.grassi.gui.FilesAdapter;
import com.beccari.grassi.gui.NoSwipePager;


/**
 * Created by Michele on 29/09/2017.
 */

public class CommunicationFragment extends Activity {
    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public void setFilesAdapter(FilesAdapter filesAdapter) {
        this.filesAdapter = filesAdapter;
    }

    AlertDialog dialog;
    FilesAdapter filesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blankfragment);
        final LinearLayout comlayout = (LinearLayout) findViewById(R.id.comlayout);
        final ImageView archButton = (ImageView) findViewById(R.id.archbutton);
        final ImageView commbutton = (ImageView) findViewById(R.id.commbutton);
        final ImageView savedbutton = (ImageView) findViewById(R.id.savedbuttons);
        final NoSwipePager viewPager = (NoSwipePager) findViewById(R.id.viewpager);
        CommPagerAdapter pagerAdapter = new CommPagerAdapter(this , CommunicationFragment.this);
        viewPager.setAdapter(pagerAdapter);
        commbutton.setBackgroundColor(getResources().getColor(R.color.clear));
        savedbutton.setBackgroundColor(getResources().getColor(R.color.clear));
        archButton.setBackgroundColor(getResources().getColor(R.color.clear));
        commbutton.setImageResource(R.drawable.comms_selected);
        archButton.setImageResource(R.drawable.archived);
        savedbutton.setImageResource(R.drawable.saved);
        viewPager.setCurrentItem(0);
        comlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commbutton.setImageResource(R.drawable.comms_selected);
                archButton.setImageResource(R.drawable.archived);
                savedbutton.setImageResource(R.drawable.saved);
                viewPager.setCurrentItem(0);

            }
        });
        archButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                commbutton.setImageResource(R.drawable.comms);
                archButton.setImageResource(R.drawable.archived_selected);
                savedbutton.setImageResource(R.drawable.saved);

            }
        });
        savedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                commbutton.setImageResource(R.drawable.comms);
                archButton.setImageResource(R.drawable.archived);
                savedbutton.setImageResource(R.drawable.saved_selected);

            }
        });

    }


    @Override
    public void onResume() {
        if (filesAdapter!=null && dialog!=null){
            filesAdapter.notifyDataSetChanged();
            dialog.getListView().setAdapter(filesAdapter);
        }
        super.onResume();
    }
}
