package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.model.Role;
import com.eipl.amcs.auth.model.RolePermission;
import com.eipl.amcs.auth.repository.RolePermissionRepository;
import com.eipl.amcs.base.repository.IdentityRepository;
import com.eipl.amcs.master.org.repository.DockRepository;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.org.repository.UnionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.rolePermissionRepository;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {


    @Override
    public List<RolePermission> findAllRolePermissionByRoles(List<Role> roles) {
        return rolePermissionRepository.findAllByRoleIn(roles);
    }

}