package io.usnack.nocomment.notion.service.exception;

public class NotionAPIFailureException extends RuntimeException {
    public NotionAPIFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
