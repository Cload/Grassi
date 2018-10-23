package com.beccari.grassi.data;

import com.beccari.grassi.Utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Michele on 29/03/2018.
 */

public class Subject {
    String name;

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void getSubjectsfromMarks(ArrayList<Mark> marks) {
        Utils.SUBJECTS.clear();
        for (int j = 0; j < marks.size(); j++) {
            Boolean f = true;                                   //TODO METODO IMMONDO DA RIVEDERE

            if (j == 0 && Utils.SUBJECTS.isEmpty()) {
                Utils.SUBJECTS.add(marks.get(j).getSubjectName());
            }
            f = true;
            String subject = marks.get(j).getSubjectName();
            for (int i = 0; i < Utils.SUBJECTS.size(); i++) {
                if (subject.equals(Utils.SUBJECTS.get(i))) {
                    f = false;
                }
            }
            if (f) {
                Utils.SUBJECTS.add(marks.get(j).getSubjectName());
            }


        }

    }

}