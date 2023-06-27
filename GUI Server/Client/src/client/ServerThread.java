package client;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private boolean running;

    private ClientReceiver receiver = null;

    public ServerThread(String address, int port) {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(ClientReceiver receiver) {
        this.receiver = receiver;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            writer = new PrintWriter(output, true);
            String message;
            while((message = reader.readLine()) != null) {
                String prefix = message.substring(0, 2);
                String postfix = message.substring(2);

                switch(prefix) {
                    case "LN" -> receiver.receiveLoginBroadcast(postfix);
                    case "LT" -> receiver.receiveLogoutBroadcast(postfix);
                    case "ON" -> receiver.receiveOnline(postfix.split(" "));
                    case "BR" -> {
                        String[] postfixArr = postfix.split(" ", 2);
                        receiver.receiveBroadcast(postfixArr[0], postfixArr[1]);
                    }
                    case "WH" -> {
                        String[] postfixArr = postfix.split(" ", 2);
                        receiver.receiveWhisper(postfixArr[0], postfixArr[1]);
                    }
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void send(String message){
        writer.println(message);
    }

    public void login(String name) {
        running = true;
        writer.println("LN"+name);
    }

    public void logout(String name) {
        running = false;
        writer.println("LT" + name);
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
