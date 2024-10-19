package com.example.module03_basicgui_db_interface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;


public class DB_Application extends Application {

    public static void main(String[] args) {
        launch();
    }


    private static Stage primaryStage;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        showScene1();

    }

    private void showScene1() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("splash_screen.fxml"));
            Scene scene = new Scene(root, 850, 560);
            scene.getStylesheets().add(DB_Application.class.getResource("styling/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            //changeScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeScene() {
        try {
            Parent newRoot = FXMLLoader.load(DB_Application.class.getResource("db_interface_gui.fxml"));

            Scene currentScene = primaryStage.getScene();
            Parent currentRoot = currentScene.getRoot();
            currentScene.getStylesheets().add("styling/style.css");
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), currentRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {


                Scene newScene = new Scene(newRoot,850, 560);
                primaryStage.setScene(newScene);

            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}