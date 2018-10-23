package com.beccari.grassi.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.beccari.grassi.Connection.OutputReader;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.activities.CommunicationFragment;
import com.beccari.grassi.gui.FilesAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;

/**
 * Created by Michele on 05/10/2017.
 */

class FileRetriever extends AsyncTask<Communication, Void, Void> {

    public FileRetriever(CommunicationFragment activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    URL filesURL;
    HttpURLConnection fconn;
    OutputReader fOpreader;
    Matcher fmatcher;
    StringBuilder fbuilder = new StringBuilder();
    String file = new String();
    Communication communication;
    CommunicationFragment activity;
    Context context;
    FileDownloader filedownloader = new FileDownloader();
    FilesAdapter filesAdapter;
    AlertDialog.Builder builder;
    AlertDialog fdialog;



    @Override
    protected Void doInBackground(Communication...c) {
        communication = c[0];
        if (communication.getFiles().isEmpty()) {
            Log.d("esecuzione in bg", "va");
            try {
                filesURL = new URL(communication.getHref());
            } catch (MalformedURLException me) {
                Log.d("Errore url file", "Errore url");
            }
            try {
                fconn = (HttpURLConnection) filesURL.openConnection();
            } catch (IOException e) {
                Log.d("Errore di connessione", "Errore IO");
            }
            try {
                fOpreader = new OutputReader(fconn.getInputStream());
                fOpreader.readStream();
            } catch (IOException e) {
                Log.d("Errore di connessione", "Errore  inputstream");
            }
            fmatcher = Utils.URLPATTERN.matcher(fOpreader.getResult());
            Log.d("Trovato  url", fOpreader.getResult());
            while (fmatcher.find()) {
                Log.d("Trovato  url1", fmatcher.group());
                file = fmatcher.group()
                        .replace("href=\"", "")
                        .replace("\" t", "");
                fbuilder.append("https://nuvola.madisoft.it");
                fbuilder.append(file);
                if (!file.isEmpty()) {
                    communication.addFile(fbuilder.toString());
                    fbuilder = new StringBuilder();
                    Log.i("asynctask", fbuilder.toString());
                }
            }
            fmatcher = Utils.NAMESPATTERN.matcher(fOpreader.getResult());
            Log.d("Trovato  nome ", fOpreader.getResult());
            while (fmatcher.find()) {
                Log.d("Trovato  url1", fmatcher.group());
                file = fmatcher.group()
                        .replaceAll("class=\"text__ellipsed\">", "")
                        .replace("</div>", "");

                if (!file.isEmpty()) {
                    communication.addFilename(Html.fromHtml(file).toString());
                    Log.i("asynctask_name", file);
                }

            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        builder  = new AlertDialog.Builder(context);
        builder.setTitle("Seleziona il documento:");
        final  int count = communication.getFiles().size();
        filesAdapter = new FilesAdapter(activity,  communication, count, filedownloader);
        builder.setAdapter(filesAdapter, null);
        fdialog = builder.create();
        fdialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filesAdapter.setWhich(position);
                activity.setDialog(fdialog);
                activity.setFilesAdapter(filesAdapter);
                filedownloader= new FileDownloader(position, communication, context, communication.getFiles().get(position));
                filedownloader.execute();
                filesAdapter.setTask(filedownloader);
                filesAdapter.notifyDataSetChanged();
                fdialog.getListView().setAdapter(filesAdapter);
            }
        });
        fdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                filedownloader.cancel(true);
            }
        });
        fdialog.show();



        super.onPostExecute(aVoid);
    }


}

