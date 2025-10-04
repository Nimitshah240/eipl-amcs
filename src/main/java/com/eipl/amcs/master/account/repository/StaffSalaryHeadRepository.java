package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface StaffSalaryHeadRepository extends BaseRepository<StaffSalaryHead, Integer> {

    @Override
    @EntityGraph(attributePaths = "society")
    List<StaffSalaryHead> findAll(Sort sort);

}
