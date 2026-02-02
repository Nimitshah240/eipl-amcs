package com.eipl.amcs.base.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.account.controller.*;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.insurance.controller.InsuranceDetailAddEditController;
import com.eipl.amcs.master.inventory.controller.ProductAddEditController;
import com.eipl.amcs.master.inventory.controller.ProductPurchaseRateAddEditController;
import com.eipl.amcs.master.inventory.controller.ProductSaleRateAddEditController;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.operation.controller.BillCriteriaAddEditController;
import com.eipl.amcs.master.operation.controller.BillHeadAddEditController;
import com.eipl.amcs.master.operation.controller.MemberEditPasswordController;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.controller.DockAddEditController;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.procurement.controller.LocalMilkSaleRateAddEditController;
import com.eipl.amcs.master.procurement.controller.MilkRateViewController;
import com.eipl.amcs.master.procurement.controller.SocietyPaymentCycleEditController;
import com.eipl.amcs.master.procurement.controller.SocietyPaymentCycleGenerateController;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.administartion.controller.*;
import com.eipl.amcs.operation.billing.controller.*;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.inventory.controller.KapaatAddEditController;
import com.eipl.amcs.operation.inventory.controller.ProductReceiptTransactionAddController;
import com.eipl.amcs.operation.inventory.controller.ProductSaleInstallmentController;
import com.eipl.amcs.operation.inventory.controller.ProductSaleTransactionAddController;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.procurement.controller.*;
import com.eipl.amcs.operation.procurement.dto.CollectionEditDelete;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.share.controller.*;
import com.eipl.amcs.setting.controller.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MappingPopupController implements MyInitialization {

    @FXML
    private BorderPane root;

    private Stage stage;
    private Object object;
    private PopupCallback callback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setResizable(false);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setForResource(String forResource) throws ExecutionException, InterruptedException {
        try {
            switch (forResource) {
                case "DockAddEdit":
                    var controller = (DockAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/org/DockAddEdit.fxml"));
                    controller.setDockMilkTypeDto(object != null ? (DockMilkTypeDto) object : null);
                    controller.setStage(stage);
                    controller.setCallback(callback);
                    root.setCenter(controller.getRoot());
                    break;
                case "LocalMilkSaleRateAddEdit":
                    var controller2 = (LocalMilkSaleRateAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/procurement/LocalMilkSaleRateAddEdit.fxml"));
                    controller2.setStage(stage);
                    controller2.setCallback(callback);
                    root.setCenter(controller2.getRoot());
                    break;
                case "SocietyPaymentCycleEdit":
                    var controller3 = (SocietyPaymentCycleEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/procurement/SocietyPaymentCycleEdit.fxml"));
                    controller3.setSocietyPaymentCycle(object != null ? (SocietyPaymentCycle) object : null);
                    controller3.setStage(stage);
                    controller3.setCallback(callback);
                    root.setCenter(controller3.getRoot());
                    break;
                case "SocietyPaymentCycleGenerate":
                    var controller4 = (SocietyPaymentCycleGenerateController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/procurement/SocietyPaymentCycleGenerate.fxml"));
                    controller4.setStage(stage);
                    controller4.setCallback(callback);
                    root.setCenter(controller4.getRoot());
                    break;
                case "ProductAddEdit":
                    var controller5 = (ProductAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/inventory/ProductAddEdit.fxml"));
                    controller5.setProduct(object != null ? (Product) object : null);
                    controller5.setStage(stage);
                    controller5.setCallback(callback);
                    root.setCenter(controller5.getRoot());
                    break;
                case "ProductPurchaseRateAddEdit":
                    var controller6 = (ProductPurchaseRateAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/inventory/ProductPurchaseRateAddEdit.fxml"));
                    controller6.setProductPurchaseRate(object != null ? (ProductPurchaseRate) object : null);
                    controller6.setStage(stage);
                    controller6.setCallback(callback);
                    root.setCenter(controller6.getRoot());
                    break;
                case "ProductSaleRateAddEdit":
                    var controller7 = (ProductSaleRateAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/inventory/ProductSaleRateAddEdit.fxml"));
                    controller7.setProductSaleRate(object != null ? (ProductSaleRate) object : null);
                    controller7.setStage(stage);
                    controller7.setCallback(callback);
                    root.setCenter(controller7.getRoot());
                    break;
                case "InstallmentsAddEdit":
                    var controller8 = (ProductSaleInstallmentController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductInstallmentsDetails.fxml"));
                    controller8.setInstallments(object != null ? (List<ProductSaleInstallment>) object : null);
                    controller8.setStage(stage);
                    controller8.setCallback(callback);
                    root.setCenter(controller8.getRoot());
                    break;
                case "LocalMilkSaleAddEdit":
                    var controller9 = (LocalMilkSaleAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/LocalMilkSaleAddEdit.fxml"));
                    controller9.setLocalMilkSaleDto(object != null ? (LocalMilkSale) object : null);
                    controller9.setStage(stage);
                    controller9.setCallback(callback);
                    root.setCenter(controller9.getRoot());
                    break;
                case "RateView":
                    var controller10 = (MilkRateViewController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/procurement/MilkPurchaseRateView.fxml"));
                    controller10.setStage(stage);
                    controller10.setRateViewDto(object != null ? (RateViewDto) object : null);
                    root.setCenter(controller10.getRoot());
                    break;
                case "TaxDetail":
                    var controller11 = (TaxDetailController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/TaxDetail.fxml"));
                    controller11.setStage(stage);
                    controller11.setDto(object != null ? (TaxDto) object : null);
                    root.setCenter(controller11.getRoot());
                    break;
                case "CollectionSetting":
                    var controller12 = (CollectionSettingController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/CollectionSetting.fxml"));
                    controller12.setStage(stage);
                    controller12.setCallback(callback);
                    root.setCenter(controller12.getRoot());
                    break;
                case "ProductReceiptTransaction":
                    var controller13 = (ProductReceiptTransactionAddController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductReceiptTransactionAdd.fxml"));
                    controller13.setReceiptTxnDto((ReceiptTxnDto) object);
                    controller13.setStage(stage);
                    controller13.setCallback(callback);
                    root.setCenter(controller13.getRoot());
                    break;
                case "ProductSaleTransaction":
                    var controller14 = (ProductSaleTransactionAddController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/ProductSaleTransactionAdd.fxml"));
                    controller14.setSaleTxnDto((SaleTxnDto) object);
                    controller14.setStage(stage);
                    controller14.setCallback(callback);
                    root.setCenter(controller14.getRoot());
                    break;
                case "MilkCollectionEditDelete":
                    var controller15 = (MilkCollectionEditDeleteController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/MilkCollectionEditDelete.fxml"));
                    controller15.setCollectionEditDelete((CollectionEditDelete) object);
                    controller15.setStage(stage);
                    controller15.setCallback(callback);
                    root.setCenter(controller15.getRoot());
                    break;
                case "MemberBillTransaction":
                    var controller16 = (MemberBillTransactionController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/MemberBillTransaction.fxml"));
                    controller16.setMemberBill((MemberBill) object);
                    controller16.setStage(stage);
                    controller16.setCallback(callback);
                    root.setCenter(controller16.getRoot());
                    break;
                case "MilkSummaryDataEntryAddEdit":
                    var controller17 = (MilkSummaryDataEntryAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/MilkCollectionDataEntryAddEdit.fxml"));
                    controller17.setMilkSummaryDataEntry((MilkCollection) object);
                    controller17.setStage(stage);
                    controller17.setCallback(callback);
                    root.setCenter(controller17.getRoot());
                    break;
                case "MemberDataMigrationPopup":
                    var controller18 = (MemberDataMigrationController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MemberMigration.fxml"));
                    controller18.setStage(stage);
                    root.setCenter(controller18.getRoot());
                    break;
                case "MemberDataMigrationSkyWayPopup":
                    var controller20 = (MemberDataMigrationSkyWayController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MemberMigrationSkyWay.fxml"));
                    controller20.setStage(stage);
                    root.setCenter(controller20.getRoot());
                    break;
                case "MilkCollectionDataMigrationPopup":
                    var controller19 = (MilkCollectionDataMigrationController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigration.fxml"));
                    controller19.setStage(stage);
                    root.setCenter(controller19.getRoot());
                    break;
                case "MilkCollectionDataMigrationSkyWayPopup":
                    var controller21 = (MilkCollectionDataMigrationSkyWayController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigrationSkyWay.fxml"));
                    controller21.setStage(stage);
                    root.setCenter(controller21.getRoot());
                    break;
                case "LocalMilkSaleMigrationPopup":
                    var controller22 = (LocalMilkSaleDataMigrationController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/LocalMilkSaleMigration.fxml"));
                    controller22.setStage(stage);
                    root.setCenter(controller22.getRoot());
                    break;
                case "ProductMigrationPopup":
                    var controller23 = (ProductDataMigrationController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/ProductMigration.fxml"));
                    controller23.setStage(stage);
                    root.setCenter(controller23.getRoot());
                    break;
                case "ProductSaleMigrationPopup":
                    var controller24 = (ProductSaleDataMigrationController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/ProductSaleMigration.fxml"));
                    controller24.setStage(stage);
                    root.setCenter(controller24.getRoot());
                    break;
                case "MilkCollectionDataMigrationFriendsPopup":
                    var controller25 = (MilkCollectionDataMigrationFriendsController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigrationFriends.fxml"));
                    controller25.setStage(stage);
                    root.setCenter(controller25.getRoot());
                    break;
                case "MemberDataMigrationFriendsPopup":
                    var controller26 = (MemberDataMigrationFriendsController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MemberMigrationFriends.fxml"));
                    controller26.setStage(stage);
                    root.setCenter(controller26.getRoot());
                    break;
                case "MemberDataMigrationE-MandaliPopup":
                    var controller27 = (MemberDataMigrationEMandaliController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MemberMigrationEMandali.fxml"));
                    controller27.setStage(stage);
                    root.setCenter(controller27.getRoot());
                    break;
                case "MilkCollectionDataMigrationE-MandaliPopup":
                    var controller28 = (MilkCollectionDataMigrationEMandaliController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigrationEMandali.fxml"));
                    controller28.setStage(stage);
                    root.setCenter(controller28.getRoot());
                    break;
                case "MemberDataMigrationPromptPopup":
                    var controller29 = (MemberDataMigrationPromptController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MemberMigrationPrompt.fxml"));
                    controller29.setStage(stage);
                    root.setCenter(controller29.getRoot());
                    break;
                case "MilkCollectionDataMigrationPromptPopup":
                    var controller30 = (MilkCollectionDataMigrationPromptController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigrationPrompt.fxml"));
                    controller30.setStage(stage);
                    root.setCenter(controller30.getRoot());
                    break;
                case "LocalMilkSaleDataMigrationFriendsPopup":
                    var controller31 = (LocalMilkSaleDataMigrationFriendsController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/LocalMilkSaleMigrationFriends.fxml"));
                    controller31.setStage(stage);
                    root.setCenter(controller31.getRoot());
                    break;
                case "LocalMilkSaleMigrationEMANDLIPopup":
                    var controller32 = (LocalMilkSaleDataMigrationEMandliController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/LocalMilkSaleMigrationEMandli.fxml"));
                    controller32.setStage(stage);
                    root.setCenter(controller32.getRoot());
                    break;
                case "CommitteeMembersAddEdit":
                    var controller33 = (CommitteeMembersAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/administration/CommitteeMembersAddEdit.fxml"));
                    controller33.setStage(stage);
                    controller33.setCallback(callback);
                    controller33.setCommitteeMembers(object != null ? (CommitteeMembers) object : null);
                    root.setCenter(controller33.getRoot());
                    break;
                case "MeetingAddEdit":
                    var controller34 = (MeetingAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/administration/MeetingAddEdit.fxml"));
                    controller34.setStage(stage);
                    controller34.setCallback(callback);
                    controller34.setMeeting(object != null ? (MeetingAgenda) object : null);
                    root.setCenter(controller34.getRoot());
                    break;
                case "SalaryHeadAddEdit":
                    var controller35 = (SalaryHeadAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/administration/SalaryHeadAddEdit.fxml"));
                    controller35.setCommitteeMembers(object != null ? (StaffSalaryHead) object : null);
                    controller35.setStage(stage);
                    controller35.setCallback(callback);
                    root.setCenter(controller35.getRoot());
                    break;
                case "MomAction":
                    var controller36 = (MomActionController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/administration/MomAction.fxml"));
                    controller36.setMom(object != null ? (Mom) object : null);
                    controller36.setStage(stage);
                    controller36.setCallback(callback);
                    root.setCenter(controller36.getRoot());
                    break;
                case "BankReportPopup":
                    var controller50 = (BankReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/BankReport.fxml"));
                    controller50.setSummay(object != null ? (MemberBillSummary) object : null);
                    controller50.setStage(stage);
                    controller50.setCallback(callback);
                    root.setCenter(controller50.getRoot());
                    break;
                case "GeneralReportPopup":
                    var controller53 = (GeneralReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/GeneralReport.fxml"));
                    controller53.setSummay(object != null ? (MemberBillSummary) object : null);
                    controller53.setStage(stage);
                    controller53.setCallback(callback);
                    root.setCenter(controller53.getRoot());
                    break;
                case "MilkDispatchReportPopup":
                    var controller51 = (MilkDispatchReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/MilkDispatchReport.fxml"));
                    controller51.setDispatch(object != null ? (MilkDispatch) object : null);
                    controller51.setStage(stage);
                    controller51.setCallback(callback);
                    root.setCenter(controller51.getRoot());
                    break;
                case "StaffSalary":
                    var controller52 = (StaffSalaryReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/administration/StaffSalaryReport.fxml"));
                    controller52.setSummay(object != null ? (MemberBillSummary) object : null);
                    controller52.setStage(stage);
                    controller52.setCallback(callback);
                    root.setCenter(controller52.getRoot());
                    break;
                case "LedgerGroupAddEdit":
                    var LedgerGroupAddEdit = (LedgerGroupAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/LedgerGroupAddEdit.fxml"));
                    LedgerGroupAddEdit.setLedgerGroup(object != null ? (LedgerGroup) object : null);
                    LedgerGroupAddEdit.setStage(stage);
                    LedgerGroupAddEdit.setCallback(callback);
                    root.setCenter(LedgerGroupAddEdit.getRoot());
                    break;
                case "LedgerTypeAddEdit":
                    var controller55 = (LedgerTypeAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/LedgerTypeAddEdit.fxml"));
                    controller55.setLedgerType(object != null ? (LedgerType) object : null);
                    controller55.setStage(stage);
                    controller55.setCallback(callback);
                    root.setCenter(controller55.getRoot());
                    break;
                case "VoucherTypeAddEdit":
                    var controller54 = (VoucherTypeAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/VoucherTypeAddEdit.fxml"));
                    controller54.setVoucherType(object != null ? (VoucherType) object : null);
                    controller54.setStage(stage);
                    controller54.setCallback(callback);
                    root.setCenter(controller54.getRoot());
                    break;
                case "VoucherSubLedger":
                    var controller56 = (VoucherSubLedgerController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/VoucherSubLedger.fxml"));
                    controller56.setVoucherTransaction(object != null ? (VoucherTransaction) object : null);
                    controller56.setStage(stage);
                    controller56.setCallback(callback);
                    root.setCenter(controller56.getRoot());
                    break;
                case "SubLedgerView":
                    var SubLedgerView = (SubLedgerOpeningBalanceViewController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/report/account/SubLedgerOpningBalanceList.fxml"));
                    SubLedgerView.setStage(stage);
                    SubLedgerView.setCallback(callback);
                    root.setCenter(SubLedgerView.getRoot());
                    break;
                case "VoucherSubLedgerPopup":
                    var VoucherSubLedger = (VoucherSubLedgerPopupController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/account/VoucherSubLedgerPopup.fxml"));
                    VoucherSubLedger.setVoucherTransaction(object != null ? (VoucherTransaction) object : null);
                    VoucherSubLedger.setStage(stage);
                    VoucherSubLedger.setCallback(callback);
                    root.setCenter(VoucherSubLedger.getRoot());
                    break;
                case "KapaatAddEdit":
                    var KapaatAddEdit = (KapaatAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/inventory/KapaatAddEdit.fxml"));
                    KapaatAddEdit.setStage(stage);
                    KapaatAddEdit.setCallback(callback);
                    root.setCenter(KapaatAddEdit.getRoot());
                    break;

                case "BonusReportPopup":
                    var BonusReportPopup = (BonusReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/BonusReport.fxml"));
                    BonusReportPopup.setSummay(object != null ? (BonusSummary) object : null);
                    BonusReportPopup.setStage(stage);
                    BonusReportPopup.setCallback(callback);
                    root.setCenter(BonusReportPopup.getRoot());
                    break;

                case "BonusReportGeneralPopup":
                    var BonusReportGeneralPopup = (GeneralBonusReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/billing/GeneralBonusReport.fxml"));
                    BonusReportGeneralPopup.setSummay(object != null ? (BonusSummary) object : null);
                    BonusReportGeneralPopup.setStage(stage);
                    BonusReportGeneralPopup.setCallback(callback);
                    root.setCenter(BonusReportGeneralPopup.getRoot());
                    break;

                case "SyncPopup":
                    var SyncPopup = (SyncDataController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/SyncData.fxml"));
                    SyncPopup.setStage(stage);
                    SyncPopup.setCallback(callback);
                    root.setCenter(SyncPopup.getRoot());
                    break;

                case "ShareIssueAddEdit":
                    var sharepopup = (ShareIssueAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareIssueAddEdit.fxml"));
                    sharepopup.setStage(stage);
                    sharepopup.setCallback(callback);
                    root.setCenter(sharepopup.getRoot());
                    break;

                case "ShareTransferAddEdit":
                    var shareTransfer = (ShareTransferAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareTransferAddEdit.fxml"));
                    shareTransfer.setStage(stage);
                    shareTransfer.setCallback(callback);
                    root.setCenter(shareTransfer.getRoot());
                    break;

                case "ShareRateAddEdit":
                    var ShareRateAddEdit = (ShareRateAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareRateAddEdit.fxml"));
                    ShareRateAddEdit.setStage(stage);
                    ShareRateAddEdit.setCallback(callback);
                    root.setCenter(ShareRateAddEdit.getRoot());
                    break;

                case "IssueReportPopup":
                    var IssueReportPopup = (ShareIssueReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareIssueReport.fxml"));
                    IssueReportPopup.setStage(stage);
                    IssueReportPopup.setCallback(callback);
                    root.setCenter(IssueReportPopup.getRoot());
                    break;

                case "ShareCancelReportPopup":
                    var CancelReportPopup = (ShareCancelReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareCancelReport.fxml"));
                    CancelReportPopup.setStage(stage);
                    CancelReportPopup.setCallback(callback);
                    root.setCenter(CancelReportPopup.getRoot());
                    break;

                case "ShareTransferReportPopup":
                    var TransferReportPopup = (ShareTransferReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareTransferReport.fxml"));
                    TransferReportPopup.setStage(stage);
                    TransferReportPopup.setCallback(callback);
                    root.setCenter(TransferReportPopup.getRoot());
                    break;

                case "DividendReportPopup":
                    var DividendReportPopup = (ShareDividendReportController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ShareDividendReport.fxml"));
                    DividendReportPopup.setStage(stage);
                    DividendReportPopup.setCallback(callback);
                    root.setCenter(DividendReportPopup.getRoot());
                    break;

                case "MilkCollectionDataMigrationHisaabMitraPopup":
                    var HisaabMitraPopup = (MilkCollectionDataMigrationHisaabMitraController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/setting/MilkCollectionMigrationHisaabMitra.fxml"));
                    HisaabMitraPopup.setStage(stage);
                    root.setCenter(HisaabMitraPopup.getRoot());
                    break;


                case "MemberEditPopup":
                    var controller57 = (MemberEditPasswordController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/MemberEditPassword.fxml"));
                    controller57.setMember(object != null ? (Member) object : null);
                    controller57.setStage(stage);
                    controller57.setCallback(callback);
                    root.setCenter(controller57.getRoot());
                    break;
                case "InsuranceDetailAddEdit":
                    var controller58 = (InsuranceDetailAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/insurance/InsuranceDetailAddEdit.fxml"));
                    controller58.setInsuranceDetailDto(object != null ? (Map<String, Object>) object : null);
                    controller58.setStage(stage);
                    controller58.setCallback(callback);
                    root.setCenter(controller58.getRoot());
                    break;
                case "SyncDataList":
                    var SyncDataList = (SyncDataListController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/dashboard/SyncDataList.fxml"));
                    SyncDataList.setStage(stage);
                    SyncDataList.setCallback(callback);
                    root.setCenter(SyncDataList.getRoot());
                    break;
                case "BillHeadAddEdit":
                    var controller59 = (BillHeadAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/BillHeadAddEdit.fxml"));
                    controller59.setBillHead(object != null ? (BillHead) object : null);
                    controller59.setStage(stage);
                    controller59.setCallback(callback);
                    root.setCenter(controller59.getRoot());
                    break;
                case "BillCriteriaAddEdit":
                    var controller60 = (BillCriteriaAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/BillCriteriaAddEdit.fxml"));
                    controller60.setBillCriteria(object != null ? (BillCriteria) object : null);
                    controller60.setStage(stage);
                    controller60.setCallback(callback);
                    root.setCenter(controller60.getRoot());
                    break;
                case "LicenseActivatePopUp":
                    var controller61 = (LicenseActivateController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/LicenseActivatePopUp.fxml"));
                    controller61.setStage(stage);
                    controller61.setCallback(callback);
                    root.setCenter(controller61.getRoot());
                case "CouponIssueAddEdit":
                    var controller62 = (CouponIssueAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/CouponIssueAddEdit.fxml"));
                    controller62.setCouponIssue(object != null ? (CouponIssue) object : null);
                    controller62.setStage(stage);
                    controller62.setCallback(callback);
                    root.setCenter(controller62.getRoot());
                    break;
                case "CouponBalance":
                    var controller63 = (CouponBalanceController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/procurement/CouponBalanceTransaction.fxml"));
                    controller63.setStage(stage);
                    controller63.setCallback(callback);
                    root.setCenter(controller63.getRoot());
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
