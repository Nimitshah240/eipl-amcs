package com.eipl.amcs.base.service;

import com.eipl.amcs.auth.model.Permission;
import javafx.scene.control.CustomMenuItem;

public interface FavoriteMenuService {

    CustomMenuItem createMenuItem(String title, Permission permission);
}