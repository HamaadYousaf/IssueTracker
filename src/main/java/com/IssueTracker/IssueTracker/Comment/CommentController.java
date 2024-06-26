package com.IssueTracker.IssueTracker.Comment;

import com.IssueTracker.IssueTracker.Comment.Errors.CommentNotFoundException;
import com.IssueTracker.IssueTracker.Comment.Errors.MissingDescException;
import com.IssueTracker.IssueTracker.Issue.Errors.IssueNotFoundException;
import com.IssueTracker.IssueTracker.Issue.IssueService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    private final IssueService issueService;

    public CommentController(CommentService commentService, IssueService issueService) {
        this.commentService = commentService;
        this.issueService = issueService;
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<CommentEntity> createIssue(@RequestBody CommentEntity commentEntity) {

        if (commentEntity.getDescription().isEmpty()) {
            throw new MissingDescException();
        }

        if (issueService.issueDoesNotExists(commentEntity.getIssueId())) {
            throw new IssueNotFoundException();
        }

        CommentEntity comment = commentService.createComment(commentEntity);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping(path = "/comments/{id}")
    public ResponseEntity<List<CommentEntity>> getCommentsByIssue(@PathVariable Integer id) {

        List<CommentEntity> comments = commentService.getCommentsByIssue(id);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping(path = "/comments/{id}")
    public ResponseEntity<CommentEntity> updateCommentById(
            @RequestBody CommentEntity commentEntity, @PathVariable("id") Long id) {

        if (commentService.commentDoesNotExists(id)) {
            throw new CommentNotFoundException();
        } else {
            CommentEntity updateComment = commentService.updateComment(commentEntity, id);

            return new ResponseEntity<>(updateComment, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/comments/{id}")
    public ResponseEntity<CommentEntity> deleteCommentById(@PathVariable("id") Long id) {

        if (commentService.commentDoesNotExists(id)) {
            throw new CommentNotFoundException();
        } else {
            commentService.deleteCommentById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
