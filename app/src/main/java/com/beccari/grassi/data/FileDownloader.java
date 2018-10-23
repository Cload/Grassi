package com.beccari.grassi.data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
;

import com.beccari.grassi.BuildConfig;
import com.beccari.grassi.Utils.Utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;

/**
 * Created by Michele on 05/10/2017.
 */


public class FileDownloader extends AsyncTask<Void, Void, Void> {

    public FileDownloader() {}
    public FileDownloader(int which, Communication communication, Context context, String url) {
        this.which = which;
        this.communication = communication;
        this.context = context;
        this.url = url;
    }
    int which;
    Communication communication;
    OutputStream outputStream;
    InputStream inputStream;
    File file;
    Context context;
    File comm;
    String url;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL fileurl = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) fileurl.openConnection();
            urlConnection.connect();
            Log.i("scaricando", String.valueOf(urlConnection.getResponseCode()));

            inputStream = fileurl.openStream();
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator +  "Comunicati" );
            file.mkdirs();
            Log.i("scaricando", String.valueOf(urlConnection.getContentLength() / 1024));
            if (communication.getFilesnames().get(which).contains(".pdf")) {
                comm = new File(file.toString() + File.separator + communication.getFilesnames().get(which) + this.getExtension(communication.getFilesnames().get(which)));
            } else {
                comm = new File(file.toString() + File.separator + communication.getFilesnames().get(which) + ".docx");
            }
            outputStream = new FileOutputStream(comm, true);
            int lenght = -1;
            byte[] buffer = new byte[1024];
            while ((lenght = inputStream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.close();
            inputStream.close();
            Log.i("scaricando", "scaricato");
        } catch (IOException e) {
            Log.d("downloadtask", "tipico");
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        final Context mcontext = context;
        Intent open = new Intent(Intent.ACTION_VIEW);
        open.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", comm), "application/pdf");
        open.setFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        open.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mcontext.startActivity(open);
    }
    public String getExtension(String filename) {
        String extension = "";
        Matcher matcher = Utils.EXTENSIONPATTERN.matcher(filename);
        while (matcher.find()) {
            extension = matcher.group();
        }
        return extension;
    }
}
