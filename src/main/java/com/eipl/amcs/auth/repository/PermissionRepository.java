package com.eipl.amcs.auth.repository;

import com.eipl.amcs.auth.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Permission findByDescription(String description);
}