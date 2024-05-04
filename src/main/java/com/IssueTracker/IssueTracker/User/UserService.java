package com.IssueTracker.IssueTracker.User;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<UserEntity> getUsersByEmailOrUsername(String email, String username) {
        return new ArrayList<>(userRepository.getUsersByEmailOrUsername(email, username));
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean userExists(Long id){
        return userRepository.existsById(id);
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
