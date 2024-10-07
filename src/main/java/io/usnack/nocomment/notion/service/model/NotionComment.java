package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.usnack.nocomment.notion.service.util.CreatedByDeserializer;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class NotionComment extends NotionData {
    private String id;
    private String parentBlockId;
    private String discussion_id;
    private ZonedDateTime created_time;
    @JsonProperty("created_by")
    @JsonDeserialize(using = CreatedByDeserializer.class)
    private String created_by;
    private ZonedDateTime last_edited_time;
    private RichText rich_text;
}
