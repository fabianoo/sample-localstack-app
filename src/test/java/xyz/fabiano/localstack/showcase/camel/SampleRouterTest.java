package xyz.fabiano.localstack.showcase.camel;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleRouterTest extends CamelTestSupport {

    @Value("${showcase.sqs.input-queue}")
    private String inputQueue;
    
    @Value("${showcase.sqs.output-queue}")
    private String outputQueue;

    @Autowired
    private AmazonSQS amazonSQS;
    private String inputQueueUrl;
    private String outputQueueUrl;

    @Override
    @Before
    public void setUp() throws Exception {
        inputQueueUrl = amazonSQS.createQueue(inputQueue).getQueueUrl();
        outputQueueUrl = amazonSQS.createQueue(outputQueue).getQueueUrl();
    }

    @Override
    public RoutesBuilder createRouteBuilder() {
        return new SampleRouter(inputQueueUrl, outputQueueUrl, 1, amazonSQS);
    }

    @Test
    public void testOnlineMessage() throws InterruptedException {
        sendMessage("/online-message.json");

        List<Message> messages = receiveMessageFromOutputQueue();

        assertTrue(messages.size() > 0);
    }

    @Test
    public void testOfflineMessage() throws InterruptedException {

        sendMessage("/offline-message.json");

        List<Message> messages = receiveMessageFromOutputQueue();

        assertTrue(messages.size() == 0);
    }

    /*
     * We should implement async helpers and expectations with timeouts
     */
    private List<Message> receiveMessageFromOutputQueue() {
        try {
            Thread.sleep(2_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return amazonSQS.receiveMessage(outputQueueUrl).getMessages();
    }

    public void sendMessage(String messageFile) {
        String message = new BufferedReader(new InputStreamReader(
            this.getClass().getResourceAsStream(messageFile)))
            .lines().collect(Collectors.joining("\n"));

        SendMessageRequest request = new SendMessageRequest();
        request.setMessageBody(message);
        request.setQueueUrl(inputQueueUrl);
        amazonSQS.sendMessage(request);
    }
}