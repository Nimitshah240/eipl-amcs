package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.TaxDetailMappingDto;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.service.LedgerMappingTaxDetailService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.TaxService;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.eipl.amcs.MainApp.context;

public class LedgerMappingTaxDetailController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingTaxDetail> tableTaxDetailData;
    @FXML
    TableColumn<LedgerMappingTaxDetail, Tax> colTax, colTaxDetail;
    @FXML
    TableColumn<LedgerMappingTaxDetail, Ledger> colLedger;

    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;
    private LedgerMappingTaxDetailService ledgerMappingTaxDetailService;
    private ObservableList<Ledger> ledgerList;
    private TaxService taxService;
    private LedgerService ledgerService;

    public LedgerMappingTaxDetailController() {
        ledgerMappingTaxDetailService = context.getBean(LedgerMappingTaxDetailService.class);
        taxService = context.getBean(TaxService.class);
        ledgerService = context.getBean(LedgerService.class);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        loadData();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        btnSave.setOnAction(e -> {
            saveData();
        });
    }

    @Override
    public void saveData() {
        for (LedgerMappingTaxDetail item : tableTaxDetailData.getItems()) {
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
        }
        ledgerMappingTaxDetailService.save(tableTaxDetailData.getItems(), CommonUtil.setIdentityHeader());
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                resourceBundle.getString("save.successful"));
        alert.createAlert();
    }

    @Override
    public void setupTable() {
        colTax.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getTaxDetail().getTax()));
        colTaxDetail.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getTaxDetail() != null ? cell.getValue().getTaxDetail().getBasicTax() : ""));
        colLedger.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getLedger()));
        colLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedger.setOnEditCommit(event -> {
            LedgerMappingTaxDetail obj = event.getRowValue();
            obj.setLedger(event.getNewValue());
        });
    }

    private StringConverter<Ledger> converter = new StringConverter<>() {
        @Override
        public String toString(Ledger object) {
            if (object == null)
                return null;
            return object.toString();
        }

        @Override
        public Ledger fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return ledgerList.stream().filter(p -> p.toString().equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };

    @Override
    public void loadData() {
        try {
            CompletableFuture<List<TaxDto>> taxDtoFuture = CompletableFuture.supplyAsync(() -> taxService.findAll());
            CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> ledgerService.findAllByIsActive());
            CompletableFuture<List<LedgerMappingTaxDetail>> ledgerMappingTaxDetailFuture = CompletableFuture.supplyAsync(() -> ledgerMappingTaxDetailService.findAll());
            CompletableFuture.allOf(taxDtoFuture, ledgerListFuture, ledgerMappingTaxDetailFuture)
                    .whenCompleteAsync((result, ex) -> {
                        try {
                            List<TaxDto> taxDetailList = new ArrayList<>(taxDtoFuture.get());
                            List<LedgerMappingTaxDetail> mapping = ledgerMappingTaxDetailFuture.get();
                            List<Ledger> ledgerList = ledgerListFuture.get();

                            List<LedgerMappingTaxDetail> listMapping = new ArrayList<>(mapping);

                            for (TaxDto taxDto : taxDetailList) {
                                for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                                    LedgerMappingTaxDetail obj = listMapping.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(taxDetail.getCode()))
                                            .findFirst().orElse(null);

                                    if (obj == null) {
                                        LedgerMappingTaxDetail mp = new LedgerMappingTaxDetail();
                                        mp.setTaxDetail(taxDetail);
                                        mp.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                                        listMapping.add(mp);
                                    } else {
                                        obj.setTaxDetail(taxDetail);
                                        obj.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                                    }
                                }
                            }
                            List<Ledger> list = new ArrayList<>(ledgerList);
                            list.add(0, new Ledger("None")); //"0",
                            TaxDetailMappingDto dto = new TaxDetailMappingDto(listMapping, list);
                            setupTable();
                            tableTaxDetailData.setItems(FXCollections.observableList(dto.getListMapping()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Tax getTax(List<TaxDto> taxDetailList, String code) {
        for (TaxDto taxDto : taxDetailList) {
            for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                if (taxDetail.getCode().equalsIgnoreCase(code))
                    return taxDto.getTax();
            }
        }
        return null;
    }
}
