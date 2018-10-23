package com.beccari.grassi.Connection;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;

import com.beccari.grassi.data.Mark;
import com.beccari.grassi.data.Subject;
import com.beccari.grassi.gui.MarksPagerAdapter;
import com.beccari.grassi.gui.Settings;
import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by Michele on 05/11/2017.
 */

public class MarksFetcher extends AsyncTask<Void, Void, Void> {


    Context context;
    AppCompatActivity activity;
    private String cookie;
    private ArrayList<Mark> marks;
    private ArrayList<Subject> subjects;
    private CookieManager cookies = new CookieManager();

    public MarksFetcher(Context context, AppCompatActivity activity) {
        this.context = context;
        this.activity = activity;
    }


    @Override
    protected void onPreExecute() {
        cookie = PreferenceManager.getDefaultSharedPreferences(context).getString("COOKIE", "");

    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpCookie nuvola = new HttpCookie("nuvola", cookie.substring(7));
        try {
            nuvola.setVersion(0);
            nuvola.setDomain("nuvola.madisoft.it");
            nuvola.setPath("/");
            cookies.getCookieStore().add(new URI("http://nuvola.madisoft.it"), nuvola);
            URL marksurl = new URL(Utils.MARKSURL);
            //  URL periodutl = new URL(Settings.CURRENT_PERIOD); //TODO RIPRENDI DA QUI
            CookieHandler.setDefault(cookies);
            HttpURLConnection marksconnection;
            marksconnection = (HttpURLConnection) marksurl.openConnection();
            OutputReader marksreader = new OutputReader(marksconnection.getInputStream());
            marksreader.readStream();
            ArrayList<String> rows = marksreader.getRows(marksreader.getTable());
            if (marksreader.getResult().contains("Non è possibile accedere alla pagina desiderata")) {
                Log.i("TEST TUTORE", "RAMO ALTERNATIVO");
                marksurl = new URL(Utils.MARKSALTERNATIVEURL);
                HttpURLConnection marksnewconnection = (HttpURLConnection) marksurl.openConnection();
                marksreader = new OutputReader(marksnewconnection.getInputStream());
                marksreader.readStream();
                rows = marksreader.getRows(marksreader.getTable());

            }
            ArrayList<String> rawmarks = new ArrayList<>(); //contains unparsed marks for later processing
            marks = new ArrayList<>();
            Matcher marksmatcher;
            for (int i = 0; i < rows.size(); i++) {
                marksmatcher = Utils.MARKSPATTERN.matcher(rows.get(i));
                while (marksmatcher.find()) {
                    rawmarks.add(marksmatcher.group(2));
                }
                //groups: 3-name 4-date 5-teacher 6-subject 7-type 8-weight 9-average 10-desc 15-value 12-href 13-displaydate
            }
            int l = marks.size();
            if (rawmarks.size() != 0) {
                for (int j = 0; j < rawmarks.size(); j++) {
                    marksmatcher = Utils.MARKSDATAPATTERN.matcher(rawmarks.get(j));
                    Mark mark = new Mark();
                    while (marksmatcher.find()) {
                        for (int k = 0; k <= marksmatcher.groupCount(); k++) {
                            Log.i("VOTI", marksmatcher.group(k));
                        }
                        try {
                            mark.setDate(Utils.MARKSDATEFORMAT.parse(marksmatcher.group(4)));
                        } catch (ParseException pe) {
                            Log.e("PARSE", "Errore nel parse della data");
                        }
                        mark.setSubject(new Subject(marksmatcher.group(6)));
                        mark.setDisplay_value(marksmatcher.group(15));
                        mark.setAverage(marksmatcher.group(9));
                        mark.setDescription(marksmatcher.group(10));
                        mark.setType(marksmatcher.group(7));
                        mark.setMath_value();
                    }
                    marks.add(mark);
                }
            } else {
                Subject currentsubject = new Subject("MATERIA");
                Pattern markpattern = Pattern.compile("valore(.*?)>(.*?)<");
                Pattern sesubjectpattern = Pattern.compile("<th(.*?)>(.*?)<");
                for (int j = 0; j < rows.size(); j++) {
                    Matcher ematcher = sesubjectpattern.matcher(rows.get(j));
                    while (ematcher.find()) {
                         currentsubject = new Subject(ematcher.group(2));
                        Log.i("MATERIA TROVATA", currentsubject.getName());
                        break;
                    }
                    Matcher emergencymarkmatcher = markpattern.matcher(rows.get(j));
                    while (emergencymarkmatcher.find()){
                        Mark mark = new Mark();
                        mark.setDisplay_value(emergencymarkmatcher.group(2));
                        mark.setSubject(currentsubject);
                        if (!mark.getDisplay_value().equals(" ") && !mark.getDisplay_value().isEmpty()) {
                            marks.add(mark);
                        }
                    }
                }

            }
            int ri = marks.size();
            if (marks.size() == 0) {
                String test = PreferenceManager.getDefaultSharedPreferences(context).getString("password", "");
                Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "") + " " + test);
                Crashlytics.getInstance().crash();

            }

            subjects = new ArrayList<>();
            for (int i = 0; i < marks.size(); i++) {
                Subject msubject = marks.get(i).getSubject();
                if (subjects.isEmpty()) {
                    subjects.add(msubject);
                }
                boolean add = true;
                for (int j = 0; j < subjects.size(); j++) {
                    if (marks.get(i).getSubject().getName().equals(subjects.get(j).getName())) {
                        add = false;
                    }
                }
                if (add) {
                    subjects.add(marks.get(i).getSubject());
                }
            }

        } catch (URISyntaxException e) {
            String test = PreferenceManager.getDefaultSharedPreferences(context).getString("password", "");
            Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "") + " " + test);
            Crashlytics.getInstance().crash();
            e.printStackTrace();
        } catch (MalformedURLException me) {
            String test = PreferenceManager.getDefaultSharedPreferences(context).getString("password", "");
            Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "") + " " + test);
            Crashlytics.getInstance().crash();
            me.printStackTrace();
            Log.e("Marksfetcher", "Url malformato");
        } catch (IOException ie) {
            String test = PreferenceManager.getDefaultSharedPreferences(context).getString("password", "");
            Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "") + " " + test);
            Crashlytics.getInstance().crash();
            Log.e("Marksfetcher", "Errore I/O");
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.setContentView(R.layout.activity_marks);
        PagerTabStrip tabStrip = new PagerTabStrip(context);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PagerAdapter pagerAdapter = new MarksPagerAdapter(fragmentManager, subjects, marks);
        ViewPager fragmentpager = (ViewPager) activity.findViewById(R.id.pager);
        fragmentpager.setAdapter(pagerAdapter);

    }
}
