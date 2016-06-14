package jjybdx4il.esc.sdw.wsrest.bug;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author jjybdx4il
 */
public class URLTest {

    private static final Logger log = Logger.getLogger(URLTest.class.getName());

    public static final String _url = "https://sdw-wsrest.ecb.europa.eu/service/data/EXR/M.USD.EUR.SP00.A";

    @Test
    public void testJavaNetURL() throws MalformedURLException, IOException {
        URL url = new URL(_url);
        log.info(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.getInputStream();
        } catch (IOException ex) {
            log.info(IOUtils.toString(conn.getErrorStream()));
            throw ex;
        }
    }
    
    @Test
    public void testHttpClient() throws MalformedURLException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {
            HttpGet httpGet = new HttpGet(_url);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                log.debug("response status: " + response.getStatusLine());
                String replyContent = IOUtils.toString(response.getEntity().getContent());
                log.debug("response content: " + replyContent);
                assertEquals(200, response.getStatusLine().getStatusCode());
                assertTrue(replyContent.contains("2002-09"));
            }
        }
    }
}
