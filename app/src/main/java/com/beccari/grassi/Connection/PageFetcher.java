package com.beccari.grassi.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.beccari.grassi.Utils.Utils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Michele on 21/10/2018.
 */

public class PageFetcher extends AsyncTask<String,Void,OutputReader> {
    private String cookie;
    private CookieManager cookies = new CookieManager();
    private OutputReader marksreader;
    Context context;


    public PageFetcher(){}

    public PageFetcher(Context context) {
            this.context = context;

    }

    public OutputReader getMarksreader() {
        return marksreader;
    }

    @Override
    protected void onPreExecute() {
        cookie = PreferenceManager.getDefaultSharedPreferences(context).getString("COOKIE", "");

    }

    @Override
    protected OutputReader doInBackground(String[] params) {
        HttpCookie nuvola = new HttpCookie("nuvola", cookie.substring(7));
        try {
            nuvola.setVersion(0);
            nuvola.setDomain("nuvola.madisoft.it");
            nuvola.setPath("/");
            cookies.getCookieStore().add(new URI("http://nuvola.madisoft.it"), nuvola);
            URL marksurl = new URL(params[0]);
            //  URL periodutl = new URL(Settings.CURRENT_PERIOD); //TODO RIPRENDI DA QUI
            CookieHandler.setDefault(cookies);
            HttpURLConnection marksconnection;
            marksconnection = (HttpURLConnection) marksurl.openConnection();
            marksreader = new OutputReader(marksconnection.getInputStream());
            marksreader.readStream();
            cookie = cookies.getCookieStore().getCookies().get(0).toString();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("COOKIE", cookie).apply();


        }  catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return marksreader;
    }
}