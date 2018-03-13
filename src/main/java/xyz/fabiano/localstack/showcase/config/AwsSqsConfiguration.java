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

    @Bean
    @Profile({"staging", "production"})
    public AmazonSQS amazonSQS(@Value("${showcase.aws.region}") String region) {
        return AmazonSQSClientBuilder.standard().withRegion(region).build();
    }

    @Bean
    @Profile({"local"})
    public AmazonSQS localstackSQS(@Value("${showcase.aws.region}") String region) {
        return AmazonSQSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("test", "test")))
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", region))
            .build();
    }
}
