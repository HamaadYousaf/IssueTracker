package com.IssueTracker.IssueTracker.Issue;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
import com.IssueTracker.IssueTracker.User.UserEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class IssueSearchDao {

    private final EntityManager em;

    public IssueSearchDao(EntityManager em) {
        this.em = em;
    }

    public List<IssueEntity> searchByFilters(
            Priority priority,
            Category category,
            Status status,
            UserEntity createdBy,
            UserEntity assignedTo,
            String dateOrder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<IssueEntity> cq = cb.createQuery(IssueEntity.class);
        Root<IssueEntity> root = cq.from(IssueEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (priority != null) {
            Predicate priorityPredicate = cb.equal(root.get("priority"), priority);
            predicates.add(priorityPredicate);
        }

        if (category != null) {
            Predicate categoryPredicate = cb.equal(root.get("category"), category);
            predicates.add(categoryPredicate);
        }

        if (status != null) {
            Predicate statusPredicate = cb.equal(root.get("status"), status);
            predicates.add(statusPredicate);
        }

        if (createdBy != null) {
            System.out.println(createdBy);
            Predicate createdByPredicate = cb.equal(root.get("createdBy"), createdBy);
            predicates.add(createdByPredicate);
        }

        if (assignedTo != null) {
            Predicate assignedToPredicate = cb.equal(root.get("assignedTo"), assignedTo);
            predicates.add(assignedToPredicate);
        }

        Predicate andPredicate = cb.and(predicates.toArray(new Predicate[0]));

        cq.where(andPredicate);

        if (Objects.equals(dateOrder, "desc")) {
            cq.orderBy(cb.desc(root.get("creationDate")));
        }
        if (Objects.equals(dateOrder, "asc")) {
            cq.orderBy(cb.asc(root.get("creationDate")));
        }

        TypedQuery<IssueEntity> query = em.createQuery(cq);
        return query.getResultList();
    }
}
