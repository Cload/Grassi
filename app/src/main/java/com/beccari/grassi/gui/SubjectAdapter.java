package com.beccari.grassi.gui;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.activities.TopicsActivity;
import com.beccari.grassi.data.Topic;

import java.util.ArrayList;

/**
 * Created by Michele on 22/10/2017.
 */

public class SubjectAdapter extends BaseAdapter {
    ArrayList<Topic> topics;
    Activity activity;
    Context context;

    public SubjectAdapter(Activity activity, Context context, ArrayList<Topic> topics) {
        this.topics = topics;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Utils.SUBJECTS.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View view = activity.getLayoutInflater().inflate(R.layout.cardadapter, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.card_sub);
        TextView title = (TextView) view.findViewById(R.id.card_title);
        if (Utils.SUBJECTS.get(position).equals("SCIENZE NATURALI (BIOLOGIA, CHIMICA, SCIENZE DELLA TERRA)")) {
            title.setText("SCIENZE"); //TODO RESOURCES
        }
        else {
            title.setText(Html.fromHtml(Utils.SUBJECTS.get(position)));
        }
        switch (position){
            case 5:
            case 9:
            case 0:
                cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.subject_1));
                break;
            case 1:
            case 8:
            case 11:
                cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.subject_2));
                break;
            case 2:
            case 7:
            case 12:
                cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.subject_3));
                break;
            case 3:
            case 6:
            case 13:
                cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.subject_4));
                break;
            case 4:
            case 10:
            case 14:
                cardView.setCardBackgroundColor(activity.getResources().getColor(R.color.subject_5));
                break;
            default:
                break;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopicsActivity mtopicsac = new TopicsActivity();
                Intent intent = new Intent(activity, mtopicsac.getClass());
                intent.putExtra("POSITION", position);
                activity.startActivity(intent);
            }
        });

        return view;
    }
}
