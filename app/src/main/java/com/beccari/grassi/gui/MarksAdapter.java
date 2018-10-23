package com.beccari.grassi.gui;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.data.Mark;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Michele on 01/04/2018.
 */

public class MarksAdapter extends BaseAdapter {
    public MarksAdapter(){}
    public MarksAdapter(Fragment fragment, ArrayList<Mark> marks) {
        this.fragment = fragment;
        this.marks = marks;
    }

    Fragment fragment;
    ArrayList<Mark> marks;

    @Override
    public int getCount() {
        return marks.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        String month = "ERR";
        final View view = fragment.getActivity().getLayoutInflater().inflate(R.layout.marksadapter, parent, false);
        TextView valuebox = (TextView) view.findViewById(R.id.marks_valuebox);
        valuebox.setText(marks.get(position).getDisplay_value());
        Calendar ccalendar = new GregorianCalendar();
        if (marks.get(position).getDate()!=null) {
            ccalendar.setTime((marks.get(position).getDate()));
            switch (ccalendar.get(Calendar.MONTH)) {
                case Calendar.JANUARY:
                    month = "GEN";
                    break;
                case Calendar.FEBRUARY:
                    month = "FEB";
                    break;
                case Calendar.MARCH:
                    month = "MAR";
                    break;
                case Calendar.APRIL:
                    month = "APR";
                    break;
                case Calendar.MAY:
                    month = "MAG";
                    break;
                case Calendar.JUNE:
                    month = "GIU";
                    break;
                case Calendar.JULY:
                    month = "LUG";
                    break;
                case Calendar.AUGUST:
                    month = "AGO";
                    break;
                case Calendar.SEPTEMBER:
                    month = "SET";
                    break;
                case Calendar.OCTOBER:
                    month = "OTT";
                    break;
                case Calendar.NOVEMBER:
                    month = "NOV";
                    break;
                case Calendar.DECEMBER:
                    month = "DIC";
                    break;
                default:
                    break;
            }
        }  else {
            LinearLayout datelayout = (LinearLayout) view.findViewById(R.id.datelayout);
            datelayout.setVisibility(View.GONE);
        }
        TextView monthview = (TextView) view.findViewById(R.id.marks_monthbox);
        monthview.setText(month);
        TextView dayvyew = (TextView) view.findViewById(R.id.marks_datebox);
        dayvyew.setText(String.valueOf(ccalendar.get(Calendar.DATE)));
        TextView descbox = (TextView) view.findViewById(R.id.marks_descbox);
        String description = (marks.get(position).getDescription());
        if (description == null) {
            descbox.setText("NESSUNA DESCRIZIONE DISPONIBILE");
        } else {
            descbox.setText(Html.fromHtml(description));
        }

        return view;
    }
}
