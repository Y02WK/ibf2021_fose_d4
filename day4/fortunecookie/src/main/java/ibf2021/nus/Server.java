package ibf2021.nus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8888, Path.of(
                "src/cookie_file.txt"), "src/password.txt");
        server.startServer();
        server.startConnection();
    }

    private int serverPort;
    private Cookie cookieJar;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private UserPassword insecureVault;

    // Constructor
    public Server(int serverPort, Path cookiePath, String vaultPath) {
        this.serverPort = serverPort;
        this.cookieJar = new Cookie(cookiePath);
        this.insecureVault = new UserPassword(vaultPath);
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
            threadPool.execute(new CookieClientHandler(socket, cookieJar, insecureVault));
        }
    }

    // closes the server
    public void stopServer() throws IOException {
        threadPool.shutdown();
        serverSocket.close();
    }
}
