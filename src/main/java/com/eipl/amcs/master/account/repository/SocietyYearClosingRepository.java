package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocietyYearClosingRepository extends BaseRepository<SocietyYearClosing, String> {

    @Override
    @EntityGraph(attributePaths = {"financialYear", "society"})
    List<SocietyYearClosing> findAll();
}
