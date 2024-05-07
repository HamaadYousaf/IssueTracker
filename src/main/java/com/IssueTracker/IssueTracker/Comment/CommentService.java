package com.IssueTracker.IssueTracker.Comment;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentEntity createComment(CommentEntity commentEntity) {
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getCommentsByIssue(String id){
        return commentRepository.getCommentsByIssueId(id);
    }
}
