package com.IssueTracker.IssueTracker.Issue.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingTitleException extends RuntimeException {

    public MissingTitleException() {
        super("Title required");
    }
}
