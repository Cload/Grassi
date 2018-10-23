package com.beccari.grassi.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Michele on 14/11/2017.
 */

public class Mark implements Parcelable {


    Date date;
    Subject subject;
    String type;
    double math_value;
    String display_value;
    Boolean average;
    String description;

    public Mark(){}
    protected Mark(Parcel in) {
        type = in.readString();
        math_value = in.readDouble();
        display_value = in.readString();
        description = in.readString();
        date =  new Date(in.readLong()*1000);
        subject = new Subject(in.readString());
        average = in.readInt() == 1;

    }

    public static final Creator<Mark> CREATOR = new Creator<Mark>() {
        @Override
        public Mark createFromParcel(Parcel in) {
            return new Mark(in);
        }

        @Override
        public Mark[] newArray(int size) {
            return new Mark[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubjectName() {
        return subject.getName();
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMath_value() {
        return math_value;
    }

    public void setMath_value() {
        if (display_value.matches(".*\\d+.*")){

            if (display_value.length() > 1) {
                if (display_value.contains("0")){
                    math_value = 10;
                }
                else {
                    math_value = Double.parseDouble(display_value.substring(0, 1)) + 0.5;
                }
            } else {
                math_value = Double.parseDouble(display_value.substring(0, 1));
            }
    }
    }

    public String getDisplay_value() {
        return display_value;
    }

    public void setDisplay_value(String display_value) {
        this.display_value = display_value;
    }

    public Boolean getAverage() {
        return average;
    }

    public void setAverage(String average) {
        if (average.equals("SI")) {
            this.average = true;
        } else {
            this.average = false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(subject.getName());
        dest.writeValue(math_value);
        dest.writeInt(average ? 1 : 0);
        dest.writeString(display_value);
        dest.writeString(description);
        dest.writeString(type);
    }

}
