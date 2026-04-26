package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {
}