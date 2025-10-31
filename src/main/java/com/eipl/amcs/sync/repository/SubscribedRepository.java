package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.Subscribed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribedRepository extends JpaRepository<Subscribed, String> {

    List<Subscribed> findTop100ByOrderByCreatedAt();

    List<Subscribed> findTop100ByTableNameInOrderByCreatedAt(List<String> prioritizedTableNameList);
}
