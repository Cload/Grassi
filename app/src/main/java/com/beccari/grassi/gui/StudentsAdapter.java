package com.beccari.grassi.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beccari.grassi.Connection.OutputReader;
import com.beccari.grassi.Connection.PageFetcher;
import com.beccari.grassi.R;
import com.beccari.grassi.activities.MenuActivity;
import com.beccari.grassi.activities.SubjectActivity;
import com.beccari.grassi.data.Student;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Michele on 21/10/2018.
 */

public class StudentsAdapter extends BaseAdapter {

    public StudentsAdapter(Activity activity, ArrayList<Student> students, Context context) {
        this.context = context;
        this.students = students;
        this.activity = activity;
    }

    ArrayList<Student> students;
    Activity activity;
    Context context;

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = activity.getLayoutInflater().inflate(R.layout.cardadapter, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.card_sub);
        TextView title = (TextView) view.findViewById(R.id.card_title);
        title.setText(students.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuStarter pageFetcher = new MenuStarter(context);
                String[] params = {"https://nuvola.madisoft.it/area_tutore/area_tutore_scelta/alunnoinvariante/" + students.get(position).getHref()};
                pageFetcher.execute(params);


            }
        });
        return view;
    }

    private class MenuStarter extends PageFetcher {
        public MenuStarter(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(OutputReader outputReader) {
        MenuActivity menuActivity = new MenuActivity();
        Intent startsubj = new Intent(activity, menuActivity.getClass());
        activity.startActivity(startsubj);
        }
    }
}
