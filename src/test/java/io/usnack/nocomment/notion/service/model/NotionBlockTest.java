package io.usnack.nocomment.notion.service.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotionBlockTest {
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void resolveProperties() throws JsonProcessingException {
        String json = """
                {
                  "object": "block",
                  "id": "110a4919-eefa-8056-aa60-d53001f72e4c",
                  "parent": {
                    "type": "workspace",
                    "workspace": true
                  },
                  "created_time": "2024-09-29T06:18:00.000Z",
                  "last_edited_time": "2024-10-01T06:50:00.000Z",
                  "created_by": {
                    "object": "user",
                    "id": "03e2e39e-e83e-4310-a4cc-08a8c25b444a"
                  },
                  "last_edited_by": {
                    "object": "user",
                    "id": "03e2e39e-e83e-4310-a4cc-08a8c25b444a"
                  },
                  "has_children": true,
                  "archived": false,
                  "in_trash": false,
                  "type": "child_page",
                  "child_page": {
                    "title": "Comment Test"
                  },
                  "request_id": "15a565e6-9edf-4fd1-b04b-bfe029a11b78"
                }
        """;
        NotionBlock notionBlock1 = objectMapper.readValue(json, NotionBlock.class);
        JsonNode jsonNode = objectMapper.readTree(json);
        System.out.println(jsonNode);
        NotionBlock notionBlock = objectMapper

                .convertValue(jsonNode, NotionBlock.class);
        System.out.println(notionBlock);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(notionBlock));
    }
}