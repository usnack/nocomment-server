package io.usnack.nocomment.notion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.usnack.nocomment.notion.service.dto.NotionBlockChildrenResponse;
import io.usnack.nocomment.notion.service.dto.NotionCommentResponse;
import io.usnack.nocomment.notion.service.model.NotionBlock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Slf4j
@ActiveProfiles("secret")
@SpringBootTest
class NotionServiceTest {
    @Value("${notion.api-key}")
    String NOTION_API_KEY;
    @Autowired
    ObjectMapper objectMapper;
    //
    WebClient client;

    @BeforeEach
    void setUp() {
        client = WebClient.builder()
                .baseUrl("https://api.notion.com/v1/")
                .defaultHeader(HttpHeaders.AUTHORIZATION, NOTION_API_KEY)
                .defaultHeader("Notion-Version", "2022-06-28")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


    static Stream<String> blockIds() {
        return Stream.of(
                "110a4919-eefa-809d-9545-ea54320cdb64"
        );
    }

    @MethodSource("blockIds")
    @ParameterizedTest
    void findNotionBlock(String blockId) {
        log.debug("blockId: {}", blockId);
        //
        Mono<String> response = client.get()
                .uri(uri -> uri
                        .pathSegment("blocks", blockId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        Disposable subscription = response.subscribe(
                result -> {
                    NotionBlock notionBlock = null;
                    try {
                        notionBlock = objectMapper.readValue(result, NotionBlock.class);
                        log.debug("result: {}", notionBlock);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> log.error("error", error)
        );

        while(!subscription.isDisposed()) {
            try {
                log.debug("waiting for subscription");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @MethodSource("blockIds")
    @ParameterizedTest
    void findNotionBlockChildren(String blockId) {
        Mono<String> response = client.get()
                .uri(uri -> uri
                        .pathSegment("blocks", blockId, "children")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        Disposable subscription = response.subscribe(
                result -> {
                    NotionBlockChildrenResponse notionBlock = null;
                    try {
                        notionBlock = objectMapper.readValue(result, NotionBlockChildrenResponse.class);
                        log.debug("result: {}", notionBlock);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> log.error("error", error)
        );

        while(!subscription.isDisposed()) {
            try {
                log.debug("waiting for subscription");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @MethodSource("blockIds")
    @ParameterizedTest
    void isCommentUpdated(String blockId) {
        final String latestCommentId = "110a4919-eefa-8068-81e0-001d6d4becc6";

        Mono<String> response = client.get()
                .uri(uri -> uri
                        .pathSegment("comments")
                        .queryParam("block_id", blockId)
                        .queryParam("start_cursor", latestCommentId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
        Disposable subscription = response.subscribe(
                result -> {
                    log.debug("result: {}", result);
                    NotionCommentResponse apiResponse = null;
                    try {
                        apiResponse = objectMapper.readValue(result, NotionCommentResponse.class);
                        boolean isCommentUpdated = apiResponse.hasMore();
                        log.debug("result: {} / isCommentUpdated: {}", apiResponse, isCommentUpdated);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> log.error("error", error)
        );

        while(!subscription.isDisposed()) {
            try {
                log.debug("waiting for subscription");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}