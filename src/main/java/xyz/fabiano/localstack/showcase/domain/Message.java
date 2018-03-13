package xyz.fabiano.localstack.showcase.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Message {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

    private String title;

    private String description;

    private boolean shouldBePublished;

    public static Message fromStream(String message) throws IOException {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(message);
        String title = jsonNode.get("title").asText();
        String description = jsonNode.get("description").asText();
        Boolean online = jsonNode.get("online").asBoolean();

        return new Message(title, description, online);
    }
}
