package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private Cookie cookieJar;

    // constructor
    public ConnectionHandler(Socket socket, Cookie cookieJar) {
        this.socket = socket;
        this.cookieJar = cookieJar;
    }

    @Override
    public void run() {
        try {
            receiveFromClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToClient(DataOutputStream dos, String message) throws IOException {
        // OutputStream os = socket.getOutputStream();
        // DataOutputStream dos = new DataOutputStream(os);
        dos.writeUTF(message);
        dos.flush();
    }

    private void receiveFromClient() throws IOException {
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);
        while (is.available() == 0) {
            String message = dis.readUTF();
            processRequest(dos, message);
        }
    }

    private void processRequest(DataOutputStream dos, String request) throws IOException {
        switch (request) {
            case "get-cookie":
                String response = cookieJar.getCookie();
                sendToClient(dos, response);
                return;
            case "close":
                closeSocket();
            default:
                sendToClient(dos, "Server error");
        }
    }

    private void closeSocket() throws IOException {
        socket.close();
    }
}
