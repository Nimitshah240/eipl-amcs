package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffSalaryHeadRepository extends BaseRepository<StaffSalaryHead, Integer> {

    @Override
    @EntityGraph(attributePaths = "society")
    List<StaffSalaryHead> findAll(Sort sort);

}
