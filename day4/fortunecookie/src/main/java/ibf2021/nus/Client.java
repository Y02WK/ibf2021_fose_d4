package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private int serverPort;
    private String serverAddress;
    private Socket socket;
    private String fortuneMessage;
    private DataInputStream dis;
    private DataOutputStream dos;

    public static void main(String[] args) {
        Client client = new Client("localhost", 8888);
        try {
            client.start(System.in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            openSocket();
            initStreams();
        } catch (UnknownHostException e) {
            System.err.println("Error. Unable to connect to the server. Please check if the host and port are valid.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(InputStream inputStream) throws IOException {
        if (inputStream != System.in) {
            this.testInput(inputStream);
        } else {
            System.out.println("Loading...");
            if (serverHandshake()) {
                System.out.println("Welcome. Enter 'get-cookie' to get a fortune cookie!");
                while (true) {
                    String input = this.getInput(inputStream);
                    if (!this.processInput(input.trim())) {
                        return;
                    }
                }
            }
        }
    }

    private void initStreams() throws IOException {
        // get input and output streams
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    private boolean serverHandshake() throws IOException {
        String handshake = receiveFromServer();
        if (handshake.equals("cookiecrumbles")) {
            return true;
        }
        return false;
    }

    private String receiveFromServer() throws IOException {
        // read the response from server
        String response = dis.readUTF();
        return response;
    }

    private void sendToServer(String message) throws IOException {
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

    private String getInput(InputStream inputStream) {
        String input = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            input = br.readLine();
            return input;
        } catch (IOException e) {
            System.err.println(e);
        }
        return input;
    }

    private void testInput(InputStream inputStream) throws IOException {
        String input = this.getInput(inputStream);
        System.out.println(input);
        this.processInput(input);
    }

    private boolean processInput(String input) throws IOException {
        if (input.isBlank()) {
            System.err.println("Input cannot be blank.");
            return true;
        }

        switch (input.toLowerCase()) {
            case "get-cookie":
                this.sendToServer(input);
                System.out.println("Getting your fortune cookie. Enter 'cookie-text' to open the cookie");
                this.fortuneMessage = receiveFromServer();
                break;
            case "cookie-text":
                if (this.fortuneMessage == null) {
                    System.out.println("You have no fortune cookie. Enter 'get-cookie' to get one.");
                    break;
                }
                System.out.println(fortuneMessage);
                System.out.println("Enter 'get-cookie' to get another cookie, or enter 'close' to exit.");
                break;
            case "close":
                this.sendToServer(input);
                this.closeSocket();
                return false;
            default:
                System.out.println("Command not recognized. Please enter only get-cookie or cookie-text.");
        }
        return true;
    }
}
