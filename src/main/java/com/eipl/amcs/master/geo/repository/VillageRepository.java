package com.eipl.amcs.master.geo.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface VillageRepository extends BaseRepository<Village, String> {

    @Override
    @EntityGraph(attributePaths = {"subDistrict"})
    List<Village> findAll(Sort sort);

    @EntityGraph(attributePaths = {"subDistrict"})
    List<Village> findBySubDistrict(SubDistrict subDistrict, Sort sort);
}
