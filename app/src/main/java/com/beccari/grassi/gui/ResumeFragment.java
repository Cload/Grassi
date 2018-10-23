package com.beccari.grassi.gui;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beccari.grassi.R;
import com.beccari.grassi.activities.SplashActivity;
import com.beccari.grassi.data.Mark;
import com.crashlytics.android.Crashlytics;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Michele on 10/04/2018.
 */

public class ResumeFragment extends Fragment {
    ArrayList<Mark> rmarks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle databundle = this.getArguments();
        rmarks = databundle.getParcelableArrayList("Marks");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArrayList<Mark> mmarks = rmarks;
        if (mmarks.size()>0 && mmarks.get(0).getDate()!=null) {
            View fView = inflater.inflate(R.layout.fragment_resume, container, false);
            GraphView resumegraph = (GraphView) fView.findViewById(R.id.resumegraph);
            DataPoint[] points;
            points = new DataPoint[mmarks.size()];
            Collections.sort(mmarks, new Comparator<Mark>() {
                @Override
                public int compare(Mark o1, Mark o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            ArrayList<DataPoint> pointArrayList = new ArrayList<>();
            for (int i = 0; i < mmarks.size(); i++) {
                if (mmarks.get(i).getMath_value() != 0.0) {
                    pointArrayList.add(new DataPoint(mmarks.get(i).getDate(), mmarks.get(i).getMath_value()));
                }

            }
            points = pointArrayList.toArray(new DataPoint[pointArrayList.size()]);
            LineGraphSeries<DataPoint> graphlinepoints = new LineGraphSeries<>(points);
            PointsGraphSeries<DataPoint> graphpoint = new PointsGraphSeries<>(points);
            graphpoint.setSize(10);
            resumegraph.addSeries(graphlinepoints);
            resumegraph.addSeries(graphpoint);
            resumegraph.getViewport().setXAxisBoundsManual(true);
            try {
                resumegraph.getViewport().setMinX(pointArrayList.get(0).getX() - 10000000);
            } catch (IndexOutOfBoundsException e) {
                String test = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("password", "");
                Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", "") + " " +test);
                Crashlytics.getInstance().crash();
            }
            resumegraph.getViewport().setYAxisBoundsManual(true);
            resumegraph.getViewport().setMinY(1);
            resumegraph.getViewport().setMaxY(10);
            resumegraph.getViewport().setDrawBorder(false);
            resumegraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
            resumegraph.getViewport().setScrollable(true);
            resumegraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
            ListView marksview = (ListView) fView.findViewById(R.id.topics_list); //TODO cambiare nome
            marksview.setAdapter(new MarksResumeAdapter(this, rmarks));
            return fView;
        }
        else {

            return null;
        }

    }
}
