package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
import com.IssueTracker.IssueTracker.Issue.Errors.IssueNotFoundException;
import com.IssueTracker.IssueTracker.Issue.Errors.MissingTitleException;
import com.IssueTracker.IssueTracker.User.UserEntity;
import com.IssueTracker.IssueTracker.User.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class IssueController {

    private final IssueService issueService;

    private final UserService userService;

    private final IssueSearchDao issueSearchDao;

    public IssueController(
            IssueService issueService, UserService userService, IssueSearchDao issueSearchDao) {
        this.issueService = issueService;
        this.userService = userService;
        this.issueSearchDao = issueSearchDao;
    }

    @GetMapping(path = "/issues")
    public ResponseEntity<List<IssueEntity>> getAllIssues(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) String assignedTo) {

        UserEntity createdByUser = null;
        UserEntity assignedToUser = null;

        if (createdBy != null && !createdBy.isBlank()) {
            createdByUser = userService.getUserByUsername(createdBy);
        }

        if (assignedTo != null && !assignedTo.isBlank()) {
            assignedToUser = userService.getUserByUsername(assignedTo);
        }

        List<IssueEntity> allIssues =
                issueSearchDao.searchByFilters(
                        priority, category, status, createdByUser, assignedToUser);

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
