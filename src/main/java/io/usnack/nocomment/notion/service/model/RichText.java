package io.usnack.nocomment.notion.service.model;

import lombok.Getter;

@Getter
public class RichText extends NotionData {
    private RichTextType type;
    private String plain_text;
    private String href;
}
