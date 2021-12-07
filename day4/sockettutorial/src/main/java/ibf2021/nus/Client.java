package ibf2021.nus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 7777);

        // get output stream from the socket
        OutputStream os = socket.getOutputStream();
        // create a data output stream from the output stream so we can send data
        // through it
        DataOutputStream dos = new DataOutputStream(os);

        dos.writeUTF("Hello from the other side!");
        dos.flush();
        dos.close();

        // get input stream from the socket
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        String message = dis.readUTF();

        System.out.println(message);

        System.out.println("Closing socket and terminating program.");
        socket.close();
    }
}
