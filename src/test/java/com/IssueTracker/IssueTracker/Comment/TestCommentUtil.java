package com.IssueTracker.IssueTracker.Comment;

import java.time.LocalDate;

public class TestCommentUtil {

    public TestCommentUtil() {}

    public static CommentEntity createTestComment1() {
        return CommentEntity.builder()
                .description("test desc 1")
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static CommentEntity createTestComment2() {
        return CommentEntity.builder()
                .description("test desc 2")
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static CommentEntity createTestCommentNew() {
        return CommentEntity.builder()
                .description("test desc new")
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }
}
