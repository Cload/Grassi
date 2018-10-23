package com.beccari.grassi.Connection;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Topic;
import com.beccari.grassi.gui.SubjectAdapter;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;

public class TopicsFetcher extends AsyncTask<Integer, Integer, Void> {


    public TopicsFetcher(Context context, String cookie, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.cookie = cookie;
    }

    Activity activity;
    String cookie;
    Context context;
    CookieManager cookies = new CookieManager();
    LinearLayout prog;
    Boolean isStudent;
    TextView progresstext;
    int progress = 0;
    ArrayList<Topic> mtopics = new ArrayList<>();

    private void isStudent() throws IOException {
        HttpURLConnection testconnection = (HttpURLConnection) new URL("https://nuvola.madisoft.it/area_tutore/").openConnection();
        testconnection.setInstanceFollowRedirects(true);
        testconnection.setRequestMethod("GET");
        testconnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
        testconnection.setRequestProperty("Cookie", cookie + ";");
        testconnection.connect();
        CookieHandler.setDefault(cookies);
        //ESEGUIRE IL METODO DOPO RECUPERO CUCHI
        OutputReader testreader = new OutputReader(testconnection.getInputStream());
        testreader.readStream();
        isStudent = testreader.getResult().contains("Non è possibile accedere alla pagina desiderata");
        testconnection.disconnect();
        int i = testconnection.getResponseCode();

    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        progresstext.setText(String.valueOf((values[0])) + "%");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() { //TODO SALVARE COOKIE COME OGGETTO PER EVITARE STA MAIALATA
        Log.i("COOKIE", cookie);
        HttpCookie nuvola = new HttpCookie("nuvola", cookie.substring(7));
        try {
            nuvola.setVersion(0);
            nuvola.setDomain("nuvola.madisoft.it");
            nuvola.setPath("/");
            cookies.getCookieStore().add(new URI("http://nuvola.madisoft.it"), nuvola);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        prog = (LinearLayout) activity.findViewById(R.id.tproglayout);
        progresstext = (TextView) activity.findViewById(R.id.tprogresstxt);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        prog.setVisibility(GONE);
        ListView tlist = (ListView) activity.findViewById(R.id.top_list);
        tlist.setAdapter(new SubjectAdapter(activity, context, mtopics));
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        OutputReader rowReader;
        Date lastcheck;
        try {
            isStudent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ArrayList<String> topics = new ArrayList<>();
            CookieHandler.setDefault(cookies);
            OutputReader treader = new OutputReader();
            HttpURLConnection connection;
            Date today = Utils.TOPICSDATEFORMAT.parse(Utils.TOPICSDATEFORMAT.format(new Date()));
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("lastcheck", "").isEmpty()) {
                lastcheck = Utils.TOPICSDATEFORMAT.parse(Utils.YEAR_BEGINNING);
            } else {
                lastcheck = Utils.TOPICSDATEFORMAT.parse(PreferenceManager.getDefaultSharedPreferences(context).getString("lastcheck", ""));
            }
            String converted = Utils.TOPICSDATEFORMAT.format(lastcheck);
            if (!PreferenceManager.getDefaultSharedPreferences(context).getString("Topics", "").isEmpty()) {
                Gson gson = new Gson();
                String json = PreferenceManager.getDefaultSharedPreferences(context).getString("Topics", "");
                ArrayList<Topic> old = Utils.linkedTreeMapToArraylist(gson.fromJson(json, ArrayList.class));
                mtopics.addAll(old);
            }
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("Topics", "").isEmpty() || lastcheck.before(today)) {

                int diff = Utils.getDateDiff(lastcheck, today, TimeUnit.DAYS);
                Log.i("async", "ingresso");
                while (lastcheck.before(today) && !isCancelled()) {
                    ArrayList<String> days = new ArrayList<>();
                    StringBuilder urlbuilder = new StringBuilder();
                    if (isStudent) {
                        urlbuilder.append(Utils.TOPICS_URL);
                    } else {
                        urlbuilder.append(Utils.TOPICS_ALTERNATIVE_URL);
                    }
                    urlbuilder.append("/");
                    urlbuilder.append(converted);
                    connection = (HttpURLConnection) new URL(urlbuilder.toString()).openConnection();
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
                    connection.setRequestProperty("Cookie", cookie + ";");
                    treader.setIs(connection.getInputStream());
                    treader.readStream();
                    progress = progress + 1;
                    Integer progresso = Integer.valueOf((progress) * 100 / (diff));
                    publishProgress(progresso);
                    topics.add(treader.getTable());
                    if (!treader.getTable().isEmpty()) {
                        rowReader = new OutputReader();
                        days = rowReader.getRows(treader.getTable());
                        if (treader.getTable() != null) {   //TODO risolvere porcata della data
                            for (int j = 0; j < days.size() && j < 6; j++) {
                                Topic mtopic = rowReader.getTopic(rowReader.getElements(days.get(j)));
                                mtopic.setDate(converted);
                                if (mtopic.getDescription() != null) {
                                    mtopics.add(mtopic);
                                }
                            }
                        }
                    } else {
                        days.add(null);
                    }
                    GregorianCalendar datesetter = new GregorianCalendar();
                    datesetter.setTime(Utils.TOPICSDATEFORMAT.parse(converted));
                    datesetter.add(Calendar.DATE, 1);
                    lastcheck = datesetter.getTime();
                    converted = Utils.TOPICSDATEFORMAT.format(datesetter.getTime());
                    Log.i("async", String.valueOf(connection.getURL()));
                }
                SharedPreferences saver = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = saver.edit();
                Gson gson = new Gson();
                String saved = gson.toJson(mtopics);
                editor.putString("Topics", saved);
                editor.putString("lastcheck", Utils.TOPICSDATEFORMAT.format(lastcheck));
                editor.apply();
                if (!mtopics.isEmpty()) {
                    Topic.getSubjects(mtopics);
                }
                Log.i("test", "s");
            } else {
                String json = PreferenceManager.getDefaultSharedPreferences(context).getString("Topics", "");
                Gson gson = new Gson();
                mtopics = Utils.linkedTreeMapToArraylist(gson.fromJson(json, ArrayList.class));
                if (!mtopics.isEmpty()) {
                    Topic.getSubjects(mtopics);
                } else {
                    Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", ""));
                    Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("password", ""));                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            Log.e("DATA", "errore nel parse della data");
            e.printStackTrace();
        }
        return null;
    }
}
