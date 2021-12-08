package ibf2021.nus;

import java.io.IOException;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Error. No arguments inputed");
            return;
        } else if (args[0].equalsIgnoreCase("fc.server")) {
            /**
             * Creates an instance of the server.
             * args should be in the format fc.server <port> <path of txt file containing
             * fortune cookie sayings>
             */
            if (args.length != 3) {
                System.err.println("Invalid argument length");
                return;
            }
            int serverPort = Integer.parseInt(args[1]);
            if (serverPort < 1024 && serverPort > 65535) {
                System.err.println("Invalid port number.");
                return;
            }
            String cookieJar = args[2];
            Path cookieJarFile = Path.of("./" + cookieJar);
            Server server = new Server(serverPort, cookieJarFile);
            server.startServer();
            System.out.println("Server started successfully.");
            server.startConnection();
        } else if (args[0].equalsIgnoreCase("fc.client")) {
            /**
             * Creates an instance of the client.
             * args should be in the format fc.client <host:port>
             */
            if (args.length != 2) {
                System.err.println("Invalid argument length");
                return;
            }
            String[] argsParts = args[1].split(":");
            if (Integer.parseInt(argsParts[1]) < 1024 && Integer.parseInt(argsParts[1]) > 65535) {
                System.err.println("Invalid port number.");
                return;
            }
            try {
                Client client = new Client(argsParts[0], Integer.parseInt(argsParts[1]));
                System.out.println("Client successfully started.");
                client.start(System.in);
            } catch (NumberFormatException e) {
                System.err.println("Error. Please enter a valid port.");
                return;
            }
        }
    }
}
