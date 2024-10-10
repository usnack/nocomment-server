package io.usnack.nocomment.notion.service;

import io.usnack.nocomment.notion.repository.NotionBlockMetaRepository;
import io.usnack.nocomment.notion.repository.entity.NotionBlockMeta;
import io.usnack.nocomment.notion.service.dto.NotionBlockChildrenResponse;
import io.usnack.nocomment.notion.service.dto.NotionCommentResponse;
import io.usnack.nocomment.notion.service.model.NotionBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.HttpRetryException;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.function.Function;

@Slf4j
@Service
public class BlockingNotionService implements NotionService {
    private final WebClient notionClient;
    private final Retry notionRetrySpec;
    private final NotionBlockMetaRepository notionBlockMetaRepository;

    public BlockingNotionService(
            @Qualifier("notionClient") WebClient notionClient,
            @Qualifier("notionRetrySpec") Retry notionRetrySpec,
            NotionBlockMetaRepository notionBlockMetaRepository
    ) {
        this.notionClient = notionClient;
        this.notionRetrySpec = notionRetrySpec;
        this.notionBlockMetaRepository = notionBlockMetaRepository;
    }

    @Override
    public NotionBlock findNotionBlock(String notionApiKey, String blockId) {
        NotionBlock notionBlock = callNotionGetApi(
                notionApiKey,
                uri -> uri
                        .pathSegment("blocks", blockId)
                        .build(),
                NotionBlock.class
        );
        return notionBlock;
    }

    @Override
    public NotionBlockChildrenResponse findNotionBlockChildren(String notionApiKey, String parentBlockId, String startCursor) {
        NotionBlockChildrenResponse response = callNotionGetApi(
                notionApiKey,
                uri -> uri
                        .pathSegment("blocks", parentBlockId, "children")
                        .build(),
                NotionBlockChildrenResponse.class
        );
        return response;
    }

    @Override
    public boolean isCommentUpdated(String notionApiKey, String blockId) {
        String latestCommentId = findLatestCommentId(blockId);
        NotionCommentResponse response = callNotionGetApi(
                notionApiKey,
                uri -> uri
                        .pathSegment("comments")
                        .queryParam("block_id", blockId)
                        .queryParam("start_cursor", latestCommentId)
                        .queryParam("page_size", 1)
                        .build(),
                NotionCommentResponse.class
        );
        return !response.hasMore();
    }

    private String findLatestCommentId(String blockId) {
        return notionBlockMetaRepository.findById(blockId)
                .map(NotionBlockMeta::getLatestCommentId)
                .orElseThrow(() -> new NoSuchElementException("No block found with id: " + blockId));
    }

    private <T> T callNotionGetApi(String notionApiKey, Function<UriBuilder, URI> uriBuildFunction, Class<T> returnType) {
        return notionClient.get()
                .uri(uriBuildFunction)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + notionApiKey)
                .retrieve()
                .onStatus(
                        status -> status.value() == 429,
                        res -> {
                            HttpRetryException rateLimitedException = new HttpRetryException("Rate limited", res.statusCode().value());
                            log.warn("rate limit..", rateLimitedException);
                            return Mono.error(rateLimitedException);    
                        }

                )
                .bodyToMono(returnType)
                .retryWhen(notionRetrySpec)
                .block();
    }
}
