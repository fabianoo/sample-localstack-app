package xyz.fabiano.localstack.showcase.camel;

import cloud.localstack.docker.annotation.IEnvironmentVariableProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleRouterTest extends CamelTestSupport {

    private static final String INPUT_QUEUE = "input-queue-test";
    private static final String OUTPUT_QUEUE = "output-queue-test";

    @Autowired
    private AmazonSQS amazonSQS;

    @Override
    @Before
    public void setUp() throws Exception {

    }

    @Override
    public RoutesBuilder createRouteBuilder() {
        return new SampleRouter(INPUT_QUEUE, OUTPUT_QUEUE, 1, amazonSQS);
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
        return amazonSQS.receiveMessage(amazonSQS.getQueueUrl(OUTPUT_QUEUE).getQueueUrl()).getMessages();
    }

    public void sendMessage(String messageFile) {
        String message = new BufferedReader(new InputStreamReader(
            this.getClass().getResourceAsStream(messageFile)))
            .lines().collect(Collectors.joining("\n"));

        SendMessageRequest request = new SendMessageRequest();
        request.setMessageBody(message);
        request.setQueueUrl(amazonSQS.getQueueUrl(INPUT_QUEUE).getQueueUrl());
        amazonSQS.sendMessage(request);
    }

    public static class OnlySQSEnvProvider implements IEnvironmentVariableProvider {
        @Override
        public Map<String, String> getEnvironmentVariables() {
            HashMap<String, String> envVars = new HashMap<>();
            envVars.put("SERVICES", "sqs");
            return envVars;
        }
    }

}