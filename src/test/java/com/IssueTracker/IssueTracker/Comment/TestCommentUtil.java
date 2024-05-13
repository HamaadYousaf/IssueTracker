package com.IssueTracker.IssueTracker.Comment;

import com.IssueTracker.IssueTracker.User.UserEntity;

import java.time.LocalDate;

public class TestCommentUtil {

    public TestCommentUtil() {}

    public static CommentEntity createTestComment1(Integer issueId, UserEntity createdBy) {
        return CommentEntity.builder()
                .description("test desc 1")
                .creationDate(LocalDate.of(2022, 1, 1))
                .issueId(issueId)
                .user(createdBy)
                .build();
    }

    public static CommentEntity createTestComment2(Integer issueId, UserEntity createdBy) {
        return CommentEntity.builder()
                .description("test desc 2")
                .creationDate(LocalDate.of(2022, 1, 1))
                .issueId(issueId)
                .user(createdBy)
                .build();
    }

    public static CommentEntity createTestCommentNew(Integer issueId, UserEntity createdBy) {
        return CommentEntity.builder()
                .description("test desc new")
                .creationDate(LocalDate.of(2022, 1, 1))
                .issueId(issueId)
                .user(createdBy)
                .build();
    }
}
