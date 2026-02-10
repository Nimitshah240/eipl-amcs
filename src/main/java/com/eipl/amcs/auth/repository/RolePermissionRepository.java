package com.eipl.amcs.auth.repository;

import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.model.RolePermission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {

    @EntityGraph(type = EntityGraphType.FETCH, attributePaths = {"role", "permission"})
    List<RolePermission> findAllByRoleIn(List<Role> roles);

    RolePermission findByPermission(Permission permission);
}
