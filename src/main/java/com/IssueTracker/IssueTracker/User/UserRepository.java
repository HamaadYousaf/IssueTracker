package com.IssueTracker.IssueTracker.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public List<UserEntity> getUsersByEmailOrUsername(String email, String username);
}
