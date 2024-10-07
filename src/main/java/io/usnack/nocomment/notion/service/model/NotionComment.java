package io.usnack.nocomment.notion.service.model;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class NotionComment extends NotionData {
    private String id;
    private String parentBlockId;
    private String discussion_id;
    private ZonedDateTime created_time;
    private String created_by;
    private ZonedDateTime last_edited_time;
    private RichText rich_text;

}
