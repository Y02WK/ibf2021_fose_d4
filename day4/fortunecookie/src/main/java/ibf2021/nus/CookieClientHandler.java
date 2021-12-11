package ibf2021.nus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class CookieClientHandler implements Runnable {
    private Socket socket;
    private Cookie cookieJar;
    private UserPassword insecureVault;
    private boolean isLogin = false;

    // constructor
    public CookieClientHandler(Socket socket, Cookie cookieJar, UserPassword insecureVault) {
        this.socket = socket;
        this.cookieJar = cookieJar;
        this.insecureVault = insecureVault;
    }

    @Override
    public void run() {
        try {
            initAndReceive();
        } catch (IOException e) {
            System.err.println("Client " + socket + " has disconnected.");
            ;
        }
    }

    private void sendToClient(DataOutputStream dos, String message) throws IOException {
        dos.writeUTF(message);
        dos.flush();
    }

    private void initAndReceive() throws IOException {
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
            // processRequest(dos, message);
        } while (processRequest(dos, message));
    }

    private boolean processRequest(DataOutputStream dos, String request) throws IOException {
        Scanner scanner = new Scanner(request);
        String cmd = scanner.next();

        String userArgs;
        String[] userArray;
        String response;
        switch (cmd.trim()) {
            case "get-cookie":
                // sends a random fortune cookie to the client
                if (isLogin) {
                    System.out.println("Sending a random fortune cookie to the client.");
                    response = "cookie-text: " + cookieJar.getCookie();
                } else {
                    response = "User not logged in. Please login using <username>/<password>. e.g. abc/123";
                }

                sendToClient(dos, response);
                break;
            case "login":
                // attempts to log the user in, if unsuccessful prompts user to register
                // get arguments from input and split by "/" to get username and password
                userArgs = scanner.next().trim();
                userArray = userArgs.split("/");

                if (insecureVault.userLogin(userArray[0], userArray[1])) {
                    isLogin = true;
                    response = userArray[0] + " has been authenticated.";
                } else {
                    response = "User " + userArray[0]
                            + " not found. Please register in the format <username>/<password>. e.g. abc/123";
                }

                sendToClient(dos, response);
                break;
            case "register":
                // registers a user
                userArgs = scanner.next().trim();

                if (insecureVault.userRegister(userArgs)) {
                    response = ("User successfully registered. Please login.");
                } else {
                    response = ("User already registered. Please login.");
                }

                sendToClient(dos, response);
                break;
            case "close":
                closeSocket();
                scanner.close();
                return false;
            default:
                sendToClient(dos, "Server error");
                scanner.close();
                return false;
        }
        scanner.close();
        return true;
    }

    private void closeSocket() throws IOException {
        socket.close();
    }
}
