package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.usnack.nocomment.notion.service.util.CreatedByDeserializer;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@ToString
@Getter
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
}

