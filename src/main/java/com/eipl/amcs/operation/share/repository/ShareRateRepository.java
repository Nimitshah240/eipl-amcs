package com.eipl.amcs.operation.share.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.share.model.ShareRate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRateRepository extends BaseRepository<ShareRate, String> {

    @Override
    @EntityGraph(attributePaths = {"society"})
    Optional<ShareRate> findById(String s);

    @Override
    List<ShareRate> findAll();

    @EntityGraph(attributePaths = {"society"})
    ShareRate findTop1ByWefDateLessThanEqualOrderByWefDate(LocalDate dt);
}