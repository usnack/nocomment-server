package io.usnack.nocomment.notion.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
public class NotionBlockMeta {
    @Id
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private Long id;
    private String latestCommentId;
}
