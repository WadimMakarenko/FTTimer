package cbot.accept.fttimer;

import cbot.accept.fttimer.model.TimeData;
import cbot.accept.fttimer.model.User;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvParser {

    public static String[] parseUserData(String fileName) throws IOException {
        String[] usrData = new String[2];
        ICsvBeanReader csvBeanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);

        String[] mapping = new String[]{"Login", "Password"};

        CellProcessor[] procs = getProcessors();
        User user;

        while ((user = csvBeanReader.read(User.class, mapping, procs)) != null) {
            usrData[0] = user.getPassword();
            usrData[1] = user.getLogin();
        }
        csvBeanReader.close();
        return usrData;
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[]{
                new NotNull(),
                new NotNull()
        };
    }

    //upload file to server
    public static String parseTimeEntries(String fileName) throws IOException{
    	 int toDelete = 0;
    	 String toStatus = "";
        ICsvBeanReader csvBeanReader = new CsvBeanReader(new FileReader(fileName), CsvPreference.STANDARD_PREFERENCE);
        String[] mapping = new String[]{"Date", "TimeFrom", "TimeTo", "ProjectText", "ProjectName"};

        CellProcessor[] procs = getProcessorsTime();
        TimeData timeEntries;
        DataController toSend = new DataController();
        while ((timeEntries = csvBeanReader.read(TimeData.class, mapping, procs)) != null) {
            int status =  toSend.sendTimeEntries(timeEntries.getDate(), timeEntries.getTimeFrom(), timeEntries.getTimeTo(), timeEntries.getProjectText(), timeEntries.getProjectName());
            if(status != 200){
                csvBeanReader.close();
                toDelete = 400;
            }else{
            	toDelete = 200;
            }
        }
        if(toDelete == 200){
        	csvBeanReader.close();
            File filePath = new File(fileName);
            filePath.delete();
            toStatus = "\nFile: "+fileName+" was uploaded and then deleted...";
        }else{
        	toStatus = "\nCan't connect to Server, try pleas later...";
        }
        
        return toStatus;
    }

    private static CellProcessor[] getProcessorsTime(){
        return new CellProcessor[] {
                new NotNull(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()};
    }
}