package com.eipl.amcs.base.repository;

import com.eipl.amcs.base.model.Identity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdentityRepository extends BaseRepository<Identity, String> {

    @Override
    List<Identity> findAll(Sort sort);

    Identity findBySocietyRefCode(String societyRefCode);

}
