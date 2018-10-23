package com.beccari.grassi.data;

import com.beccari.grassi.Utils.Utils;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

/**
 * Created by Michele on 21/10/2017.
 */

public class Topic {

    int hour;
    String href;
    String type;
    String docente;
    String subject;
    String compresenza;
    String notes;
    String date;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getHour() {
        return hour;
    }

    public String getType() {
        return type;
    }

    public String getDocente() {
        return docente;
    }

    public String getSubject() {
        return subject;
    }

    public String getCompresenza() {
        return compresenza;
    }

    public String getNotes() {
        return notes;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCompresenza(String compresenza) {
        this.compresenza = compresenza;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static void getSubjects(ArrayList<Topic> topics) {
        Utils.SUBJECTS.clear();
        Boolean f = true;                                   //TODO METODO IMMONDO DA RIVEDERE
        for (int j = 0; j < topics.size(); j++) {
            if (j == 0 && Utils.SUBJECTS.isEmpty()) {
                Utils.SUBJECTS.add(topics.get(j).getSubject());
            }
            f = true;
            String subject = topics.get(j).getSubject();
            for (int i = 0; i < Utils.SUBJECTS.size(); i++) {
                if (subject.equals(Utils.SUBJECTS.get(i))) {
                    f = false;
                }
            }
            if (f) {
                Utils.SUBJECTS.add(topics.get(j).getSubject());
            }
        }
    }



    public static ArrayList<Topic> getTopicsFromSubject(ArrayList<Topic> topics, String subject) {
        ArrayList<Topic> stopics = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i).getSubject().equals(subject)) {
                stopics.add(topics.get(i));
            }
        }
        return stopics;
    }
}

