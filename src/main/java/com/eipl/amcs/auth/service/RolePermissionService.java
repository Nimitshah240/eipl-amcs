package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.model.RolePermission;

import java.util.List;

public interface RolePermissionService {

    List<RolePermission> findAllRolePermissionByRoles(List<Role> roles);
}