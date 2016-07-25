/**
 * Created by attlabs on 6/27/2016.
 */

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.ClientConfiguration;

public class AWS
{
    public static void main(String[] args)
    {

        //BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJISJ6VNSMLNUK4YA", "i4YN3lictpFkBni+LoNxNkqxJgK8Igds663G0biu");
        //AmazonS3 s3Client = new AmazonS3Client(awsCreds);
        AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());
        snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
        String topicArn = "arn:aws:sns:us-east-1:118062017605:JSONLambda";


        //publish to an SNS topic
        String msg = "My text published to SNS topic with email endpoint";
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        PublishResult publishResult = snsClient.publish(publishRequest);
        //print MessageId of message published to SNS topic
        System.out.println("MessageId - " + publishResult.getMessageId());
    }

}

