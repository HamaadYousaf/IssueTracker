package com.IssueTracker.IssueTracker.Comment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentEntity createComment(CommentEntity commentEntity) {
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getCommentsByIssue(Integer id) {
        return commentRepository.getCommentsByIssueId(id);
    }

    public boolean commentDoesNotExists(Long id) {
        return !commentRepository.existsById(id);
    }

    public CommentEntity updateComment(CommentEntity commentEntity, Long id) {
        return commentRepository
                .findById(id)
                .map(
                        existingComment -> {
                            existingComment.setId(id);
                            Optional.ofNullable(commentEntity.getDescription())
                                    .ifPresent(existingComment::setDescription);
                            return commentRepository.save(existingComment);
                        })
                .orElseThrow(() -> new RuntimeException("Comment does not exist"));
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }
}
