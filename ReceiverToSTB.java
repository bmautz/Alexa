import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by attlabs on 7/22/2016.
 */
public class ReceiverToSTB
{
    public static void main(String[] args) throws IOException
    {

        String ip = "192.168.1.134";
        String channel = "100";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String http = "http://";
        String portCommand = ":8080/tv/tune?major=";
        String fullURL = http + ip + portCommand + channel;
        HttpGet httpget = new HttpGet(fullURL);
        CloseableHttpResponse response = httpclient.execute(httpget);
        System.out.println(fullURL);
        System.out.println(response.toString());
        response.close();
    }
}
