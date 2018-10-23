package com.beccari.grassi.Connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.activities.MenuActivity;
import com.beccari.grassi.activities.SplashActivity;
import com.beccari.grassi.activities.StudentChoiceActivity;
import com.beccari.grassi.activities.SubjectActivity;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class LoginChecker extends AsyncTask<String, Void, Boolean> {
    public LoginChecker(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public LoginChecker(Context context, Activity activity, String cookie) {
        this.cookie = cookie;
        this.context = context;
        this.activity = activity;
    }

    public LoginChecker(Context context, Activity activity, Boolean start) {
        this.start = start;
        this.context = context;
        this.activity = activity;
    }

    CookieManager cookieManager;
    Context context;
    Activity activity, target; //activity che eventualmente dovrà essere avviata
    Boolean start = false;
    String usr = "";
    String cookie; //TODO COOKIE è UN COOKIE NON UNA STRINGA
    String psw = "";
    private Matcher matcher;
    Boolean needspasswordchange = false;
    Boolean needstudentchoice = false;
    String token;
    ArrayList<String> strings = new ArrayList<>();
    TextView progtextview;


    @Override
    protected void onPreExecute() {
        progtextview = (TextView) activity.findViewById(R.id.splashprogtextView);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        progtextview.setText("Caricamento...");
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (!aBoolean) {
            Log.i("TEST", "RAMOGIUSTO");
            MenuActivity menuActivity = new MenuActivity();
            cookie = cookieManager.getCookieStore().getCookies().get(0).toString();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("password", psw).apply();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("username", usr).apply();
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("COOKIE", cookie).apply();
            Utils.LOGGEDIN = Boolean.TRUE;
            //TODO TOGLIERE COSE INUTILI
            if (!needstudentchoice) {
                Intent intent = new Intent(activity, menuActivity.getClass());
                intent.putExtra("COOKIE", cookie);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            } else {
                StudentChoiceActivity studentChoiceActivity = new StudentChoiceActivity();
                Intent intent = new Intent(activity, studentChoiceActivity.getClass());
                intent.putExtra("COOKIE", cookie);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }


        } else {
            if (needspasswordchange) {
                Toast error = Toast.makeText(context, "Cambio password necessario, utilizza un browser per cambiare la password", Toast.LENGTH_SHORT);
                error.show();
                SplashActivity splashActivity = (SplashActivity) activity;
                splashActivity.beginLoginDialog(null);

            } else {


                try {
                    Toast error = Toast.makeText(context, "Nome utente o password errata", Toast.LENGTH_SHORT);
                    error.show();
                    SplashActivity splashActivity = (SplashActivity) activity;
                    splashActivity.beginLoginDialog(true);
                } catch (WindowManager.BadTokenException e) {
                    String test = PreferenceManager.getDefaultSharedPreferences(context).getString("password", "");
                    Crashlytics.log(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "") + " " + test);
                    Crashlytics.getInstance().crash();
                }
            }


        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            usr = params[0]; //PARAMETERS : USER AND PASSWORD
            psw = params[1];
            cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(cookieManager);
            URL login = new URL(Utils.LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) login.openConnection();
            connection.setRequestMethod("GET");
            publishProgress();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            for (int i = 0; i < 15; i++) {
                strings.add(connection.getHeaderField(i));
            }
            OutputReader tokenreader = new OutputReader(connection.getInputStream());

            tokenreader.readStream();
            Log.i("COOKIE", String.valueOf(cookieManager.getCookieStore().getCookies().get(0)));
            String a = tokenreader.getResult();

            matcher = Utils.TOKENPATTERN.matcher(a); //TODO SISTEMARE IL TOKEN CHE E IMMONDO
            while (matcher.find()) {
                token = matcher.group();
            }
            token = token.replace("name=\"_csrf_token\" value=\"", "").replace(" />", "").replace("\"", "");
            connection = (HttpURLConnection) new URL(Utils.LOGIN_CHECK).openConnection();
            publishProgress();
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            StringBuilder bodybuilder = new StringBuilder(); //Todo mettere string concat
            bodybuilder.append("_csrf_token=")
                    .append(token)
                    .append("&_username=")
                    .append(usr)
                    .append("&_password=")
                    .append(URLEncoder.encode(psw, "UTF-8"));
            String body = bodybuilder.toString();
            DataOutputStream logstream = new DataOutputStream(connection.getOutputStream());
            logstream.write(body.getBytes(StandardCharsets.UTF_8));
            int i = connection.getResponseCode();
            tokenreader.readStream();
            Log.i("COOKIE", String.valueOf(cookieManager.getCookieStore().getCookies().get(0)));
            HttpURLConnection testconnection = (HttpURLConnection) new URL(Utils.TEST_URL).openConnection();
            testconnection.setInstanceFollowRedirects(true);
            testconnection.connect();
            tokenreader.setIs(testconnection.getInputStream());
            tokenreader.readStream();
            String response = tokenreader.getResult();
            Log.i("TEST", String.valueOf(connection.getResponseCode()));
            if (connection.getURL().toString().contains("reset")) {
                needspasswordchange = true;
            }
            if (connection.getURL().toString().contains("area_tutore_scelta/alunni")) {
                needstudentchoice = true;
            }
            return response.contains("dimenticate");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(context, "Errore di connessione di rete", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}