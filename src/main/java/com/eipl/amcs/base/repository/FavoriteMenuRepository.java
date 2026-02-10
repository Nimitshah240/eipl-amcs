package com.eipl.amcs.base.repository;

import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.model.FavoriteMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMenuRepository extends JpaRepository<FavoriteMenu, Integer> {

    List<FavoriteMenu> findByUser(User user);

    FavoriteMenu findByUserAndPermission(User user, Permission permission);

    List<FavoriteMenu> findAll(Sort sort);
}
