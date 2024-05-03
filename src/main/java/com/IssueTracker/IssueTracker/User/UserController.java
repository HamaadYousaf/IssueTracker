package com.IssueTracker.IssueTracker.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        List<UserEntity> emailOrUsernameExists =
                userService.getUsersByEmailOrUsername(
                        userEntity.getEmail(), userEntity.getUsername());

        if (!emailOrUsernameExists.isEmpty()) {
            throw new UserExistsException();
        }

        UserEntity createdUser = userService.saveUser(userEntity);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserEntity>> getUsers() {

        List<UserEntity> allUsers = userService.getUsers();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
}
