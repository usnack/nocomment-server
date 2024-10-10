package io.usnack.nocomment.notion.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotionBlockMeta {
    @Id
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    private String id;
    private String latestCommentId;
}
