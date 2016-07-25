import java.util.Date;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;

// Example SNS Sender
public class AmazonSNSSender {

    // AWS credentials -- replace with your credentials
    static String ACCESS_KEY = "AKIAIOYJC5Y56OSAKEGQ";
    static String SECRET_KEY = "Un5ZopwI5e/Wm0wE8P65w4iQ2oB+OQXX/PLU3/Jf";

    // Sender loop
    public static void main(String... args) throws Exception {

        // Create a client
        AmazonSNSClient service = new AmazonSNSClient(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));

        // Create a topic
        String topicArn = "arn:aws:sns:us-east-1:118062017605:JSONLambda";

        //CreateTopicRequest createReq = new CreateTopicRequest()
        //    .withName("MyTopic");
        //CreateTopicResult createRes = service.createTopic(createReq);

        for (;;) {

            // Publish to a topic
            PublishRequest publishReq = new PublishRequest().withTopicArn(topicArn).withMessage("Example notification sent at " + new Date());
            //PublishRequest publishReq = new PublishRequest().withTopicArn(createRes.getTopicArn()).withMessage("Example notification sent at " + new Date());
            service.publish(publishReq);

            Thread.sleep(1000);
        }
    }
}
