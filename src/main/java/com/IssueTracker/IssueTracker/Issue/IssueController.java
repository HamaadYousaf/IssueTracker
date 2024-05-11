package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Errors.IssueNotFoundException;
import com.IssueTracker.IssueTracker.Issue.Errors.MissingTitleException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = "/issues/{id}")
    public ResponseEntity<IssueEntity> getIssueById(@PathVariable("id") Integer id) {

        Optional<IssueEntity> issueById = issueService.getIssueById(id);

        if (issueById.isEmpty()) {
            throw new IssueNotFoundException();
        }

        return new ResponseEntity<>(issueById.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/issues/{id}")
    public ResponseEntity<IssueEntity> updateIssueById(
            @RequestBody IssueEntity issueEntity, @PathVariable("id") Integer id) {

        if (issueService.issueDoesNotExists(id)) {
            throw new IssueNotFoundException();
        } else {
            IssueEntity updatedIssue = issueService.updateIssue(issueEntity, id);

            return new ResponseEntity<>(updatedIssue, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/issues/{id}")
    public ResponseEntity<IssueEntity> deleteIssueById(@PathVariable("id") Integer id) {

        if (issueService.issueDoesNotExists(id)) {
            throw new IssueNotFoundException();
        } else {
            issueService.deleteIssueById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
