package xyz.fabiano.localstack.showcase.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SimpleConsumer {

    @Autowired
    private AmazonSQS sqsClient;

    @Value("${showcase.sqs.input-queue2}")
    private String inputQueue;

    @PostConstruct
    public void postConstruct() {
        try {
            inputQueue = sqsClient.createQueue(inputQueue).getQueueUrl();
        } catch (Exception e) {
            inputQueue = sqsClient.getQueueUrl(inputQueue).getQueueUrl();
        }
    }

    public String consumeMessage() {
        ReceiveMessageResult result = sqsClient.receiveMessage(inputQueue);
        return result.getMessages().get(0).getBody();
    }
}
