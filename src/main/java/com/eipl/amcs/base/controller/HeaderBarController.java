package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.PermissionComparator;
import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.model.RolePermission;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.auth.model.UserRole;
import com.eipl.amcs.auth.service.RolePermissionService;
import com.eipl.amcs.auth.service.UserRoleService;
import com.eipl.amcs.auth.service.UserService;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.FavoriteMenu;
import com.eipl.amcs.base.repository.FavoriteMenuRepository;
import com.eipl.amcs.base.service.FavoriteMenuService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class HeaderBarController implements MyInitialization, PopupCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderBarController.class);
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    @FXML
    AnchorPane root;
    @FXML
    private Label lblName;
    @FXML
    private MenuBar menuBar;
    //    @FXML
//    Button btnMinimize,btnClose;
    private ResourceBundle resourceBundle;
    private List<Permission> permissions;
    private Map<Permission, Map<Permission, List<Permission>>> menu;

    @FXML
    private ImageView logoView;

    private FavoriteMenuService favoriteMenuService;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDynamicLogo();
        this.resourceBundle = resourceBundle;
        loadControls();
        loadMenu();
        favoriteMenuService = EmcsAppContext.getContext().getBean(FavoriteMenuService.class);
//        btnClose.setOnAction(e -> {
//            stage.close();
//        });
    }

    @Override
    public void loadControls() {
        lblName.setText(MainApp.identityDto.getSociety().getName() + " - " + MainApp.identityDto.getSociety().getCode());
    }

    private void loadMenu() {
        if (MainApp.getUser() == null) return;
        var task = new MenuGenerateTask();
        task.setOnSucceeded(e -> {
            try {
                short resp = task.get();
                if (resp == (short) 0) {
                    menu.forEach((main, sub) -> {
                        Menu mainMenu = new Menu(resourceBundle.getString(main.getDescription()));
                        menuBar.getMenus().add(mainMenu);

                        sub.forEach((k, v) -> {
                            try {
                                if (v.isEmpty()) {
                                    MenuItem item = new MenuItem(resourceBundle.getString(k.getDescription()));
                                    setupClickEvent(item, k.getModule());
                                    mainMenu.getItems().add(item);
                                } else {
                                    Menu subMenu = new Menu(resourceBundle.getString(k.getDescription()));
                                    v.forEach(item -> {
                                        try {
                                            CustomMenuItem menuItem = favoriteMenuService.createMenuItem(resourceBundle.getString(item.getDescription()), item);
                                            setupClickEvent(menuItem, item.getModule());
                                            subMenu.getItems().add(menuItem);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                                    mainMenu.getItems().add(subMenu);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    });
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setupClickEvent(MenuItem menuItem, String urlPath) {
        try {
            menuItem.setOnAction(e -> {
                if (MainApp.contentPane.getLeft() == null) {
                    MainApp.getContentPane().setLeft(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Navbar.fxml")));
                }
                MainApp.contentPane.setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource(urlPath.trim())));
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setDynamicLogo() {
        try {
            String clientCode = MainApp.getProperty("client.code", "AMUL");
            String imageUrl = "/com/eipl/amcs/view/images/";

            switch (clientCode) {
                case "JAIPUR_AMCS":
                    imageUrl = imageUrl.concat("saras_logo_crop.png");
                    logoView.setFitHeight(65);
                    logoView.setFitWidth(100);
                    break;
                case "BANAS_AMCS":
                    imageUrl = imageUrl.concat("amulpng.png");
                    logoView.setFitHeight(50);
                    logoView.setFitWidth(100);
                    break;
                default:
                    imageUrl = imageUrl.concat("logo.png");
                    logoView.setFitHeight(70);
                    logoView.setFitWidth(100);
                    break;
            }

            Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
            logoView.setImage(image);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class MenuGenerateTask extends Task<Short> {

        @Override
        protected Short call() throws Exception {
            try {
                UserService service = EmcsAppContext.getContext().getBean(UserService.class);
                UserRoleService userRoleService = EmcsAppContext.getContext().getBean(UserRoleService.class);
                RolePermissionService rolePermissionService = EmcsAppContext.getContext().getBean(RolePermissionService.class);
                FavoriteMenuRepository favoriteMenuRepository = EmcsAppContext.getContext().getBean(FavoriteMenuRepository.class);
                String username = MainApp.getUser().getUsername();
                Optional<User> user = service.findByUsername(username);
                if (user.isEmpty()) {
                    return null;
                }

                List<UserRole> userRoles = userRoleService.findAllByUser(user.get());
                if (userRoles == null || userRoles.isEmpty()) {
                    return null;
                }

                List<RolePermission> rolePermissions = rolePermissionService.findAllRolePermissionByRoles(
                        userRoles.stream().map(m -> m.getRole()).collect(Collectors.toList()));
                if (userRoles.isEmpty()) {
                    return null;
                }
                permissions = rolePermissions.stream().map(m -> m.getPermission()).collect(Collectors.toList());

                List<FavoriteMenu> favoriteMenuList = favoriteMenuRepository.findByUser(MainApp.getUser());
                List<Permission> favoriteMenuPermission = favoriteMenuList.stream().map(m -> {
                    Permission permission = m.getPermission();
                    permission.setParentCode(m.getParentCode());
                    permission.setObject(m.getObject());
                    permission.setType(m.getPermissionType());
                    return permission;
                }).collect(Collectors.toList());
                permissions.addAll(favoriteMenuPermission);
                permissions.sort(Comparator.comparing(Permission::getCode));

                menu = new TreeMap<>(new PermissionComparator());

                permissions.forEach(r -> {
                    if (r.getType() != null && r.getType().equals("MENU")) {
                        if (r.getParentCode() == null || r.getParentCode() == 0) {
                            menu.put(r, new TreeMap<>(new PermissionComparator()));
                        }
                    }
                    MainApp.getUser().getPermissions().add(r.getName());
                });

                for (Permission permission : permissions) {
                    if ("SUB_MENU".equalsIgnoreCase(permission.getType())) {
                        menu.forEach((k, v) -> {
                            if (k.getCode().intValue() == permission.getParentCode().intValue())
                                v.put(permission, new ArrayList<>());
                        });
                    } else if ("SUB_MENU_1".equalsIgnoreCase(permission.getType())) {
                        menu.forEach((k, v) -> {
                            v.forEach((k1, v1) -> {
                                if (k1.getCode().intValue() == permission.getParentCode().intValue()) {
                                    v1.add(permission);
                                }
                            });
                        });
                    }
                    MainApp.getUser().getPermissions().add(permission.getName());
                }
                LOGGER.info("Menu {}", menu);
                return (short) 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (short) 0;
        }
    }
}
