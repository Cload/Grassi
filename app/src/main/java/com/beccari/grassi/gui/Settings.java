package com.beccari.grassi.gui;

import android.content.Context;

import com.beccari.grassi.R;

import java.text.ParseException;
import java.util.Date;

import static com.beccari.grassi.Utils.Utils.MARKSDATEFORMAT;

/**
 * Created by Michele on 03/02/2018.
 */

public class Settings {

    public static int CURRENT_PERIOD; // 1 = FIRST TERM, 2 = SECOND TERM, 3 = whole year

    public  void getCurrentPeriod(Context context) throws ParseException {
        Date today = new Date();
        Date firstterm = MARKSDATEFORMAT.parse(context.getResources().getString(R.string.termdate));
        if (today.before(firstterm)) {
            CURRENT_PERIOD = 1;
        } else {
            CURRENT_PERIOD = 2;
        }
    }
}
