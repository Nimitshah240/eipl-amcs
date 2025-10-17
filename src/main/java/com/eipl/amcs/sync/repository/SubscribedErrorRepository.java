package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.SubscribedError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedErrorRepository extends JpaRepository<SubscribedError, String> {

}
