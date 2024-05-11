package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
import com.IssueTracker.IssueTracker.User.UserEntity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "issues")
public class IssueEntity {

    @Id private Integer id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category = Category.OTHER;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.NEW;

    @Column(nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "created_id", nullable = false)
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_id")
    private UserEntity assignedTo;
}
