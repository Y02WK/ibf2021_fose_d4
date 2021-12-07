package ibf2021.nus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private int serverPort;
    private String serverAddress;
    private Socket socket;
    private String fortuneMessage;

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8888);
        client.sendToServer("get-cookie");
        client.closeSocket();
    }

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            openSocket();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String receiveFromServer() throws IOException {
        // get input stream from the socket
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        // read the response from server
        String response = dis.readUTF();
        return response;
    }

    private void sendToServer(String message) throws IOException {
        // get output stream from the connected socket
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        // write message
        dos.writeUTF(message);
        dos.flush();
    }

    private void openSocket() throws UnknownHostException, IOException {
        socket = new Socket(serverAddress, serverPort);
    }

    private void closeSocket() throws UnknownHostException, IOException {
        socket.close();
    }

}
