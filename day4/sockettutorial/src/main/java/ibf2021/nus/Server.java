package ibf2021.nus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(7777);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = server.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");

        // get input stream from the connected socket
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        String message = dis.readUTF();
        System.out.println("Message was: " + message);

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        String response = "Hello from Server!";
        dos.writeUTF(response);

        System.out.println("Closing socket here");
        socket.close();
        server.close();
    }
}
