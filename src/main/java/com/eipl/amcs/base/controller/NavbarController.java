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
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.task.*;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.task.ProductDispatchManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductDispatchTransactionManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionTransactionManualSaveTask;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchTransactionRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class NavbarController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(NavbarController.class);
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    @FXML
    AnchorPane root;
    @FXML
    Button btnDashboard;
    @FXML
    private VBox menuVbox;
    @FXML
    private Label lblVersion;
    private ResourceBundle resourceBundle;
    private List<Permission> permissions;
    private Map<Permission, Map<Permission, List<Permission>>> menu;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDashboard.setFont(Font.font(16));
        this.resourceBundle = resourceBundle;
        lblVersion.setText("Version: " + AppConstant.versionNo);
        loadData();
        btnDashboard.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        loadNotification();
        callApi();
        ftpBackup();
    }

    private void checkSentBoxCount() {
        SentBoxCountTask task = new SentBoxCountTask(MainApp.identityDto.getSociety().getCode(), "");
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> map = task.get();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void checkSentBox(int count) {
        var task = new SentBoxCheckTask(MainApp.identityDto.getSociety().getCode(), "");
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> map = task.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void saveSentbox(List<Subscribed> list) {
        SentboxSaveTask task = new SentboxSaveTask(list, (short) 0);
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    private void saveProductRequisition(ProductRequisition requisition) {
        ProductRequisitionManualSaveTask task = new ProductRequisitionManualSaveTask(requisition, (short) 0);
        task.setOnSucceeded(e -> {
            System.out.println("Saved");
        });
        new Thread(task).start();
    }

    private void saveProductRequisitionTransaction(ProductRequisitionTransaction requisition) {
        ProductRequisitionTransactionManualSaveTask task = new ProductRequisitionTransactionManualSaveTask(requisition, (short) 0);
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    private void saveProductDispatch(ProductDispatch requisition) {
        ProductDispatchManualSaveTask task = new ProductDispatchManualSaveTask(requisition, (short) 0);
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    private void saveProductDispatchTransaction(ProductDispatchTransaction requisition) {
        ProductDispatchTransactionManualSaveTask task = new ProductDispatchTransactionManualSaveTask(requisition, (short) 0);
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    private void saveNotification() {
        NotificationSaveTask task = new NotificationSaveTask(MainApp.notificationList);
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }


    private void callApi() {
        var task = new UpdaterCheckTask();
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> res = task.get();
                if (res != null) {
                    Map<String, Object> map = (Map<String, Object>) res.get("data");
                    if (map.get("url") != null && (Double.parseDouble((String) map.get("latestVersion")) - Double.parseDouble(AppConstant.versionNo) > 0)) {
                        var task1 = new DownloadFileTask(map.get("url").toString());
                        task1.setOnSucceeded(e1 -> {
                            MyAlert alert = new InformationAlert(MainApp.getStage(), "Application Update Successful",
                                    "Please Restart Your Computer");
                            alert.createAlert();
                            try {
                                File dir = new File("resources/appupdate");
                                for (File file : dir.listFiles()) {
                                    for (File listFile : file.listFiles()) {
                                        listFile.delete();
                                    }
                                    file.delete();
                                }
                                dir.delete();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                        new Thread(task1).start();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void callApiFromGoogle() {
        var task1 = new DownloadFileTask
                ("https://drive.google.com/u/0/uc?id=1WJUdROcE_mHx2hqGc7n9_s6zzbUVEuvS&export=download&confirm=t&uuid=dfd5646f-9dc8-471d-9a5f-50f0861df0f4&at=AKKF8vzWRnQmVGEfseAKhGqygA0v:1688030940725");
        task1.setOnSucceeded(e1 -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), "Application Update Successful",
                    "Please Restart Your Computer");
            alert.createAlert();
            try {
                File dir = new File("resources/appupdate");
                for (File file : dir.listFiles()) {
                    for (File listFile : file.listFiles()) {
                        listFile.delete();
                    }
                    file.delete();
                }
                dir.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }


    private void checkNotification() {
        var task = new NotificationCheckTask(MainApp.identityDto.getSociety().getCode(), "");
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> res = task.get();
                if (res != null) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) res.get("notificationDetail");
                    if (res.get("notificationDetail") != null && !((List<?>) res.get("notificationDetail")).isEmpty())
                        saveNotification(list);
                    else {
                        loadNotification();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void ReSync() {
        var task = new SyncCheckTask(MainApp.identityDto.getSociety().getCode(), "");
        task.setOnSucceeded(e -> {
            try {
                Map<String, Object> res = task.get();
                List<Map<String, Object>> list = (List<Map<String, Object>>) res.get("syncRequest");
                if (res != null && !list.isEmpty()) {
                    var task1 = new ReSyncTask(list);
                    StringBuffer content = new StringBuffer();
                    for (Map<String, Object> objectMap : list) {
                        content.append((String) objectMap.get("forceSyncRequestCode"));
                        content.append(",");
                    }
                    task1.setOnSucceeded(ee -> {
                        SyncCheckAcknowledgementTask syncCheckAcknowledgementTask = new SyncCheckAcknowledgementTask(content.substring(0, content.length() - 1));
                        syncCheckAcknowledgementTask.setOnSucceeded(eee -> {
                        });
                        new Thread(syncCheckAcknowledgementTask).start();
                    });
                    new Thread(task1).start();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void saveNotification(List<Map<String, Object>> res) {
        List<Notification> notificationList = new ArrayList<>();
        for (Map<String, Object> map : res) {
            Notification notification = new Notification();
            notification.setBulkNotificationId((Integer) map.get("bulkNotificationId"));
            notification.setUnionCode((String) map.get("unionCode"));
            notification.setPlantCode((String) map.get("plantCode"));
            notification.setMccPlantCode((String) map.get("mccPlantCode"));
            notification.setBmcCode((String) map.get("bmcCode"));
            notification.setSocietyCode(MainApp.identityDto.getSociety().getCode());
            notification.setMemberCode((String) map.get("memberCode"));
            notification.setAppType((String) map.get("appType"));
            notification.setLoginType((String) map.get("loginType"));
            notification.setWefDate(map.get("wefDate") != null ? LocalDateTime.parse((String) map.get("wefDate"), formatter) : null);
            notification.setTitle((String) map.get("title"));
            notification.setMessage((String) map.get("message"));
            notification.setCampaignName((String) map.get("campaignName"));
            notification.setReceiverType((Integer) map.get("bulkNotificationId"));
            notification.setStatus((Integer) map.get("status"));
            notification.setOriginatingOrgCode((String) map.get("originatingOrgCode"));
            notification.setOriginatingOrgType((String) map.get("originatingOrgType"));
            notification.setOriginatingType((Integer) map.get("originatingType"));
            notification.setFromDate(map.get("fromDate") != null ? LocalDateTime.parse((String) map.get("fromDate"), formatter) : null);
            notification.setToDate(map.get("toDate") != null ? LocalDateTime.parse((String) map.get("toDate"), formatter) : null);
            notification.setFromShift((Integer) map.get("fromShiftCode"));
            notification.setToShift((Integer) map.get("toShiftCode"));
            notification.setNotificationType((Integer) map.get("notificationType"));
            notification.setFileName((String) map.get("filename"));
            notification.setFilePath((String) map.get("filePath"));
            notification.setCreatedAt(map.get("createdAt") != null ? LocalDateTime.parse((String) map.get("createdAt"), formatter) : null);
            notification.setCreatedBy((String) map.get("createdBy"));
            notificationList.add(notification);
        }
        var task = new NotificationSaveTask(notificationList);
        task.setOnSucceeded(e -> {
            sendAcknowledgement();
        });
        new Thread(task).start();
    }

    private void loadNotificationAfterSave() {
        var task1 = new NotificationLoadTask();
        task1.setOnSucceeded(e1 -> {
            try {
                if (task1.get() != null) {
                    MainApp.notificationList = new ArrayList<>();
                    MainApp.notificationList.addAll(task1.get());
                    if (MainApp.notificationList != null)
                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                    sendAcknowledgement();
                }
            } catch (InterruptedException | ExecutionException ee) {
                loadNotification();
                throw new RuntimeException(ee);
            }
        });
        new Thread(task1).start();
    }

    public void loadNotification() {
        var task1 = new NotificationLoadTask();
        task1.setOnSucceeded(e1 -> {
            try {
                if (task1.get() != null) {
                    MainApp.notificationList = new ArrayList<>();
                    MainApp.notificationList.addAll(task1.get());
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                } else {
                    MainApp.notificationList = new ArrayList<>();
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
                }
            } catch (InterruptedException | ExecutionException ee) {
                throw new RuntimeException(ee);
            }
        });
        new Thread(task1).start();
    }

    private void sendAcknowledgement() {
        StringBuffer code = new StringBuffer();
        for (Notification notification : MainApp.notificationList) {
            code.append(notification.getBulkNotificationId());
            code.append(",");
        }
        var task = new NotificationAcknowledgementTask(code.substring(0, code.length() - 1));
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    private void sendAcknowledgementForSentbox(List<String> list) {
        StringBuilder code = new StringBuilder();
        for (String uuid : list) {
            code.append(uuid);
            code.append(",");
        }
        var task = new SentBoxAcknowledgementTask(code.substring(0, code.length() - 1));
        task.setOnSucceeded(e -> {
        });
        new Thread(task).start();
    }

    @Override
    public void loadData() {
        if (MainApp.getUser() == null)
            return;
        var task = new MenuGenerateTask();
        task.setOnSucceeded(e -> {
            try {
                short resp = task.get();
                if (resp == (short) 0) {
                    menu.forEach((main, sub) -> {
                        final ContextMenu contextMenu = new ContextMenu();

                        final Button btn = new Button(resourceBundle.getString(main.getDescription()));
                        btn.setOnAction(event1 -> {
                            contextMenu.show(btn, Side.RIGHT, -10, 0);
                        });
                        btn.setMnemonicParsing(true);
                        btn.setMaxWidth(Double.MAX_VALUE);
                        btn.getStyleClass().add("nav-button");
                        btn.setFont(Font.font(16));
                        menuVbox.getChildren().add(btn);

                        // context menu
                        sub.forEach((k, v) -> {
                            try {
                                if (v.isEmpty()) {

                                    MenuItem item = new MenuItem(resourceBundle.getString(k.getDescription()));
                                    setupClickEvent(item, k.getModule());
                                    contextMenu.getItems().add(item);
                                } else {
                                    Menu menuSub = new Menu(resourceBundle.getString(k.getDescription()));

                                    v.forEach(item -> {
                                        try {
                                            MenuItem menuItem = new MenuItem(resourceBundle.getString(item.getDescription()));
                                            setupClickEvent(menuItem, item.getModule());
                                            menuSub.getItems().add(menuItem);
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                                    contextMenu.getItems().add(menuSub);
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
        menuItem.setOnAction(e -> {
            MainApp.contentPane.setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource(urlPath.trim())));
        });
    }

    private void ftpBackup() {
        //FTP Backup
        if (LocalDate.now().getDayOfMonth() == 30) {
            FtpDetailsCheckTask task = new FtpDetailsCheckTask();
            new Thread(task).start();
        }
    }

    class MenuGenerateTask extends Task<Short> {

        @Override
        protected Short call() throws Exception {
            try {
                UserService service = EmcsAppContext.getContext().getBean(UserService.class);
                UserRoleService userRoleService = EmcsAppContext.getContext().getBean(UserRoleService.class);
                RolePermissionService rolePermissionService = EmcsAppContext.getContext().getBean(RolePermissionService.class);
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

                permissions = rolePermissions.stream().map(m -> m.getPermission())
                        .collect(Collectors.toList());

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


class ReSyncTask extends Task<List> {

    private final List<Map<String, Object>> list;

    public ReSyncTask(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    protected List call() throws Exception {
        try {
            MilkCollectionService milkCollectionRepository = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            MemberRepository memberRepository = EmcsAppContext.getContext().getBean(MemberRepository.class);
            MemberDetailRepository memberDetailRepository = EmcsAppContext.getContext().getBean(MemberDetailRepository.class);
            SocietyPaymentCycleRepository paymentCycleRepository = EmcsAppContext.getContext().getBean(SocietyPaymentCycleRepository.class);
            MilkDispatchRepository dispatchRepository = EmcsAppContext.getContext().getBean(MilkDispatchRepository.class);
            MilkDispatchTransactionRepository milkDispatchTransactionRepository = EmcsAppContext.getContext().getBean(MilkDispatchTransactionRepository.class);

            for (Map<String, Object> syncResponse : list) {
                switch ((String) syncResponse.get("tableName")) {
                    case "tbl_milk_collection":
                        milkCollectionRepository.findAllCollectionByDate(LocalDateTime.parse((String) syncResponse.get("fromDatetime"), AppConstant.SYNC_DATE_TIME_FMT), LocalDateTime.parse((String) syncResponse.get("toDatetime"), AppConstant.SYNC_DATE_TIME_FMT), CommonUtils.setIdentityHeader());
                        break;
                    case "tbl_member":
                        for (Member member : memberRepository.findAll()) {
                            member.setSociety(Hibernate.unproxy(member.getSociety(), Society.class));
                            member.setMemberType(Hibernate.unproxy(member.getMemberType(), MemberType.class));
                            member.setMilkType(Hibernate.unproxy(member.getMilkType(), MilkType.class));
                            memberRepository.customSaveForSync(member, CommonUtils.setIdentityHeader());
                        }
                        for (MemberDetail detail : memberDetailRepository.findAll()) {
                            detail.setState(Hibernate.unproxy(detail.getState(), com.eipl.amcs.master.geo.model.State.class));
                            detail.setDistrict(Hibernate.unproxy(detail.getDistrict(), District.class));
                            detail.setSubDistrict(Hibernate.unproxy(detail.getSubDistrict(), SubDistrict.class));
                            detail.setVillage(Hibernate.unproxy(detail.getVillage(), Village.class));
                            detail.setHamlet(Hibernate.unproxy(detail.getHamlet(), Hamlet.class));
                            detail.setBank(Hibernate.unproxy(detail.getBank(), Bank.class));
                            detail.setBranch(Hibernate.unproxy(detail.getBranch(), Branch.class));
                            memberDetailRepository.customSaveForSync(detail, CommonUtils.setIdentityHeader());
                        }
                        break;
                    case "tbl_dcs_payment_cycle":
                        for (SocietyPaymentCycle societyPaymentCycle : paymentCycleRepository.findAll()) {
                            societyPaymentCycle.setSociety(Hibernate.unproxy(societyPaymentCycle.getSociety(), Society.class));
                            societyPaymentCycle.setFromShift(Hibernate.unproxy(societyPaymentCycle.getFromShift(), Shift.class));
                            societyPaymentCycle.setToShift(Hibernate.unproxy(societyPaymentCycle.getToShift(), Shift.class));
                            paymentCycleRepository.customSaveForSync(societyPaymentCycle, CommonUtils.setIdentityHeader());
                        }
                        break;
                    case "tbl_milk_dispatch":
                        for (MilkDispatch md : dispatchRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(LocalDateTime.parse((String) syncResponse.get("fromDatetime"), AppConstant.SYNC_DATE_TIME_FMT), LocalDateTime.parse((String) syncResponse.get("toDatetime"), AppConstant.SYNC_DATE_TIME_FMT))) {
                            dispatchRepository.customSaveForSync(md, CommonUtils.setIdentityHeader());
                            for (MilkDispatchTransaction dispatchTransaction : milkDispatchTransactionRepository.findByMilkDispatch(md)) {
                                milkDispatchTransactionRepository.customSaveForSync(dispatchTransaction, CommonUtils.setIdentityHeader());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

