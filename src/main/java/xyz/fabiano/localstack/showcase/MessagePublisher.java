package xyz.fabiano.localstack.showcase;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class MessagePublisher {

    public static void main(String[] args) {
        AmazonSQS sqsClient = AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("test", "test")))
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", "sa-east-1"))
            .build();

        sqsClient.sendMessage("http://localhost:4576/queue/INPUT_QUEUE", "{\n" +
            "  \"messageId\": \"606060\",\n" +
            "  \"title\": \"Localstack Showcase Online\",\n" +
            "  \"description\": \"This is a simple example of Localstack\",\n" +
            "  \"online\": true\n" +
            "}");
    }
}
