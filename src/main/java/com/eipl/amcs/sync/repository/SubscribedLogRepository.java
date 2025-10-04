package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.SubscribedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedLogRepository extends JpaRepository<SubscribedLog, String> {

}
