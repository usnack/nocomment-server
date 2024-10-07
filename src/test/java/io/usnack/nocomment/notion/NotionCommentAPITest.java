package io.usnack.nocomment.notion;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;


@Slf4j
@ActiveProfiles("secret")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-secret.yaml")
public class NotionCommentAPITest {
    @Value("${notion.api-key}")
    private String NOTION_API_KEY;
    @Autowired
    private ObjectMapper objectMapper;
    //
    private final String NOTION_API_BASE_URL = "https://api.notion.com/v1/";
    private final String NOTION_VERSION = "2022-06-28";
    private WebClient client;
    //
    private final Path outputPath = Path.of("src", "test", "resources", "output");

    @BeforeEach
    void setClient() {
        client = WebClient.builder()
                .baseUrl(NOTION_API_BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION.toString(), NOTION_API_KEY)
                .defaultHeader("Notion-Version", NOTION_VERSION)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Test
    void findCommentByBlockIdTest() throws IOException {
        final String block_id = "110a4919eefa8056aa60d53001f72e4c";

        Mono<Map> response = client.get()
                .uri(uriBuilder -> uriBuilder.path("comments")
                        .queryParam("block_id", block_id)
                        .build())
                .httpRequest(request -> {
                    log.debug("request: {} | {}", request.getMethod(), request.getURI());
                })
                .retrieve()
                .bodyToMono(Map.class);
        objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValue(new File(outputPath.resolve("findCommentByBlockIdTest.json").toString()), response.block());

        response.subscribe(
                result -> System.out.println("Response: " + result),
                error -> System.err.println("Error: " + error.getMessage())
        );

        // WebClient는 비동기적으로 동작하므로, 메인 스레드가 종료되지 않도록 대기
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findBlockWithChildrenTest() throws IOException {
//        final String block_id = "110a4919eefa8056aa60d53001f72e4c";
        final String block_id = "110a4919-eefa-802f-baea-cebd19ceda3f";

        Mono<Map> response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("blocks", block_id, "children")
                        .build())
                .httpRequest(request -> {
                    log.debug("request: {} | {}", request.getMethod(), request.getURI());
                })
                .retrieve()
                .bodyToMono(Map.class);

        new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File("findBlockWithChildrenTest.json"), response.block());

        response.subscribe(
                result -> System.out.println("Response: " + result),
                error -> System.err.println("Error: " + error.getMessage())
        );

        // WebClient는 비동기적으로 동작하므로, 메인 스레드가 종료되지 않도록 대기
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
