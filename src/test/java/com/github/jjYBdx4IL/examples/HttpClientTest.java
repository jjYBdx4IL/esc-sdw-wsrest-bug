package com.github.jjYBdx4IL.examples;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjybdx4il
 */
public class HttpClientTest {

    private static final Logger log = LoggerFactory.getLogger(HttpClientTest.class);

    public static final String _url = "https://sdw-wsrest.ecb.europa.eu/service/data/EXR/M.USD.EUR.SP00.A";

    @Test
    public void testJavaNetURL() throws MalformedURLException, IOException {
        //JavaUtilLoggingUtils.setJavaNetURLConsoleLoggingLevel(Level.FINEST);

        URL url = new URL(_url);
        log.info(url.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        for(String a : conn.getRequestProperties().keySet()) {
            System.out.println(a + " => " + conn.getRequestProperty(a));
        }
        conn.setRequestProperty("Accept", "text/xml");
        try {
            conn.getInputStream();
        } catch (IOException ex) {
            log.info(IOUtils.toString(conn.getErrorStream()));
            throw ex;
        }
    }
    
    @Test
    public void testApacheHttpClient() throws MalformedURLException, IOException {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {
            HttpGet httpGet = new HttpGet(_url);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                log.debug("response status: " + response.getStatusLine());
                String replyContent = IOUtils.toString(response.getEntity().getContent());
                log.debug("response content: " + replyContent.substring(0,500));
                assertEquals(200, response.getStatusLine().getStatusCode());
                assertTrue(replyContent.contains("2002-09"));
            }
        }
    }
}
