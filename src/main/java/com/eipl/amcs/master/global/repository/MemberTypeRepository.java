package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MemberType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTypeRepository extends BaseRepository<MemberType, Integer> {

    @Override
    List<MemberType> findAll(Sort sort);
}
