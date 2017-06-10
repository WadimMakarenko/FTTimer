package cbot.accept.fttimer;

import javafx.application.Application;
import javafx.stage.Stage;
import cbot.accept.fttimer.CsvParser;
import cbot.accept.fttimer.MD5;
import cbot.accept.fttimer.StageController;
import cbot.accept.fttimer.model.resManager;
import cbot.accept.fttimer.view.DialogView;
import cbot.accept.fttimer.view.Registration;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp extends Application {
	public double initialX, initialY;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
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

	        Text regText = new Text("Login");
	        regText.getStyleClass().add("loginText");
	        regText.setTextAlignment(TextAlignment.CENTER);
	        body.setTopAnchor(regText, 20.0);
	        body.setLeftAnchor(regText, 110.0);

	        ImageView loginImg = new ImageView(resManager.getKeyImg());
	        loginImg.setFitWidth(25);
	        loginImg.setFitHeight(25);
	        body.setTopAnchor(loginImg, 21.0);
	        body.setLeftAnchor(loginImg, 170.0);

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
	        passInp.setOnKeyPressed(new EventHandler<KeyEvent>(){
	            @Override
	            public void handle(KeyEvent ke)
	            {
	                if (ke.getCode().equals(KeyCode.ENTER))
	                {
	                    String fName = loginInp.getText();
	                    String pass = passInp.getText();

	                    if(!fName.equals("")){
	                        if(!pass.equals("")){
	                            int checkData = 0;
	                            try {
	                                checkData = validatePassAndLog(pass, fName);
	                            } catch (IOException e) {
	                                e.printStackTrace();
	                            }
	                            //here place to tocken variable and query to get tocken function
	                            if(checkData == 500){
	                                showAlert("Pleas go to Registration...");
	                            }else{
	                                if(checkData == 300){
	                                    showAlert("Pleas Enter Valid Login!");
	                                }else{
	                                    if(checkData == 400){
	                                        showAlert("Pleas Enter Valid Password!");
	                                    }else{
	                                        try {
	                                            StageController toCartView = new StageController();
	                                            toCartView.SwitchScene(primaryStage,"FTimeEntry", fName);
	                                        } catch (Exception e) {
	                                            e.printStackTrace();
	                                        }
	                                    }
	                                }
	                            }
	                        }else{showAlert("Pleas Enter Password!");}
	                    }else{
	                        showAlert( "Pleas Enter Login!");
	                    }
	                }
	            }
	        });
	        body.setTopAnchor(passInp, 95.0);
	        body.setLeftAnchor(passInp, 30.0);

	        Text support = new Text("Support");
	        support.setCursor(Cursor.HAND);
	        support.getStyleClass().add("reportText");
	        support.setOnMouseClicked(event -> {
	            showAlert("Support:\nvadim.makarenko@cbot-accept.de");
	        });
	        body.setTopAnchor(support, 150.0);
	        body.setLeftAnchor(support, 30.0);



	        Button loginBtn = new Button("Login");
	        loginBtn.getStyleClass().add("addBtn");
	        loginBtn.setCursor(Cursor.HAND);
	        loginBtn.setPrefWidth(120);
	        loginBtn.setPrefHeight(30);
	        body.setTopAnchor(loginBtn, 175.0);
	        body.setLeftAnchor(loginBtn, 30.0);
	        loginBtn.setOnAction(event -> {
	            String fName = loginInp.getText();
	            String pass = passInp.getText();

	            if(!fName.equals("")){
	                if(!pass.equals("")){
	                    int checkData = 0;
	                    try {
	                        checkData = validatePassAndLog(pass, fName);
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                    //here place to tocken variable and query to get tocken function
	                    if(checkData == 500){
	                        showAlert("Pleas go to Registration...");
	                    }else{
	                        if(checkData == 300){
	                            showAlert("Pleas Enter Valid Login!");
	                        }else{
	                            if(checkData == 400){
	                                showAlert("Pleas Enter Valid Password!");
	                            }else{
	                                try {
	                                    StageController toCartView = new StageController();
	                                    toCartView.SwitchScene(primaryStage,"FTimeEntry", fName);
	                                } catch (Exception e) {
	                                    e.printStackTrace();
	                                }
	                            }
	                        }
	                    }
	                }else{showAlert("Pleas Enter Password!");}
	            }else{showAlert( "Pleas Enter Login!");}
	        });

	        Button registerBtn = new Button("Registration");
	        registerBtn.getStyleClass().add("addBtn");
	        registerBtn.setCursor(Cursor.HAND);
	        registerBtn.setPrefWidth(120);
	        registerBtn.setPrefHeight(30);
	        body.setTopAnchor(registerBtn, 175.0);
	        body.setLeftAnchor(registerBtn, 160.0);
	        registerBtn.setOnAction(event -> {
	            try {
	                Registration registration = new Registration();
	                registration.start(primaryStage);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });

	        body.getChildren().addAll(regText, loginInp, passInp, loginBtn, registerBtn, loginImg, support);
	        //End Body

	        root.setTop(header);
	        root.setCenter(body);
	        primaryStage.initStyle(StageStyle.UNDECORATED);
	        primaryStage.setOpacity(0.98);
	        primaryStage.setTitle("FTTimer");
	        primaryStage.getIcons().add(new Image("file:resources/images/AddEntry.png"));
	        primaryStage.setScene(scene);
	        primaryStage.show();
		
	}
	
	
	 private int validatePassAndLog(String password, String login) throws IOException {
		 int toStatus = 0;
	        MD5 md = new MD5();
	        CsvParser parse = new CsvParser();
	        Pattern p = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!&*]).{6,15})");
	        Matcher m = p.matcher(password);

	        String mdPass = md.crypt(password);
	        String mdLog = md.crypt(login);
	        File f = new File("log_information.log");
	        if(f.exists()){
	            String[] usrData = new String[2];
	            usrData = parse.parseUserData("log_information.log");

	            if(mdLog.equals(usrData[1])){
	                if(m.find() && m.group().equals(password)){
	                    if(mdPass.equals(usrData[0])){
	                    	toStatus = 200;
	                    }else{
	                    	toStatus = 400;
	                    }
	                }else{
	                	toStatus = 400;
	                }
	            }else{
	            	toStatus = 300;
	            }
	        }else{
	        	toStatus = 500;
	        }
	        return (int)toStatus;
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
