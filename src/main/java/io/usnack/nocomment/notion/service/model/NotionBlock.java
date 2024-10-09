package io.usnack.nocomment.notion.service.model;

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
    private ZonedDateTime last_edited_time;
}

