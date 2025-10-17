package com.eipl.amcs.auth.repository;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, String> {

    @EntityGraph(attributePaths = {"society"})
    Optional<User> findByUsernameAndPasswordAndSocietyAndActiveTrue(String username, String password, Society society);

    @EntityGraph(attributePaths = {"society"})
    Optional<User> findByUsernameAndActiveTrue(String username);

}
