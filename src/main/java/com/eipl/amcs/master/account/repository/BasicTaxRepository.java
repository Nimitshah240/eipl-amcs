package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.BasicTax;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicTaxRepository extends BaseRepository<BasicTax, Integer> {

    @Override
    List<BasicTax> findAll(Sort sort);

}
