package ibf2021.nus;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for Cookie class.
 */
public class CookieTest {
    /**
     * Rigorous Test :-)
     */
    Cookie cookieJar;

    @Before
    public void setUp() throws IOException {
        cookieJar = new Cookie(Path.of(
                "/Users/waikinyong/Documents/NUS_ISS/Fundamentals_of_Software_Engineering/Day_4/day4/fortunecookie/src/cookie_file.txt"));
    }

    @After
    public void tearDown() {
        cookieJar = null;
    }

    @Test
    public void testGetCookie() {
        String cookie = cookieJar.getCookie();
        assertTrue(!cookie.isBlank());
    }
}
