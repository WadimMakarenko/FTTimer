package cbot.accept.fttimer.view;

import cbot.accept.fttimer.model.resManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class DialogView extends Application {
    public String errorContent;

    public DialogView(){}

    public DialogView(String content){
        this.errorContent = content;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("grid");
        Scene scene = new Scene(root, 300, 150, Color.BLACK);
        scene.getStylesheets().add("file:resources/Style.css");

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

        header.getChildren().addAll(icon, appName, hideBtn, closeBtn);

        //Body
        AnchorPane body = new AnchorPane();
        body.setPrefHeight(90);
        body.setPrefWidth(200);
        body.getStyleClass().add("-fx-background-color: #7b858e;");

        Text errorText = new Text(errorContent);
        errorText.getStyleClass().add("dialogText");
        errorText.setTextAlignment(TextAlignment.CENTER);
        body.setTopAnchor(errorText, 25.0);
        body.setLeftAnchor(errorText, 80.0);

        ImageView errorImg = new ImageView(resManager.getErrorImg());
        errorImg.setFitWidth(40);
        errorImg.setFitHeight(40);
        body.setTopAnchor(errorImg, 15.0);
        body.setLeftAnchor(errorImg, 30.0);

        Button erOk = new Button("Ok");
        erOk.setOnAction(event -> {
            primaryStage.close();
        });
        erOk.getStyleClass().add("errorOkBtn");
        header.setBottomAnchor(erOk, 10.0);
        header.setRightAnchor(erOk, 10.0);

        body.getChildren().addAll(errorText, errorImg, erOk);

        root.setTop(header);
        root.setCenter(body);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOpacity(0.9);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
