package com.beccari.grassi.data;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.beccari.grassi.Connection.OutputReader;
import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.activities.CommunicationFragment;
import com.beccari.grassi.gui.CommunicationAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Michele on 05/10/2017.
 */

public class CFetcher extends AsyncTask<Void, Integer, Void> {

 public    CFetcher(Context context, View container, String url, CommunicationFragment activity) {
        this.url = url;
        this.context = context;
        this.view = container;
        this.activity = activity;
    }

    View view;
    Context context;
    String url;
    CommunicationFragment activity;
    URL cURL;
    HttpURLConnection CFconn;
    InputStream cIS;
    ProgressBar cProg;
    OutputReader cOpreader;
    ArrayList<Communication> communications = new ArrayList<>();
    ListView cView;

    @Override
    protected void onPreExecute() {
        cView = (ListView) view.findViewById(R.id.com_list);
        cView.setVisibility(View.GONE);
        cProg = (ProgressBar) view.findViewById(R.id.progressBar);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("Asynctask", "Operazione in bg");
        try {
            cURL = new URL(url);
        } catch (MalformedURLException mue) {
            Log.d("Rete", "URLMalformato");
            cancel(true);
        }
        try {
            CFconn = (HttpURLConnection) cURL.openConnection();
        } catch (IOException ie) {
            Log.d("Rete", "Errore di IO");
            cancel(true);
        }
        try {
            cIS = CFconn.getInputStream();
            cOpreader = new OutputReader(cIS);
            cOpreader.readStream();
            ArrayList<String> rows;
            rows = cOpreader.getRows(cOpreader.getTable());
            communications = cOpreader.getCommunications(rows);
            Log.i("Main", "operazione in bg ok");


        } catch (IOException ie) {
            Log.d("Rete", "Errore nella lettura della pagina");
            cancel(true);
        }
        for (Communication c: communications){
            c.getFiles();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        cProg.setVisibility(View.GONE);
        cView.setVisibility(View.VISIBLE);
        cView = (ListView) view.findViewById(R.id.com_list);
        cView.setVisibility(View.VISIBLE);
        CommunicationAdapter communicationAdapter = new CommunicationAdapter(context, android.R.layout.simple_list_item_1, communications);
        cView.setAdapter(communicationAdapter);
        cView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("ONITEMCLICK", "CHIAMATA");
                communications.get(position).getFiles();
                ActivityCompat.requestPermissions(  activity  , Utils.PERMISSIONS_STORAGE, Utils.REQUEST_EXTERNAL_STORAGE);
                FileRetriever fileRetriever = new FileRetriever(activity, context);
                fileRetriever.execute(communications.get(position));
            }
        });
        super.onPostExecute(v);
    }
}
