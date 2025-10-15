package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.model.MemberEkyc;
import com.eipl.amcs.master.operation.task.AllMemberDetailsLoadTask;
import com.eipl.amcs.master.operation.task.MemberEkycLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.utils.CommonUtils.getMemberShortCode;

public class MemberEkycController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
    @FXML
    private TableView<MemberEkyc> tableMemberEkyc;
    @FXML
    private TableColumn<MemberEkyc, String> colCode, colName, colMobileNo, colAadharNo, colIsStatus;
    @FXML
    private TableColumn<MemberEkyc, Void> colIsVerified;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnClose;
    private final ObjectProperty<MemberEkyc> propMember = new SimpleObjectProperty<>();
    private List<MemberEkyc> listMember = new ArrayList<>();
    private Map<String, MemberDetail> mapDetails = new HashMap<>();
    private Stage stage;
    public PopupCallback callback;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ResourceBundle resourceBundle;
    public List<Member> memberList = new ArrayList<>();
    private String memberCode;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadDetails();
//        loadData();
        btnClose.setOnAction(e ->
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

//    private void loadDetails() {
//        var task = new AllMemberDetailsLoadTask();
//        task.setOnSucceeded(e -> {
//            try {
//                List<MemberDetail> list = task.get();
//                for (MemberDetail memberDetail : list) {
//                    mapDetails.put(memberDetail.getCode(), memberDetail);
//                }
//                setupTable();
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            } catch (ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
//    }

    private void loadDetails() {
        var task = new AllMemberDetailsLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberDetail> list = task.get();
                for (MemberDetail memberDetail : list) {
                    mapDetails.put(memberDetail.getCode(), memberDetail);
                }
                setupTable();
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                lblStatus.setText("Error loading member details.");
            }
        });

        new Thread(task).start();
    }

    private StringBuilder errorMsg;
    private List<MemberDto> listMemberDto;

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data ->
                    new SimpleStringProperty(getMemberShortCode(String.valueOf(data.getValue().getCode())))
            );

            colName.setCellValueFactory(data -> {
                Member m = data.getValue() != null ? data.getValue().getMember() : null;
                if (m != null) {
                    String fullName = (m.getFirstName() != null ? m.getFirstName() + " " : "") +
                            (m.getMiddleName() != null ? m.getMiddleName() + " " : "") +
                            (m.getLastName() != null ? m.getLastName() : "");
                    return new SimpleStringProperty(fullName.trim());
                } else {
                    return new SimpleStringProperty("");
                }
            });

            colMobileNo.setCellValueFactory(data -> {
                Member member = data.getValue() != null ? data.getValue().getMember() : null;
                String mobileNo = (member != null && member.getMobileNo() != null) ? member.getMobileNo() : "";
                return new SimpleStringProperty(mobileNo);
            });


            colAadharNo.setCellValueFactory(data -> {
                Member member = data.getValue() != null ? data.getValue().getMember() : null;
                if (member != null && member.getCode() != null) {
                    MemberDetail detail = mapDetails.get(member.getCode());
                    String aadharNo = (detail != null && detail.getAadharNo() != null) ? detail.getAadharNo() : "";
                    return new SimpleStringProperty(aadharNo);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            colIsStatus.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getStatus())
            );
            colIsVerified.setCellFactory(param -> new TableCell<>() {
                private final Button verifyButton = new Button("Verify");
                {
                    verifyButton.setMaxWidth(Double.MAX_VALUE);
                    verifyButton.setOnAction(event -> {
                        MemberEkyc currentItem = getTableView().getItems().get(getIndex());
                        System.out.println("Verify button clicked for: " + currentItem.getCode());

                        if ("Verified".equalsIgnoreCase(currentItem.getStatus())) {
                            currentItem.setStatus("Unverified");
                            verifyButton.setText("Verify");
                        } else {
                            currentItem.setStatus("Verified");
                            verifyButton.setText("Unverify");
                        }

                        getTableView().refresh();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        MemberEkyc currentItem = getTableView().getItems().get(getIndex());
                        verifyButton.setText(
                                "Verified".equalsIgnoreCase(currentItem.getStatus()) ? "Unverify" : "Verify"
                        );
                        verifyButton.prefWidthProperty().bind(this.widthProperty());
                        setGraphic(verifyButton);
                    }
                }
            });


            propMember.bind(tableMemberEkyc.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("setupTable Exception");
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableMemberEkyc.setItems(null);
        lblStatus.setText("Loading...");

        MemberEkycLoadTask task = new MemberEkycLoadTask();

        task.setOnSucceeded(e -> {
            try {
                List<MemberEkyc> memberList = task.get();
                if (memberList != null && !memberList.isEmpty()) {
                    listMember = memberList;
                    tableMemberEkyc.setItems(FXCollections.observableArrayList(memberList));
                    lblStatus.setText("Loaded " + memberList.size() + " records.");
                } else {
                    tableMemberEkyc.setItems(FXCollections.observableArrayList()); // clear table
                    lblStatus.setText("No records found.");
                }
            } catch (InterruptedException | ExecutionException ex) {
                lblStatus.setText("Error loading data.");
                ex.printStackTrace();
            }
        });

        task.setOnFailed(e -> {
            lblStatus.setText("Failed to load data.");
            Throwable ex = task.getException();
            if (ex != null) {
                ex.printStackTrace();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
