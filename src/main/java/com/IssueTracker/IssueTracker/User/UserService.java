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

    public UserEntity createUser(UserEntity userEntity) {
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

    public boolean userExists(Long id) {
        return !userRepository.existsById(id);
    }

    public UserEntity updateUser(UserEntity userEntity, Long id){
        return userRepository
                .findById(id)
                .map(
                        existingUser -> {
                            existingUser.setId(id);
                            Optional.ofNullable(userEntity.getUsername())
                                    .ifPresent(existingUser::setUsername);
                            Optional.ofNullable(userEntity.getEmail())
                                    .ifPresent(existingUser::setEmail);
                            Optional.ofNullable(userEntity.getPassword())
                                    .ifPresent(existingUser::setPassword);
                            return userRepository.save(existingUser);
                        })
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }
}
