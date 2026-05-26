package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Narration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NarrationRepository extends BaseRepository<Narration, String> {

    @Override
    @EntityGraph(attributePaths = {"narrationType", "society"})
    List<Narration> findAll();

}
