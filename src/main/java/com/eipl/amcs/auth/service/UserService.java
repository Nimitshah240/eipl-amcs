package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.auth.model.User;

import java.util.Optional;

public interface UserService {

    User authenticate(LoginDto dto);

    Optional<User> findByUsername(String username);

}
