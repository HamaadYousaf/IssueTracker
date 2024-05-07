package com.IssueTracker.IssueTracker.Comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<CommentEntity> createIssue(@RequestBody CommentEntity commentEntity) {

        CommentEntity comment = commentService.createComment(commentEntity);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping(path = "/comments/{id}")
    public ResponseEntity<List<CommentEntity>> getCommentsByIssue(@PathVariable String id) {

        List<CommentEntity> comment = commentService.getCommentsByIssue(id);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
}
