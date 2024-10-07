package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.Deserializers;
import io.usnack.nocomment.notion.service.util.CreatedByDeserializer;
import lombok.Getter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotionComment extends NotionData {
    private String id;
    @JsonProperty("parent")
    @JsonDeserialize(using = ParentBlockDeserializer.class)
    private String parentBlockId;
    private String discussion_id;
    private ZonedDateTime created_time;
    @JsonProperty("created_by")
    @JsonDeserialize(using = CreatedByDeserializer.class)
    private String created_by;
    private ZonedDateTime last_edited_time;
    private List<RichText> rich_text;

    static class ParentBlockDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return node.get("block_id").asText();
        }
    }
}
