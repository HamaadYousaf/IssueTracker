package com.IssueTracker.IssueTracker.Issue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends CrudRepository<IssueEntity, Integer> {}
