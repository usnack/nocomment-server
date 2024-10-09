package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@ToString
@Getter
public class NotionComment extends NotionData {
    private String id;
    private String discussion_id;
    private ZonedDateTime created_time;
    private ZonedDateTime last_edited_time;
    private List<RichText> rich_text;

    static class ParentBlockDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            return node.get("block_id").asText();
        }
    }

    public String getParentBlockId() {
        LinkedHashMap<String, Object> parent = (LinkedHashMap<String, Object>) getExtraProps().getOrDefault("parent", new LinkedHashMap<>());
        return Optional.ofNullable(parent.get("block_id")).map(String.class::cast).orElse("");
    }
}
