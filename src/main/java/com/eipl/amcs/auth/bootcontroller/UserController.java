package com.eipl.amcs.auth.bootcontroller;

import com.eipl.amcs.auth.dto.LoginDto;
import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.model.RolePermission;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.auth.service.RolePermissionService;
import com.eipl.amcs.auth.service.UserRoleService;
import com.eipl.amcs.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService service;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RolePermissionService rolePermissionService;

    @PostMapping
    public ResponseEntity<User> login(@RequestBody LoginDto dto) {
        return new ResponseEntity<User>(service.authenticate(dto), HttpStatus.OK);
    }

    @GetMapping("/permission/{username}")
    public ResponseEntity<Set<Permission>> getPermissionForUser(@PathVariable("username") String username) {
        try {
            LOGGER.info("Get permission for user {}", username);
            Optional<User> user = service.findByUsername(username);
            if (user == null || !user.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            List<UserRole> userRoles = userRoleService.findAllByUser(user.get());
            if (userRoles == null || userRoles.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            List<RolePermission> rolePermissions = rolePermissionService.findAllRolePermissionByRoles(
                    userRoles.stream().map(m -> m.getRole()).collect(Collectors.toList()));
            if (userRoles == null || userRoles.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            Set<Permission> permissions = rolePermissions.stream().map(m -> m.getPermission())
                    .collect(Collectors.toSet());
            return new ResponseEntity<Set<Permission>>(permissions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
