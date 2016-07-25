import java.io.IOException;
import java.lang.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


public class AmazonPub
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
        //mConnection.setPacketReplyTimeout(10000);
        try
        {
            try
            {
                try
                {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                }
                catch(XMPPException e)
                {
                    System.out.println("login failed");
                }
            }
            catch (SmackException e)
            {
                System.out.println("smack exception");
            }
        }
        catch(IOException e)
        {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        //chatManager.addChatListener(listener);

        String userJID = "user1@ec2-54-152-188-80.compute-1.amazonaws.com/Smack";
        String userName = "user2";
        String message = "Hello from the other side, I am user2";
        String thread = "alexa";

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(message);
            chat.sendMessage(message);
            chat.sendMessage(message);
        }
        catch(SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());


        mConnection.disconnect();
    }
}
