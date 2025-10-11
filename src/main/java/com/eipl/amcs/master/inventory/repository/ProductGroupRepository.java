package com.eipl.amcs.master.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductGroupRepository extends BaseRepository<ProductGroup, String> {

    @Override
    @EntityGraph(attributePaths = {"unit"})
    List<ProductGroup> findAll(Sort sort);

    @EntityGraph(attributePaths = {"unit"})
    public ProductGroup findByCode(Integer Code);

    @Override
    @EntityGraph(attributePaths = {"unit"})
    Optional<ProductGroup> findById(String id);
}
