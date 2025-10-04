package com.eipl.amcs.auth.service;

import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.repository.UserRepository;
import com.eipl.amcs.exception.AuthenticationFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User authenticate(LoginDto dto) {
        return userRepository.findByUsernameAndPasswordAndSocietyAndActiveTrue(dto.getUsername(), dto.getPassword(), dto.getSociety())
                .orElseThrow(() -> new AuthenticationFailException(User.class, "invalid.username.password"));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameAndActiveTrue(username);
    }

}
