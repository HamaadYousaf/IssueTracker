package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Errors.MissingPropertiesException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping(path = "/issues")
    public ResponseEntity<IssueEntity> createIssue(@RequestBody IssueEntity issueEntity) {

        if (issueEntity.getTitle().isEmpty()) {
            throw new MissingPropertiesException();
        }

        IssueEntity issue = issueService.createIssue(issueEntity);

        return new ResponseEntity<>(issue, HttpStatus.CREATED);
    }
}
