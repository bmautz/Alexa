import java.io.IOException;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

/**
 * Created by attlabs on 6/22/2016.
 */
public class JSONURL
{
    public static void main(String[] args) throws IOException
    {
        JsonBuilderFactory response = Json.createBuilderFactory(null);
        JsonObject value3 = response.createObjectBuilder()
                .add("session", response.createObjectBuilder()
                        .add("sessionID", "SessionId.60b16767-c0e9-45bd-8e69-34c1b2a67a54")
                        .add("application", response.createObjectBuilder()
                                .add("applicationId", "amzn1.echo-sdk-ams.app.d62bf22a-d406-4935-a4b6-231a9ea7c697"))
                        .add("attributes", response.createArrayBuilder())
                        .add("user", response.createObjectBuilder()
                                .add("userId", "amzn1.ask.account.AFP3ZWPOS2BGJR7OWJZ3DHPKMOMNWY4AY66FUR7ILBWANIHQN73QGSDKCHPDSZMN2ICGKFOB76GOHB2MBT5P726IY22TMAG5GORGEVDMLQV7MP46ZLPKS2ZAU3HFI6BHPD5B7PGIUXC562HQLUETHHR5KO6AFBBT33JN6HI7DE45JRFRZTWBJTMDMXDNIYRZW2DJH3ZBJXBD55A"))
                        .add("new", "false")
                )
                .add("request", response.createObjectBuilder()
                        .add("type", "IntentRequest")
                        .add("requestId", "EdwRequestId.e4126ffd-22cc-4d4b-88b6-fd9480ae8900")
                        .add("timestamp", "2016-06-21T20:12:23Z")
                        .add("intent", response.createObjectBuilder()
                                .add("name", "ChannelChange")
                                .add("slots", response.createObjectBuilder()
                                        .add("Channel", response.createObjectBuilder()
                                                .add("name", "Channel")
                                                .add("value", "300")
                                        )
                                )
                        )
                        .add("locale", "en-US")
                )
                .add("version", "1.0")
                .build();

        JsonParser parser3 = Json.createParser(new StringReader(value3.toString()));
        JsonParser.Event channelAns = parser3.next();

        String channel = "";
        String name = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME")) {
                name = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (name.equals("value")) {
                    channelAns = parser3.next();
                    channel = parser3.getString();
                    break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }

        System.out.println("This is name: " + name);
        System.out.println("This is channel: " + channel);

        String ip = "192.168.1.134";
        String finalResponse = "";
        JSONURL createURl = new JSONURL();
        createURl.changeChannel(ip, channel);
        //createURl.getIntent(ip);

    }

    public void changeChannel(String ip, String channel) throws IOException
    {
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

    public void getIntent(String ip) throws IOException
    {

        JsonBuilderFactory response = Json.createBuilderFactory(null);
        JsonObject value3 = response.createObjectBuilder()
            .add("session", response.createObjectBuilder()
                    .add("sessionID", "SessionId.14b10d68-a1aa-4ddc-a26a-0d4e0e6953d8")
                    .add("application", response.createObjectBuilder()
                            .add("applicationId", "amzn1.echo-sdk-ams.app.d62bf22a-d406-4935-a4b6-231a9ea7c697"))
                    .add("attributes", response.createArrayBuilder())
                    .add("user", response.createObjectBuilder()
                            .add("userId", "amzn1.ask.account.AFP3ZWPOS2BGJR7OWJZ3DHPKMOMNWY4AY66FUR7ILBWANIHQN73QGSDKCHPDSZMN2ICGKFOB76GOHB2MBT5P726IY22TMAG5GORGEVDMLQV7MP46ZLPKS2ZAU3HFI6BHPD5B7PGIUXC562HQLUETHHR5KO6AFBBT33JN6HI7DE45JRFRZTWBJTMDMXDNIYRZW2DJH3ZBJXBD55A"))
                    .add("new", "false")
            )
            .add("request", response.createObjectBuilder()
                    .add("type", "IntentRequest")
                    .add("requestId", "EdwRequestId.e4126ffd-22cc-4d4b-88b6-fd9480ae8900")
                    .add("timestamp", "2016-06-21T20:12:23Z")
                    .add("intent", response.createObjectBuilder()
                            .add("name", "GetVersion")
                            .add("slots", response.createArrayBuilder()))

                    .add("locale", "en-US")
            )
            .add("version", "1.0")
            .build();

        JsonParser parser3 = Json.createParser(new StringReader(value3.toString()));
        JsonParser.Event channelAns = parser3.next();

        String name = "";
        String intent = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME")) {
                name = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (name.equals("name")) {
                    channelAns = parser3.next();
                    intent = parser3.getString();
                    break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }

        System.out.println("This is intent: " + intent);


        //this would be moved to getVersion
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String http = "http://";
        String portCommand = ":8080/info/getVersion";
        String fullURL = http + ip + portCommand;
        HttpGet httpget = new HttpGet(fullURL);
        System.out.println(fullURL);
        CloseableHttpResponse getVerResponse = httpclient.execute(httpget);
        System.out.println(response.toString());
        getVerResponse.close();
    }

    public void parseJSON(JSONObject json)
    {
        JsonParser parser3 = Json.createParser(new StringReader(json.toString()));
        JsonParser.Event channelAns = parser3.next();
        String name = "";
        String intent = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME")) {
                name = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (name.equals("name")) {
                    channelAns = parser3.next();
                    intent = parser3.getString();
                    break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }

        System.out.println("This is intent: " + intent);

    }

    public void findChannel (JSONObject json)
    {
        JsonParser parser3 = Json.createParser(new StringReader(json.toString()));
        JsonParser.Event channelAns = parser3.next();

        String channel = "";
        String name = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME")) {
                name = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (name.equals("value")) {
                    channelAns = parser3.next();
                    channel = parser3.getString();
                    break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }

        System.out.println("This is name: " + name);
        System.out.println("This is channel: " + channel);
    }


}



