package com.beccari.grassi.data;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.beccari.grassi.Connection.OutputReader;
import com.beccari.grassi.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;

/**
 * Created by Michele on 12/08/2017.
 */

public class Communication {

    private String title;
    private String href;
    private Integer number;
    private Date date;
    private   ArrayList<String> files = new ArrayList<>();
    private  ArrayList<String> filesnames = new ArrayList<>();
    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
    public void setFilesnames(ArrayList<String> filesnames) {
        this.filesnames = filesnames;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public String getHref() {
        return href;
    }
    public Integer getNumber() {
        return number;
    }
    public ArrayList<String> getFiles() {
        return files;
    }
    public void addFile(String file){
        files.add(file);
    }
    public void addFilename(String filename){
        filesnames.add(filename);
    }
    public ArrayList<String> getFilesnames() {
        return filesnames;
    }
}
