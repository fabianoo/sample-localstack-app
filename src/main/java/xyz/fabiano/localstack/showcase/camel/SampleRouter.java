package xyz.fabiano.localstack.showcase.camel;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.sqs.SqsComponent;
import org.apache.camel.component.aws.sqs.SqsConfiguration;
import org.apache.camel.component.aws.sqs.SqsEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fabiano.localstack.showcase.domain.Message;

import static org.apache.camel.LoggingLevel.INFO;

@Slf4j
@Component
public class SampleRouter extends RouteBuilder {

    private final String inputQueue;
    private final String outputQueue;
    private Integer concurrentConsumers;
    private AmazonSQS amazonSQS;

    public SampleRouter(
        @Value("${showcase.sqs.inputQueue}") String inputQueue,
        @Value("${showcase.sqs.outputQueue}") String outputQueue,
        @Value("${showcase.sqs.concurrentConsumers}") Integer concurrentConsumers,
        AmazonSQS amazonSQS) {

        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.concurrentConsumers = concurrentConsumers;
        this.amazonSQS = amazonSQS;
    }

    @Override
    public void configure() {
        from(sqs(inputQueue))
            .autoStartup(true)
            .routeId("Queue Reader")
            .log(INFO, log, "New Message: ${body}")
            .to("direct:showcase.message.parser");


        from("direct:showcase.message.parser")
            .routeId("Message Parser")
            .transform(method(Message.class, "fromStream"))
            .log(INFO, log, "Title: ${body.title}")
            .log(INFO, log, "This message should be published: ${body.shouldBePublished}")
            .filter(simple("${body.shouldBePublished}"))
            .transform(simple("${body.description}"))
            .log(INFO, "Message: (${body})")
            .to("direct:showcase.message.publisher");

        from("direct:showcase.message.publisher")
            .routeId("Message Publisher")
            .log(INFO, log,"Output Message: ${body}")
            .to(sqs(outputQueue));
    }

    private Endpoint sqs(String queueName) {
        SqsConfiguration config = new SqsConfiguration();

        config.setAmazonSQSClient(amazonSQS);
        config.setQueueName(queueName);
        config.setConcurrentConsumers(concurrentConsumers);
        config.setDeleteAfterRead(true);
        config.setDeleteIfFiltered(true);

        SqsComponent component = getContext().getComponent("aws-sqs", SqsComponent.class);

        return new SqsEndpoint(String.format("aws-sqs://%s", inputQueue), component, config);
    }
}
