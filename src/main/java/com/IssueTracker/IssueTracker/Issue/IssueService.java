package com.IssueTracker.IssueTracker.Issue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public List<IssueEntity> getAllIssues() {
        return StreamSupport.stream(issueRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
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
}
