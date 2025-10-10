package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilkTypeRepository extends BaseRepository<MilkType, Integer> {

    @Override
    List<MilkType> findAll(Sort sort);

    MilkType findByCode(Integer code);
}
