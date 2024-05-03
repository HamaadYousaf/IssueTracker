package com.IssueTracker.IssueTracker.Comment;

import com.IssueTracker.IssueTracker.Issue.IssueEntity;
import com.IssueTracker.IssueTracker.User.UserEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

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
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty private String description;

    private LocalDate creationDate = LocalDate.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "issue_id")
    private IssueEntity issue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
