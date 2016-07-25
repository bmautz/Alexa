import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.lang.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
//import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.chat.ChatManagerListener;


/**
 * Created by attlabs on 7/7/2016.
 */
public class ExperimentalPub
{
    public static void main(String... args) throws Exception
    {
        //create a connection configuration
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //config.setSocketFactory(SSLSocketFactory.getDefault());
        //config.setUsernameAndPassword("user1", "alexa1"); //admin
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);
        //config.setDebuggerEnabled(true); //this line is weird
        //config.setSendPresence(true);
        //config.setSocketFactory(SSLSocketFactory.getDefault());

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
        mConnection.setPacketReplyTimeout(10000);

        try {
            mConnection.connect();
        }
        catch(IOException e)
        {
            System.out.println("connect failed");
        }
        mConnection.login("user1", "alexa1");  //also a login with no parameters //here is where issues

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
        ChatMessageListener chatListener = new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message)
            {
                System.out.println(message);
                System.out.println("Received message: " + (message != null ? message.getBody() : "NULL"));
            }
        };

        //chatManager.addChatListener(listener);

        String userJID = "user2@ec2-54-152-188-80.compute-1.amazonaws.com";
        String userName = "user2";
        String message = "Hello from the other side";
        String thread = "alexa";

        Chat chat = chatManager.createChat(userJID, chatListener);
        chat.sendMessage(message);

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 20000) // do for 20 seconds
        {
            Thread.sleep(500);
        }
        mConnection.disconnect();
    }
}
