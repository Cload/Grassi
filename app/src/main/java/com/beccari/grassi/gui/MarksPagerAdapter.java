package com.beccari.grassi.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;

import com.beccari.grassi.data.Mark;
import com.beccari.grassi.data.Subject;

import java.util.ArrayList;

/**
 * Created by Michele on 29/03/2018.
 */

public class MarksPagerAdapter extends FragmentStatePagerAdapter {
    public MarksPagerAdapter(FragmentManager fm, ArrayList<Subject> subjects, ArrayList<Mark> marks) {
        super(fm);
        this.subjects = subjects;
        this.marks = marks;
    }

    ArrayList<Subject> subjects;
    ArrayList<Mark> marks;

    public MarksPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position!=0){
        ArrayList<Mark> subjectmarks = new ArrayList<>();
        for (int i=0; i<marks.size(); i++){
            if (marks.get(i).getSubject().getName().equals(subjects.get(position -1).getName())){
                subjectmarks.add(marks.get(i));
            }
        }
        MarkFragment markFragment = new MarkFragment();
        Bundle mbundle = new Bundle();
        mbundle.putParcelableArrayList("Marks", subjectmarks);
        markFragment.setArguments(mbundle);
        return markFragment;

        } else {
            ResumeFragment resumeFragment = new ResumeFragment();
            Bundle mbundle = new Bundle();
            mbundle.putParcelableArrayList("Marks", marks);
            resumeFragment.setArguments(mbundle);
            return resumeFragment;
        }


    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "SITUAZIONE GENERALE";
        }
        else {
            return Html.fromHtml(subjects.get(position -1).getName());
        }
    }

    @Override
    public int getCount() {
        return subjects.size() + 1;
    }
}
