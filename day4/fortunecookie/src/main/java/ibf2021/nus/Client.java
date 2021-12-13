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
        Client client;
        // check for valid args length
        if (args.length != 1) {
            System.err.println("Invalid argument length");
            return;
        }

        // split args into two parts <host>:<port>
        String[] argsParts = args[1].split(":");
        if (Integer.parseInt(argsParts[1]) < 1024 && Integer.parseInt(argsParts[1]) > 65535) {
            System.err.println("Invalid port number.");
            return;
        }

        // tries to initialize the client and the socket
        try {
            client = new Client(argsParts[0], Integer.parseInt(argsParts[1]));
            System.out.println("Client successfully started.");
        } catch (NumberFormatException e) {
            System.err.println("Error. Please enter a valid integer for port.");
            return;
        } catch (UnknownHostException e) {
            System.err.println(
                    "Error. Unable to connect to the server. Please check if host and port are valid.");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // starts the client input loop
        try {
            client.start(System.in);
        } catch (IOException e) {
            System.err.println("Error. Please run the client again.");
        }
    }

    public Client(String serverAddress, int serverPort) throws IOException, UnknownHostException {
        // init new Client and attempts to open the socket and init streams
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        openSocket();
        initStreams();
    }

    public void start(InputStream inputStream) throws IOException {
        // starts the client input loop
        // if not System.in, calls testInput method instead of starting the loop (for
        // testing)
        if (inputStream != System.in) {
            this.testInput(inputStream);
        } else {
            System.out.println("Loading...");
            // check if server is currently processing this client
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
        // one-sided handshake to check if server has begun processing this client in
        // threadpool
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
        // input method for testing purposes only
        String input = this.getInput(inputStream);
        System.out.println(input);
        this.processInput(input);
    }

    private boolean processInput(String input) throws IOException {
        // processes the input accordingly
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
                System.out.println(fortuneMessage.substring(12));
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
