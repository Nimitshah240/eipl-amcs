package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Plant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends BaseRepository<Plant, String> {

    @Override
    @EntityGraph(attributePaths = {"union", "state", "district", "subDistrict", "village", "hamlet"})
    List<Plant> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"union", "state", "district", "subDistrict", "village", "hamlet"})
    Optional<Plant> findById(String id);
}
