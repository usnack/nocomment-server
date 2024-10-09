package io.usnack.nocomment.notion.config;

import io.usnack.nocomment.notion.service.exception.NotionAPIFailureException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.net.HttpRetryException;
import java.time.Duration;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@Configuration
public class NotionConfiguration {
    @Bean("notionClient")
    public WebClient notionClient(NotionConfigurationProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Notion-Version", properties.getVersion())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .build();
    }

    @Bean("notionRetrySpec")
    public Retry notionRetrySpec() {
        return RetrySpec.backoff(3, Duration.ofSeconds(1))
                .filter(err -> err instanceof HttpRetryException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new NotionAPIFailureException("Notion API failed after 3 attempts.", retrySignal.failure());
                });
    }
}
