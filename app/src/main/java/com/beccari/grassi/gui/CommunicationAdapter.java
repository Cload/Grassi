package com.beccari.grassi.gui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.data.Communication;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Michele on 13/08/2017.
 */
//todo sistemare i nomi che si inizia a non capire più un cazzo
public class CommunicationAdapter extends ArrayAdapter{

    Context context;
    ArrayList<Communication> communications = new ArrayList<>();

    public CommunicationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Communication> communications) {
        super(context, resource, communications);
        this.context = context;
        this.communications = communications;
    }

    @Override
    public int getCount() {
        return communications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.comlist_line, parent, false);
        }
        if (communications.get(position).getDate()!=null) {
            String month = "ERR";
            Calendar ccalendar = new GregorianCalendar();
            ccalendar.setTime(communications.get(position).getDate());
            Log.d("calendario", String.valueOf(communications.get(position).getDate()));

            TextView titleview = (TextView) convertView.findViewById(R.id.ctitlebox);
            try {
                titleview.setText(Html.fromHtml(communications.get(position).getTitle()));
            } catch (NullPointerException n) {
                Log.d("nullpointer", String.valueOf(position));
            }
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
            TextView monthview = (TextView) convertView.findViewById(R.id.monthbox);
            monthview.setText(month);
            TextView dayvyew = (TextView) convertView.findViewById(R.id.cdatebox);
            dayvyew.setText(String.valueOf(ccalendar.get(Calendar.DATE)));
        }
        return convertView;
    }
}
