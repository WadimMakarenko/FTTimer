package cbot.accept.fttimer.model;

/**
 * Created by Vadim on 5/4/2017.
 */
public class TimeData {
    private String Date;
    private String TimeFrom;
    private String TimeTo;
    private String ProjectText;
    private String ProjectName;

    /**
     * Default no-args constructor
     */
    public TimeData(){}

    public TimeData(String projName, String desc, String tFrom, String tTo, String dt ){
        this.ProjectName = projName;
        this.ProjectText = desc;
        this.TimeFrom = tFrom;
        this.TimeTo = tTo;
        this.Date = dt;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.TimeFrom = timeFrom;
    }

    public String getTimeTo() {
        return TimeTo;
    }

    public void setTimeTo(String timeTo) {
        this.TimeTo = timeTo;
    }

    public String getProjectText() {
        return ProjectText;
    }

    public void setProjectText(String projectText) {
        this.ProjectText = projectText;
    }
    public String getProjectName() {
        return ProjectName;
    }
    public void setProjectName(String projectName) {
        this.ProjectName = projectName;
    }

    @Override
    public String toString() {
        return "\nDate=" + getDate()+";TimeFrom=" + getTimeFrom() + ";TimeTo="
                + getTimeTo() + ";ProjectText=" + getProjectText()+ ";ProjectName="+getProjectName();
    }
}
