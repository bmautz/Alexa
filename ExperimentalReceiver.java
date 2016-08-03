import java.io.*;
import java.lang.*;
import java.net.*;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.stream.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by attlabs on 7/7/2016.
 */
public class ExperimentalReceiver
{

    private final String USER_AGENT = "Mozilla/5.0";
    public String ROKU_URL = "";
    private int ROKU_PORT = 8060;
    private int TYPE_DELAY = 500;


    public static void main(String... args) throws Exception
    {

        String ip = "192.168.1.134";
        String channel = "100";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String http = "http://";
        String portCommand = ":8080/tv/tune?major=";
        String infoCommand = ":8080/tv/getProgInfo?major=";

        //create a connection configuration
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
        mConnection.setPacketReplyTimeout(10000);
        try
        {
            mConnection.connect();
        }
        catch(IOException e)
        {
            System.out.println("connect failed");
        }
        mConnection.login("user1", "alexa1");  //also a login with no parameters //here is where issues

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
        ExperimentalReceiver rec = new ExperimentalReceiver();
        ChatMessageListener uselesschatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
                System.out.println(message);
                System.out.println("Received message: " + (message != null ? message.getBody() : "NULL"));

                if(message.getBody().equals("roku"))
                {
                    try {
	    		        /* create byte arrays to hold our send and response data */
                        byte[] sendData = new byte[1024];
                        byte[] receiveData = new byte[1024];

			            /* our M-SEARCH data as a byte array */
                        String MSEARCH = "M-SEARCH * HTTP/1.1\nHost: 239.255.255.250:1900\nMan: \"ssdp:discover\"\nST: roku:ecp\n";
                        sendData = MSEARCH.getBytes();

			            /* create a packet from our data destined for 239.255.255.250:1900 */
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("239.255.255.250"), 1900);

			            /* send packet to the socket we're creating */
                        DatagramSocket clientSocket = new DatagramSocket();
                        clientSocket.send(sendPacket);

			            /* recieve response and store in our receivePacket */
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);

			            /* get the response as a string */
                        String response = new String(receivePacket.getData());

			            /* close the socket */
                        clientSocket.close();

			            /* parse the IP from the response */
			            /* the response should contain a line like:
                        Location:  http://192.168.1.9:8060/
                        and we're only interested in the address -- not the port.
                        So we find the line, then split it at the http:// and the : to get the address.
			             */
                        response = response.toLowerCase();
                        String address = response.split("location:")[1].split("\n")[0].split("http://")[1].split(":")[0].trim();
                        String url = "http://" + address + ":8060";
                        rec.ROKU_URL = url;

                        try
                        {
                            chat.sendMessage(address);
                        }

                        catch (SmackException.NotConnectedException e)
                        {
                            System.out.println("Not connected error");
                        }

                    }

                    catch (Exception e)
                    {
                        try
                        {
                            chat.sendMessage("");
                        }

                        catch (SmackException.NotConnectedException ef)
                        {
                            System.out.println("Not connected error");
                        }
                    }
                }

                else if(message.getBody().equals("home"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/home");
                }

                else if(message.getBody().equals("netflix"))
                {
                    rec.sendPost(rec.ROKU_URL+ "/launch/12");
                }

                else if(message.getBody().equals("up"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/up");
                }

                else if(message.getBody().equals("down"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/down");
                }

                else if(message.getBody().equals("left"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/left");
                }

                else if(message.getBody().equals("right"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/right");
                }

                else if(message.getBody().equals("select"))
                {
                    rec.sendPost(rec.ROKU_URL + "/keypress/select");
                }

                else if(message.getBody().equals("directv"))
                {
                    rec.sendPost(rec.ROKU_URL + "/launch/95121");
                }

                else if(message.getBody().equals("jurassic"))
                {
                    try
                    {
                        String des = rec.parseJurassicDescription();
                        try
                        {
                            chat.sendMessage(des);
                        }
                        catch (SmackException.NotConnectedException e)
                        {
                            System.out.println("Not connected error");
                        }
                    }
                    catch(IOException e)
                    {

                    }
                }

                else if(message.getBody().equals("first"))
                {
                    try
                    {
                        String des = rec.parseFirstDescription();
                        try
                        {
                            chat.sendMessage(des);
                        }
                        catch (SmackException.NotConnectedException e)
                        {
                            System.out.println("Not connected error");
                        }
                    }
                    catch(IOException e)
                    {

                    }
                }

                else if(message.getBody().equals("jurassicPlay"))
                {
                    try
                    {
                        rec.playJurassic();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Not connected error on playJurassic");
                    }
                }

                else if(message.getBody().equals("firstPlay"))
                {
                    try
                    {
                        rec.playFirst();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Not connected error on playJurassic");
                    }
                }

                else if(message.getBody().equals("getPlaylist"))
                {
                    try
                    {
                        String result = rec.getDVRInfo();
                        try
                        {
                            chat.sendMessage(result);
                        }
                        catch (SmackException.NotConnectedException e)
                        {
                            System.out.println("Not connected error");
                        }
                        System.out.println(result);

                    }
                    catch(IOException e)
                    {
                        System.out.println("ERROR: IO Exception");
                    }
                }

                else
                {

                //String fullURL = http + ip + portCommand + message.getBody();
                HttpGet httpget = new HttpGet(message.getBody());
                try
                {
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    if(response.getEntity() == null)
                    {
                        //System.out.println(fullURL);
                        System.out.println(response.toString());
                        response.close();
                        System.out.println("ERROR: entity was null");
                    }
                    else
                    {
                        HttpEntity ent = response.getEntity();
                        InputStream instream = ent.getContent();
                        String result = rec.convertStreamToString(instream);
                        try
                        {
                            chat.sendMessage(result);
                        }
                        catch (SmackException.NotConnectedException e)
                        {
                            System.out.println("Not connected error");
                        }
                        System.out.println(result);
                    }
                }
                catch(IOException e)
                {
                    System.out.println("tried to execute http response");
                }
                }
            }
        };

        //chatManager.addChatListener(listener);


        String userJID = "user2@ec2-54-152-188-80.compute-1.amazonaws.com/Smack";
        String userName = "user2";
        String message = "Hello from the other side, I am user1";
        String thread = "alexa";

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        //chat.sendMessage(message);
        System.out.println(chat.toString());


        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 100000000 < 20000) // do for 20 seconds
        {
            Thread.sleep(50000);
        }
        mConnection.disconnect();
    }

    public String convertStreamToString(InputStream is) throws IOException
    {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }

    private String sendPost(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "";

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return "true";
        } catch (Exception E) {
            return "false";
        }

    }

    public String getDVRInfo() throws IOException
    {
        String userName = "c0pi10t";
        String password = "8th5Bre$Wrus";
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        //HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/playList?action=get"));
        HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/getPlayList")); //summary and play
        //System.out.println(response.toString());

        HttpEntity ent = response.getEntity();
        InputStream instream = ent.getContent();
        String result = convertStreamToString(instream);
        //System.out.println(response);
        return parseDVRInfo(result);

    }

    public String parseDVRInfo (String result)
    {
        JsonParser parser3 = Json.createParser(new StringReader(result));
        JsonParser.Event channelAns = parser3.next();

        String titles = "";
        String playlist = "";
        String show = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME")) {
                playlist = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (playlist.equals("title")) {
                    channelAns = parser3.next();
                    show = parser3.getString();
                    titles = titles + " " + show;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }
        System.out.println("The available play list titles are " + titles);
        return titles;
    }


    public String parseJurassicDescription() throws IOException
    {
        String userName = "c0pi10t";
        String password = "8th5Bre$Wrus";
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/playList?action=get"));
        System.out.println(response.toString());

        HttpEntity ent = response.getEntity();
        InputStream instream = ent.getContent();
        String result = convertStreamToString(instream);

        JsonParser parser3 = Json.createParser(new StringReader(result));
        JsonParser.Event channelAns = parser3.next();

        String title = "";
        String des = "";
        String show = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME"))
            {
                title = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (title.equals("description"))
                {
                    channelAns = parser3.next();
                    des = parser3.getString();
                }
                else if (title.equals("title"))
                {
                    channelAns = parser3.next();
                    show = parser3.getString();
                    if(show.equals("Jurassic World"))
                        break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }
        System.out.println("Here is the description " + des);
        return des;
    }

    public String parseFirstDescription() throws IOException
    {
        String userName = "c0pi10t";
        String password = "8th5Bre$Wrus";
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/playList?action=get"));
        System.out.println(response.toString());

        HttpEntity ent = response.getEntity();
        InputStream instream = ent.getContent();
        String result = convertStreamToString(instream);

        JsonParser parser3 = Json.createParser(new StringReader(result));
        JsonParser.Event channelAns = parser3.next();

        String title = "";
        String des = "";
        String show = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME"))
            {
                title = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (title.equals("description"))
                {
                    channelAns = parser3.next();
                    des = parser3.getString();
                }
                else if (title.equals("title"))
                {
                    channelAns = parser3.next();
                    show = parser3.getString();
                    if(show.equals("First Take"))
                        break;
                }

                channelAns = parser3.next();

            } else {
                channelAns = parser3.next();
            }
        }
        System.out.println("Here is the description " + des);
        return des;
    }

    public void playJurassic() throws IOException
    {
        String userName = "c0pi10t";
        String password = "8th5Bre$Wrus";
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/playList?action=get"));
        System.out.println(response.toString());

        HttpEntity ent = response.getEntity();
        InputStream instream = ent.getContent();
        String result = convertStreamToString(instream);

        JsonParser parser3 = Json.createParser(new StringReader(result));
        JsonParser.Event channelAns = parser3.next();

        String title = "";
        String ID = "";
        String show = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME"))
            {
                title = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (title.equals("title"))
                {
                    channelAns = parser3.next();
                    show = parser3.getString();
                    if (show.equals("Jurassic World"))
                    {
                        channelAns = parser3.next();
                        channelAns = parser3.next();
                        ID = parser3.getString();
                        break;
                    }

                    channelAns = parser3.next();
                }
                else
                    channelAns = parser3.next();

            }

            else
            {
                channelAns = parser3.next();
            }
        }
        System.out.println("Here is the unique ID of Jurassic World " + ID);
        HttpResponse dvrResponse = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/play?uniqueId=" + ID));
        System.out.println(dvrResponse.toString());
    }

    public void playFirst() throws IOException
    {
        String userName = "c0pi10t";
        String password = "8th5Bre$Wrus";
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

        HttpResponse response = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/playList?action=get"));
        System.out.println(response.toString());

        HttpEntity ent = response.getEntity();
        InputStream instream = ent.getContent();
        String result = convertStreamToString(instream);

        JsonParser parser3 = Json.createParser(new StringReader(result));
        JsonParser.Event channelAns = parser3.next();

        String title = "";
        String ID = "";
        String show = "";

        while (parser3.hasNext() == true) {
            if (channelAns.toString().equals("KEY_NAME"))
            {
                title = parser3.getString();
                //System.out.println("Name is being set: " + name);
                if (title.equals("title"))
                {
                    channelAns = parser3.next();
                    show = parser3.getString();
                    if (show.equals("First Take"))
                    {
                        channelAns = parser3.next();
                        channelAns = parser3.next();
                        ID = parser3.getString();
                        break;
                    }

                    channelAns = parser3.next();
                }
                else
                    channelAns = parser3.next();

            }

            else
            {
                channelAns = parser3.next();
            }
        }
        System.out.println("Here is the unique ID of First Take " + ID);
        HttpResponse dvrResponse = client.execute(new HttpGet("http://192.168.1.134:8080/dvr/play?uniqueId=" + ID));
        System.out.println(dvrResponse.toString());
    }
}
