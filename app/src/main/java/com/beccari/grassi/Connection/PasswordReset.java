package com.beccari.grassi.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.beccari.grassi.Utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michele on 13/05/2018.
 */


public class PasswordReset extends AsyncTask<String, Void, Void> {

    private CookieManager cookies = new CookieManager();
    Context context;
    private String cookie;

    public PasswordReset(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        System.setProperty("http.keepAlive", "true");
        HttpCookie nuvola = new HttpCookie("nuvola", cookie.substring(7));
        try {
            URL resetpsw = new URL("https://nuvola.madisoft.it/reset-password");
            nuvola.setVersion(0);
            nuvola.setDomain("nuvola.madisoft.it");
            nuvola.setPath("/");
            cookies.getCookieStore().add(new URI("http://nuvola.madisoft.it"), nuvola);
            CookieHandler.setDefault(cookies);
            HttpURLConnection resetpswconnection = (HttpURLConnection) resetpsw.openConnection();
            resetpswconnection.setDoOutput(true);
            resetpswconnection.setRequestMethod("POST");
            resetpswconnection.setInstanceFollowRedirects(true);
            OutputReader reader = new OutputReader(resetpswconnection.getInputStream());
            reader.readStream();
            String token = "";
            String test = reader.getResult();

            Matcher matcher = Pattern.compile("<input type=\"hidden\"  value=\"(.*?)\"").matcher(test); //TODO SISTEMARE IL TOKEN CHE E IMMONDO
            while (matcher.find()) {
                token = matcher.group(1);
            }
            resetpswconnection = (HttpURLConnection) resetpsw.openConnection();
            resetpswconnection.setRequestMethod("POST");
            resetpswconnection.setInstanceFollowRedirects(true);
            StringBuilder bodybuilder = new StringBuilder(); //Todo mettere string concat
            bodybuilder.append("nuvola_form_cambio_password%5Bcurrent_password%5D=")
                    .append(URLEncoder.encode(params[0], "UTF-8"))
                    .append("&nuvola_form_cambio_password%5BplainPassword%5D%5Bfirst%5D=")
                    .append(URLEncoder.encode(params[1], "UTF-8"))
                    .append("&nuvola_form_cambio_password%5BplainPassword%5D%5Bsecond%5D=")
                    .append(URLEncoder.encode(params[2], "UTF-8"))
                    .append("&nuvola_form_cambio_password%5B_token%5D=")
                    .append(token);
            String body = bodybuilder.toString();
            DataOutputStream logstream = new DataOutputStream(resetpswconnection.getOutputStream());
            logstream.write(body.getBytes());
            reader = new OutputReader(resetpswconnection.getInputStream());
            reader.readStream();
            test = reader.getResult();
            if (test.contains("corrisponde")){
                //TODO toast che le password vecchia non corrisponde
            } else {
                if (test.contains("coincidono")){

                }


            }
            int i = resetpswconnection.getResponseCode();



        } catch (IOException e) {
            e.printStackTrace();


            return null;

        } catch (URISyntaxException ue) {
            ue.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        cookie = PreferenceManager.getDefaultSharedPreferences(context).getString("COOKIE", "");

    }
}
