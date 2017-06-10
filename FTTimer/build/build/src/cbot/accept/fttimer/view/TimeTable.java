package cbot.accept.fttimer.view;

import cbot.accept.fttimer.DataController;

import cbot.accept.fttimer.ScreenEvents;
import cbot.accept.fttimer.model.getTimeEntries;
import cbot.accept.fttimer.model.resManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimeTable extends Application {
    public double initialX, initialY;
    public int dataSize = 0;
    public String DATEFROM;
    public String DATETO;
    public String PROJECTNAME;
    public String STATUS = null;
    public List<getTimeEntries> data = createData(DATEFROM, DATETO, PROJECTNAME);
    public String UserName;

    public String TimeID;
    public String MainDate;
    public String TimeFrom;
    public String TimeTo;
    public String ProjectName;
    public String Description;
    public boolean CLOSED = true;


    /* Constructor */
    public TimeTable(String UserName){
        this.UserName = UserName;
    }

    private List<getTimeEntries> createData(String dateFrom, String dateTo, String projectName){
        STATUS = "Running..\n";
        try {
        List<getTimeEntries> data = new ArrayList<>(dataSize);
            DataController dataController = new DataController();
            String result = dataController.getTimeEntries(dateFrom, dateTo, projectName);
            if(result.equals("400")){
                STATUS += "Status connection: 400\nCan't connect to SERVER...\n";
            }else{
                STATUS += "Status connection: 200\n";

                JSONObject statusJsonObject = (JSONObject) JSONValue.parseWithException(result);
                JSONArray statusArray = (JSONArray) statusJsonObject.get("result");
                dataSize = statusArray.size();
                for (int i = 0; i < dataSize; i++) {
                    JSONObject statusData = (JSONObject) statusArray.get(i);
                    String TimeID = (String) statusData.get("TimeID");
                    String Date = (String) statusData.get("Date");
                    String timeBegin = (String)statusData.get("TimeBegin");
                    String timeEnd = (String)statusData.get("TimeEnd");
                    String ProjectName = (String)statusData.get("ProjectName");
                    String ProjectText = (String)statusData.get("ProjectText");
                    data.add(new getTimeEntries(TimeID, Date, timeBegin, timeEnd, ProjectName, ProjectText));
                }
                    STATUS += "Time Entries was loaded...\n";
                return data;
            }
            return data;
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            STATUS += "Can't connect to SERVER...\n";
            return null;
        } catch (IOException e) {
            STATUS += "Can't connect to SERVER...\n";
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        addDragListeners(root);
        Scene scene = new Scene(root, 900, 450);
        scene.getStylesheets().add("file:resources/Style.css");

        //Header
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(30);
        header.getStyleClass().add("back-darkGray");
        ImageView icon = new ImageView(resManager.getTimerImg());
        icon.setFitWidth(35);
        icon.setFitHeight(30);
        header.setTopAnchor(icon, 1.0);
        header.setLeftAnchor(icon, 10.0);

        Text appName = new Text();
        appName.setText("FTTimer");
        appName.getStyleClass().add("cardText");
        header.setTopAnchor(appName, 10.0);
        header.setLeftAnchor(appName, 45.0);

        ImageView iconUsr = new ImageView(resManager.getUsrIcon());
        iconUsr.setFitWidth(35);
        iconUsr.setFitHeight(30);
        header.setTopAnchor(iconUsr, 0.0);
        header.setRightAnchor(iconUsr, 70.0);

        Text usrNm = new Text();
        usrNm.setText(UserName);
        usrNm.getStyleClass().add("cardText");
        header.setTopAnchor(usrNm, 10.0);
        header.setRightAnchor(usrNm, 115.0);

        Button closeBtn = new Button("X");
        closeBtn.setOnAction(event -> {
            primaryStage.close();
        });
        closeBtn.getStyleClass().add("closeBtn");
        header.setTopAnchor(closeBtn, 1.0);
        header.setRightAnchor(closeBtn, 5.0);

        Button hideBtn = new Button("_");
        hideBtn.setOnAction(e -> {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
        });
        hideBtn.getStyleClass().add("closeBtn");
        header.setTopAnchor(hideBtn, 1.0);
        header.setRightAnchor(hideBtn, 35.0);

        header.getChildren().addAll(icon, appName, usrNm, hideBtn, closeBtn, iconUsr);
        root.setTop(header);
        //End header

        //Left menu pane Vbox
        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(2));
        vbox.setSpacing(2);
        vbox.setPrefHeight(155);
        vbox.setPrefWidth(150);
        vbox.getStyleClass().add("back-gray");

        //FTTimer
        AnchorPane mFtimer = new AnchorPane();
        mFtimer.setOnMouseClicked(new ScreenEvents.OnMouseClicked(primaryStage, "FTimeEntry", UserName));
        mFtimer.setCursor(Cursor.HAND);
        mFtimer.setPrefHeight(35);
        mFtimer.setPrefWidth(150);
        mFtimer.getStyleClass().add("back-darkGray");
        ImageView ftTimerImg = new ImageView(resManager.getFTimeEntryImg());
        ftTimerImg.setFitWidth(45);
        ftTimerImg.setFitHeight(40);
        mFtimer.setTopAnchor(ftTimerImg, 5.0);
        mFtimer.setLeftAnchor(ftTimerImg, 5.0);
        Text fTimerText = new Text("FTTimer");
        fTimerText.getStyleClass().add("cardText");
        mFtimer.setTopAnchor(fTimerText, 13.0);
        mFtimer.setRightAnchor(fTimerText, 13.0);
        mFtimer.getChildren().addAll(ftTimerImg, fTimerText);

        //STTimer
        AnchorPane sFtimer = new AnchorPane();
        sFtimer.setOnMouseClicked(new ScreenEvents.OnMouseClicked(primaryStage, "STTimer", UserName));
        sFtimer.setCursor(Cursor.HAND);
        sFtimer.setPrefHeight(35);
        sFtimer.setPrefWidth(150);
        sFtimer.getStyleClass().add("back-darkGray");
        ImageView stTimerImg = new ImageView(resManager.getTimerImg());
        stTimerImg.setFitWidth(45);
        stTimerImg.setFitHeight(40);
        sFtimer.setTopAnchor(stTimerImg, 5.0);
        sFtimer.setLeftAnchor(stTimerImg, 5.0);
        Text sTimerText = new Text("STTimer");
        sTimerText.getStyleClass().add("cardText");
        sFtimer.setTopAnchor(sTimerText, 13.0);
        sFtimer.setRightAnchor(sTimerText, 13.0);
        sFtimer.getChildren().addAll(stTimerImg, sTimerText);

        //TimeEntries
        AnchorPane timetab = new AnchorPane();
        timetab.setCursor(Cursor.HAND);
        timetab.setPrefHeight(35);
        timetab.setPrefWidth(150);
        timetab.getStyleClass().add("back-darkGray");
        ImageView timetabImg = new ImageView(resManager.getTimeTableImg());
        timetabImg.setFitWidth(45);
        timetabImg.setFitHeight(40);
        timetab.setTopAnchor(timetabImg, 5.0);
        timetab.setLeftAnchor(timetabImg, 5.0);
        Text timetabText = new Text("TETable");
        timetabText.getStyleClass().add("cardText");
        timetab.setTopAnchor(timetabText, 13.0);
        timetab.setRightAnchor(timetabText, 13.0);
        timetab.getChildren().addAll(timetabImg, timetabText);

        vbox.getChildren().addAll(mFtimer, sFtimer, timetab);
        root.setLeft(vbox);
        //End Left menu pane Vbox

        //Table View

        TableView<getTimeEntries> table = new TableView<>();

        TableColumn<getTimeEntries, String> column1 = new TableColumn<>("TimeID");
        column1.setCellValueFactory(param -> param.getValue().TimeID);
        column1.setPrefWidth(60);

        TableColumn<getTimeEntries, String> column2 = new TableColumn<>("Date");
        column2.setCellValueFactory(param -> param.getValue().Date);
        column2.setPrefWidth(100);

        TableColumn<getTimeEntries, String> column3 = new TableColumn<>("TimeBegin");
        column3.setCellValueFactory(param -> param.getValue().TimeBegin);
        column3.setPrefWidth(70);

        TableColumn<getTimeEntries, String> column4 = new TableColumn<>("TimeEnd");
        column4.setCellValueFactory(param -> param.getValue().TimeEnd);
        column4.setPrefWidth(70);

        TableColumn<getTimeEntries, String> column5 = new TableColumn<>("ProjectName");
        column5.setCellValueFactory(param -> param.getValue().ProjectName);
        column5.setPrefWidth(130);

        TableColumn<getTimeEntries, String> column6 = new TableColumn<>("ProjectText");
        column6.setCellValueFactory(param -> param.getValue().ProjectText);
        column6.setPrefWidth(200);

        table.getColumns().addAll(column1, column2, column3, column4, column5, column6);
        table.getItems().addAll(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(oldSelection != null){
                oldSelection.setTimeID(TimeID);
                oldSelection.setDate(MainDate);
                oldSelection.setTimeBegin(TimeFrom);
                oldSelection.setTimeEnd(TimeTo);
                oldSelection.setProjectName(ProjectName);
                oldSelection.setProjectText(Description);
            }
            if (newSelection != null) {
                TimeID = newSelection.getTimeID();
                MainDate = newSelection.getDate();
                TimeFrom = newSelection.getTimeBegin();
                TimeTo = newSelection.getTimeEnd();
                ProjectName = newSelection.getProjectName();
                Description = newSelection.getProjectText();
            }
        });
        //Table View end

        //Right pane
        AnchorPane rightPane = new AnchorPane();
        rightPane.getStyleClass().add("back-lightDark");
        rightPane.setPrefWidth(150);

        ImageView searchImg = new ImageView(resManager.getDBImg());
        searchImg.setFitWidth(60);
        searchImg.setFitHeight(60);
        rightPane.setTopAnchor(searchImg, 10.0);
        rightPane.setLeftAnchor(searchImg, 45.0);

        Text searchTxt = new Text("Search TimeEntry:");
        searchTxt.getStyleClass().add("cardText");
        searchTxt.setTextAlignment(TextAlignment.CENTER);
        rightPane.setTopAnchor(searchTxt, 67.0);
        rightPane.setLeftAnchor(searchTxt, 15.0);

        final DatePicker dateFrom = new DatePicker();
        dateFrom.setPromptText("Date From");
        dateFrom.setPrefWidth(150);
        rightPane.setTopAnchor(dateFrom, 90.0);
        rightPane.setLeftAnchor(dateFrom, 0.0);

        final DatePicker dateTo = new DatePicker();
        dateTo.setPromptText("Date To");
        dateTo.setPrefWidth(150);
        rightPane.setTopAnchor(dateTo, 120.0);
        rightPane.setLeftAnchor(dateTo, 0.0);


        ObservableList<String> projects = FXCollections.observableArrayList("Allgemeines / Interne Umsetzungen", "AOK System", "ASSECO Solutions AG ", "Auxilium", "Auxilium Dark Energy", "Axonic", "BASF", "BPEX McAfee Consulting", "BPEX McAFee HZD", "BPEX Presales", "BPEX Virenblog", "BPEX Website", "CC-IT", "CC-IT Allgemein", "CC-IT GPK Support & Programmierung", "CC-IT HBM Support", "CC-IT IGHH Frontend Support", "CC-IT IGHH Verwaltungstool", "CC-IT interner Support", "CC-IT Mauerhoff DB", "CC-IT Motoallround", "CC-IT Porsche Inventur Support ", "CC-IT Salzburg AG Servicedesk Support", "CC-IT Swiss Steel", "ChannelPlace", "Coba Betrieb Allgemein", "Coba NSSR 30004001 Development Environment", "DPDS", "Dürr-mPKI", "DZBank CloudFactory", "DZBank Linux Software", "DZBank WFS Refresh", "EON CSA", "Finanzinformatik", "GKVI", "Haus der kleinen Forscher", "HBM Orga", "IBN", "McAfee allgem", "McAfee Daimler Security", "McAfee SOW Release", "McAfee SOW Release Block1-4", "McAfee threat intelligence", "McAfee_Access DB", "McAfee_Access SOW Processmanager Support", "McAfee_Finanz Informatik", "McAfee_LIDL_Nekarsulm", "MLP DL", "MLP MetaDir", "MLP Monitoring", "MLP Presales", "Müller Milch Group AG", "Pause / non billable stuff", "Reisezeiten", "Rheinenergie", "RoB IT-Services", "Saecon", "Schule", "Sicherheitshalber App", "Stadt Wuppertal", "Swissteel", "UmfrageTool", "Urlaub", "Voestalpine", "VW Betrieb", "VW Client Migrator", "VW Dashboard", "VW EMIL Betrieb", "VW MAN ", "VW SCCM");
        ComboBox searchProject = new ComboBox(projects);
        searchProject.getStyleClass().add("fttimerPr");
        searchProject.setPromptText("Projects");
        searchProject.setPrefWidth(150);
        rightPane.setTopAnchor(searchProject, 150.0);
        rightPane.setLeftAnchor(searchProject, 0.0);

        Button searchBtn = new Button("Search");
        searchBtn.setPrefWidth(150);
        searchBtn.getStyleClass().add("addBtn");
        rightPane.setTopAnchor(searchBtn, 190.0);
        rightPane.setLeftAnchor(searchBtn, 0.0);

        Button editBtn = new Button("Edit");
        editBtn.setOnMouseClicked(event -> {
            if(CLOSED){
            	if(!TimeID.equals("")){
            		showEditDialog();
                    CLOSED = false;
            	}else{
            		showAlert("Select pleas row...");
            	}
            }
        });
        editBtn.setPrefWidth(150);
        editBtn.getStyleClass().add("submitBtn");
        rightPane.setTopAnchor(editBtn, 226.0);
        rightPane.setLeftAnchor(editBtn, 0.0);

        Button deleteBtn = new Button("Delete");
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Attention!\nTime Entry will be delete...");
        deleteBtn.setTooltip(tooltip);
        deleteBtn.setPrefWidth(150);
        deleteBtn.getStyleClass().add("resetBtn");
        rightPane.setTopAnchor(deleteBtn, 262.0);
        rightPane.setLeftAnchor(deleteBtn, 0.0);
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setPrefWidth(150);
        refreshBtn.getStyleClass().add("addBtn");
        rightPane.setTopAnchor(refreshBtn, 298.0);
        rightPane.setLeftAnchor(refreshBtn, 0.0);

        rightPane.getChildren().addAll(searchImg, searchTxt, dateFrom, dateTo, searchProject, searchBtn, editBtn, deleteBtn, refreshBtn);
        //Right pane end


        //footer
        AnchorPane footer = new AnchorPane();
        footer.setPrefHeight(70);
        footer.getStyleClass().add("footer");
        AnchorPane delimiter = new AnchorPane();
        delimiter.setPrefHeight(20);
        delimiter.getStyleClass().add("delimiter");
        delimiter.setPrefWidth(900);
        ImageView logImg = new ImageView(resManager.getLogImg());
        logImg.setFitWidth(20);
        logImg.setFitHeight(20);
        delimiter.setTopAnchor(logImg,0.0);
        delimiter.setLeftAnchor(logImg,5.0);

        Text logInf = new Text("Log Information:");
        logInf.getStyleClass().add("logText");
        delimiter.setTopAnchor(logInf,3.0);
        delimiter.setLeftAnchor(logInf,30.0);
        delimiter.getChildren().addAll(logImg, logInf);
        footer.setTopAnchor(delimiter, 0.0);
        footer.setLeftAnchor(delimiter, 0.0);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(45);
        scrollPane.setPrefWidth(900);
        scrollPane.getStyleClass().add("footer");
        Text LogTxt = new Text(STATUS);
        LogTxt.getStyleClass().add("logText");

        scrollPane.setContent(LogTxt);

        footer.setTopAnchor(scrollPane, 23.0);
        footer.setLeftAnchor(scrollPane, 0.0);
        footer.getChildren().addAll(delimiter, scrollPane);
        //footer end

        //button handle
        searchBtn.setOnAction(event -> {
            PROJECTNAME = (String) searchProject.getValue();
            LocalDate dateF = dateFrom.getValue();
            DATEFROM = dateF.toString();
            LocalDate dateT = dateTo.getValue();
            DATETO = dateT.toString();

            List<getTimeEntries> newdata = createData(DATEFROM,DATETO,PROJECTNAME);
            LogTxt.setText(STATUS);
            table.getItems().clear();
            table.getItems().addAll(newdata);
        });
        //button handle end
        //Action functions
        deleteBtn.setOnMouseClicked(event -> {
        	if(!TimeID.equals("")){
        		DataController dataController = new DataController();
                try {
                    dataController.deleteTEnry(TimeID);
                    LogTxt.setText("Time Entry -> TimeID: "+TimeID+" was deleted!");
                    List<getTimeEntries> newdata = createData(null,null,null);
                    table.getItems().clear();
                    table.getItems().addAll(newdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        	}else{
        		showAlert("Select pleas row...");
        	}
            
        });
        
        refreshBtn.setOnMouseClicked(event -> {
            List<getTimeEntries> newdata = createData(null,null,null);
			table.getItems().clear();
			table.getItems().addAll(newdata);  
        });
        //Action functions end


        root.setTop(header);
        root.setCenter(table);
        root.setRight(rightPane);
        root.setBottom(footer);

        primaryStage.setTitle("Login FTTimer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private void showEditDialog(){
        Stage dialog = new Stage();
        dialog.setResizable(false);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("grid");
        addDragListeners(root);
        Scene scene = new Scene(root, 350, 360, Color.BLACK);
        scene.getStylesheets().add("file:resources/Style.css");

        //header
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(30);
        header.getStyleClass().add("back-darkGray");

        ImageView icon = new ImageView(resManager.getTimerImg());
        icon.setFitWidth(35);
        icon.setFitHeight(30);
        header.setTopAnchor(icon, 1.0);
        header.setLeftAnchor(icon, 10.0);

        Text appName = new Text();
        appName.setText("FTTimer");
        appName.getStyleClass().add("cardText");
        header.setTopAnchor(appName, 10.0);
        header.setLeftAnchor(appName, 45.0);

        Button closeBtn = new Button("X");
        closeBtn.setOnAction(event -> {
            CLOSED = true;
            dialog.close();
        });
        closeBtn.getStyleClass().add("closeBtn");
        header.setTopAnchor(closeBtn, 1.0);
        header.setRightAnchor(closeBtn, 5.0);

        Button hideBtn = new Button("_");
        hideBtn.setOnAction(e -> {
            ((Stage)((Button)e.getSource()).getScene().getWindow()).setIconified(true);
        });
        hideBtn.getStyleClass().add("closeBtn");
        header.setTopAnchor(hideBtn, 1.0);
        header.setRightAnchor(hideBtn, 35.0);

        header.getChildren().addAll(icon, appName, hideBtn, closeBtn);
        //header end

        //body
        AnchorPane body = new AnchorPane();
        body.setPrefHeight(500);
        body.getStyleClass().add("back-lightDark");

        Text headtext = new Text("Edit TEntry:");
        headtext.getStyleClass().add("loginText");
        headtext.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(headtext, 10.0);
        body.setLeftAnchor(headtext, 115.0);

        //date
        Text dateText = new Text("Edit date:");
        dateText.getStyleClass().add("editTRText");
        dateText.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(dateText, 60.0);
        body.setLeftAnchor(dateText, 20.0);


        final DatePicker dp = new DatePicker();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.GERMANY);
        dp.setValue(LocalDate.parse(MainDate.replaceAll(" ", ""), formatter));
        dp.setPrefWidth(230);
        body.setTopAnchor(dp, 55.0);
        body.setRightAnchor(dp, 20.0);
        //date end

        //time
        Text timeText = new Text("Edit time:");
        timeText.getStyleClass().add("editTRText");
        timeText.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(timeText, 100.0);
        body.setLeftAnchor(timeText, 20.0);

        ObservableList<String> options = FXCollections.observableArrayList("07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
                "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "24:00"
        );

        ComboBox tFrom = new ComboBox(options);
        tFrom.setValue(TimeFrom);
        tFrom.setPrefWidth(110);
        body.setTopAnchor(tFrom, 90.0);
        body.setRightAnchor(tFrom, 140.0);

        ComboBox tTo = new ComboBox(options);
        tTo.setPrefWidth(110);
        tTo.setValue(TimeTo);
        body.setTopAnchor(tTo, 90.0);
        body.setRightAnchor(tTo, 20.0);
        //time end

        //project

        Text projectText = new Text("Project:");
        projectText.getStyleClass().add("editTRText");
        projectText.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(projectText, 135.0);
        body.setLeftAnchor(projectText, 20.0);

        ObservableList<String> projects = FXCollections.observableArrayList("Allgemeines / Interne Umsetzungen", "AOK System", "ASSECO Solutions AG ", "Auxilium", "Auxilium Dark Energy", "Axonic", "BASF", "BPEX McAfee Consulting", "BPEX McAFee HZD", "BPEX Presales", "BPEX Virenblog", "BPEX Website", "CC-IT", "CC-IT Allgemein", "CC-IT GPK Support & Programmierung", "CC-IT HBM Support", "CC-IT IGHH Frontend Support", "CC-IT IGHH Verwaltungstool", "CC-IT interner Support", "CC-IT Mauerhoff DB", "CC-IT Motoallround", "CC-IT Porsche Inventur Support ", "CC-IT Salzburg AG Servicedesk Support", "CC-IT Swiss Steel", "ChannelPlace", "Coba Betrieb Allgemein", "Coba NSSR 30004001 Development Environment", "DPDS", "Dürr-mPKI", "DZBank CloudFactory", "DZBank Linux Software", "DZBank WFS Refresh", "EON CSA", "Finanzinformatik", "GKVI", "Haus der kleinen Forscher", "HBM Orga", "IBN", "McAfee allgem", "McAfee Daimler Security", "McAfee SOW Release", "McAfee SOW Release Block1-4", "McAfee threat intelligence", "McAfee_Access DB", "McAfee_Access SOW Processmanager Support", "McAfee_Finanz Informatik", "McAfee_LIDL_Nekarsulm", "MLP DL", "MLP MetaDir", "MLP Monitoring", "MLP Presales", "Müller Milch Group AG", "Pause / non billable stuff", "Reisezeiten", "Rheinenergie", "RoB IT-Services", "Saecon", "Schule", "Sicherheitshalber App", "Stadt Wuppertal", "Swissteel", "UmfrageTool", "Urlaub", "Voestalpine", "VW Betrieb", "VW Client Migrator", "VW Dashboard", "VW EMIL Betrieb", "VW MAN ", "VW SCCM");
        ComboBox project = new ComboBox(projects);
        project.setValue(ProjectName);
        project.getStyleClass().add("fttimerPr");
        project.setPromptText("Projects");
        project.setPrefWidth(230);
        body.setTopAnchor(project, 130.0);
        body.setRightAnchor(project, 20.0);
        //project end

        //Description
        Text descText = new Text("Description:");
        descText.getStyleClass().add("editTRText");
        descText.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(descText, 175.0);
        body.setLeftAnchor(descText, 20.0);

        TextArea descr = new TextArea();
        descr.getStyleClass().add("textArea");
        descr.setText(Description);
        descr.setPrefWidth(230);
        descr.setPrefHeight(100);
        body.setTopAnchor(descr, 170.0);
        body.setRightAnchor(descr, 20.0);
        //Description end


        //body end

        //footer
        Button cancel = new Button("Cancel");
        cancel.setOnMouseClicked(event -> {
            CLOSED = true;
            dialog.close();
        });
        cancel.getStyleClass().add("resetBtn");
        cancel.setCursor(Cursor.HAND);
        cancel.setPrefWidth(80);
        cancel.setPrefHeight(20);
        body.setBottomAnchor(cancel, 10.0);
        body.setRightAnchor(cancel, 20.0);

        Button update = new Button("Update");
        update.setOnMouseClicked(event -> {
            LocalDate mainNewDate = dp.getValue();
            if(!mainNewDate.toString().equals("") && !tFrom.getValue().toString().equals("") && !tTo.getValue().toString().equals("") && !project.getValue().equals("") && !descr.getText().equals("")){

                String timeFrom = mainNewDate.toString()+" "+tFrom.getValue().toString();
                String timeTo = mainNewDate.toString()+" "+tTo.getValue().toString();
                String projName = (String) project.getValue();
                String desc = descr.getText();

                DataController dataController = new DataController();
                try {
                    dataController.updateTEntry(TimeID, timeFrom, timeTo, desc, projName);

                    MainDate = mainNewDate.toString();
                    TimeFrom = tFrom.getValue().toString();
                    TimeTo = tTo.getValue().toString();
                    ProjectName = projName;
                    Description = desc;
                    
                    CLOSED = true;
                    dialog.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                showAlert("Check Time Entry...");
            }

        });
        update.getStyleClass().add("submitBtn");
        update.setCursor(Cursor.HAND);
        update.setPrefWidth(80);
        update.setPrefHeight(20);
        body.setBottomAnchor(update, 10.0);
        body.setRightAnchor(update, 110.0);

        //footer end
        body.getChildren().addAll(headtext, dateText, dp, timeText, tFrom, tTo, projectText, project, descr, descText, cancel, update);

        root.setTop(header);
        root.setCenter(body);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setOpacity(0.9);
        dialog.setTitle("EditTE");
        dialog.getIcons().add(new Image("file:resources/images/AddEntry.png"));
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAlert(String content){
        DialogView alert = new DialogView(content);
        Stage toAlert = new Stage();
        try {
            alert.start(toAlert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addDragListeners(final Node n){
        n.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if(me.getButton()!= MouseButton.MIDDLE)
                {
                    initialX = me.getSceneX();
                    initialY = me.getSceneY();
                }
                else
                {
                    n.getScene().getWindow().centerOnScreen();
                    initialX = n.getScene().getWindow().getX();
                    initialY = n.getScene().getWindow().getY();
                }

            }
        });

        n.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if(me.getButton()!=MouseButton.MIDDLE)
                {
                    n.getScene().getWindow().setX( me.getScreenX() - initialX );
                    n.getScene().getWindow().setY( me.getScreenY() - initialY);
                }
            }
        });
    }
}