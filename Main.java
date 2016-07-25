
 import org.json.simple.JSONObject;

 import java.io.IOException;
 import java.io.StringReader;
 import java.io.StringWriter;
 import java.util.HashMap;
 import java.util.Map;
 import javax.json.Json;
 import javax.json.JsonBuilderFactory;
 import javax.json.JsonObject;
 import javax.json.stream.JsonParser;

 /**
 * Created by attlabs on 6/20/2016;
 */
public class Main {

    public static void main(String[] args) throws IOException
    {

        //this is the lambda response
        String sampleJSONResponse =  "{\n" +
                "  \"version\": \"1.0\",\n" +
                "  \"response\": {\n" +
                "    \"outputSpeech\": {\n" +
                "      \"type\": \"PlainText\",\n" +
                "      \"text\": \"Changing channel to channel 100\"\n" +
                "    },\n" +
                "    \"card\": {\n" +
                "      \"content\": \"Changing channel to channel 100\",\n" +
                "      \"title\": \"Session\",\n" +
                "      \"type\": \"Simple\"\n" +
                "    },\n" +
                "    \"reprompt\": {\n" +
                "      \"outputSpeech\": {\n" +
                "        \"type\": \"PlainText\",\n" +
                "        \"text\": \"We are now on the channel 100\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"shouldEndSession\": false\n" +
                "  },\n" +
                "  \"sessionAttributes\": {}\n" +
                "}";

        System.out.println(sampleJSONResponse);
        System.out.println("");


        JsonBuilderFactory amazon = Json.createBuilderFactory(null);
        JsonObject value2 = amazon.createObjectBuilder()
                .add("Version", "1.0")
                .add("response", amazon.createObjectBuilder()
                        .add("outputSpeech", amazon.createObjectBuilder()
                        .add("type", "PlainText")
                        .add("text", "Changing channel to channel 100"))
                .add("card", amazon.createObjectBuilder()
                                .add("Content", "Changing channel to channel 100")
                                .add("title", "Session")
                                .add("type", "Simple"))
                .add("reprompt", amazon.createObjectBuilder()
                         .add("type", "PlainText")
                         .add ("text", "We are now on the channel 100"))
                .add("shouldEndSession", "false"))
                .add("sessionAttributes", amazon.createArrayBuilder())
                .build();

        System.out.println(value2);


        JsonParser parser = Json.createParser(new StringReader(value2.toString()));
        JsonParser.Event event = parser.next();
        System.out.println(event);
        if(event.toString().equals("START_OBJECT"))
            System.out.println("Start object confirmed");
        event = parser.next();
        System.out.println(event);
        if(event.toString().equals("KEY_NAME"))
            System.out.println("Key object confirmed");
        System.out.println(parser.getString());
        event = parser.next();
        System.out.println(event);
        System.out.println(parser.getString());
        System.out.println("");


/*
        String sampleJSONRequest = {
            "session": {
            "sessionId": "SessionId.60b16767-c0e9-45bd-8e69-34c1b2a67a54",
                    "application": {
                "applicationId": "amzn1.echo-sdk-ams.app.d62bf22a-d406-4935-a4b6-231a9ea7c697"
            },
            "attributes": {},
            "user": {
                "userId": "amzn1.ask.account.AFP3ZWPOS2BGJR7OWJZ3DHPKMOMNWY4AY66FUR7ILBWANIHQN73QGSDKCHPDSZMN2ICGKFOB76GOHB2MBT5P726IY22TMAG5GORGEVDMLQV7MP46ZLPKS2ZAU3HFI6BHPD5B7PGIUXC562HQLUETHHR5KO6AFBBT33JN6HI7DE45JRFRZTWBJTMDMXDNIYRZW2DJH3ZBJXBD55A"
            },
            "new": false
        },
            "request": {
            "type": "IntentRequest",
                    "requestId": "EdwRequestId.e4126ffd-22cc-4d4b-88b6-fd9480ae8900",
                    "timestamp": "2016-06-21T20:12:23Z",
                    "intent": {
                "name": "ChangeChannel",
                        "slots": {
                    "Channel": {
                        "name": "Channel",
                                "value": "100"
                    }
                }
            },
            "locale": "en-US"
        },
            "version": "1.0"
        ""};    */

        //this is the one you want

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
                        .add("intent",  response.createObjectBuilder()
                                .add("name", "ChannelChange")
                                .add("slots", response.createObjectBuilder()
                                        .add("Channel", response.createObjectBuilder()
                                                .add("name", "Channel")
                                                .add("value", "100")
                                            )
                                    )
                            )
                        .add("locale", "en-US")
                     )
                .add("version", "1.0")
                .build();

        System.out.println(value3);


        /*
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        System.out.println(parser3.getString());
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
       // System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());
        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
//        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());

        System.out.println(channelAns);
        channelAns = parser3.next();
        System.out.println(parser3.getString());
        System.out.println(channelAns);
    */




        JsonParser parser3 = Json.createParser(new StringReader(value3.toString()));
        JsonParser.Event channelAns = parser3.next();

        System.out.println("");
        String channel = "";
        String name = "";
        String test = "";

       while(parser3.hasNext() == true)
       {
           if(channelAns.toString().equals("KEY_NAME"))
           {
               name = parser3.getString();
               System.out.println("Name is being set: " + name);
               if(name.equals("value"))
               {
                   channelAns = parser3.next();
                   channel = parser3.getString();
                   break;
               }

               channelAns = parser3.next();

           }

           else
           {
               channelAns = parser3.next();
               continue;
           }
       }

        System.out.println("This is channel: " + channel);
        System.out.println("This is name: " + name);
        //System.out.println(parser3.getString());




    }
}


 /*{
  "version": "1.0",
  "response": {
    "outputSpeech": {
      "type": "PlainText",
      "text": "Changing channel to channel 100"
    },
    "card": {
      "content": "Changing channel to channel 100",
      "title": "Session",
      "type": "Simple"
    },
    "reprompt": {
      "outputSpeech": {
        "type": "PlainText",
        "text": "We are now on the channel 100"
      }
    },
    "shouldEndSession": false
  },
  "sessionAttributes": {}
}


        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObject value = factory.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", factory.createObjectBuilder()
                        .add("streetAddress", "21 2nd Street")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
                .add("phoneNumber", factory.createArrayBuilder()
                        .add(factory.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "212 555-1234"))
                        .add(factory.createObjectBuilder()
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();

        System.out.println(value);
        System.out.println("");
 */