package ibf2021.nus;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Error. No arguments inputted");
            return;
        } else if (args[0].equalsIgnoreCase("fc.server")) {
            int serverPort = Integer.parseInt(args[1]);
            String cookieJar = "./" + args[2];
            Path cookieJarFile = Paths.get(cookieJar);
            Server server = new Server(serverPort, cookieJarFile);
            server.startServer();
            System.out.println("Server started successfully.");
            server.startConnection();
        } else if (args[0].equalsIgnoreCase("fc.client")) {
            String[] argsParts = args[1].split(":");
            Client client = new Client(argsParts[0], Integer.parseInt(argsParts[1]));
            System.out.println("Client successfully started.");
        }
    }
}
