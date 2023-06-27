package com.example.revision;

import com.example.revision.client.ServerThread;
import com.example.revision.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class HelloController {
    private Server server;
    private ServerThread serverThread;
    private Consumer<Dot> consumer;
    private GraphicsContext graphicsContext;
    public TextField addressField;
    public TextField portField;
    public ColorPicker colorPicker;
    public Slider radiusSlider;
    public Canvas canvas;

//    public HelloController(Server server, ServerThread serverThread) {
//        this.server = server;
//        this.serverThread = serverThread;
//
//        consumer = dot -> {
//            double x = dot.x();
//            double y = dot.y();
//            double radius = dot.radius();
//            Color color = dot.color();
//
//            graphicsContext.setFill(color);
//            graphicsContext.fillOval(x - radius/2, y - radius/2, radius, radius);
//        };
//    }

    public void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double radius = radiusSlider.getValue();
        Color color = colorPicker.getValue();

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        graphicsContext.setFill(color);
        graphicsContext.fillOval(x - radius/2, y - radius/2, radius, radius);

//        serverThread.send(x, y, radius, color);
    }
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onStartServerClicked(ActionEvent actionEvent) {

    }

    public void onConnectClicked(ActionEvent actionEvent) {
    }
}