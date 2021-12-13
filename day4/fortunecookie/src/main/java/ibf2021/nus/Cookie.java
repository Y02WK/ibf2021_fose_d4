package ibf2021.nus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Cookie {
    private Path cookieJarPath;
    private List<String> cookieJar;

    public Cookie(Path cookieJarPath) {
        this.cookieJarPath = cookieJarPath;
        this.cookieJar = loadCookies();
    }

    protected ArrayList<String> loadCookies() {
        // Loads all fortunes from the fortunecookie file into an ArrayList
        String item;
        ArrayList<String> cookieLoader = new ArrayList<String>();
        try {
            BufferedReader reader = Files.newBufferedReader(cookieJarPath);
            while ((item = reader.readLine()) != null) {
                cookieLoader.add(item);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file.");
        }

        return cookieLoader;
    }

    protected String getCookie() {
        // Returns a random fortune from the cookie ArrayList.
        return cookieJar.get(ThreadLocalRandom.current().nextInt(cookieJar.size()));
    }

}
