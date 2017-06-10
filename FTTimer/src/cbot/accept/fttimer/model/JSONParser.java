package cbot.accept.fttimer.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {
    public static String parseUrl(URL url) {
        if (url == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public static void parseCurrentJson(String resultJson) {
        try {
            // convert String from Json to JSONObject
            JSONObject statusJsonObject = (JSONObject) JSONValue.parseWithException(resultJson);
            JSONArray statusArray = (JSONArray) statusJsonObject.get("result");

            System.out.println(statusArray.size());
            JSONObject statusData = (JSONObject) statusArray.get(0);

            String TimeID = (String) statusData.get("TimeID");
            String Date = (String) statusData.get("Date");
            String timeBegin = (String)statusData.get("TimeBegin");
            String timeEnd = (String)statusData.get("TimeEnd");
            String ProjectName = (String)statusData.get("ProjectName");
            String ProjectText = (String)statusData.get("ProjectText");

            ObservableList<getTimeEntries> entries = FXCollections.observableArrayList();
            entries.add(new getTimeEntries(TimeID, Date, timeBegin, timeEnd, ProjectName, ProjectText));
            System.out.println(entries);

        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    // create new JSON Object
    public static String buildTEJson(String Tocken, String mainDate,String timeFrom, String timeTo, String desc, String projName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tocken", Tocken);
        jsonObject.put("date", mainDate);
        jsonObject.put("time_begin", timeFrom);
        jsonObject.put("time_end", timeTo);
        jsonObject.put("project_text", desc);
        jsonObject.put("project_name", projName);

        return jsonObject.toJSONString();
    }

    // create new JSON Object
    public static String getToUpdateJson(String Tocken, String TimeID, String timeFrom, String timeTo, String desc, String projName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tocken", Tocken);
        jsonObject.put("TimeID", TimeID);
        jsonObject.put("time_begin", timeFrom);
        jsonObject.put("time_end", timeTo);
        jsonObject.put("project_text", desc);
        jsonObject.put("project_name", projName);

        return jsonObject.toJSONString();
    }

    // create new JSON Object
    public static String getTodelete(String Tocken, String TimeID) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tocken", Tocken);
        jsonObject.put("TimeID", TimeID);
        return jsonObject.toJSONString();
    }

    //###Create new JSOn Object and then GET JSON Time Entries
    public static String buildSendData(String Tocken, String timeFrom, String timeTo,String projName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tocken", Tocken);
        jsonObject.put("dateFrom", timeFrom);
        jsonObject.put("dateTo", timeTo);
        jsonObject.put("ProjectName", projName);

        return jsonObject.toJSONString();
    }

    public static URL createUrl(String link) {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
