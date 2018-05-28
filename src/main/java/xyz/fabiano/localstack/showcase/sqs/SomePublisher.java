package xyz.fabiano.localstack.showcase.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SomePublisher {

    @Autowired
    private AmazonSQS sqsClient;

    @Value("${showcase.sqs.inputQueue2}")
    private String inputQueue;

    @PostConstruct
    public void postConstruct() {
        try {
            inputQueue = sqsClient.createQueue(inputQueue).getQueueUrl();
        } catch (Exception e) {
            inputQueue = sqsClient.getQueueUrl(inputQueue).getQueueUrl();
        }
    }

    public void publishSimpleMessage(String message) {
        sqsClient.sendMessage(inputQueue, message);
    }
}
