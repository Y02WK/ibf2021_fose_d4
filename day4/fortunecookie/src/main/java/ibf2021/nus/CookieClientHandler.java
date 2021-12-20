package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CookieClientHandler implements Runnable {
    private Socket socket;
    private Cookie cookieJar;
    private DataOutputStream dos;
    private DataInputStream dis;

    // constructor
    public CookieClientHandler(Socket socket, Cookie cookieJar) {
        this.socket = socket;
        this.cookieJar = cookieJar;
    }

    @Override
    public void run() {
        try {
            initStreams();
            sendToClient("cookiecrumbles");
        } catch (IOException e1) {
            System.err.println("Unable to initialize streams");
        }

        try {
            receiveFromClient();
        } catch (IOException e) {
            System.err.println("Client " + socket + " has disconnected.");
        }
    }

    private void initStreams() throws IOException {
        this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        return;
    }

    private void sendToClient(String message) throws IOException {
        this.dos.writeUTF(message);
        this.dos.flush();
    }

    private void receiveFromClient() throws IOException {
        String message = "";
        do {
            message = dis.readUTF();
        } while (processRequest(message));
    }

    private boolean processRequest(String request) throws IOException {
        switch (request.strip()) {
            case "get-cookie":
                System.out.println("Sending a random fortune cookie to the client.");

                String response = "cookie-text " + cookieJar.getCookie();
                sendToClient(response);

                return true;
            case "close":
                closeSocket();
                return false;
            default:
                sendToClient("Server error");
                return false;
        }
    }

    private void closeSocket() throws IOException {
        socket.close();
    }
}
