package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.auth.repository.RolePermissionRepository;
import com.eipl.amcs.auth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.userRoleRepository;

@Service
public class UserRoleServiceImpl implements UserRoleService {


	@Override
	public List<UserRole> findAllByUser(User user) {
		return userRoleRepository.findAllByUser(user);
	}

}
