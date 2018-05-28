package xyz.fabiano.localstack.showcase.camel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xyz.fabiano.localstack.showcase.sqs.SimpleConsumer;
import xyz.fabiano.localstack.showcase.sqs.SomePublisher;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublishAndConsumeTest {

    @Autowired
    private SimpleConsumer consumer;

    @Autowired
    private SomePublisher publisher;

    @Test
    public void test() throws InterruptedException {
        String messageExpected = "test-message-007";

        try {
            String message = consumer.consumeMessage();
            System.out.println("Message: " + message);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        publisher.publishSimpleMessage(messageExpected);

        Thread.sleep(1_000);

        String message = consumer.consumeMessage();
        System.out.println("Message: " + message);

        Assert.assertEquals(messageExpected, message);
    }
}
