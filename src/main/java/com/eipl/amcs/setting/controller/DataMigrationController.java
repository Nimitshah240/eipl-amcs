package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DataMigrationController implements MyInitialization {
    private final String VENDOR_PROMPT = "Prompt";
    private final String VENDOR_SKYWAY = "SkyWay";
    private final String VENDOR_FRIENDS = "Friends";
    private final String VENDOR_E_MANDLI = "E-Mandli";
    private final String VENDOR_PROMPT_SQL_SERVER = "Prompt SQL Server";
    private final String VENDOR_HISAABMITRA = "Hisaab Mitra";
    private final String OP_MEMBER = "Member";
    private final String OP_MILK_COLLECTION = "Milk Collection";
    private final String OP_MILK_SALE = "Local Milk Sale";
    private final String OP_MILK_DISPATCH = "Milk Dispatch";
    private final String OP_PRODUCT = "Product";
    private final String OP_PRODUCT_SALE = "Product Sale";
    @FXML
    StackPane root;
    @FXML
    ComboBox<String> cboxFrom, cboxType;
    @FXML
    Button btnImport;
    @FXML
    BorderPane paneContainer;
    private ResourceBundle resourceBundle;
    private final List<String> vendorList;
    private final List<String> operationTypeList;
    private final String path = null;


    public DataMigrationController() {
        vendorList = new ArrayList<>();
        vendorList.add(VENDOR_PROMPT);
        vendorList.add(VENDOR_SKYWAY);
        vendorList.add(VENDOR_FRIENDS);
        vendorList.add(VENDOR_E_MANDLI);
        vendorList.add(VENDOR_PROMPT_SQL_SERVER);
        vendorList.add(VENDOR_HISAABMITRA);

        operationTypeList = new ArrayList<>();
        operationTypeList.add(OP_MEMBER);
        operationTypeList.add(OP_MILK_COLLECTION);
        operationTypeList.add(OP_MILK_SALE);
        operationTypeList.add(OP_MILK_DISPATCH);
        operationTypeList.add(OP_PRODUCT);
        operationTypeList.add(OP_PRODUCT_SALE);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();

        btnImport.setOnAction(e -> {
            String vendorOperation = cboxFrom.getValue() + "-" + cboxType.getValue();
            String popupFor = null;
            switch (vendorOperation) {
                case VENDOR_PROMPT + "-" + OP_MEMBER:
                    popupFor = "MemberDataMigrationPopup";
                    break;
                case VENDOR_PROMPT + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationPopup";
                    break;
                case VENDOR_FRIENDS + "-" + OP_MEMBER:
                    popupFor = "MemberDataMigrationFriendsPopup";
                    break;
                case VENDOR_FRIENDS + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationFriendsPopup";
                    break;
                case VENDOR_HISAABMITRA + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationHisaabMitraPopup";
                    break;
                case VENDOR_FRIENDS + "-" + OP_MILK_SALE:
                    popupFor = "LocalMilkSaleDataMigrationFriendsPopup";
                    break;
                case VENDOR_PROMPT + "-" + OP_MILK_SALE:
                    break;
                case VENDOR_PROMPT + "-" + OP_MILK_DISPATCH:
                    break;
                case VENDOR_PROMPT + "-" + OP_PRODUCT:
                    break;
                case VENDOR_PROMPT + "-" + OP_PRODUCT_SALE:
                    break;
                case VENDOR_SKYWAY + "-" + OP_MEMBER:
                    popupFor = "MemberDataMigrationSkyWayPopup";
                    break;
                case VENDOR_SKYWAY + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationSkyWayPopup";
                    break;
                case VENDOR_SKYWAY + "-" + OP_MILK_SALE:
                    popupFor = "LocalMilkSaleMigrationPopup";
                    break;
                case VENDOR_SKYWAY + "-" + OP_MILK_DISPATCH:
                    break;
                case VENDOR_SKYWAY + "-" + OP_PRODUCT:
                    popupFor = "ProductMigrationPopup";
                    break;
                case VENDOR_SKYWAY + "-" + OP_PRODUCT_SALE:
                    popupFor = "ProductSaleMigrationPopup";
                    break;
                case VENDOR_E_MANDLI + "-" + OP_MEMBER:
                    popupFor = "MemberDataMigrationE-MandaliPopup";
                    break;
                case VENDOR_E_MANDLI + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationE-MandaliPopup";
                    break;
                case VENDOR_E_MANDLI + "-" + OP_MILK_SALE:
                    popupFor = "LocalMilkSaleMigrationEMANDLIPopup";
                    break;
                case VENDOR_PROMPT_SQL_SERVER + "-" + OP_MEMBER:
                    popupFor = "MemberDataMigrationPromptPopup";
                    break;
                case VENDOR_PROMPT_SQL_SERVER + "-" + OP_MILK_COLLECTION:
                    popupFor = "MilkCollectionDataMigrationPromptPopup";
                    break;

            }

            if (popupFor == null || popupFor.isEmpty())
                return;

            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), popupFor, null, null);

//            if (cboxType.getValue().equalsIgnoreCase("Member") && cboxFrom.getValue().equalsIgnoreCase("Prompt")) {
//                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MemberDataMigrationPopup", null, null);
//            } else if (cboxType.getValue().equalsIgnoreCase("Member") && cboxFrom.getValue().equalsIgnoreCase("SkyWay")) {
//                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MemberDataMigrationSkyWayPopup", null, null);
//            } else if (cboxType.getValue().equalsIgnoreCase("Milk Collection") && cboxFrom.getValue().equalsIgnoreCase("Prompt")) {
//                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkCollectionDataMigrationPopup", null, null);
//            } else if (cboxType.getValue().equalsIgnoreCase("Milk Collection") && cboxFrom.getValue().equalsIgnoreCase("SkyWay")) {
//                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkCollectionDataMigrationSkyWayPopup", null, null);
//            }
//            if (cboxType.getValue().equalsIgnoreCase("Local Milk Sale")) {
//                LocalMilkSaleDataMigrationController controller = (LocalMilkSaleDataMigrationController) MainApp.getFxmlLoaderUtil()
//                        .loadAndSet(MainApp.class.getResource("view/setting/LocalMilkSaleMigration.fxml"));
//                controller.loadImportPreReq(cboxFrom.getValue(), path);
//                paneContainer.setCenter((controller).getRoot());
//            }
//            if (cboxType.getValue().equalsIgnoreCase("Product")) {
//                ProductDataMigrationController controller = (ProductDataMigrationController) MainApp.getFxmlLoaderUtil()
//                        .loadAndSet(MainApp.class.getResource("view/setting/ProductMigration.fxml"));
//                controller.loadImportPreReq(cboxFrom.getValue(), path);
//                paneContainer.setCenter((controller).getRoot());
//            }
//            if (cboxType.getValue().equalsIgnoreCase("Product Sale")) {
//                ProductSaleDataMigrationController controller = (ProductSaleDataMigrationController) MainApp.getFxmlLoaderUtil()
//                        .loadAndSet(MainApp.class.getResource("view/setting/ProductSaleMigration.fxml"));
//                controller.loadImportPreReq(cboxFrom.getValue(), path);
//                paneContainer.setCenter((controller).getRoot());
//            }
        });
    }

    @Override
    public void setupComboBox() {
        cboxFrom.getItems().addAll(vendorList);
        cboxType.getItems().addAll(operationTypeList);
    }
}
