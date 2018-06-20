package xyz.fabiano.localstack.showcase.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class IntegrationPublisher {

    private AmazonSQS amazonSQS;
    private String integrationQueue;

    public IntegrationPublisher(AmazonSQS amazonSQS,
                                @Value("${showcase.sqs.integration-queue}") String integrationQueue) {

        this.amazonSQS = amazonSQS;
        this.integrationQueue = integrationQueue;
    }

    public void publishSimpleMessage(String message) {
        amazonSQS.sendMessage(integrationQueue, message);
    }

    @Profile("local")
    @Bean
    private boolean local() {
        integrationQueue = amazonSQS.createQueue(integrationQueue).getQueueUrl();
        return true;
    }
}
