package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CookieClientHandler implements Runnable {
    private Socket socket;
    private Cookie cookieJar;

    // constructor
    public CookieClientHandler(Socket socket, Cookie cookieJar) {
        this.socket = socket;
        this.cookieJar = cookieJar;
    }

    @Override
    public void run() {
        try {
            receiveFromClient();
        } catch (IOException e) {
            System.err.println("Client " + socket + " has disconnected.");
            ;
        }
    }

    private void sendToClient(DataOutputStream dos, String message) throws IOException {
        dos.writeUTF(message);
        dos.flush();
    }

    private void receiveFromClient() throws IOException {
        String message = "";
        // get output stream from the socket
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);

        // get input stream from the socket
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        do {
            message = dis.readUTF();
        } while (processRequest(dos, message));
    }

    private boolean processRequest(DataOutputStream dos, String request) throws IOException {
        switch (request.trim()) {
            case "get-cookie":
                System.out.println("Sending a random fortune cookie to the client.");
                String response = "cookie-text " + cookieJar.getCookie();
                sendToClient(dos, response);
                return true;
            case "close":
                closeSocket();
                return false;
            default:
                sendToClient(dos, "Server error");
                return false;
        }
    }

    private void closeSocket() throws IOException {
        socket.close();
    }
}
