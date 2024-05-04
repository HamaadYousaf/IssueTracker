package com.IssueTracker.IssueTracker.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("id") Long id) {

        Optional<UserEntity> userById = userService.getUserById(id);

        if (userById.isEmpty()) {
            throw new UserNotFoundException();
        }

        return new ResponseEntity<>(userById.get(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<UserEntity> deleteUserById(@PathVariable("id") Long id) {

        if (!userService.userExists(id)) {
            throw new UserNotFoundException();
        }

        userService.deleteUserById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
