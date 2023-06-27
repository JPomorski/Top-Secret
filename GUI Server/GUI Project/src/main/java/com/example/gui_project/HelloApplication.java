package com.example.gui_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import client.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ServerThread serverThread = new ServerThread("localhost", 5000);
        ClientGUIReceiver receiver = new ClientGUIReceiver();
        serverThread.setDaemon(true);
        serverThread.start();

        serverThread.setReceiver(receiver);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> new HelloController(serverThread, receiver));

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Login");
        dialog.setHeaderText("Login");
        dialog.setContentText("Please enter your nickname:");

        Optional<String> result = dialog.showAndWait();
        String login = null;
        if(result.isPresent()) {
            login = result.get();
            serverThread.login(login);
            stage.setTitle("Chat - " + login);
        }

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}