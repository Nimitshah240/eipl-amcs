package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;

import java.util.List;

public interface UserRoleService {

    List<UserRole> findAllByUser(User user);
}