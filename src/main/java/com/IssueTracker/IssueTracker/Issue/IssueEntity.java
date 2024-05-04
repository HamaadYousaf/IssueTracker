package com.IssueTracker.IssueTracker.Issue;

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
@Table(name = "issues")
public class IssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty private String title;

    private String description;

    @Column(columnDefinition="varchar(255) not null")
    private Priority priority;

    @Column(columnDefinition="varchar(255)")
    private Category category;

    @Column(columnDefinition="varchar(255) not null")
    private Status status;

    private LocalDate creationDate = LocalDate.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "created_id")
    private UserEntity createdBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_id")
    private UserEntity assignedTo;
}
