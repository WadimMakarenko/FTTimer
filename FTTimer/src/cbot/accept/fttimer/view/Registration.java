package cbot.accept.fttimer.view;

import cbot.accept.fttimer.MainApp;
import cbot.accept.fttimer.CsvFileWriter;
import cbot.accept.fttimer.MD5;
import cbot.accept.fttimer.model.User;
import cbot.accept.fttimer.model.resManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vadim on 5/6/2017.
 */
public class Registration extends Application {
    public double initialX, initialY;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        root.getStyleClass().add("grid");
        addDragListeners(root);
        Scene scene = new Scene(root, 300, 270, Color.BLACK);
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

        header.getChildren().addAll(icon, appName, hideBtn, closeBtn);
        //End Header

        //Body
        AnchorPane body = new AnchorPane();
        body.setPrefHeight(240);
        body.getStyleClass().add("back-lightDark");

        ImageView sec = new ImageView(resManager.getSecurityImg());
        sec.setFitWidth(55);
        sec.setFitHeight(55);
        body.setTopAnchor(sec, 0.0);
        body.setLeftAnchor(sec, 125.0);

        TextField loginInp = new TextField();
        loginInp.getStyleClass().add("loginInput");
        loginInp.setPrefWidth(250);
        loginInp.setPrefHeight(30);
        loginInp.setPromptText("Enter Your login...");
        body.setTopAnchor(loginInp, 55.0);
        body.setLeftAnchor(loginInp, 30.0);


        PasswordField passInp = new PasswordField();
        passInp.getStyleClass().add("loginInput");
        passInp.setPrefWidth(250);
        passInp.setPrefHeight(30);
        passInp.setPromptText("Enter Your Password...");
        body.setTopAnchor(passInp, 95.0);
        body.setLeftAnchor(passInp, 30.0);

        PasswordField repeatPInp = new PasswordField();
        repeatPInp.getStyleClass().add("loginInput");
        repeatPInp.setPrefWidth(250);
        repeatPInp.setPrefHeight(30);
        repeatPInp.setPromptText("Repeat Your Password...");
        body.setTopAnchor(repeatPInp, 135.0);
        body.setLeftAnchor(repeatPInp, 30.0);

        Button registerBtn = new Button("Submit");
        registerBtn.getStyleClass().add("addBtn");
        registerBtn.setCursor(Cursor.HAND);
        registerBtn.setPrefWidth(250);
        registerBtn.setPrefHeight(30);
        body.setTopAnchor(registerBtn, 175.0);
        body.setLeftAnchor(registerBtn, 30.0);
        registerBtn.setOnAction((ActionEvent event) -> {
        	File f = new File("log_information.log");
	        if(!f.exists()){
	            String log = loginInp.getText();
	            String pass = passInp.getText();
	            String rpass = repeatPInp.getText();
	            if(!log.equals("")){
	                int check = 0;
	                try {
	                    check = validatePassAndLog(pass, rpass);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                if(check == 200){
	                    MD5 md = new MD5();
	                    String mdPass = md.crypt(pass);
	                    String mdLog = md.crypt(log);
	                    List<User> usrData = new ArrayList<>();
	                    usrData.add(new User(mdLog, mdPass));
	                    try {
	                        File file = new File("log_information.log");
	                        if(file.exists()){
	                            file.delete();
	                        }
	                        CsvFileWriter.writeUserData("log_information.log", usrData);
	                        MainApp toLogin = new MainApp();
	                        Stage newStage = new Stage();
	                        toLogin.start(newStage);
	                        primaryStage.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }else{
	                    showAlert("Pleas check Passwort requirement:\n- Length 8 character;\n- Minimum 1 Uppercase character;\n- Alphanumeric Passwort;\n- And symbols '!@#$%^'");
	                }
	            }
	        }else{
	        	showAlert("You don't have Permission.\nAnother User was registrated...");
	        }

        });

        body.getChildren().addAll(sec, loginInp, passInp, repeatPInp, registerBtn);

        //End Body
        root.setTop(header);
        root.setCenter(body);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private int validatePassAndLog(String password, String repass) throws IOException {
        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&*]).{6,15})");
        Matcher m = p.matcher(password);
        if(m.find() && m.group().equals(password)){
            if(password.equals(repass)){
                return 200;
            }else{
                return 400;
            }
        }else{
            return 400;
        }
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
