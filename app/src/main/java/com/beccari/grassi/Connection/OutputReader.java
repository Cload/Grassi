package com.beccari.grassi.Connection;


import android.content.Context;
import android.util.Log;


import com.beccari.grassi.R;
import com.beccari.grassi.Utils.Utils;
import com.beccari.grassi.data.Communication;
import com.beccari.grassi.data.Topic;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michele on 07/08/2017.
 */

public class OutputReader {
    //todo rivedere la parte con tutti i replace e risistemarla con regex
    InputStream is;
    String result;
    private Integer progress;

    public OutputReader() {
    }

    //Todo inputstream è una porcata cambiare costruttore (anche no basta aggiungerne uno buoto)
    public OutputReader(InputStream is) {
        this.is = is;
    }
    public void setIs(InputStream is) {
        this.is = is;
    }
    public void readStream() {

        StringBuilder sb = new StringBuilder();
        String line;

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (br.read() != -1) {
                line = br.readLine();
                sb.append(line);
            }
            br.close();
        } catch (IOException ie) {
            Log.d("Parse", "Errore di parsing");
        }
        setResult(sb.toString());
        Log.d("Debug", "Test Di Debug");
    }
    public String getResult() {
        return result;
    }
    public String getTable() {
        StringBuilder tBuilder = new StringBuilder();
        String table = "";
        Pattern cPattern = Pattern.compile("<tbody>.*</tbody>");
        Matcher matcher = cPattern.matcher(getResult());
        while (matcher.find()) {
            tBuilder = new StringBuilder();
            tBuilder.append(matcher.group());
            Log.d("Matcher", matcher.group());
            break;
        }
        table = tBuilder.toString();
        return table;
    }
    public String getTable(int which) {
        StringBuilder tBuilder = new StringBuilder();
        String table = "";
        Pattern cPattern = Pattern.compile("<tbody>.*</tbody>");
        Matcher matcher = cPattern.matcher(getResult());
        while (matcher.find()) {
            tBuilder = new StringBuilder();
            tBuilder.append(matcher.group(which));
            Log.d("Matcher", matcher.group(which));
            break;
        }
        table = tBuilder.toString();
        return table;
    }
    public ArrayList<String> getRows(String table) { //table is not a class attribute to make class as general as possible
        ArrayList<String> rows = new ArrayList<>();
        Pattern cPattern = Pattern.compile("<tr(.*?)</tr>", Pattern.DOTALL);
        Matcher matcher = cPattern.matcher(table);
        while (matcher.find()) {
            rows.add(matcher.group().replace(" data", ""));
            Log.d("Matcher", matcher.group());
        }
        return rows;
    }
    public ArrayList<Communication> getCommunications(ArrayList<String> rows) {
        Log.d("va", "almeno dovrebbe");
        ArrayList<Communication> communications = new ArrayList<>();
        String value;
        Matcher matcher;
        for (int i = 0; i < rows.size(); i++) {
            Communication communication = new Communication();
            matcher = Utils.NPATTERN.matcher((rows.get(i)));
            while (matcher.find()) {
                value = matcher.group();
                value = value
                        .replace("<td class=\"grid-column-documentoRegistro_numeroRegistro\">", "")
                        .replace(" last-row", "")
                        .replace("</td>", "");
                Log.d("Valuta parsata", value);
                try {
                    communication.setNumber(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    Log.d("parse dei numeri", String.valueOf(i) + "   " + value);
                    communication.setNumber(null);

                }
            }
            matcher = Utils.HREFPATTERN.matcher(rows.get(i));
            while (matcher.find()) {
                value = matcher.group();
                value = value
                        .replace("<a href=\"", "")
                        .replace(" last-row", "")
                        .replace("\"", "")
                        .replace(" ta", "");

                communication.setHref(value);
            }
            matcher = Utils.TITLEPATTERN.matcher(rows.get(i));
            while (matcher.find()) {
                value = matcher.group();
                value = value
                        .replace("<td class=\"grid-column-documentoRegistro_documento_oggetto\">", "")
                        .replace(" last-row", "")
                        .replace("'", "\'")
                        .replace("</td>", "");

                communication.setTitle(value);
            }
            matcher = Utils.DATEPATTERN.matcher(rows.get(i));
            while (matcher.find()) {
                value = matcher.group();
                value = value
                        .replace("<td class=\"grid-column-dataPubblicazioneOnline", "")
                        .replace(" align-center\"", "")
                        .replace(" last-row", "")
                        .replace("'", "\'")
                        .replace("</td>", "")
                        .replace(">", "")
                        .replace("\"", "")
                        .replace("align-center", "")
                        .replace(" ", "");
                try {
                    Log.d("Data giusta", value);
                    communication.setDate(Utils.CDATEFORMAT.parse(value));
                } catch (ParseException e) {
                    Log.d("errore data", "q" + value);
                }
            }
            if (communication.getDate()!=null) { //date è flag per vedere se la comunicazione è nulla
                communications.add(communication);
            }
        }

        return communications;
    }
    public Boolean islastDate(HttpURLConnection connection) {
        Date currentdate = new Date();
        String date = connection.getURL().toString();
        date = date.substring(date.length() - 5);
        return Utils.TOPICSDATEFORMAT.format(currentdate).equals(date);
    }
    private void setResult(String result) {
        this.result = result;
    }
    public ArrayList<String> getElements(String row) {
        ArrayList<String> elements = new ArrayList<>();
        Pattern cPattern = Pattern.compile("<td(.*?)</td>", Pattern.DOTALL);
        Matcher matcher = cPattern.matcher(row);
        while (matcher.find()) {
            elements.add(matcher.group().replace(" data", ""));
            Log.d("Matcher", matcher.group());
        }
        return elements;
    }
    public Topic getTopic(ArrayList<String> elements) {
        Topic topic = new Topic();
        Pattern cPattern = Pattern.compile("<td(.*?)</td>", Pattern.DOTALL);
        for (int i = 0; i < elements.size(); i++) {
            if (!elements.get(i).isEmpty()) {
                Matcher matcher = cPattern.matcher(elements.get(i));
                while (matcher.find()) {
                    switch (i) {
                        case 0:
                            try {
                                topic.setHour(Integer.parseInt(matcher.group().replace("<td>", "").replace("</td>", "")));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            topic.setType(matcher.group().replace("<td>", "").replace("</td>", ""));
                            break;
                        case 2:
                            Matcher href = Utils.TOPICREFPATTERN.matcher(matcher.group());
                            Matcher desc = Utils.TOPICDESCPATTERN.matcher(matcher.group());
                            while (href.find()) {
                                topic.setHref("https://nuvola.madisoft.it" + href.group().replace("href=\"", "").replace("\">", ""));
                            }
                            while (desc.find()) {
                                topic.setDescription(desc.group().replace("\">", "").replace(" </a>", ""));
                            }
                            break;
                        case 3:
                            topic.setDocente(matcher.group().replace("<td>", "").replace("</td>", ""));
                            break;
                        case 4:
                            topic.setSubject(matcher.group().replace("<td>", "").replace("</td>", ""));
                            break;
                        case 5:
                            topic.setNotes(matcher.group().replace("<td>", "").replace("</td>", ""));
                            break;
                        case 6:
                            topic.setCompresenza(matcher.group().replace("<td>", "").replace("</td>", ""));
                            break;
                    }

                }
            }
        }
        return topic;
    }

}
