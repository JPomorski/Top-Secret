package com.example.gui_project;

import client.ServerThread;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;

public class HelloController {

    private ServerThread serverThread;
    private ClientGUIReceiver receiver;

    public TextArea outputArea;
    public GridPane mainPane;
    public TextField inputField;
    public ProgressBar progressBar;
    public Button sendButton;
    public Button sendFileButton;
    public ListView clientsList;

    public HelloController(ServerThread serverThread, ClientGUIReceiver receiver) {
        this.serverThread = serverThread;
        this.receiver = receiver;
        receiver.setController(this);
    }

    public void initialize() {
        serverThread.online();
    }

    public void sendMessage() {
        String message = inputField.getText();
        serverThread.broadcast(message);
        inputField.clear();
    }

    public void showBroadcast(String sender, String message) {
        outputArea.appendText(sender + ": " + message + "\n");
    }

    public void addClient(String clientName) {
        clientsList.getItems().add(clientName);
    }

    public void removeClient(String clientName) {
        clientsList.getItems().remove(clientName);
    }

    public void populateOnlineList(List<String> clientNames) {
        clientsList.getItems().clear();
        clientNames.stream()
                .forEach(name -> clientsList.getItems().add(name));
    }
}