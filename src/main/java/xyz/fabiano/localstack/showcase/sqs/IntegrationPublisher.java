package xyz.fabiano.localstack.showcase.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class IntegrationPublisher {

    @Autowired
    private AmazonSQS sqsClient;

    @Value("${showcase.sqs.integration-queue}")
    private String integrationQueue;

    public void publishSimpleMessage(String message) {
        sqsClient.sendMessage(integrationQueue, message);
    }

    @Bean
    @Profile("local")
    public boolean local() {
        try {
            integrationQueue = sqsClient.createQueue(integrationQueue).getQueueUrl();
        } catch (Throwable t) {
            integrationQueue = sqsClient.getQueueUrl(integrationQueue).getQueueUrl();
        }
        return true;
    }
}
