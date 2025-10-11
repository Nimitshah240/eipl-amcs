package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilkClassRepository extends BaseRepository<MilkClass, Integer> {

    @Override
    List<MilkClass> findAll(Sort sort);

    MilkClass findByCode(Integer code);
}
