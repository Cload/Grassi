package com.beccari.grassi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;

/**
 * Created by Michele on 24/04/2018.
 */

public class SettingsActivity extends AppCompatActivity {


    Context context = this;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        ImageView logout = (ImageView)  findViewById(R.id.logout_buttin);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
                Utils.SUBJECTS.clear();
                SplashActivity splashActivity = new SplashActivity();
                Intent startspash = new Intent(SettingsActivity.this, splashActivity.getClass());
                startspash.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startspash);
            }
        });
        super.onCreate(savedInstanceState);
    }
}
