package cbot.accept.fttimer.model;

import javafx.beans.property.SimpleStringProperty;

public class getTimeEntries {
    public final SimpleStringProperty TimeID;
    public final SimpleStringProperty Date;
    public final SimpleStringProperty TimeBegin;
    public final SimpleStringProperty TimeEnd;
    public final SimpleStringProperty ProjectName;
    public final SimpleStringProperty ProjectText;


  public getTimeEntries(String timeID, String date, String timeBegin, String timeEnd, String projectName, String projectText){

      this.TimeID = new SimpleStringProperty(timeID);
      this.Date = new SimpleStringProperty(date);
      this.TimeBegin = new SimpleStringProperty(timeBegin);
      this.TimeEnd = new SimpleStringProperty(timeEnd);
      this.ProjectName = new SimpleStringProperty(projectName);
      this.ProjectText = new SimpleStringProperty(projectText);
  }


    public String getTimeID() {
        return TimeID.get();
    }

    public SimpleStringProperty timeIDProperty() {
        return TimeID;
    }

    public String getDate() {
        return Date.get();
    }

    public SimpleStringProperty dateProperty() {
        return Date;
    }

    public String getTimeBegin() {
        return TimeBegin.get();
    }

    public SimpleStringProperty timeBeginProperty() {
        return TimeBegin;
    }

    public String getTimeEnd() {
        return TimeEnd.get();
    }

    public SimpleStringProperty timeEndProperty() {
        return TimeEnd;
    }

    public String getProjectName() {
        return ProjectName.get();
    }

    public SimpleStringProperty projectNameProperty() {
        return ProjectName;
    }

    public String getProjectText() {
        return ProjectText.get();
    }

    public SimpleStringProperty projectTextProperty() {
        return ProjectText;
    }

    public void setTimeID(String timeID) {
        this.TimeID.set(timeID);
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public void setTimeBegin(String timeBegin) {
        this.TimeBegin.set(timeBegin);
    }

    public void setTimeEnd(String timeEnd) {
        this.TimeEnd.set(timeEnd);
    }

    public void setProjectName(String projectName) {
        this.ProjectName.set(projectName);
    }

    public void setProjectText(String projectText) {
        this.ProjectText.set(projectText);
    }
}
