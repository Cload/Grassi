package com.beccari.grassi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.gui.Settings;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.ParseException;

import io.fabric.sdk.android.Fabric;

public class MenuActivity extends Activity {
    ImageView ccloud, topics, marks, settings;
    Context context = this;
    EditText username, password;
    Intent startCFetch;
    String psw;

    @Override
    protected void onResume() {
        topics.setEnabled(true);
        super.onResume();
    }

    //TODO CREARE CLASSE CHE ESTENDA SHAREDPREFERENCES
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, "ca-app-pub-5736238666521337~5681019634");
        AdView menuad = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        menuad.loadAd(adRequest);
        Settings msettings = new Settings();
        try {
            msettings.getCurrentPeriod(getApplicationContext());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        marks = (ImageView) findViewById(R.id.marks_button);
        ccloud = (ImageView) findViewById(R.id.cloud_button);
        topics = (ImageView) findViewById(R.id.topics);
        settings = (ImageView) findViewById(R.id.settings);

        topics.setEnabled(true);
        ccloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = PreferenceManager.getDefaultSharedPreferences(context).getString("CommURL", "");
                if (json.equals("")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View dialogview = getLayoutInflater().inflate(R.layout.schoolcodedialog, null);
                    builder.setView(dialogview);
                    builder.setCancelable(true);
                    final EditText id = (EditText) dialogview.findViewById(R.id.schoolidtext);
                    builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String CommURL =  id.getText().toString();
                            if (!CommURL.equals("")) {
                                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("CommURL", CommURL).apply();
                                startCFetch = new Intent(MenuActivity.this, CommunicationFragment.class);
                                startActivity(startCFetch);
                            } else {
                                Toast.makeText(context, "Inserisci un indirizzo valido", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().dismiss();
                        }
                    });
                    builder.create();
                    builder.show();
                } else {
                    startCFetch = new Intent(MenuActivity.this, CommunicationFragment.class);
                    startActivity(startCFetch);
                }
            }
        });
        topics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topics.setEnabled(false);
                SubjectActivity subjectActivity = new SubjectActivity();
                Intent startsubj = new Intent(MenuActivity.this, subjectActivity.getClass());
                startActivity(startsubj);

            }
        });
        marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* MarksFetcher marksFetcher = new MarksFetcher(getApplicationContext(), MenuActivity.this);
                marksFetcher.execute();*/
                MarksActivity activity = new MarksActivity();
                Intent startmarks = new Intent(MenuActivity.this, activity.getClass());
                startActivity(startmarks);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity settingsActivity = new SettingsActivity();
                Intent startsettings = new Intent(MenuActivity.this, settingsActivity.getClass());
                startActivity(startsettings);
            }
        });
    }

    @Override
    protected void onStart() {
        topics.setEnabled(true);
        super.onStart();
    }
}