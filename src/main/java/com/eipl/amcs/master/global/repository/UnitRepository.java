package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Unit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends BaseRepository<Unit, Integer> {

    @Override
    List<Unit> findAll(Sort sort);

}
