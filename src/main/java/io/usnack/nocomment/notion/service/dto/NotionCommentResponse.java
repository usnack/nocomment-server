package io.usnack.nocomment.notion.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.usnack.nocomment.notion.service.model.NotionBlock;
import io.usnack.nocomment.notion.service.model.NotionComment;

import java.util.ArrayList;
import java.util.List;

public record NotionCommentResponse(
        @JsonProperty("results")
        List<NotionComment> comments,
        @JsonProperty("next_cursor")
        String nextCursor,
        @JsonProperty("has_more")
        boolean hasMore
) { }
