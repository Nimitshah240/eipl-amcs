package com.eipl.amcs.auth.repository;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @EntityGraph(type = EntityGraphType.FETCH, attributePaths = {"user", "role", "society"})
    List<UserRole> findAllByUser(User user);
}