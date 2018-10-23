package com.beccari.grassi.gui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.data.Mark;
import com.beccari.grassi.data.Subject;

import java.util.ArrayList;

/**
 * Created by Michele on 29/03/2018.
 */

public class MarkFragment extends Fragment {

    public MarkFragment(){}
    Subject subject;
    ArrayList<Mark> marks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle databundle  = this.getArguments();
        marks = databundle.getParcelableArrayList("Marks");
        int i = marks.size();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fView = inflater.inflate(R.layout.fragment_mark, container, false);
        ListView marklist = (ListView) fView.findViewById(R.id.marks_list);
        marklist.setAdapter(new MarksAdapter(this, marks));
        return fView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }
}
