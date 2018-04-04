package xyz.fabiano.localstack.showcase.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsSqsConfiguration {

    @Bean
    @Profile({"staging", "production"})
    public AmazonSQS amazonSQS(@Value("${showcase.aws.region}") String region) {
        return AmazonSQSClientBuilder.standard().withRegion(region).build();
    }
}
