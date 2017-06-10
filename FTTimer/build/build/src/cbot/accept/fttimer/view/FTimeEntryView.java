package cbot.accept.fttimer.view;

import cbot.accept.fttimer.MainApp;

import cbot.accept.fttimer.CsvParser;
import cbot.accept.fttimer.DataController;
import cbot.accept.fttimer.ScreenEvents;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import sun.util.calendar.BaseCalendar;
import sun.util.calendar.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;


public class FTimeEntryView extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    //First data to initialize
    public double initialX, initialY;
    public String UserName;
    public Stage st;
    public String LogText;

    //public BaseCalendar.Date mainDate;
    public FTimeEntryView(String UserName){
        this.UserName = UserName;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.st = primaryStage;
        BorderPane root = new BorderPane();
        addDragListeners(root);
        Scene scene = new Scene(root, 800, 400);
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
        timetab.setOnMouseClicked(new ScreenEvents.OnMouseClicked(primaryStage, "TimeTable", UserName));
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

        //Center grid Pane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getStyleClass().add("back-lightDark");

        //Top Grid Info and button back
        //back button
        AnchorPane topGrid = new AnchorPane();
        topGrid.getStyleClass().add("addBtn");
        topGrid.setOnMouseClicked(event -> {
            MainApp toLogin = new MainApp();
            Stage newStage = new Stage();
            try {
                toLogin.start(newStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.close();
        });

        ImageView logOutImg = new ImageView(resManager.getLogOutImg());
        logOutImg.setFitWidth(20);
        logOutImg.setFitHeight(20);
        topGrid.setTopAnchor(logOutImg, 5.0);
        topGrid.setRightAnchor(logOutImg, 10.0);

        Text back = new Text("LogOut");
        back.getStyleClass().add("reportText");
        topGrid.setTopAnchor(back, 10.0);
        topGrid.setLeftAnchor(back, 10.0);

        topGrid.getChildren().addAll(logOutImg, back);
        gridPane.setColumnIndex(topGrid, 4);
        gridPane.setRowIndex(topGrid, 0);
        //End back button
        //info
        AnchorPane infoPane = new AnchorPane();
        infoPane.getStyleClass().add("addBtn");
        ImageView infoInfoImg = new ImageView(resManager.getInfoInfoImg());
        infoInfoImg.setFitWidth(30);
        infoInfoImg.setFitHeight(30);
        infoPane.setTopAnchor(infoInfoImg, 0.0);
        infoPane.setLeftAnchor(infoInfoImg, 10.0);

        Text infInfText = new Text("You can use button: 'SubmitForm' to submit all Entries!");
        infInfText.getStyleClass().add("reportText");
        topGrid.setTopAnchor(infInfText, 10.0);
        topGrid.setLeftAnchor(infInfText, 40.0);

        infoPane.getChildren().addAll(infoInfoImg, infInfText);
        gridPane.setColumnSpan(infoPane, 4);
        gridPane.setColumnIndex(infoPane, 0);
        gridPane.setRowIndex(infoPane, 0);
        gridPane.getChildren().addAll(infoPane, topGrid);
        //End Top Grid Info and button back

        //Datetime
        final DatePicker dp = new DatePicker();
        dp.setPromptText("dd/mm/YYYY");
        gridPane.add(dp, 0,1);

        Button reset = new Button("ResetForm");
        reset.setPrefWidth(190);
        reset.getStyleClass().add("resetBtn");
        gridPane.add(reset, 0,2);

        Button submit = new Button("SubmitForm");
        submit.setPrefWidth(190);
        submit.getStyleClass().add("submitBtn");
        gridPane.add(submit, 0,4);

        //Filechooser and browse button
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv", "*.xls"),
                new FileChooser.ExtensionFilter("Text Files", "*txt"),
                new FileChooser.ExtensionFilter("Log Files", "*.log"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Button browse = new Button("Upload");
        browse.setPrefWidth(190);
        browse.setOnMouseEntered(event -> {
            long countFiles = 0;
            try {
                countFiles = getCountFiles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Tooltip tooltip = new Tooltip();
            tooltip.setText("You have "+ countFiles+" Files to Upload");
            browse.setTooltip(tooltip);
        });
        browse.getStyleClass().add("addBtn");
        gridPane.add(browse, 0,3);
        //Filechooser and browse button end

        //Options
        ObservableList<String> options = FXCollections.observableArrayList("07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00",
                "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
                "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "24:00"
                );
        //TimeFrom
        ComboBox tFrom = new ComboBox(options);
        tFrom.setPromptText("From");
        tFrom.setPrefWidth(120);
        gridPane.add(tFrom, 1, 1);

        ComboBox tFrom2 = new ComboBox(options);
        tFrom2.setPrefWidth(120);
        tFrom2.setPromptText("From");
        gridPane.add(tFrom2, 3, 1);
        //Time TO
        ComboBox tTo = new ComboBox(options);
        tTo.setPrefWidth(120);
        tTo.setPromptText("To");
        gridPane.add(tTo, 2, 1);

        ComboBox tTo2 = new ComboBox(options);
        tTo2.setPrefWidth(120);
        tTo2.setPromptText("To");
        gridPane.add(tTo2, 4, 1);

        ObservableList<String> projects = FXCollections.observableArrayList("Allgemeines / Interne Umsetzungen", "AOK System", "ASSECO Solutions AG ", "Auxilium", "Auxilium Dark Energy", "Axonic", "BASF", "BPEX McAfee Consulting", "BPEX McAFee HZD", "BPEX Presales", "BPEX Virenblog", "BPEX Website", "CC-IT", "CC-IT Allgemein", "CC-IT GPK Support & Programmierung", "CC-IT HBM Support", "CC-IT IGHH Frontend Support", "CC-IT IGHH Verwaltungstool", "CC-IT interner Support", "CC-IT Mauerhoff DB", "CC-IT Motoallround", "CC-IT Porsche Inventur Support ", "CC-IT Salzburg AG Servicedesk Support", "CC-IT Swiss Steel", "ChannelPlace", "Coba Betrieb Allgemein", "Coba NSSR 30004001 Development Environment", "DPDS", "Dürr-mPKI", "DZBank CloudFactory", "DZBank Linux Software", "DZBank WFS Refresh", "EON CSA", "Finanzinformatik", "GKVI", "Haus der kleinen Forscher", "HBM Orga", "IBN", "McAfee allgem", "McAfee Daimler Security", "McAfee SOW Release", "McAfee SOW Release Block1-4", "McAfee threat intelligence", "McAfee_Access DB", "McAfee_Access SOW Processmanager Support", "McAfee_Finanz Informatik", "McAfee_LIDL_Nekarsulm", "MLP DL", "MLP MetaDir", "MLP Monitoring", "MLP Presales", "Müller Milch Group AG", "Pause / non billable stuff", "Reisezeiten", "Rheinenergie", "RoB IT-Services", "Saecon", "Schule", "Sicherheitshalber App", "Stadt Wuppertal", "Swissteel", "UmfrageTool", "Urlaub", "Voestalpine", "VW Betrieb", "VW Client Migrator", "VW Dashboard", "VW EMIL Betrieb", "VW MAN ", "VW SCCM");
        ComboBox project = new ComboBox(projects);
        project.getStyleClass().add("fttimerPr");
        project.setPromptText("Projects");
        project.setPrefWidth(300);
        gridPane.setColumnSpan(project, 2);
        gridPane.setColumnIndex(project, 1);
        gridPane.setRowIndex(project, 2);

        ComboBox project2 = new ComboBox(projects);
        project2.getStyleClass().add("fttimerPr");
        project2.setPromptText("Projects");
        gridPane.setColumnSpan(project2, 2);
        gridPane.setColumnIndex(project2, 3);
        gridPane.setRowIndex(project2, 2);

        gridPane.getChildren().addAll(project, project2);

        TextArea descr = new TextArea();
        descr.getStyleClass().add("textArea");
        descr.setPromptText("Description");
        descr.setPrefWidth(300);
        descr.setPrefHeight(100);
        gridPane.setColumnSpan(descr, 2);
        gridPane.setColumnIndex(descr, 1);
        gridPane.setRowIndex(descr, 3);

        TextArea descr2 = new TextArea();
        descr2.getStyleClass().add("textArea");
        descr2.setPromptText("Description");
        descr2.setPrefWidth(300);
        descr2.setPrefHeight(100);
        gridPane.setColumnSpan(descr2, 2);
        gridPane.setColumnIndex(descr2, 3);
        gridPane.setRowIndex(descr2, 3);

        gridPane.getChildren().addAll(descr, descr2);

        Button btnAdd = new Button("Add");
        btnAdd.setPrefWidth(300);
        btnAdd.getStyleClass().add("addBtn");
        gridPane.setColumnSpan(btnAdd, 2);
        gridPane.setColumnIndex(btnAdd, 1);
        gridPane.setRowIndex(btnAdd, 4);

        Button btnAdd2 = new Button("Add");
        btnAdd2.setPrefWidth(300);
        btnAdd2.getStyleClass().add("addBtn");
        gridPane.setColumnSpan(btnAdd2, 2);
        gridPane.setColumnIndex(btnAdd2, 3);
        gridPane.setRowIndex(btnAdd2, 4);

        gridPane.getChildren().addAll(btnAdd, btnAdd2);


        root.setCenter(gridPane);

        //EndCenter grid Pane

        AnchorPane footer = new AnchorPane();
        footer.setPrefHeight(70);
        footer.getStyleClass().add("footer");
        AnchorPane delimiter = new AnchorPane();
        delimiter.setPrefHeight(20);
        delimiter.getStyleClass().add("delimiter");
        delimiter.setPrefWidth(800);
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
        //vad123@V
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(45);
        scrollPane.setPrefWidth(800);
        scrollPane.getStyleClass().add("footer");
        Text LogTxt = new Text("Here you will find Log information...");
        LogTxt.getStyleClass().add("logText");

        scrollPane.setContent(LogTxt);

        footer.setTopAnchor(scrollPane, 23.0);
        footer.setLeftAnchor(scrollPane, 0.0);
        footer.getChildren().addAll(delimiter, scrollPane);
        root.setBottom(footer);

        /*
        Bindings part
         */
        btnAdd.setOnAction(event -> {
            DataController dc = new DataController();
            if(!tFrom.getValue().toString().equals("") && !tTo.getValue().toString().equals("") && !project.getValue().equals("") && !descr.getText().equals("")){
                LocalDate mainDate = dp.getValue();
                String timeFrom = tFrom.getValue().toString();
                String timeTo = tTo.getValue().toString();
                String projName = (String) project.getValue();
                String desc = descr.getText();

                try {
                    LogText = dc.saveTimeEntries(mainDate.toString(), timeFrom, timeTo, desc, projName, 1);
                    LogTxt.textProperty().set(null);
                    LogTxt.textProperty().set(LogText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                LogTxt.textProperty().set(null);
                LogTxt.textProperty().set("\nCheck Your entries");
            }

        });
        btnAdd2.setOnAction(event -> {
            if(!tFrom2.getValue().toString().equals("") && !tTo2.getValue().toString().equals("") && !project2.getValue().equals("") && !descr2.getText().equals("")){
                DataController dc = new DataController();
                LocalDate mainDate = dp.getValue();
                String timeFrom = tFrom2.getValue().toString();
                String timeTo = tTo2.getValue().toString();
                String projName = (String) project2.getValue();
                String desc = descr2.getText();
                try {
                    LogText = dc.saveTimeEntries(mainDate.toString(), timeFrom, timeTo, desc, projName, 1);
                    LogTxt.textProperty().set("");
                    LogTxt.textProperty().set(LogText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                LogTxt.textProperty().set(null);
                LogTxt.textProperty().set("\nCheck Your entries");
            }
        });

        submit.setOnAction(event -> {
            DataController dc = new DataController();
            if(!tFrom.getValue().toString().equals("") && !tTo.getValue().toString().equals("") && !project.getValue().equals("") && !descr.getText().equals("")){
                if(!tFrom2.getValue().toString().equals("") && !tTo2.getValue().toString().equals("") && !project2.getValue().equals("") && !descr2.getText().equals("")){
                    LocalDate mainDate = dp.getValue();
                    String timeFrom = tFrom.getValue().toString();
                    String timeTo = tTo.getValue().toString();
                    String projName = (String) project.getValue();
                    String desc = descr.getText();

                    String timeFrom2 = tFrom2.getValue().toString();
                    String timeTo2 = tTo2.getValue().toString();
                    String projName2 = (String) project2.getValue();
                    String desc2 = descr2.getText();
                    try {
                        String LogText2;
                        LogText = dc.saveTimeEntries(mainDate.toString(), timeFrom, timeTo, desc, projName, 1);
                        LogText2 = dc.saveTimeEntries(mainDate.toString(), timeFrom2, timeTo2, desc2, projName2, 1);
                        LogTxt.textProperty().set(null);
                        LogTxt.textProperty().set("\n"+LogText);
                        LogTxt.textProperty().set("\n"+LogText2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    LogTxt.textProperty().set(null);
                    LogTxt.textProperty().set("\nCheck Your entries");
                }
            }else{
                LogTxt.textProperty().set(null);
                LogTxt.textProperty().set("\nCheck Your entries");
            }
        });

        browse.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if(file != null){
                LogTxt.textProperty().set(null);
                String path = file.getAbsolutePath();
                LogTxt.textProperty().set("File: "+path);
                try{
                    CsvParser sendFile = new CsvParser();
                    String stat = sendFile.parseTimeEntries(path);
                    LogTxt.textProperty().set(stat);
                }catch (IOException e){

                }
            }

        });

        reset.setOnAction(event -> {
            dp.valueProperty().set(null);
            tFrom.valueProperty().set(null);
            tFrom2.valueProperty().set(null);
            tTo.valueProperty().set(null);
            tTo2.valueProperty().set(null);
            project.valueProperty().set(null);
            project2.valueProperty().set(null);
            descr.textProperty().set(null);
            descr2.textProperty().set(null);
        });
        //End
        primaryStage.setTitle("FTTimer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public long getCountFiles() throws IOException {
        long count = Files.list(Paths.get("TimeEntries")).count();
        return count;
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
