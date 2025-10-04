package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.BroadcastedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastedLogRepository extends JpaRepository<BroadcastedLog, String>{

}
