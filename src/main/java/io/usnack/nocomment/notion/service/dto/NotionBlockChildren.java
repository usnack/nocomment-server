package io.usnack.nocomment.notion.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.usnack.nocomment.notion.service.model.NotionBlock;

import java.util.List;

public record NotionBlockChildren(
        @JsonProperty("results")
        List<NotionBlock> children,
        @JsonProperty("next_cursor")
        String nextCursor,
        @JsonProperty("has_more")
        boolean hasMore
) { }
