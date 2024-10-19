package com.example.module03_basicgui_db_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.HashMap;


public class SplashScreen {
    @FXML
    private Text message;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    private final ObservableMap<String, String> users =
            FXCollections.observableMap(new HashMap<>(){{
                put("", "");
            }});


    @FXML
    protected void register() {
        String newUserName  = username.getText();
        String newPassword = password.getText();
        users.put(newUserName,newPassword);

        message.setText("Register Successful!!!");
        username.clear();
        password.clear();
    }

    /**
     * if login in successful, change the scene,else clear all and need enter again
     */
    @FXML
    protected void login() {
        String enteredPassword = password.getText();
        String storedPassword = users.get(username.getText());
        // if user exit in map and password is correct
        if(storedPassword != null && enteredPassword.equals(storedPassword)){
            DB_Application.changeScene(); // change the scene to db_interface_gui
        }else {
            message.setText("!!!Invalid username or password");
            username.clear();
            password.clear();
        }

    }
}
