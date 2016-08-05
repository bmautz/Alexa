package session;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.io.IOException;
import java.lang.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonBuilderFactory;
import net.maritimecloud.internal.core.javax.json.JsonObject;
import net.maritimecloud.internal.core.javax.json.stream.JsonParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.Thread;
import java.net.*;

/**
 * This sample shows how to create a simple speechlet for handling intent requests and managing
 * session interactions.
 */
public class SessionSpeechlet implements Speechlet
{
    private static final Logger log = LoggerFactory.getLogger(SessionSpeechlet.class);

    private static final String COLOR_KEY = "COLOR";
    private static final String COLOR_SLOT = "Color";

    private static String PLAYLIST_KEY = "Nothing";
    private static final String PLAYLIST_SLOT = "PlaylistType";

    private static final String SHOW_KEY = "SHOW";
    private static final String SHOW_SLOT = "Show";

    private static  String CHANNEL_KEY = "0";
    private static final String  CHANNEL_SLOT = "Channel";

    private static  String CHANNELINFO_KEY = "0";
    private static final String  CHANNELINFO_SLOT = "ChannelInfo";

    private static String DVR_KEY = "NO";
    private static final String DVR_SLOT = "Movielist";

    private static String DVRPLAY_KEY = "NOPLAY";
    private static final String DVRPLAY_SLOT = "Movieplaylist";


    String AspeechText;
    String ArepromptText;
    XMPPTCPConnection mConnection;
    ChatManager chatManager;
    String userJID = "user1@ec2-54-152-188-80.compute-1.amazonaws.com/Smack";
    String userName = "user2";
    //String message = "Hello from the other side, I am user2";
    String thread = "alexa";
    ChatMessageListener chatListener;

    private final String USER_AGENT = "Mozilla/5.0";
    private String ROKU_URL = "";
    private int ROKU_PORT = 8060;


    //first function - no mod
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException
    {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }




    //second function - no mod
    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException
    {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }




    //third function - modified
    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException
    {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.

        /*
        if ("GetPlaylist".equals(intentName))
        {
            //return setColorInSession(intent, session);
            return setPlaylistTypeinSession(intent, session);
        }

        else if("PlayPlaylist".equals(intentName))
        {
            return playPlaylistTypeinSession(intent, session);
        }

*/
        //would have an else here
         if("ChangeChannel".equals(intentName))
        {
            return changeChannelinSession(intent, session);
        }

        else if("GetVersion".equals(intentName))
        {
            return getVersioninSession(intent, session);
        }

        else if("ChanUp".equals(intentName))
        {
            return getChanUpinSession(intent, session);
        }

        else if("ChanDown".equals(intentName))
        {
            return getChanDowninSession(intent, session);
        }

        else if("TurnOn".equals(intentName))
        {
            return getTurnOninSession(intent, session);
        }

        else if("TurnOff".equals(intentName))
        {
            return getTurnOffinSession(intent, session);
        }

         else if("Roku".equals(intentName))
         {
             return getRokuinSession(intent, session);
         }

         else if("Home".equals(intentName))
         {
             return getRokuHomeinSession(intent, session);
         }

         else if("Netflix".equals(intentName))
         {
             return getRokuNetflixinSession(intent, session);
         }

         else if("RokuMoveUp".equals(intentName))
         {
             return getRokuMoveUpinSession(intent, session);
         }

         else if("RokuMoveDown".equals(intentName))
         {
             return getRokuMoveDowninSession(intent, session);
         }

         else if("RokuMoveLeft".equals(intentName))
         {
             return getRokuMoveLeftinSession(intent, session);
         }

         else if("RokuMoveRight".equals(intentName))
         {
             return getRokuMoveRightinSession(intent, session);
         }

         else if("RokuSelect".equals(intentName))
         {
             return getRokuSelectinSession(intent, session);
         }

         else if("RokuDirectv".equals(intentName))
         {
             return getRokuDirectvinSession(intent, session);
         }

         else if("GetPlaylist".equals(intentName))
         {
             return getPlaylistinSession(intent, session);
         }

         else if ("GetDescription".equals(intentName))
         {
             return getDescriptioninSession(intent, session);
         }

         else if("PlayFromPlaylist".equals(intentName))
         {
             return playFromPlaylistinSession(intent, session);
         }

         else if("Guide".equals(intentName))
         {
             return getGuideinSession(intent, session);
         }

         else if("GuideUp".equals(intentName))
         {
             return getGuideUpinSession(intent, session);
         }

         else if("GuideDown".equals(intentName))
         {
             return getGuideDowninSession(intent, session);
         }

         else if("GuideSelect".equals(intentName))
         {
             return getGuideSelectinSession(intent,session);
         }

         else if("Play".equals(intentName))
         {
             return playinSession(intent, session);
         }

         else if("Pause". equals(intentName))
         {
             return pauseinSession(intent, session);
         }

        else
        {
            throw new SpeechletException("Invalid Intent");
        }

    }



    //fourth function - no mod
    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException
    {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }



    //fifth function - modified
    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual welcome message
     */
    private SpeechletResponse getWelcomeResponse()
    {
        // Create the welcome message.
        String speechText =
                "Welcome to the direct tv skill set. Please tell me what you would like to do by saying a command";
        //Can give examples

        String repromptText =
                "Please tell me what you would like to do by saying a command";

        return getSpeechletResponse(speechText, repromptText, true);
    }


    //sixth and a 1/2 function, made by me
    private SpeechletResponse playPlaylistTypeinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if (PLAYLIST_KEY != null)
        {
            speechText =
                    String.format("Now playing %s", PLAYLIST_KEY);
            repromptText = String.format(
                    "We are now playing the show %s", PLAYLIST_KEY);

        }

        else
        {
            // Render an error since we don't know what the users favorite color is.
            speechText = "You have not selected a type of playlist to watch, please ask to find a play list of the type genie, system, or user";
            repromptText =
                    "You have not selected a type of playlist to watch, please select one";
        }
        //assume user is now watching, end session
        return getSpeechletResponse(speechText, repromptText, true);
    }



    //sixth and a 3/4 function, made by me
    private SpeechletResponse changeChannelinSession(final Intent intent, final Session session)
    {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot channelSlot = slots.get(CHANNEL_SLOT);
        String channelNum = channelSlot.getValue();
        String speechText, repromptText;

        String http = "http://";
        String portCommand = ":8080/tv/tune?major=";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String ip = "192.168.1.134";

        // Check for favorite color and create output to user.
        if (!channelNum.equals("0"))
        {
            CHANNEL_KEY = channelNum;

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
            config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
            config.setPort(5222);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

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

            Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
            String fullUrl = http + ip + portCommand + channelNum;
            try
            {
                chat.sendMessage(fullUrl);
            }
            catch(SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }

            System.out.println(chat.toString());

            mConnection.disconnect();

            speechText =
                    String.format("Changing channel to channel %s", channelNum);
            repromptText = String.format(
                    "We are now on the channel %s", channelNum);

        }

        else
        {
            // Render an error since we don't know what the users favorite color is.
            speechText = "Error not a valid channel";
            repromptText =
                    "Please pick a different channel to watch";
        }
        //assume user is now watching, end session
        return getSpeechletResponse(speechText, repromptText, true);
    }


    private SpeechletResponse getVersioninSession(final Intent intent, final Session session)
    {

        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot channelSlot = slots.get(CHANNELINFO_SLOT);
        String channelNum = channelSlot.getValue();
        String speechText, repromptText;

        String http = "http://";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String ip = "192.168.1.134";

        if (!channelNum.equals("0"))
        {
            CHANNELINFO_KEY = channelNum;
            speechText =
                    String.format("Retrieving info for %s", channelNum);
            repromptText = String.format(
                    "we got info for %s", channelNum);

            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
            config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
            config.setPort(5222);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

            try {
                try {
                    try {
                        mConnection.connect();
                        mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                    } catch (XMPPException e) {
                        System.out.println("login failed");
                    }
                } catch (SmackException e) {
                    System.out.println("smack exception");
                }
            } catch (IOException e) {
                System.out.println("connect failed");
            }

            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

            ChatMessageListener chatListener = new ChatMessageListener()
            {
                @Override
                public void processMessage(Chat chat, Message message)
                {
                    JsonParser parser3 = Json.createParser(new StringReader(message.getBody()));
                    JsonParser.Event channelAns = parser3.next();

                    String title = "";
                    String show = "";

                    while (parser3.hasNext() == true) {
                        if (channelAns.toString().equals("KEY_NAME")) {
                            title = parser3.getString();
                            //System.out.println("Name is being set: " + name);
                            if (title.equals("title")) {
                                channelAns = parser3.next();
                                show = parser3.getString();
                                break;
                            }

                            channelAns = parser3.next();

                        } else {
                            channelAns = parser3.next();
                        }
                    }


                    parser3 = Json.createParser(new StringReader(message.getBody()));
                    channelAns = parser3.next();

                    String episode = "";
                    String epName = "";

                    while (parser3.hasNext() == true)
                    {
                        if (channelAns.toString().equals("KEY_NAME")) {
                            episode = parser3.getString();
                            //System.out.println("Name is being set: " + name);
                            if (episode.equals("episodeTitle")) {
                                channelAns = parser3.next();
                                epName = parser3.getString();
                                break;
                            }

                            channelAns = parser3.next();

                        } else {
                            channelAns = parser3.next();
                        }
                    }

                    if(show.equals(""))
                    {
                        AspeechText = "Channel " + CHANNELINFO_KEY + "does not exist";
                        ArepromptText = "Channel " + CHANNELINFO_KEY + "does not exist";
                    }

                    else if(epName.equals(""))
                    {
                        AspeechText = "Channel " + CHANNELINFO_KEY + " is now playing " + show;
                        ArepromptText = "Channel " + CHANNELINFO_KEY + " is now playing " + show;
                    }

                    else
                    {
                        AspeechText = "Channel " + CHANNELINFO_KEY + " is now playing " + show + " episode " + epName;
                        ArepromptText = "Channel " + CHANNELINFO_KEY + " is now playing " + show + " episode " + epName;
                    }

                }
            };

            Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
            String fullUrl = http + ip + infoCommand + channelNum;
            try
            {
                chat.sendMessage(fullUrl);
            }

            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }

            System.out.println(chat.toString());

            final long start = System.nanoTime();
            while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
            {
                try {
                    Thread.sleep(200);
                }
                catch(InterruptedException e)
                {
                    System.out.println("no thread sleep allowed");
                }
            }
            mConnection.disconnect();
        }

        else
        {
            speechText = "Couldnt find information for your requested channel";
            repromptText = "Couldnt find information for your requested channel";
            return getSpeechletResponse(speechText, repromptText, true);
        }

        //assume user is now watching, end session
        return getSpeechletResponse(AspeechText, ArepromptText, true);

    }

    private SpeechletResponse getChanUpinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String http = "http://";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=chanup";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now moving up one channel";
        repromptText = "We have moved up one channel";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getChanDowninSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String http = "http://";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=chandown";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now moving down one channel";
        repromptText = "we have moved down one channel";
        return getSpeechletResponse(speechText, repromptText, true);

    }


    private SpeechletResponse getTurnOffinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String http = "http://";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=poweroff";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now turning off the set top box";
        repromptText = "We have turned off the set top box";
        return getSpeechletResponse(speechText, repromptText, true);

    }


    private SpeechletResponse getTurnOninSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String http = "http://";
        String infoCommand = ":8080/tv/getProgInfo?major=";
        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=poweron";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now turning the set top box on";
        repromptText = "We have turned on the set top box";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuinSession(final Intent intent, final Session session)
    {

        String speechText, repromptText;
        String aSpeechText = "";
        String aRepromptText = "";
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
                if(!message.getBody().equals(""))
                {
                    ROKU_URL = message.getBody();
                    AspeechText = "I have found your roku, you can now issue roku commands";
                    ArepromptText = "I have found your roku, you can now issue roku commands";
                }
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("roku");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        if(AspeechText.equals(""))
        {
            speechText = "I was not able to find your roku, please make sure it is connected to the network";
            repromptText = "I was not able to find your roku, please make sure it is connected to the network";
            return getSpeechletResponse(speechText, repromptText, true);
        }
        else
        {
            return getSpeechletResponse(AspeechText , ArepromptText, true);
        }
    }

    private SpeechletResponse getRokuHomeinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("home");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Now tuning to roku home menu";
        repromptText = "We have tuned to the roku home menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuNetflixinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("netflix");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Now tuning to Netflix";
        repromptText = "We have tuned to netflix";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuMoveUpinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("up");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Moving one up on the Roku menu";
        repromptText = "We have moved the cursor up one on the roku menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuMoveDowninSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("down");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Moving one down on the Roku menu";
        repromptText = "We have moved the cursor down one on the roku menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuMoveLeftinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("left");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Moving left on the Roku menu";
        repromptText = "We have moved the cursor left on the roku menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuMoveRightinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("right");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Moving right on the Roku menu";
        repromptText = "We have moved the cursor right on the roku menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuSelectinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("select");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Now selecting";
        repromptText = "We have selected on the roku menu";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getRokuDirectvinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("directv");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        mConnection.disconnect();

        speechText = "Now launching the directv app";
        repromptText = "We have launched the directv app";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getPlaylistinSession(final Intent intent, final Session session)
    {

        String speechText, repromptText;
        String aSpeechText = "";
        String aRepromptText = "";
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
                    AspeechText = "The current available playlists are " + message.getBody();
                    ArepromptText = "The current available playlists are " + message.getBody();
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage("getPlaylist");
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        if(AspeechText.equals(""))
        {
            speechText = "I was not able to find your playlists, please make sure you are connected to the network";
            repromptText = "I was not able to find your playlists, please make you are connected to the network";
            return getSpeechletResponse(speechText, repromptText, true);
        }
        else
        {
            return getSpeechletResponse(AspeechText , ArepromptText, true);
        }
    }

    private SpeechletResponse getDescriptioninSession(final Intent intent, final Session session)
    {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot dvrSlot = slots.get(DVR_SLOT);
        String dvr = dvrSlot.getValue();

        String speechText, repromptText;
        String aSpeechText = "";
        String aRepromptText = "";
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
                AspeechText = "Here is the description " + message.getBody();
                ArepromptText = "Here is the description " + message.getBody();
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        if(dvr.equals("Jurassic world"))
        {
            try
            {
                chat.sendMessage("jurassic");
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }
        else if (dvr.equals("1st take"))
        {
            try
            {
                chat.sendMessage("first");
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }

        else
        {
            try
            {
                chat.sendMessage(dvr);
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        if(AspeechText.equals(""))
        {
            speechText = "I was not able to find your requested show, please make sure you said the correct name";
            repromptText = "I was not able to find your playlists, please make sure you said the correct name";
            return getSpeechletResponse(speechText, repromptText, true);
        }
        else
        {
            return getSpeechletResponse(AspeechText , ArepromptText, true);
        }
    }

    private SpeechletResponse playFromPlaylistinSession(final Intent intent, final Session session)
    {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot dvrPlaySlot = slots.get(DVRPLAY_SLOT);
        String dvr = dvrPlaySlot.getValue();

        String speechText, repromptText;
        String aSpeechText = "";
        String aRepromptText = "";
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {
            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        if(dvr.equals("Jurassic world"))
        {
            try
            {
                chat.sendMessage("jurassicPlay");
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }
        else if (dvr.equals("1st take"))
        {
            try
            {
                chat.sendMessage("firstPlay");
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }

        else
        {
            try
            {
                chat.sendMessage(dvr);
            }
            catch (SmackException.NotConnectedException e)
            {
                System.out.println("Not connected");
            }
        }

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

            speechText = "Now playing your selection";
            repromptText = "Now playing your selection";
            return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getGuideinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=guide";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now accessing the guide";
        repromptText = "We have accessed the guide";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getGuideUpinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=up";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Moving up one on the guide";
        repromptText = "We have moved up one on the guide";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getGuideDowninSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=down";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Moving down one on the guide";
        repromptText = "We have moved down one on the guide";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse getGuideSelectinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=select";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now selecting";
        repromptText = "We have selected";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse playinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=play";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now playing";
        repromptText = "We are playing";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    private SpeechletResponse pauseinSession(final Intent intent, final Session session)
    {
        String speechText, repromptText;

        String fullUrl = "http://192.168.1.134:8080/remote/processKey?key=pause";

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName("ec2-54-152-188-80.compute-1.amazonaws.com"); //"127.0.0.1" "10.0.2.2"
        config.setHost("ec2-54-152-188-80.compute-1.amazonaws.com");
        config.setPort(5222);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());

        try {
            try {
                try {
                    mConnection.connect();
                    mConnection.login("user2", "alexa1");  //also a login with no parameters //here is where issues
                } catch (XMPPException e) {
                    System.out.println("login failed");
                }
            } catch (SmackException e) {
                System.out.println("smack exception");
            }
        } catch (IOException e) {
            System.out.println("connect failed");
        }

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        ChatMessageListener chatListener = new ChatMessageListener()
        {
            @Override
            public void processMessage(Chat chat, Message message)
            {

            }
        };

        Chat chat = chatManager.createChat(userJID, "alexa", chatListener);
        try
        {
            chat.sendMessage(fullUrl);
        }

        catch (SmackException.NotConnectedException e)
        {
            System.out.println("Not connected");
        }

        System.out.println(chat.toString());

        final long start = System.nanoTime();
        while ((System.nanoTime() - start) / 1000000 < 2000) // do for 20 seconds
        {
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException e)
            {
                System.out.println("no thread sleep allowed");
            }
        }
        mConnection.disconnect();

        speechText = "Now pausing";
        repromptText = "We are paused";
        return getSpeechletResponse(speechText, repromptText, true);

    }

    //eighth function - no mod
    private SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
                                                   boolean isAskResponse)
    {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse) {
            // Create reprompt
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } else {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}