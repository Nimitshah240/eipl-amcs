package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.dto.Permission;
import com.eipl.amcs.auth.dto.PermissionComparator;
import com.eipl.amcs.base.model.*;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.task.FtpDetailsCheckTask;
import com.eipl.amcs.base.task.SentboxSaveTask;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.task.ProductDispatchManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductDispatchTransactionManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionManualSaveTask;
import com.eipl.amcs.operation.inventory.task.ProductRequisitionTransactionManualSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class NavbarController implements MyInitialization {

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

    private static final Logger LOGGER = LoggerFactory.getLogger(NavbarController.class);
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
    ObjectMapper mapper = new ObjectMapper();


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
//        callApiFromGoogle();
//        checkNotification();
//        ReSync();
//        checkSentBoxCount();
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


//                    Map jsonText = mapper.readValue(sentBox.getDataText(), Map.class);
//                    switch (sentBox.getTableName()) {
//                        case "tbl_product_requisition":
//                            ProductRequisition requisition = new ProductRequisition();
//                            requisition.setCode((String) jsonText.get("productRequisitionCode"));
//                            if (jsonText.get("cancelledAt") != null)
//                                requisition.setCancelledAt(LocalDateTime.parse((String) jsonText.get("cancelledAt"), AppConstant.Formatter4));
//                            requisition.setCancelledBy((String) jsonText.get("cancelledBy"));
//                            if (jsonText.get("requisitionDate") != null)
//                                requisition.setRequisitionDate(LocalDate.parse((String) jsonText.get("requisitionDate"), AppConstant.Formatter4));
//                            requisition.setDescription((String) jsonText.get("description"));
//                            if (jsonText.get("entryType") != null)
//                                requisition.setEntryType(Integer.parseInt((String) jsonText.get("entryType")));
//                            if (jsonText.get("isCancel") != null)
//                                requisition.setCancel(Boolean.valueOf((String) jsonText.get("isCancel")));
//                            if (jsonText.get("isDelete") != null)
//                                requisition.setDelete(Boolean.valueOf((String) jsonText.get("isDelete")));
//                            requisition.setStatus((String) jsonText.get("status"));
//                            requisition.setSyncStatus((String) jsonText.get("syncStatus"));
//                            if (jsonText.get("syncTimestamp") != null)
//                                requisition.setSyncTimestamp(LocalDateTime.parse((String) jsonText.get("syncTimestamp"), AppConstant.Formatter4));
//                            if (jsonText.get("createdAt") != null)
//                                requisition.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), AppConstant.Formatter4));
//                            if (jsonText.get("createdBy") != null)
//                                requisition.setCreatedBy((String) jsonText.get("createdBy"));
//                            if (jsonText.get("updatedAt") != null)
//                                requisition.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), AppConstant.Formatter4));
//                            if (jsonText.get("updatedBy") != null)
//                                requisition.setCreatedBy((String) jsonText.get("updatedBy"));
//                            requisition.setSociety(MainApp.identityDto.getSociety());
//                            requisition.setUnionCode((String) jsonText.get("unionCode"));
//                            saveProductRequisition(requisition);
//                            break;
//                        case "tbl_product_requisition_transaction":
//                            ProductRequisitionTransaction requisitionTransaction = new ProductRequisitionTransaction();
//                            requisitionTransaction.setCode((String) jsonText.get("requisitionTransactionCode"));
//                            requisitionTransaction.setApprovedBy((String) jsonText.get("approvedBy"));
//                            if (jsonText.get("approvedDate") != null)
//                                requisitionTransaction.setApprovedDate(LocalDate.parse((String) jsonText.get("approvedDate"), AppConstant.Formatter5));
//                            if (jsonText.get("approvedQuantity") != null)
//                                requisitionTransaction.setApprovedQuantity(new BigDecimal(String.valueOf(jsonText.get("approvedQuantity"))));
//                            if (jsonText.get("cancelledAt") != null)
//                                requisitionTransaction.setCancelledAt(LocalDateTime.parse((String) jsonText.get("cancelledAt"), AppConstant.Formatter4));
//                            if (jsonText.get("createdAt") != null)
//                                requisitionTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), AppConstant.Formatter4));
//                            if (jsonText.get("createdBy") != null)
//                                requisitionTransaction.setCreatedBy((String) jsonText.get("createdBy"));
//                            if (jsonText.get("updatedAt") != null)
//                                requisitionTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), AppConstant.Formatter4));
//                            if (jsonText.get("updatedBy") != null)
//                                requisitionTransaction.setCreatedBy((String) jsonText.get("updatedBy"));
//                            requisitionTransaction.setCancelledBy((String) jsonText.get("cancelledBy"));
//                            if (jsonText.get("discountAmount") != null)
//                                requisitionTransaction.setDiscountAmount(new BigDecimal(String.valueOf(jsonText.get("discountAmount"))));
//                            if (jsonText.get("isApproved") != null)
//                                requisitionTransaction.setIsApproved((int) jsonText.get("isApproved"));
//                            requisitionTransaction.setCancel(Boolean.valueOf((String) jsonText.get("isCancel")));
//                            if (jsonText.get("provisionalAmount") != null)
//                                requisitionTransaction.setAmount(new BigDecimal(String.valueOf(jsonText.get("provisionalAmount"))));
//                            if (jsonText.get("provisionalRate") != null)
//                                requisitionTransaction.setRate(new BigDecimal((String) jsonText.get("provisionalRate")));
//                            if (jsonText.get("quantity") != null)
//                                requisitionTransaction.setQuantity(new BigDecimal((String) jsonText.get("quantity")));
//                            if (jsonText.get("requisitionDate") != null)
//                                requisitionTransaction.setRequisitionDate(LocalDate.parse((String) jsonText.get("requisitionDate"), AppConstant.Formatter4));
//                            if (jsonText.get("expectedDeliveryDate") != null)
//                                requisitionTransaction.setExpectedDeliveryDate(LocalDate.parse((String) jsonText.get("expectedDeliveryDate"), AppConstant.Formatter4));
//                            requisitionTransaction.setSchemeAddType((String) jsonText.get("schemeAddType"));
//                            requisitionTransaction.setStatus((String) jsonText.get("status"));
////                            requisitionTransaction.setProduct((String) jsonText.get("product"));
////                            requisitionTransaction.setProductRequisition((String) jsonText.get("productRequisition"));
//                            requisitionTransaction.setProductSchemeCode((String) jsonText.get("productSchemeCode"));
//                            if (jsonText.get("passonToMember") != null)
//                                requisitionTransaction.setPassonToMember(Integer.parseInt((String) jsonText.get("passonToMember")));
//                            requisitionTransaction.setUnionCode((String) jsonText.get("unionCode"));
//                            requisitionTransaction.setSocietyCode((String) jsonText.get("societyCode"));
//                            saveProductRequisitionTransaction(requisitionTransaction);
//                            break;
//                        case "tbl_product_dispatch":
//                            ProductDispatch productDispatch = new ProductDispatch();
//                            productDispatch.setChallanNo((String) jsonText.get("challanNo"));
//                            if (jsonText.get("challanVerified") != null)
//                                productDispatch.setChallanVerified(Boolean.valueOf((String) jsonText.get("challanVerified")));
//                            if (jsonText.get("requisitionDate") != null)
//                                productDispatch.setRequisitionDate(LocalDateTime.parse((String) jsonText.get("requisitionDate"), AppConstant.Formatter4));
//                            if (jsonText.get("dispatchDate") != null)
//                                productDispatch.setDispatchDate(LocalDate.parse((String) jsonText.get("dispatchDate"), AppConstant.Formatter5));
//                            if (jsonText.get("isDelete") != null)
//                                productDispatch.setDelete(Boolean.valueOf((String) jsonText.get("isDelete")));
//                            productDispatch.setReferenceNo((String) jsonText.get("referenceNo"));
//                            productDispatch.setSyncStatus((String) jsonText.get("syncStatus"));
//                            if (jsonText.get("syncTimestamp") != null)
//                                productDispatch.setSyncTimestamp(LocalDateTime.parse((String) jsonText.get("syncTimestamp"), AppConstant.Formatter4));
//                            productDispatch.setVehicleNo((String) jsonText.get("vehicleNo"));
//                            if (jsonText.get("updatedAt") != null)
//                                productDispatch.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), AppConstant.Formatter4));
//                            if (jsonText.get("updatedBy") != null)
//                                productDispatch.setCreatedBy((String) jsonText.get("updatedBy"));
//                            productDispatch.setUnionCode((String) jsonText.get("unionCode"));
////                            productDispatch.setSociety((String) jsonText.get("society"));
//                            productDispatch.setRouteCode((String) jsonText.get("routeCode"));
//                            productDispatch.setDelete(false);
//                            productDispatch.setActive(true);
//                            productDispatch.setSociety(MainApp.identityDto.getSociety());
//                            saveProductDispatch(productDispatch);
//                            break;
//
//                        case "tbl_product_dispatch_transaction":
//                            ProductDispatchTransaction productDispatchTransaction = new ProductDispatchTransaction();
//                            productDispatchTransaction.setCode((String) jsonText.get("dispatchTransactionCode"));
//                            if (jsonText.get("amount") != null)
//                                productDispatchTransaction.setAmount(new BigDecimal(String.valueOf(jsonText.get("amount"))));
//                            if (jsonText.get("dispatchDate") != null)
//                                productDispatchTransaction.setDispatchDate(LocalDate.parse((String) jsonText.get("dispatchDate"), AppConstant.Formatter5));
//                            if (jsonText.get("discount") != null)
//                                productDispatchTransaction.setDiscountAmount(new BigDecimal(String.valueOf(jsonText.get("discount"))));
//                            if (jsonText.get("quantity") != null)
//                                productDispatchTransaction.setDispatchQty(new BigDecimal(String.valueOf(jsonText.get("dispatchQty"))));
//                            if (jsonText.get("rate") != null)
//                                productDispatchTransaction.setRate(new BigDecimal(String.valueOf(jsonText.get("rate"))));
//                            if (jsonText.get("updatedAt") != null)
//                                productDispatchTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), AppConstant.Formatter4));
//                            if (jsonText.get("updatedBy") != null)
//                                productDispatchTransaction.setCreatedBy((String) jsonText.get("updatedBy"));
//                            productDispatchTransaction.setUnionCode((String) jsonText.get("unionCode"));
//                            productDispatchTransaction.setSociety(MainApp.identityDto.getSociety());
////                            productDispatchTransaction.setProductReceipt((String) jsonText.get("productReceipt"));
////                            productDispatchTransaction.setProduct((String) jsonText.get("product"));
////                            productDispatchTransaction.setTax((String) jsonText.get("tax"));
////                            productDispatchTransaction.setUnit((String) jsonText.get("unit"));
//                            saveProductDispatchTransaction(productDispatchTransaction);
//                            break;
//                        case "tbl_bulk_notification":
//                            Notification notification = new Notification();
//                            notification.setBulkNotificationId((Integer) jsonText.get("bulkNotificationId"));
//                            notification.setUnionCode((String) jsonText.get("unionCode"));
//                            notification.setPlantCode((String) jsonText.get("plantCode"));
//                            notification.setMccPlantCode((String) jsonText.get("mccPlantCode"));
//                            notification.setBmcCode((String) jsonText.get("bmcCode"));
//                            notification.setSocietyCode(MainApp.identityDto.getSociety().getCode());
//                            notification.setMemberCode((String) jsonText.get("memberCode"));
//                            notification.setAppType((String) jsonText.get("appType"));
//                            notification.setLoginType((String) jsonText.get("loginType"));
//                            notification.setWefDate(jsonText.get("wefDate") != null ? LocalDateTime.parse((String) jsonText.get("wefDate"), AppConstant.Formatter4) : null);
//                            notification.setTitle((String) jsonText.get("title"));
//                            notification.setMessage((String) jsonText.get("message"));
//                            notification.setCampaignName((String) jsonText.get("campaignName"));
//                            notification.setReceiverType((Integer) jsonText.get("bulkNotificationId"));
//                            notification.setStatus((Integer) jsonText.get("status"));
//                            notification.setOriginatingOrgCode((String) jsonText.get("originatingOrgCode"));
//                            notification.setOriginatingOrgType((String) jsonText.get("originatingOrgType"));
//                            notification.setOriginatingType((Integer) jsonText.get("originatingType"));
//                            notification.setFromDate(jsonText.get("fromDate") != null ? LocalDateTime.parse((String) jsonText.get("fromDate"), AppConstant.Formatter4) : null);
//                            notification.setToDate(jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), AppConstant.Formatter4) : null);
//                            notification.setFromShift((Integer) jsonText.get("fromShiftCode"));
//                            notification.setToShift((Integer) jsonText.get("toShiftCode"));
//                            notification.setNotificationType((Integer) jsonText.get("notificationType"));
//                            notification.setFileName((String) jsonText.get("filename"));
//                            notification.setFilePath((String) jsonText.get("filePath"));
//                            notification.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), AppConstant.Formatter4) : null);
//                            notification.setCreatedBy((String) jsonText.get("createdBy"));
//                            System.out.println(notification);
//                            MainApp.notificationList.add(notification);
//                            saveNotification();
//                            break;
//                        default:
//                            break;
//                    }
//                    sentBoxUuidList.add((String) map.get("uuid"));


    private void saveProductRequisition(ProductRequisition requisition) {
        ProductRequisitionManualSaveTask task = new ProductRequisitionManualSaveTask(requisition, (short) 0);
        task.setOnSucceeded(e -> {
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
//            loadNotificationAfterSave();
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
                        SyncCheckAcknowledgementTask syncCheckAcknowledgementTask = new SyncCheckAcknowledgementTask(content.toString().substring(0, content.length() - 1));
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


//        Map<String, Object> res = new HashMap<>();
//        res.put("fromDate", "2021-01-01 06:00:00");
//        res.put("toDate", "2022-01-01 18:00:00");
//        res.put("tableName", "milk_dispatch");
//        var task1 = new ReSyncTask((String) res.get("fromDate"), (String) res.get("toDate"), (String) res.get("tableName"));
//        task1.setOnSucceeded(ee -> {
//            System.out.println("Resync data successful");
//        });
//        new Thread(task1).start();
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
//            notification.setContentId((String) map.get("contentId"));
            notification.setStatus((Integer) map.get("status"));
//            notification.setEntry(LocalDateTime.parse((String) map.get("entryDatetime"),formatter));
//            notification.setPickup(LocalDateTime.parse((String) map.get("pickupDatetime"),formatter));
//            notification.setResponse(LocalDateTime.parse((String) map.get("responseDatetime"),formatter));
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
        var task = new NotificationAcknowledgementTask(code.toString().substring(0, code.length() - 1));
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

    class MenuGenerateTask extends Task<Short> {

        @Override
        protected Short call() throws Exception {
            try {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.AUTH + "/permission/{username}";
                Map<String, Object> uriVariables = new HashMap<>();
                uriVariables.put("username", MainApp.getUser().getUsername());
                ResponseEntity<Permission[]> response = restTemplate.getForEntity(url, Permission[].class, uriVariables);
                if (response.getBody() == null)
                    return 0;

                if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                    return 500;
                if (response.getStatusCode() == HttpStatus.NOT_FOUND)
                    return 404;

                LOGGER.info("Permission fetched: {}", response.getBody().length);

                permissions = Arrays.asList(response.getBody());
                permissions.sort(Comparator.comparing(Permission::getCode));
                menu = new TreeMap<>(new PermissionComparator());

                permissions.forEach(r -> {
                    if (r.getType() != null && r.getType().equals("MENU")) {
                        if (r.getParentCode() == null || r.getParentCode().intValue() == 0) {
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

    private void ftpBackup() {
        //FTP Backup
        if (LocalDate.now().getDayOfMonth() == 30) {
            FtpDetailsCheckTask task = new FtpDetailsCheckTask();
            new Thread(task).start();
        }
    }
}


class ReSyncTask extends Task<List> {

    private List<Map<String, Object>> list;

    public ReSyncTask(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    protected List call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + "sync/resync";
            ResponseEntity<Map[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(list), Map[].class);

            if (response.getStatusCode() == HttpStatus.OK)
                return Arrays.asList(response.getBody());
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

