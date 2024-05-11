package com.IssueTracker.IssueTracker.Comment.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingDescException extends RuntimeException {

    public MissingDescException() {
        super("Title required");
    }
}
