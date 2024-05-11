package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Errors.MissingTitleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping(path = "/issues")
    public ResponseEntity<List<IssueEntity>> getAllIssues() {

        List<IssueEntity> allIssues = issueService.getAllIssues();

        return new ResponseEntity<>(allIssues, HttpStatus.OK);
    }

    @PostMapping(path = "/issues")
    public ResponseEntity<IssueEntity> createIssue(@RequestBody IssueEntity issueEntity) {

        if (issueEntity.getTitle().isEmpty()) {
            throw new MissingTitleException();
        }

        IssueEntity issue = issueService.createIssue(issueEntity);

        return new ResponseEntity<>(issue, HttpStatus.CREATED);
    }
}
