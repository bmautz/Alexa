import java.io.*;
import java.lang.*;

import org.apache.http.HttpEntity;
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
                            /*
                            String userJID = "user2@ec2-54-152-188-80.compute-1.amazonaws.com/Smack";
                            Chat responseChat = chatManager.createChat(userJID, "alexaResp", uselesschatListener);
                            System.out.println(responseChat.toString());
                            responseChat.sendMessage(result);
                            */
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
        while ((System.nanoTime() - start) / 1000000 < 20000) // do for 20 seconds
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
}
