package com.beccari.grassi.gui;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Topic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Michele on 24/10/2017.
 */

public class TopicsAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<Topic> topics;

    public TopicsAdapter(Activity activity, ArrayList<Topic> topics) {
        this.activity = activity;
        this.topics = topics;
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
    public int getCount() {
        return topics.size();
    }

//TODO creare metodo per creare la view della data non fare ctrl c ctr v che è da marci
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String month = "ERR";
        final View view = activity.getLayoutInflater().inflate(R.layout.topicline, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.ttitlebox);
        textView.setText(Html.fromHtml(topics.get(position).getDescription()));
        Calendar ccalendar =  new GregorianCalendar();
        try {
            ccalendar.setTime(Utils.TOPICSDATEFORMAT.parse(topics.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (ccalendar.get(Calendar.MONTH)){
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
        TextView monthview = (TextView) view.findViewById(R.id.monthbox);
        monthview.setText(month);
        TextView dayvyew = (TextView) view.findViewById(R.id.cdatebox);
        dayvyew.setText(String.valueOf(ccalendar.get(Calendar.DATE)));
        return view;
    }
}
