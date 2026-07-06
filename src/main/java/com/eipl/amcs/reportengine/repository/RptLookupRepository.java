package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RptLookupRepository extends JpaRepository<RptLookup, Long> {
    List<RptLookup> findByActiveTrue();
}