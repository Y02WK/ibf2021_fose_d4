package ibf2021.nus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class UserPassword {
    private HashMap<String, String> insecureVault = new HashMap<String, String>();
    private Path vaultPath;

    public UserPassword(String vaultPath) {
        this.vaultPath = Path.of(vaultPath);
        loadVault();
    }

    private void loadVault() {
        String item;
        try {
            BufferedReader reader = Files.newBufferedReader(this.vaultPath);
            while ((item = reader.readLine()) != null) {
                String[] vars = item.split("/");
                insecureVault.put(vars[0], vars[1]);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    protected boolean userLogin(String username, String password) {
        if (password.equals(insecureVault.get(username))) {
            return true;
        }
        return false;
    }

    protected boolean userRegister(String[] userArgs) {
        // Check if username is already registered.
        if (insecureVault.containsKey(userArgs[0])) {
            return false;
        }

        // Puts username and password into the HashMap
        insecureVault.put(userArgs[0], userArgs[1]);

        // Write to the text file
        try {
            Files.write(vaultPath, (userArgs[0] + "/" + userArgs[1]).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error accessing file. Please check.");
        }
        return true;
    }

}
