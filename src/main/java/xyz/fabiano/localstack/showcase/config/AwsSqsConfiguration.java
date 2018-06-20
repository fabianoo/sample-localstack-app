package xyz.fabiano.localstack.showcase.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsSqsConfiguration {

    @Value("${showcase.aws.region}")
    private String region;

    @Bean
    @Profile({"staging", "production"})
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard().withRegion(region).build();
    }

    @Configuration
    @Profile("local")
    public static class LocalstackConfig {

        @Value("${showcase.sqs.integration-queue}")
        private String integrationQueue;

        @Value("${showcase.sqs.endpoint}")
        private String sqsEndpoint;

        @Bean
        public AmazonSQS amazonSQS() {
            return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(sqsEndpoint, ""))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test")))
                .build();
        }
    }
}
