package ibf2021.nus;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Cookie {
    private Path cookieJar;
    private ArrayList<String> cookieStore;

    public Cookie(Path cookieJar) {
        this.cookieJar = cookieJar;
        this.cookieStore = loadCookies();
    }

    protected ArrayList<String> loadCookies() {
        String item;
        ArrayList<String> cookieLoader = new ArrayList<String>();
        try {
            BufferedReader reader = Files.newBufferedReader(cookieJar);
            while ((item = reader.readLine()) != null) {
                cookieLoader.add(item);
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e);
        }

        return cookieLoader;
    }

    protected String getCookie() {
        return cookieStore.get(ThreadLocalRandom.current().nextInt(cookieStore.size()));
    }

}
