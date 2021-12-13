package ibf2021.nus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        // takes two arguments from the command line to init server <port number>
        // <cookie_file.txt>
        // check for correct number of arguments
        if (args.length != 2) {
            System.err.println("Invalid argument length");
            return;
        }

        // check for valid port number
        int serverPort = Integer.parseInt(args[0]);
        if (serverPort < 1024 && serverPort > 65535) {
            System.err.println("Invalid port number.");
            return;
        }

        Path cookieJarFile = Path.of(args[1]);
        Server server = new Server(serverPort, cookieJarFile);
        server.startServer();
        server.startConnection();
    }

    private int serverPort;
    private Cookie cookieJar;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    // Constructor
    public Server(int serverPort, Path cookiePath) {
        this.serverPort = serverPort;
        this.cookieJar = new Cookie(cookiePath);
    }

    // starts the server
    public void startServer() throws IOException {
        this.threadPool = Executors.newFixedThreadPool(3);
        this.serverSocket = new ServerSocket(serverPort);
        System.out.println("Server started");
    }

    // waits for input
    public void startConnection() throws IOException {
        while (true) {
            System.out.println("Waiting for socket connection");
            Socket socket = serverSocket.accept();
            System.out.println("Connection from " + socket + "!");
            System.out.println("Sending to threadPool");
            threadPool.execute(new CookieClientHandler(socket, cookieJar));
        }
    }

    // closes the server
    public void stopServer() throws IOException {
        threadPool.shutdown();
        serverSocket.close();
    }
}
