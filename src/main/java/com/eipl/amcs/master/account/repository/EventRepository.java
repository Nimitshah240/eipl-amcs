package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Events;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends BaseRepository<Events, Integer> {

    @Override
    @EntityGraph(attributePaths = {"society"})
    List<Events> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"society"})
    Optional<Events> findById(Integer code);

    @EntityGraph(attributePaths = {"society"})
    List<Events> findByEventCode(Integer eventCode);

    @EntityGraph(attributePaths = {"society"})
    Events findByEventNameContainingIgnoreCase(String eventName);
}
