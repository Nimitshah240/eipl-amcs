package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.CasteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasteCategoryRepository extends JpaRepository<CasteCategory, Integer> {
}