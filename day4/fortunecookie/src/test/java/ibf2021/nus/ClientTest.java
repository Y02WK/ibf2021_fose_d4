package ibf2021.nus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;

public class ClientTest {
    Client client1;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() {
        try {
            client1 = new Client("localhost", 8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        client1 = null;
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // @Test
    // public void testEmptyCookieTextClient() throws IOException {
    // client1.start(new ByteArrayInputStream("cookie-text".getBytes()));
    // assertTrue(outContent.toString().contains("You have no fortune cookie. Enter
    // 'get-cookie' to get one."));
    // }

    // @Test
    // public void testInvalidInput() throws IOException {
    // client1.start(new ByteArrayInputStream("invalidinput".getBytes()));
    // assertTrue(
    // outContent.toString().contains("Command not recognized. Please enter only
    // get-cookie or cookie-text."));
    // }

    // @Test
    // public void testGetCookie() throws IOException {
    // client1.start(new ByteArrayInputStream("get-cookie".getBytes()));
    // assertTrue(
    // outContent.toString().contains("Getting your fortune cookie. Enter
    // 'cookie-text' to open the cookie"));
    // }
}
