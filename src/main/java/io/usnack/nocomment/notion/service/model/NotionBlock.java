package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.time.ZonedDateTime;

@ToString
@Getter
@Setter
public class NotionBlock extends NotionData {
    private String id;
    private NotionBlock parent;
    private String type;
    private ZonedDateTime created_time;
    @JsonProperty("created_by")
    @JsonDeserialize(using = CreatedByDeserializer.class)
    private String created_by;
    private ZonedDateTime last_edited_time;
    @JsonProperty("last_edited_by")
    @JsonDeserialize(using = CreatedByDeserializer.class)
    private String last_edited_by;

    static class CreatedByDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return node.get("id").asText();
        }
    }
}

