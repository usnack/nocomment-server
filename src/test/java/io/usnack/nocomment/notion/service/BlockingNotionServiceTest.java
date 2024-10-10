package io.usnack.nocomment.notion.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.usnack.nocomment.notion.repository.NotionBlockMetaRepository;
import io.usnack.nocomment.notion.repository.entity.NotionBlockMeta;
import io.usnack.nocomment.notion.service.dto.NotionBlockChildrenResponse;
import io.usnack.nocomment.notion.service.exception.NotionAPIFailureException;
import io.usnack.nocomment.notion.service.model.NotionBlock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.Yaml;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpRetryException;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BlockingNotionServiceTest {
    private static String NOTION_API_KEY;
    private final static String NOTION_BASE_URL = "https://api.notion.com/v1/";
    private final static String NOTION_VERSION = "2022-06-28";
    @Spy
    private WebClient notionClient = WebClient.builder()
            .baseUrl(NOTION_BASE_URL)
            .defaultHeader("Notion-Version", NOTION_VERSION)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
            .build();
    @Spy
    private Retry notionRetrySpec = RetrySpec.backoff(3, Duration.ofSeconds(1))
            .filter(err -> err instanceof HttpRetryException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                throw new NotionAPIFailureException("Notion API failed after 3 attempts.", retrySignal.failure());
            });
    @Mock
    private NotionBlockMetaRepository notionBlockMetaRepository;
    @InjectMocks
    private BlockingNotionService underTest;

    @BeforeAll
    static void setUp() {
        loadSecretProperties();
        setupLogging();
    }

    private static void loadSecretProperties() {
        Yaml yaml = new Yaml();
        Map<String, Object> properties = null;
        try {
            properties = yaml.load(new FileInputStream(ResourceUtils.getFile("classpath:application-secret.yaml")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        NOTION_API_KEY = (String) ((Map<String, Object>) properties.get("notion")).get("api-key");
    }

    private static void setupLogging() {
        // SLF4J를 통해 Logback의 루트 로거를 가져옵니다.
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);
        Logger appLogger = (Logger) LoggerFactory.getLogger("io.usnack.nocomment");
        appLogger.setLevel(Level.DEBUG);
    }


    @Nested
    class WhenFindingNotionBlock {
        private final String BLOCK_ID = "110a4919-eefa-809d-9545-ea54320cdb64";

        @BeforeEach
        void setup() {
        }

        @Test
        void baseTest(){
            NotionBlock notionBlock = underTest.findNotionBlock(NOTION_API_KEY, BLOCK_ID);
            assertThat(notionBlock)
                    .hasFieldOrPropertyWithValue("id", BLOCK_ID)
                    .isNotNull();
        }
    }

    @Nested
    class WhenFindingNotionBlockChildren {
        private final String PARENT_BLOCK_ID = "110a4919-eefa-809d-9545-ea54320cdb64";
        private final String START_CURSOR = "";

        @BeforeEach
        void setup() {
        }

        @Test
        void baseTest(){
            NotionBlockChildrenResponse notionBlockChildren = underTest.findNotionBlockChildren(NOTION_API_KEY, PARENT_BLOCK_ID, START_CURSOR);
            log.debug("result : {}", notionBlockChildren);
            assertThat(notionBlockChildren)
                    .isNotNull();
        }
    }

    @Nested
    class WhenCheckingIfIsCommentUpdated {
        private final String BLOCK_ID = "110a4919-eefa-809d-9545-ea54320cdb64";
        private final String LATEST_COMMENT_ID = "110a4919-eefa-8068-81e0-001d6d4becc6";

        @BeforeEach
        void setup() {
        }

        @Test
        void baseTest(){
            Mockito.when(notionBlockMetaRepository.findById(BLOCK_ID))
                    .thenReturn(Optional.of(new NotionBlockMeta(BLOCK_ID, LATEST_COMMENT_ID)));
            boolean commentUpdated = underTest.isCommentUpdated(NOTION_API_KEY, BLOCK_ID);
            log.debug("comment Updated : {}", commentUpdated);
        }
    }
}