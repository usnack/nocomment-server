package io.usnack.nocomment.notion.service;

import io.usnack.nocomment.notion.service.model.NotionBlock;
import io.usnack.nocomment.notion.service.dto.NotionBlockChildrenResponse;

public interface NotionService {
    NotionBlock findNotionBlock(String token, String blockId);
    NotionBlockChildrenResponse findNotionBlockChildren(String token, String parentBlockId, String startCursor);
    boolean isCommentUpdated(String token, String blockId);
}
