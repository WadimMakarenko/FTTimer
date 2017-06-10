package cbot.accept.fttimer;


import cbot.accept.fttimer.model.JSONParser;
import cbot.accept.fttimer.model.TimeData;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataController {
    public String USER_AGENT = "Mozilla/5.0";
    public String PERSON_URL = "https://cbot-accept.de/index.php/TimeAPI/setTimeEntries";
    public String DIRECTORY = "TimeEntries";
    public String FILE_PATH = "TimeEntries/TimeEntries_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".csv";
    public String STATUS = "";

    public String saveTimeEntries(String mainDate,String timeFrom, String timeTo, String desc, String projName, int resource) throws IOException {
        int sentStatus = sendTimeEntries(mainDate, timeFrom, timeTo, desc, projName);
        STATUS += "Connection status: "+sentStatus;
        if(sentStatus != 200){
            STATUS += "\nCan't connect to server...";
            File dir = new File(DIRECTORY);
            File file = new File(FILE_PATH);
            if (dir.exists() && dir.isDirectory()) {

                if (file.exists()) {
                    appendUsingFileWriter(mainDate, timeFrom, timeTo,desc,projName);
                    System.out.println("File existed");
                    STATUS += "Directory existed...\nFile was updated...\nPath: "+FILE_PATH;
                } else {
                    writeCSVFileTE(mainDate, timeFrom, timeTo,desc,projName);
                    STATUS += "Directory existed...\nFile was created...\nPath: "+FILE_PATH;
                }
            }else{
                File directory = new File(DIRECTORY);
                boolean successful = directory.mkdir();
                if (successful) {
                    STATUS += "Directory was created...\nPath: "+DIRECTORY;
                    writeCSVFileTE(mainDate, timeFrom, timeTo,desc,projName);

                } else {
                    STATUS += "\nFailed trying to create the directory: You can try manually create new Directory "+FILE_PATH;
                }
            }
        }
        if(resource == 1){
            return STATUS;
        }else{
            return String.valueOf(sentStatus);
        }

    }

    //####Write Time Entries into .csv file
    private void writeCSVFileTE(String mainDate, String timeFrom, String timeTo, String desc, String projName) throws IOException {
        List<TimeData> times = new ArrayList<>();
        times.add(new TimeData(projName, desc, timeFrom, timeTo, mainDate));
        // write csv
      CsvFileWriter.writeCsv(FILE_PATH, times);
    }

    //####Update Time Entries into file
    private  void appendUsingFileWriter(String mainDate, String timeFrom, String timeTo, String desc, String projName) throws IOException{
        String text = mainDate+","+timeFrom+","+timeTo+","+desc+","+projName+"\n";
        File file = new File(FILE_PATH);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            fr.write(text);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
               fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //####Make HTTPRequest and get Response
    public int sendTimeEntries(String mainDate, String timeFrom, String timeTo, String desc, String projName) throws IOException {
        URL obj = new URL(PERSON_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        //get tocken from log
        CsvParser parse = new CsvParser();
        String[] usrData = new String[2];
        usrData = parse.parseUserData("log_information.log");

        String postJsonData = JSONParser.buildTEJson(usrData[1],mainDate, timeFrom, timeTo,desc,projName);

        // Send post request
        con.setDoOutput(true);
        try{
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //Debug info
            //System.out.println("nSending 'POST' request to URL : " + PERSON_URL);
            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            //printing result from response
            STATUS += response.toString()+"\n";
            return responseCode;
        }catch (Exception e){
            return 400;
        }
    }

    //####Make POST Request and get response TimeTable data
    public String getTimeEntries(String timeFrom, String timeTo, String projName) throws IOException {
        String nurl = "https://cbot-accept.de/index.php/TimeAPI/getTimeEntries";
        URL obj = new URL(nurl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        //get Tocken from log
        CsvParser parse = new CsvParser();
        String[] usrData = new String[2];
        usrData = parse.parseUserData("log_information.log");

        String postJsonData = JSONParser.buildSendData(usrData[1], timeFrom, timeTo, projName);

        // Send post request
        con.setDoOutput(true);
        try{
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //Debug info
            //System.out.println("nSending 'POST' request to URL : " + nurl);
            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            System.out.println(response.toString());
            return response.toString();
        }catch (Exception e){
            return "400";
        }
    }

    //####Update TE HTTPRequest and get Response
    public int updateTEntry(String TimeID, String timeFrom, String timeTo, String desc, String projName) throws IOException {
        String url = "https://cbot-accept.de/index.php/TimeAPI/updateTEntry";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        //get tocken from log
        CsvParser parse = new CsvParser();
        String[] usrData = new String[2];
        usrData = parse.parseUserData("log_information.log");

        String postJsonData = JSONParser.getToUpdateJson(usrData[1], TimeID, timeFrom, timeTo,desc,projName);

        // Send post request
        con.setDoOutput(true);
        try{
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //Debug info
            //System.out.println("nSending 'POST' request to URL : " + PERSON_URL);
            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            return responseCode;
        }catch (Exception e){
            return 400;
        }
    }

    //####Delete TE HTTPRequest and get Response
    public int deleteTEnry(String TimeID) throws IOException {
        String url = "https://cbot-accept.de/index.php/TimeAPI/deleteTimeEntry";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");

        //get tocken from log
        CsvParser parse = new CsvParser();
        String[] usrData = new String[2];
        usrData = parse.parseUserData("log_information.log");

        String postJsonData = JSONParser.getTodelete(usrData[1], TimeID);

        // Send post request
        con.setDoOutput(true);
        try{
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postJsonData);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //Debug info
            //System.out.println("nSending 'POST' request to URL : " + PERSON_URL);
            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            return responseCode;
        }catch (Exception e){
            return 400;
        }
    }
}




