package com.IssueTracker.IssueTracker.User;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistsException extends RuntimeException {

    public UserExistsException() {
        super("Username or Email already exists");
    }
}
