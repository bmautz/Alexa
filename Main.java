package Post;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import sun.security.util.Length;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



public class Main {

    public static void main(String[] args) throws IOException
                                            //throws URISyntaxException
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://192.168.1.134:8080/tv/tune?major=300");
        //HttpGet httpget = new HttpGet("http://192.168.1.134:8080/dvr/playList");
        CloseableHttpResponse response = httpclient.execute(httpget);

        try
        {
            System.out.print(response.toString());
        }

        finally
        {
            response.close();
        }



        //round two fight
        /*
         URL myURL = new URL("http://192.168.1.134:8080/tv/tune?major=300");
         URLConnection myURLConnection = myURL.openConnection();
         myURLConnection.connect();
         */

        /*
        //round three
        URI uri = new URIBuilder().setScheme("http").setHost("192.168.1.134").setPort(8080).setPath("/tv/tune/tune").setParameter("major", "300").build();
        HttpGet httpget = new HttpGet(uri);
        System.out.println(httpget.getURI());
        */




        /*
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://192.168.1.134:8080/tv/tune?major=300"); ///tune?major=202
        System.out.println(httppost.getURI());

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null)
        {
            InputStream instream = entity.getContent();
            try
            {
                System.out.print(response.toString());
            }

            finally
            {
                instream.close();
            }
        }*/
    }
}
