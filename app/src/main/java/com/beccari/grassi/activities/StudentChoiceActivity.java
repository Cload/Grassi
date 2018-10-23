package com.beccari.grassi.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.beccari.grassi.Connection.OutputReader;
import com.beccari.grassi.Connection.PageFetcher;
import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Student;
import com.beccari.grassi.gui.StudentsAdapter;
import com.beccari.grassi.gui.TopicsAdapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michele on 20/10/2018.
 */

public class StudentChoiceActivity  extends AppCompatActivity{
    ListView slist;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schoice);
        StudentFetcher fetcher = new StudentFetcher(getApplicationContext(), this);
        slist = (ListView) findViewById(R.id.studentlist);
        String[] params = {Utils.SCHOICE_URL};
        fetcher.execute(params);





    }
    private class StudentFetcher extends PageFetcher {
        Activity activity;
        public StudentFetcher(Context context, Activity activity){
            super(context);
            this.activity = activity;
        }
        @Override
        protected OutputReader doInBackground(String[] params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onPostExecute(OutputReader outputReader) {
            String result = outputReader.getResult();
            int i = outputReader.getResult().hashCode();
            ArrayList<Student> students = new ArrayList<>();
            Pattern pattern = Pattern.compile("<a href=\"/area_tutore/area_tutore_scelta/alunnoinvariante/(.*?)\" class='btn btn-outline-secondary btn-block btn-lg my-1'>(.*?)</a>");
            Matcher studentmacher = pattern.matcher(result);
            while (studentmacher.find()){
                Student student = new Student();
                student.setHref(studentmacher.group(1));
                student.setName(studentmacher.group(2));
                students.add(student);
            }
            StudentsAdapter studentsAdapter = new StudentsAdapter(activity, students,context );
            slist.setAdapter(studentsAdapter);


        }
    }

}
