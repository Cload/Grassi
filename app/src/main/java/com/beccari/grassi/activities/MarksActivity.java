package com.beccari.grassi.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.beccari.grassi.Connection.MarksFetcher;
import com.beccari.grassi.R;
import com.beccari.grassi.gui.MarkFragment;
import com.beccari.grassi.gui.MarksPagerAdapter;

/**
 * Created by Michele on 21/03/2018.
 */
//TODO magari mergeare l'asynctask dei voti che tanto si usa solo qua
public class MarksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MarksFetcher mfetcher = new MarksFetcher(this, this);
        mfetcher.execute();

    }
}
