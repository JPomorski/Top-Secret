import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket;
    private PrintWriter writer;
    private Server server;
    private String clientName = null;

    public ClientThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
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
                    case "LN" -> login(postfix);
                    case "LT" -> logout();
                    case "BR" -> server.broadcast(this,postfix);
                    case "WH" -> server.whisper(this,postfix);
                    case "ON" -> server.online(this);
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            server.broadcastLogout(this);
            server.removeClient(this);
            //e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getClientName() {
        return clientName;
    }

    public void send(String message) {
        writer.println(message);
    }

    public void login(String name) {
        clientName = name;
        //server.online(this);
        server.broadcastLogin(this);
    }

    public void logout() {
        server.broadcastLogout(this);
    }


}
