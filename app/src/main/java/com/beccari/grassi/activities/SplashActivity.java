package com.beccari.grassi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beccari.grassi.Connection.LoginChecker;
import com.beccari.grassi.Connection.PasswordReset;
import com.beccari.grassi.R;

/**
 * Created by Michele on 19/11/2017.
 */

public class SplashActivity extends Activity {
    private Context activitycontext;
    AlertDialog logindialog, resetpswdialog;
    String usr, psw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activitycontext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        beginLoginDialog(null);

    }

    public void beginLoginDialog(@Nullable Boolean reset) {
        activitycontext = this;
        String mpassword = PreferenceManager.getDefaultSharedPreferences(this).getString("password", "");
        if (mpassword.isEmpty() || mpassword.equals("") || reset!=null)  {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogview = getLayoutInflater().inflate(R.layout.logincustomdialog, null);
            builder.setTitle(R.string.dialoglog);
            builder.setView(dialogview);
            final EditText password = (EditText) dialogview.findViewById(R.id.password);
            final EditText username = (EditText) dialogview.findViewById(R.id.username);
            password.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            username.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            builder.setPositiveButton("CONFERMA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    psw = password.getText().toString();
                    usr = username.getText().toString();
                    LoginChecker loginChecker = new LoginChecker(SplashActivity.this, SplashActivity.this);
                    loginChecker.execute(usr, psw);
                }
            });
            logindialog = builder.create();
            logindialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            logindialog.setCancelable(false);
            logindialog.show();
        } else {
            psw = mpassword;
            usr = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
            LoginChecker loginChecker = new LoginChecker(SplashActivity.this, SplashActivity.this);
            loginChecker.execute(usr, psw);
        }
    }

    public void beginPswResetDialog() {
        if (logindialog != null && logindialog.isShowing()) {
            logindialog.dismiss();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogview = getLayoutInflater().inflate(R.layout.resetpswcustomdialog, null);
        builder.setTitle(R.string.resetpswtitle);
        builder.setView(dialogview);
        builder.setPositiveButton("PROSEGUI", null);
        builder.setNegativeButton("INDIETRO", null);
        resetpswdialog = builder.create();
        final EditText currentpassword = (EditText) dialogview.findViewById(R.id.current_psw);
        final EditText newpassword = (EditText) dialogview.findViewById(R.id.new_psw);
        final EditText confirmpassword = (EditText) dialogview.findViewById(R.id.confirm_psw);
        currentpassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        newpassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        confirmpassword.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        resetpswdialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("TESTDIALOGO", "dovrebbe essere");
                        LinearLayout step1 = (LinearLayout) resetpswdialog.findViewById(R.id.step1);
                        step1.setVisibility(View.GONE);
                       LinearLayout step2 = (LinearLayout) resetpswdialog. findViewById(R.id.step2);
                        step2.setVisibility(View.VISIBLE);
                        if (currentpassword.getVisibility()==View.VISIBLE){
                            if (confirmpassword.getText().length()>0
                                    && newpassword.getText().length()>0
                                        && currentpassword.length()>0){
                                        PasswordReset passwordResetter = new PasswordReset(getApplicationContext());
                                        passwordResetter.execute(currentpassword.getText().toString(), newpassword.getText().toString(),
                                                confirmpassword.getText().toString());
                                        Log.i("TESTRESET", "Partito");

                            } else {
                                Toast.makeText(activitycontext, "Inserisci i tuoi dati", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                Button button1 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout step1 = (LinearLayout) resetpswdialog.findViewById(R.id.step1);
                        LinearLayout step2 = (LinearLayout) resetpswdialog. findViewById(R.id.step2);
                        if (step2.getVisibility()==View.VISIBLE) {
                            step2.setVisibility(View.GONE);
                            step1.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });
        resetpswdialog.setCancelable(false);
        resetpswdialog.show();
        resetpswdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

    }
}
