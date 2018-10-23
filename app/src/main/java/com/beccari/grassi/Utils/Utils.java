package com.beccari.grassi.Utils;


import android.Manifest;
import android.os.Environment;
import android.text.Html;

import com.beccari.grassi.data.Topic;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Michele on 07/08/2017.
 */

public class Utils {
    //TODO  RIVEDERE I PATTERN PERCHÈ FANNO ABBASTANZA SCHIFO
    public static ArrayList<String> SUBJECTS = new ArrayList<>(); //TODO  VA NELLE SHAREDPREFERENCES
    public static Boolean LOGGEDIN = false;
    public static String SCHOICE_URL = "https://nuvola.madisoft.it/area_tutore/area_tutore_scelta/alunni";

    public static String LOGIN_URL = "https://nuvola.madisoft.it/login";
    public static String LOGIN_CHECK = "https://nuvola.madisoft.it/login_check";
    public static String YEAR_BEGINNING = "2018-09-10";
    public static String MARKSURL = "https://nuvola.madisoft.it/area_tutore/voto/situazione";
    public static String MARKSALTERNATIVEURL = "https://nuvola.madisoft.it/area_studente/voto/situazione";
    public static String TOPICS_URL = "https://nuvola.madisoft.it/area_studente/argomento_lezione/form/visuale-giornaliera";
    public static String TOPICS_ALTERNATIVE_URL = "https://nuvola.madisoft.it/area_tutore/argomento_lezione/form/visuale-giornaliera";
    public static String TEST_URL = "https://nuvola.madisoft.it/area_tutore/argomento_lezione/form/visuale-giornaliera/2018-09-11";
    public static Pattern TOKENPATTERN = Pattern.compile("name=\"_csrf_token\" value=\"(.*?) />");
    public static Pattern NPATTERN = Pattern.compile("<td class=\\\"grid-column-documentoRegistro_numeroRegistro\\\">(.*?)</td>");
    public static Pattern HREFPATTERN = Pattern.compile("<a href=\"(.*?) ta");
    public static Pattern TITLEPATTERN = Pattern.compile("<td class=\"grid-column-documentoRegistro_documento_oggetto(.*?)</td>");
    public static Pattern TOPICREFPATTERN = Pattern.compile("href=\"(.*?)\">");
    public static Pattern DATEPATTERN = Pattern.compile("<td class=\\\"grid-column-dataPubblicazioneOnline(.*?)</td>");
    public static Pattern TOPICDESCPATTERN = Pattern.compile("\">(.*?) </a>");
    public static Pattern MARKSPATTERN = Pattern.compile("((<span class=\"dato\">(:?.*?)<a title=\"(.*?) - (.*?) - Autore: (.*?) - Materia: (.*?) - (.*?) - Peso: (.*?)% - Fa media: (.*?) - (.*?)\"(:?.*?)href=\"/area_(?:.*?)/voto/show/(.*?)\"> <span class=\"dataTemporale\">(:?.*?)</span>(:?.*?)<span class=\"valore\">(.*?)</span></a>)+?)");
    public static Pattern MARKSDATAPATTERN = Pattern.compile("(<span class=\"dato\">(:?.*?)<a title=\"(.*?) - (.*?) - Autore: (.*?) - Materia: (.*?) - (.*?) - Peso: (.*?)% - Fa media: (.*?) - (.*?)\"(:?.*?)href=\"/area_(?:.*?)/voto/show/(.*?)\"> <span class=\"dataTemporale\">(.*?)</span>(.*?)<span class=\"valore\">(.*)</span></a>)");
    public static SimpleDateFormat CDATEFORMAT =  new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat TOPICSDATEFORMAT =  new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat MARKSDATEFORMAT =  new SimpleDateFormat("dd/MM/yyyy");
    public static Pattern URLPATTERN = Pattern.compile("href=\"/fi(.*?)\" t");
    public static Pattern NAMESPATTERN = Pattern.compile("class=\"text__ellipsed\">(.*?)</div>");
    public static Pattern EXTENSIONPATTERN= Pattern.compile("(.*?)");
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static File DOWNLOADDIRECTORY = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "Comunicati");
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static int getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return  (int)timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }


    //TODO creare un costruttore o meglio ancora un cast o settare i campi
    public static ArrayList<Topic> linkedTreeMapToArraylist(ArrayList<LinkedTreeMap> maps){
        ArrayList<Topic> topics = new ArrayList<>();
        for (LinkedTreeMap m : maps){
            Topic topic = new Topic();
            topic.setHour( (int) Double.parseDouble(String.valueOf(m.get("hour"))));
            topic.setCompresenza(String.valueOf(m.get("compresenza")));
            topic.setDescription(String.valueOf(Html.fromHtml(String.valueOf(m.get("description")))));
            topic.setDate(String.valueOf(m.get("date")));
            topic.setSubject(String.valueOf(Html.fromHtml(String.valueOf(m.get("subject")))));
            topic.setNotes(String.valueOf(m.get("notes")));
            topic.setHref(String.valueOf(m.get("href")));
            topic.setType(String.valueOf(m.get("type")));
            topic.setDocente(String.valueOf(m.get("docente")));
            topics.add(topic);
        }
        return topics;
    }
}
