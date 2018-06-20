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
    private AmazonSQS amazonSQS;

    @Value("${showcase.sqs.integration-queue}")
    private String integrationQueue;

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
