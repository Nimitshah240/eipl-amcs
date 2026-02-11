package com.eipl.amcs.base.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.repository.PermissionRepository;
import com.eipl.amcs.auth.repository.RolePermissionRepository;
import com.eipl.amcs.base.model.FavoriteMenu;
import com.eipl.amcs.base.repository.FavoriteMenuRepository;
import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class FavoriteMenuServiceImpl implements FavoriteMenuService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private FavoriteMenuRepository favoriteMenuRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public CustomMenuItem createMenuItem(String title, Permission permission) {
        HBox rootNode = new HBox();
        rootNode.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(title);
        Label favoriteIcon = new Label();
        favoriteIcon.setStyle("-fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 5px;");

        FavoriteMenu favoriteMenu = favoriteMenuRepository.findByUserAndPermission(MainApp.getUser(), permission);

        updateIcon(favoriteIcon, favoriteMenu != null);

        favoriteIcon.setOnMouseClicked(event -> {
            if (favoriteMenu != null && Objects.equals(favoriteMenu.getPermission().getCode(), permission.getCode())) {
                updateIcon(favoriteIcon, false);
                deleteFavoriteMenu(favoriteMenu);
            } else {
                updateIcon(favoriteIcon, true);
                saveFavoriteMenu(permission);
            }
            MainApp.getContentPane().setTop(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/HeaderBar.fxml")));
            event.consume();
        });

        rootNode.getChildren().addAll(favoriteIcon, titleLabel);
        return new CustomMenuItem(rootNode, true);
    }

    private static void updateIcon(Label icon, boolean isFavorite) {
        if (isFavorite) {
            icon.setText("★");
            icon.setStyle("-fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 5px; " +
                    "-fx-text-fill: gold; " +
                    "-fx-text-stroke-color: black; " +
                    "-fx-text-stroke-width: 0.5px;");
        } else {
            icon.setText("☆");
            icon.setStyle("-fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 0 5px; " +
                    "-fx-text-fill: black; " +
                    "-fx-text-stroke-color: black; " +
                    "-fx-text-stroke-width: 0.5px;");
        }
    }

    private void saveFavoriteMenu(Permission permission) {

        FavoriteMenu favoriteMenu = new FavoriteMenu();

        favoriteMenu.setPermission(permission);
        favoriteMenu.setPermissionType("SUB_MENU");
        favoriteMenu.setModule(permission.getModule());
        favoriteMenu.setUser(MainApp.getUser());
        favoriteMenu.setUserName(MainApp.getUser().getName());
        favoriteMenu.setRolePermission(rolePermissionRepository.findByPermission(permission));
        favoriteMenu.setParentCode(permissionRepository.findByDescription("favorite").getCode());
        List<FavoriteMenu> list = favoriteMenuRepository.findAll(Sort.by("object").descending());
        favoriteMenu.setObject(list.isEmpty() ? 1 : list.get(0).getObject() + 1);
        favoriteMenuRepository.save(favoriteMenu);
    }

    private void deleteFavoriteMenu(FavoriteMenu favoriteMenu) {
        favoriteMenuRepository.delete(favoriteMenu);
    }
}