package cbot.accept.fttimer.view;

import cbot.accept.fttimer.DataController;

import cbot.accept.fttimer.model.resManager;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vadim on 5/9/2017.
 */
public class STimerView extends Application {
    public String UserName;
    public double initialX, initialY;

    public STimerView(){}

    public STimerView(String usrN){
        this.UserName = usrN;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        AnchorPane root = new AnchorPane();
        root.getStyleClass().add("grid");
        addDragListeners(root);
        Scene scene = new Scene(root, 300, 200);
        scene.getStylesheets().add("file:resources/Style.css");
        //helpers fields
        Text startTime = new Text();
        startTime.setVisible(false);
        Text endTime = new Text();
        endTime.setVisible(false);
        //Top
        AnchorPane top = new AnchorPane();
        top.setPrefHeight(105);
        top.setPrefWidth(270);
        top.getStyleClass().add("back-darkGray");

        Text digitalClock = new Text();
        digitalClock.setId("digitalClock");

        final Timeline digitalTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                Calendar calendar            = GregorianCalendar.getInstance();
                                String hourString   = pad(2, '0', calendar.get(Calendar.HOUR)   == 0 ? "12" : calendar.get(Calendar.HOUR) + "");
                                String minuteString = pad(2, '0', calendar.get(Calendar.MINUTE) + "");
                                String secondString = pad(2, '0', calendar.get(Calendar.SECOND) + "");
                                String ampmString   = calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                                digitalClock.setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );

        // time never ends.
        digitalTime.setCycleCount(Animation.INDEFINITE);
        digitalTime.play();

        top.setTopAnchor(digitalClock, 10.0);
        top.setLeftAnchor(digitalClock, 30.0);


        //digital Timer
        Text stimerLable = new Text("STTimer:");
        stimerLable.getStyleClass().add("digitalTimerLable");
        top.setTopAnchor(stimerLable, 65.0);
        top.setLeftAnchor(stimerLable, 10.0);

        //Status
        ImageView recording = new ImageView(resManager.getRecStatus());
        recording.setVisible(false);
        recording.setFitWidth(20);
        recording.setFitHeight(20);
        top.setTopAnchor(recording, 60.0);
        top.setLeftAnchor(recording, 210.0);

        ImageView errorStatus = new ImageView(resManager.getErrorStatus());
        errorStatus.setCursor(Cursor.HAND);
        errorStatus.setOnMouseClicked(event -> {
            showAlert("Can't connect to server\nTE saved into Your Computer");
        });
        errorStatus.setVisible(false);
        errorStatus.setFitWidth(15);
        errorStatus.setFitHeight(15);
        top.setTopAnchor(errorStatus, 63.0);
        top.setLeftAnchor(errorStatus, 235.0);

        ImageView successStatus = new ImageView(resManager.getSuccessStatus());
        successStatus.setCursor(Cursor.HAND);
        successStatus.setOnMouseClicked(event -> {
            showAlert("TimeEntry saved...");
        });
        successStatus.setVisible(false);
        successStatus.setFitWidth(15);
        successStatus.setFitHeight(15);
        top.setTopAnchor(successStatus, 63.0);
        top.setLeftAnchor(successStatus, 235.0);
        //Status end
        Text digitalTimer = new Text();
        StringProperty time = new SimpleStringProperty();
        digitalTimer.textProperty().bind(time);
        digitalTimer.getStyleClass().add("digitalTimer");
        BooleanProperty running = new SimpleBooleanProperty();
        AnimationTimer timer = new AnimationTimer() {

            private long startTime ;

            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                running.set(true);
                super.start();
                recording.setVisible(true);
                errorStatus.setVisible(false);
                successStatus.setVisible(false);
            }

            @Override
            public void stop() {
                running.set(false);
                super.stop();
            }

            @Override
            public void handle(long timestamp) {
                long now = System.currentTimeMillis();
                String timerTime = convertSecondsToHMmSs((now - startTime)/1000);
                time.set(timerTime);
            }
        };

        top.setTopAnchor(digitalTimer, 60.0);
        top.setLeftAnchor(digitalTimer, 90.0);

        top.getChildren().addAll(stimerLable, digitalClock, digitalTimer, recording, errorStatus, successStatus);

        //top pane
        root.setTopAnchor(top, 0.0);
        root.setLeftAnchor(top, 0.0);

        //Projects
        ObservableList<String> projects = FXCollections.observableArrayList("Allgemeines / Interne Umsetzungen", "AOK System", "ASSECO Solutions AG ", "Auxilium", "Auxilium Dark Energy", "Axonic", "BASF", "BPEX McAfee Consulting", "BPEX McAFee HZD", "BPEX Presales", "BPEX Virenblog", "BPEX Website", "CC-IT", "CC-IT Allgemein", "CC-IT GPK Support & Programmierung", "CC-IT HBM Support", "CC-IT IGHH Frontend Support", "CC-IT IGHH Verwaltungstool", "CC-IT interner Support", "CC-IT Mauerhoff DB", "CC-IT Motoallround", "CC-IT Porsche Inventur Support ", "CC-IT Salzburg AG Servicedesk Support", "CC-IT Swiss Steel", "ChannelPlace", "Coba Betrieb Allgemein", "Coba NSSR 30004001 Development Environment", "DPDS", "Dürr-mPKI", "DZBank CloudFactory", "DZBank Linux Software", "DZBank WFS Refresh", "EON CSA", "Finanzinformatik", "GKVI", "Haus der kleinen Forscher", "HBM Orga", "IBN", "McAfee allgem", "McAfee Daimler Security", "McAfee SOW Release", "McAfee SOW Release Block1-4", "McAfee threat intelligence", "McAfee_Access DB", "McAfee_Access SOW Processmanager Support", "McAfee_Finanz Informatik", "McAfee_LIDL_Nekarsulm", "MLP DL", "MLP MetaDir", "MLP Monitoring", "MLP Presales", "Müller Milch Group AG", "Pause / non billable stuff", "Reisezeiten", "Rheinenergie", "RoB IT-Services", "Saecon", "Schule", "Sicherheitshalber App", "Stadt Wuppertal", "Swissteel", "UmfrageTool", "Urlaub", "Voestalpine", "VW Betrieb", "VW Client Migrator", "VW Dashboard", "VW EMIL Betrieb", "VW MAN ", "VW SCCM");
        ComboBox project = new ComboBox(projects);
        project.setId("stimerProjects");
        project.setPromptText("Projects");
        project.setPrefHeight(20);
        project.setPrefWidth(270);

        root.setTopAnchor(project, 90.0);
        root.setLeftAnchor(project, 0.0);

        //Input text Wm2468wm!
        TextArea textEntry = new TextArea();
        textEntry.getStyleClass().add("textArea");
        textEntry.setPromptText("Description");
        textEntry.setPrefHeight(85);
        textEntry.setPrefWidth(270);
        root.setTopAnchor(textEntry, 115.0);
        root.setLeftAnchor(textEntry, 0.0);

        //Right pane
        AnchorPane right = new AnchorPane();
        right.getStyleClass().add("back-lightDark");
        right.setPrefWidth(30);
        right.setPrefHeight(200);

        Button closeBtn = new Button();
        closeBtn.getStyleClass().add("startBtn");
        closeBtn.setOnAction(event -> {
            primaryStage.close();
        });
        ImageView closeBtnImg = new ImageView(resManager.getCloseBtnImg());
        closeBtnImg.setFitHeight(30);
        closeBtnImg.setFitWidth(30);
        closeBtn.setGraphic(closeBtnImg);
        closeBtn.setPadding(new Insets(0.0, 0, 0.0, 0.0));
        right.setTopAnchor(closeBtn, 0.0);
        right.setRightAnchor(closeBtn, 0.0);

        Button startBtn = new Button();
        startBtn.getStyleClass().add("startBtn");
        ImageView startBtnImg = new ImageView(resManager.getStartBtnImg());
        startBtnImg.setFitHeight(30);
        startBtnImg.setFitWidth(30);
        startBtn.setGraphic(startBtnImg);
        startBtn.setPadding(new Insets(0.0, -1, 0.0, 0.0));
        startBtn.setOnAction(event -> {
            timer.start();
            Date date = new Date();
            String currentTime = new SimpleDateFormat("HH:mm").format(date);
            startTime.setText(currentTime);
        });
        right.setTopAnchor(startBtn, 50.0);
        right.setRightAnchor(startBtn, 0.0);

        Button stopBtn = new Button();
        stopBtn.getStyleClass().add("startBtn");
        ImageView stopBtnImg = new ImageView(resManager.getStopBtnImg());
        stopBtnImg.setFitHeight(30);
        stopBtnImg.setFitWidth(30);
        stopBtn.setGraphic(stopBtnImg);
        stopBtn.setPadding(new Insets(0.0, -1, 0.0, 0.0));
        stopBtn.setOnAction(event -> {
            timer.stop();
            Date date = new Date();
            String currentTime = new SimpleDateFormat("HH:mm").format(date);
            endTime.setText(currentTime);
        });
        right.setTopAnchor(stopBtn, 85.0);
        right.setRightAnchor(stopBtn, 0.0);

        Button resetBtn = new Button();
        resetBtn.getStyleClass().add("startBtn");
        ImageView resetBtnImg = new ImageView(resManager.getResetBtnImg());
        resetBtnImg.setFitHeight(30);
        resetBtnImg.setFitWidth(30);
        resetBtn.setGraphic(resetBtnImg);
        resetBtn.setPadding(new Insets(0.0, -1, 0.0, 0.0));
        resetBtn.setOnAction(event -> {
            time.setValue(null);
            project.valueProperty().set(null);
            textEntry.textProperty().set(null);
            startTime.textProperty().set(null);
            endTime.textProperty().set(null);
        });
        right.setTopAnchor(resetBtn, 120.0);
        right.setRightAnchor(resetBtn, 0.0);

        Button plusBtn = new Button();
        plusBtn.getStyleClass().add("startBtn");
        ImageView plusBtnImg = new ImageView(resManager.getPlusBtnImg());
        plusBtnImg.setFitHeight(30);
        plusBtnImg.setFitWidth(30);
        plusBtn.setGraphic(plusBtnImg);
        plusBtn.setPadding(new Insets(0.0, 0.0, 0.0, 0.0));
        right.setBottomAnchor(plusBtn, 0.0);
        right.setRightAnchor(plusBtn, 0.0);
        plusBtn.setOnAction(event -> {
            recording.setVisible(false);
            Date date = new Date();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String selectedProject = (String) project.getValue();
            String desc = textEntry.getText();
            String timeFrom = startTime.getText();
            String timeTo = endTime.getText();
            System.out.println(currentDate+" _ "+timeFrom+" _ "+timeTo+" _ "+selectedProject+" _ "+desc);
            if(!timeFrom.equals("") && !timeTo.equals("") && !selectedProject.equals("") && !desc.equals("")){
                try {
                    DataController dc = new DataController();
                    String status = dc.saveTimeEntries(currentDate, timeFrom, timeTo, desc, selectedProject, 2);
                    if(status.equals("200")){
                        errorStatus.setVisible(false);
                        successStatus.setVisible(true);
                    }else{
                        successStatus.setVisible(false);
                        errorStatus.setVisible(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{

            }
        });


        right.getChildren().addAll(closeBtn, startBtn, stopBtn, plusBtn, resetBtn);

        root.setTopAnchor(right, 0.0);
        root.setRightAnchor(right, 0.0);

        root.getChildren().addAll(top, project, textEntry, right,startTime,endTime);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOpacity(0.9);
        primaryStage.setTitle("STTimer");
        primaryStage.getIcons().add(new Image("file:resources/images/sekTimer.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }
    private String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);

        return sb.toString();
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
