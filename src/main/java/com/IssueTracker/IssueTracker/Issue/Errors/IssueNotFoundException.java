package com.IssueTracker.IssueTracker.Issue.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException() {
        super("Issue not found");
    }
}
