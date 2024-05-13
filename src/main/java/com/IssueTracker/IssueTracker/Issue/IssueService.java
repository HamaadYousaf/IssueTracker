package com.IssueTracker.IssueTracker.Issue;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public IssueEntity createIssue(IssueEntity issueEntity) {
        int randomID = (int) ((Math.random() * (99999 - 10000)) + 10000);

        while (issueRepository.findById(randomID).isPresent()) {
            randomID = (int) ((Math.random() * (99999 - 10000)) + 10000);
        }

        issueEntity.setId(randomID);

        return issueRepository.save(issueEntity);
    }

    public boolean issueDoesNotExists(Integer id) {
        return !issueRepository.existsById(id);
    }

    public Optional<IssueEntity> getIssueById(Integer id) {
        return issueRepository.findById(id);
    }

    public IssueEntity updateIssue(IssueEntity issueEntity, Integer id) {
        return issueRepository
                .findById(id)
                .map(
                        existingIssue -> {
                            existingIssue.setId(id);
                            Optional.ofNullable(issueEntity.getTitle())
                                    .ifPresent(existingIssue::setTitle);
                            Optional.ofNullable(issueEntity.getDescription())
                                    .ifPresent(existingIssue::setDescription);
                            Optional.ofNullable(issueEntity.getPriority())
                                    .ifPresent(existingIssue::setPriority);
                            Optional.ofNullable(issueEntity.getStatus())
                                    .ifPresent(existingIssue::setStatus);
                            Optional.ofNullable(issueEntity.getCategory())
                                    .ifPresent(existingIssue::setCategory);
                            Optional.ofNullable(issueEntity.getAssignedTo())
                                    .ifPresent(existingIssue::setAssignedTo);
                            return issueRepository.save(existingIssue);
                        })
                .orElseThrow(() -> new RuntimeException("Issue does not exist"));
    }

    public void deleteIssueById(Integer id) {
        issueRepository.deleteById(id);
    }
}
