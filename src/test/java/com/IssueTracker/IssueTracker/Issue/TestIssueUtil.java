package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
import com.IssueTracker.IssueTracker.User.UserEntity;

import java.time.LocalDate;

public class TestIssueUtil {

    public TestIssueUtil() {}

    public static IssueEntity createIssue1() {
        return IssueEntity.builder()
                .id(12345)
                .title("test title 1")
                .description("test description 1")
                .priority(Priority.HIGH)
                .category(Category.BUG)
                .status(Status.ASSIGNED)
                .creationDate(LocalDate.of(2020, 1, 1))
                .build();
    }

    public static IssueEntity createIssue2() {
        return IssueEntity.builder()
                .id(12345)
                .title("test title 2")
                .description("test description 2")
                .priority(Priority.LOW)
                .category(Category.REFACTOR)
                .status(Status.RESOLVED)
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static IssueEntity createIssueNew() {
        return IssueEntity.builder()
                .id(12345)
                .title("test title new")
                .description("test description new")
                .priority(Priority.NORMAL)
                .category(Category.FEATURE)
                .status(Status.ASSIGNED)
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static UserEntity createdUser() {
        return UserEntity.builder()
                .username("test name create")
                .email("testcreate@email.com")
                .password("123123")
                .build();
    }

    public static UserEntity assignedUser() {
        return UserEntity.builder()
                .username("test name assigned")
                .email("testassigned@email.com")
                .password("123123")
                .build();
    }
}
