package ibf2021.nus;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        if (args[0].equalsIgnoreCase("fc.client")) {
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
