package cbot.accept.fttimer;

import cbot.accept.fttimer.model.TimeData;
import cbot.accept.fttimer.model.User;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {

    public static void writeCsv(String csvFileName, List<TimeData> times) throws IOException {
        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] {"Date", "TimeFrom", "TimeTo", "ProjectText", "ProjectName"};
            final CellProcessor[] processors = getProcessors();
            // write the beans
            for(final TimeData course : times ) {
                beanWriter.write(course, header, processors);
            }
        } finally {
            if( beanWriter != null ) {
                beanWriter.close();
            }
        }
    }

    private static CellProcessor[] getProcessors(){
        return new CellProcessor[] {
                new NotNull(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional()};
    }

    public static void writeUserData(String csvFileName, List<User> usrData) throws IOException {
        ICsvBeanWriter beanWriter = null;
        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName), CsvPreference.STANDARD_PREFERENCE);

            // the header elements are used to map the bean values to each column (names must match)
            final String[] header = new String[] { "Login", "Password"};
            final CellProcessor[] processors = getSmProcessor();

            for(final User course : usrData ) {
                beanWriter.write(course, header, processors);
            }
        } finally {
            if( beanWriter != null ) {
                beanWriter.close();
            }
        }
    }
    private static CellProcessor[] getSmProcessor(){
        return new CellProcessor[] {
                new NotNull(),
                new NotNull()};
    }
}
