package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffSalaryMappingRepository extends BaseRepository<StaffSalaryMapping, Integer> {

    @Override
    @EntityGraph(attributePaths = { "staffMember", "society", "staffSalaryHead" })
    List<StaffSalaryMapping> findAll(Sort sort);

}
