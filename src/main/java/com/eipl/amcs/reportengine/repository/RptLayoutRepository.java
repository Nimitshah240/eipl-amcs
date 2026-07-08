package com.eipl.amcs.reportengine.repository;

import com.eipl.amcs.reportengine.model.RptLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RptLayoutRepository extends JpaRepository<RptLayout, Long> {
    Optional<RptLayout> findByLayoutCode(String layoutCode);
}