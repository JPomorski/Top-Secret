package com.example.revision.client;

import com.example.revision.Dot;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private boolean running;
    private Consumer<Dot> consumer;

    public ServerThread(String address, int port) {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConsumer(Consumer<Dot> consumer) {
        this.consumer = consumer;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while((message = reader.readLine()) != null) {
                System.out.println(message);
                if(message.startsWith("DT")) {
                    Dot dot = Dot.fromMessage(message);
                    if(consumer != null) {
                        consumer.accept(dot);
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void send(double x, double y, double radius, Color color){
        String message = Dot.toMessage(new Dot(x, y, radius, color));
        writer.println("DT"+message);
    }

    public void login(String name) {
        running = true;
        writer.println("LN"+name);
    }

    public void logout(String name) {
        running = false;
        writer.println("LT"+name);
    }

    public void broadcast(String message) {
        writer.println("BR"+message);
    }

    public void whisper(String message) {
        writer.println("WH"+message);
    }

    public void online() {
        writer.println("ON");
    }
}
