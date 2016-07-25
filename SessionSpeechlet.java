package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    XMPPTCPConnection mConnection;
    ChatManager chatManager;
    String userJID = "user1@ec2-54-152-188-80.compute-1.amazonaws.com/Smack";
    String userName = "user2";
    //String message = "Hello from the other side, I am user2";
    String thread = "alexa";
    ChatMessageListener chatListener;

    //private static final String ID_KEY = "ID";
    //private static final String ID_SLOT = "id";

    //private static final String SUMMARY_KEY = "SUMMARY";
    //private static final String SUMMARY_SLOT = "summary";

    //private static final int SHOWNAME_KEY = "NAME";
    //private static final int SHOWNAME_SLOT = "name";

    //private static final int CHANNEL_KEY = "CHANNEL";
    //private static final int CHANNEL_SLOT = "channel";

    String [][] DVR_Data = { {"breaking bad", "001", "user", "how to make meth", "28"},
            {"game of thrones", "002", "system", "dragons and swords", "12"},
            {"the returned", "003", "genie", "I have no clue", "4"}
    };
    //String [] DVR_Data = {"User", "System", "Genie"};


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

        if ("GetPlaylist".equals(intentName))
        {
            //return setColorInSession(intent, session);
            return setPlaylistTypeinSession(intent, session);
        }

        else if("PlayPlaylist".equals(intentName))
        {
            return playPlaylistTypeinSession(intent, session);
        }


        else if("ChangeChannel".equals(intentName))
        {
            return changeChannelinSession(intent, session);
        }

        else if("GetVersion".equals(intentName))
        {
            return getVersioninSession(intent, session);
        }
        /*
        else if ("DeletePlayList".equals(intentName))
        {
            //return getColorFromSession(intent, session)
            ;
        }

        else if ("GetPlayListInfo".equals(intentName))
        {
            //return getColorFromSession(intent, session)
            ;
        }

        else if ("Play".equals(intentName))
        {
            //return getColorFromSession(intent, session)
            ;
        }

        else if ("Tune".equals(intentName))
        {
            //return getColorFromSession(intent, session)
            ;
        }
        */
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




    //sixth function - modified
    /**
     * Creates a {@code SpeechletResponse} for the intent and stores the extracted playlist type win the
     * Session.
     *
     * @param intent
     *            intent for the request
     * @return SpeechletResponse spoken and visual response the given intent
     */
    private SpeechletResponse setPlaylistTypeinSession(final Intent intent, final Session session)
    {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot playlistTypeSlot = slots.get(PLAYLIST_SLOT);
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if (playlistTypeSlot != null)
        {
            // Store the user's desired Playlist type in the Session and create response.
            String searchType = playlistTypeSlot.getValue();
            session.setAttribute(PLAYLIST_KEY, searchType);
            String feedback = "";

            for(int x  = 0; x < 3; x++)
            {
                if(DVR_Data[x][2].equals(searchType))
                    feedback = feedback + ", " + DVR_Data[x][0] ;
                //FIXME check this works
            }

            PLAYLIST_KEY = feedback;

            speechText =
                    String.format("The shows of the %s type that you requested are %s", searchType, feedback);
            repromptText =
                    "What type of playlist are you looking for";

        }

        else
        {
            // Render an error since we don't know what the users favorite color is.
            speechText = "I'm not sure what type of playlist you are looking for, please try again. playlist types lot was null";
            repromptText =
                    "I'm not sure what type of playlist you are looking for, please try again";
        }

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
            try
            {
                chat.sendMessage(channelNum);
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
        Map<String, Slot> slots = intent.getSlots();
        String speechText, repromptText;
        speechText =
                String.format("Getting set top box version information");
        repromptText = String.format(
                "Getting set top box version information");

        return getSpeechletResponse(speechText, repromptText, true);

    }



    //seventh function - mod
    /**
     * Creates a {@code SpeechletResponse} for the intent and get the user's favorite color from the
     * Session.
     *
     * @param intent
     *            intent for the request
     * @return SpeechletResponse spoken and visual response for the intent
     */
    private SpeechletResponse getPlaylistTypeinSession(final Intent intent, final Session session)
    {
        String speechText;
        boolean isAskResponse = false;

        // Get the user's favorite color from the session.
        String playlistType = (String) session.getAttribute(PLAYLIST_KEY);

        // Check to make sure user's favorite color is set in the session.
        if (StringUtils.isNotEmpty(playlistType)) {
            speechText = String.format("The type you searched was %s. Goodbye.", playlistType);
        } else {
            // Since the user's favorite color is not set render an error message.
            speechText =
                    "I'm not sure what playlist type you searched";
            isAskResponse = true;
        }

        return getSpeechletResponse(speechText, speechText, isAskResponse);
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