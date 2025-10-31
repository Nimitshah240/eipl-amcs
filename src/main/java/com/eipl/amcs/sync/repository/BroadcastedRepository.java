package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.Broadcasted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BroadcastedRepository extends JpaRepository<Broadcasted, String> {

    List<Broadcasted> findAllByTableNameNotInOrderByCreatedAt(List<String> list);

    List<Broadcasted> findTop50ByTableNameNotInOrderByCreatedAt(List<String> list);

    List<Broadcasted> findTop100ByTableNameInOrderByCreatedAt(List<String> insuranceDetail);
}
