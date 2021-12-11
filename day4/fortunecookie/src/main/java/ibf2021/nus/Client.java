package ibf2021.nus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private int serverPort;
    private String serverAddress;
    private Socket socket;
    private String fortuneMessage;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            openSocket();
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
            System.out.println("Welcome. Enter 'get-cookie' to get a fortune cookie!");
            while (true) {
                String input = this.getInput(inputStream);
                if (!this.processInput(input.trim())) {
                    return;
                }
            }
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
                String response = receiveFromServer();
                if (response.contains("cookie-text")) {
                    System.out.println("Getting your fortune cookie. Enter 'cookie-text' to open the cookie");
                    this.fortuneMessage = response;
                } else {
                    System.out.println(response);
                }
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
                if (this.processLogin(input)) {
                    this.sendToServer(input);
                    System.out.println(receiveFromServer());
                } else {
                    System.out.println("Command not recognized. Please enter only get-cookie or cookie-text.");
                }
        }
        return true;
    }

    private boolean processLogin(String input) {
        if (input.contains("login") || input.contains("register")) {
            return true;
        } else {
            return false;
        }
    }
}
