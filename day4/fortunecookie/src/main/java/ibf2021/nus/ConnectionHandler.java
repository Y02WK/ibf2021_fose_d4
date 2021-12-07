package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket socket;
    private Cookie cookieJar;

    // constructor
    public ConnectionHandler(Socket socket, Cookie cookieJar) {
        this.socket = socket;
        this.cookieJar = cookieJar;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.receiveFromClient());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void sendToClient(String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(message);
        bw.flush();
    }

    private String receiveFromClient() throws IOException {
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        String message = dis.readUTF();
        return message;
    }
}
