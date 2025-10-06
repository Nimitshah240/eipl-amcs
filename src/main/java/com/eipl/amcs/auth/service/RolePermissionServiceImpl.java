package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.model.RolePermission;
import com.eipl.amcs.auth.repository.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public List<RolePermission> findAllRolePermissionByRoles(List<Role> roles) {
        return rolePermissionRepository.findAllByRoleIn(roles);
    }

}