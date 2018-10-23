package com.beccari.grassi.gui;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Mark;
import com.beccari.grassi.data.Subject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Michele on 01/04/2018.
 */

public class MarksResumeAdapter extends BaseAdapter {
    public MarksResumeAdapter(Fragment fragment, ArrayList<Mark> marks) {
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
        if (position > 1) {
            final View view = fragment.getActivity().getLayoutInflater().inflate(R.layout.marksresumeadapter, parent, false);
            TextView valuebox = (TextView) view.findViewById(R.id.marks_valuebox);
            valuebox.setText(marks.get(position).getDisplay_value());
            Calendar ccalendar = new GregorianCalendar();
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
            TextView monthview = (TextView) view.findViewById(R.id.marks_monthbox);
            monthview.setText(month);
            TextView dayvyew = (TextView) view.findViewById(R.id.marks_datebox);
            dayvyew.setText(String.valueOf(ccalendar.get(Calendar.DATE)));
            TextView descbox = (TextView) view.findViewById(R.id.marks_descbox);
            String description = (marks.get(position).getDescription());
            if (description.length() == 0) {
                descbox.setText("NESSUNA DESCRIZIONE DISPONIBILE");
            } else {
                descbox.setText(Html.fromHtml(description));
            }
            TextView subbox = (TextView) view.findViewById(R.id.marks_subbox);
            subbox.setText(Html.fromHtml(marks.get(position).getSubjectName()));
            return view;
        } else {
            final View view = fragment.getActivity().getLayoutInflater().inflate(R.layout.averageadapter, parent, false);
            TextView averageview = (TextView) view.findViewById(R.id.averageView);
            Double average = 0.0;
            int markscount = 0;
            if (position == 0) {
                for (int i = 0; i < marks.size(); i++) {
                    if (marks.get(i).getMath_value() != 0.0) {
                        average += marks.get(i).getMath_value();
                        markscount++;
                    }
                }
                average = average / markscount;
                averageview.setText(String.format(Locale.ITALY, "%.2f", average));
                TextView averagetextview = (TextView) view.findViewById(R.id.averagetextview);
                averagetextview.setText("VOTO MEDIO");
            } else if (position==1) {
                Subject.getSubjectsfromMarks(marks);
                Log.i("Media materie", String.valueOf(Utils.SUBJECTS.size()));
                ArrayList<Double> subjectsaverages = new ArrayList<>();
                for (int i = 0; i < Utils.SUBJECTS.size(); i++) {
                    Log.i("Media materie", String.valueOf(Utils.SUBJECTS.size()));
                    int smarkscount = 0;
                    Double saverage = 0.0;//TODO creare metodo per media voti da qualche parte
                    int ddi = marks.size();
                    if (!Utils.SUBJECTS.get(i).contains("religione")) {
                        Log.i("Media materie", "fa media");
                        //s stands for "subject"
                        for (int j = 0; j < marks.size(); j++) {
                            Mark mark = marks.get(j);
                            if (mark.getSubjectName().equals(Utils.SUBJECTS.get(i)) && (mark.getAverage()) && (mark.getMath_value() != 0.0)) {
                                smarkscount++;
                                saverage += mark.getMath_value();
                            }
                        }
                    }
                    saverage = saverage / smarkscount;
                    subjectsaverages.add(saverage);
                }
                for (int i = 0; i < subjectsaverages.size(); i++) {
                    average += subjectsaverages.get(i);
                }
                average = average / subjectsaverages.size();
                averageview.setText(String.format(Locale.ITALY, "%.2f", average));
                TextView averagetextview = (TextView) view.findViewById(R.id.averagetextview);
                averagetextview.setText("MEDIA DELLE MATERIE");
            }

            return view;
        }
    }
}
