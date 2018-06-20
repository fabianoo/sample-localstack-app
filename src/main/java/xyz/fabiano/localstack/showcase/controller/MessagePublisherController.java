package xyz.fabiano.localstack.showcase.controller;

import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.fabiano.localstack.showcase.sqs.IntegrationPublisher;

import java.util.Map;

@Controller
public class MessagePublisherController {

    private IntegrationPublisher publisher;

    public MessagePublisherController(IntegrationPublisher publisher) {
        this.publisher = publisher;
    }

    @RequestMapping(value = "/publish", method = RequestMethod.PUT)
    public ResponseEntity<Void> publishMessage(@RequestBody Map<String, String> body) {

        publisher.publishSimpleMessage(body.get("message"));

        return ResponseEntity.ok().build();
    }
}
