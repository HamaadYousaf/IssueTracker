package com.IssueTracker.IssueTracker.Issue;

import org.springframework.stereotype.Service;

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
}
