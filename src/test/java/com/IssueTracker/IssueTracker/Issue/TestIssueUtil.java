package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
import com.IssueTracker.IssueTracker.User.UserEntity;

import java.time.LocalDate;

public class TestIssueUtil {

    public TestIssueUtil() {}

    public static IssueEntity createIssue1(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title 1")
                .description("test description 1")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.HIGH)
                .category(Category.BUG)
                .status(Status.ASSIGNED)
                .creationDate(LocalDate.of(2020, 1, 1))
                .build();
    }

    public static IssueEntity createIssue2(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title 2")
                .description("test description 2")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.LOW)
                .category(Category.REFACTOR)
                .status(Status.RESOLVED)
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static IssueEntity createIssue3(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title")
                .description("test description")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.NORMAL)
                .category(Category.FEATURE)
                .status(Status.NEW)
                .creationDate(LocalDate.of(2020, 1, 1))
                .build();
    }

    public static IssueEntity createIssue4(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title")
                .description("test description")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.NORMAL)
                .category(Category.FEATURE)
                .status(Status.NEW)
                .creationDate(LocalDate.of(2021, 1, 1))
                .build();
    }

    public static IssueEntity createIssue5(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title")
                .description("test description")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.NORMAL)
                .category(Category.FEATURE)
                .status(Status.NEW)
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }

    public static IssueEntity createIssueNew(UserEntity createdBy, UserEntity assignedTo) {
        return IssueEntity.builder()
                .title("test title new")
                .description("test description new")
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .priority(Priority.NORMAL)
                .category(Category.FEATURE)
                .status(Status.ASSIGNED)
                .creationDate(LocalDate.of(2022, 1, 1))
                .build();
    }
}
