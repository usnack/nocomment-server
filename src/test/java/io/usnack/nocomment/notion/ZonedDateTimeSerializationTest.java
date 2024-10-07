package io.usnack.nocomment.notion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ZonedDateTimeSerializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testZonedDateTimeSerialization() throws Exception {
        ZonedDateTime dateTime = ZonedDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
        String json = objectMapper.writeValueAsString(dateTime);
        assertEquals("\"2023-01-01T12:00:00Z\"", json);
    }
}