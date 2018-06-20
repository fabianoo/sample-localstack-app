package xyz.fabiano.localstack.showcase.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class MessagePublisherController {

    private AmazonSQS amazonSQS;
    private String integrationQueue;

    public MessagePublisherController(AmazonSQS amazonSQS,
                                      @Value("${showcase.sqs.integration-queue}") String integrationQueue) {
        this.amazonSQS = amazonSQS;
        this.integrationQueue = integrationQueue;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.PUT)
    public ResponseEntity<Void> publishMessage(@RequestBody Map<String, String> body) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setMessageBody(body.get("message"));
        sendMessageRequest.setQueueUrl(integrationQueue);

        amazonSQS.sendMessage(sendMessageRequest);

        return ResponseEntity.ok().build();
    }
}
