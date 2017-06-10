package cbot.accept.fttimer.view;


import cbot.accept.fttimer.model.resManager;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Browser extends Application {
    public String UserName;
    public double initialX, initialY;

    public Browser(){}

    public Browser(String usrN){
        this.UserName = usrN;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception{
        primaryStage.setResizable(true);
        BorderPane root = new BorderPane();
        addDragListeners(root);
        Scene scene = new Scene(root, 900, 700);
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
        //End header

        //Body
        //web view
        final WebView browser = new WebView();
        browser.setPrefHeight(700);
        browser.setPrefWidth(900);
        final WebEngine webEngine = browser.getEngine();



        //ProgressBar and listener
        Label stateLabel = new Label();
        header.setTopAnchor(stateLabel, 10.0);
        header.setLeftAnchor(stateLabel, 430.0);
        stateLabel.setTextFill(Color.RED);
        ProgressBar progressBar = new ProgressBar();

        Worker<Void> worker = webEngine.getLoadWorker();
        // Listening to the status of worker
        worker.stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
                stateLabel.setText(newValue.toString()+"...");
                if (newValue == Worker.State.SUCCEEDED) {
                    stateLabel.setText("LOADED...");
                }
            }
        });
        progressBar.progressProperty().bind(worker.progressProperty());
        //ProgressBar and listener end

        //String url = "http://10.1.1.236/zeiterfassung/index.php";
        String url = "https://www.google.de/?gws_rd=ssl";
        webEngine.load(url);
        //web view end
        //Body end

        header.getChildren().addAll(icon, appName, hideBtn, closeBtn, stateLabel);
        root.setTop(header);
        root.setCenter(browser);


        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOpacity(0.98);
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setMaximized(true);
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
