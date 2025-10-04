package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.Member;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MemberEditPasswordController implements MyInitialization {
    @FXML
    StackPane root;

    @FXML
    private TextField txtPassword;

    @FXML
    Button btnOk, btnClose;
    private Stage stage;
    public PopupCallback callback;
    private Member member;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private ResourceBundle resourceBundle;
    private ObjectProperty<Member> propMember;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnOk.setOnAction(e -> {
            if (txtPassword.getText().equalsIgnoreCase(LocalDate.now().format(DateTimeFormatter.ofPattern("ddyyMM")))) {
                this.stage.close();
                if (member != null) {
                    MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil()
                            .loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
                    controller.setMember(member);
                    MainApp.getContentPane().setCenter((controller).getRoot());
                }
            }
        });

        btnClose.setOnAction(e -> {
            this.stage.close();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Member.fxml")));
        });
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    public void setMember(Member member) {
        this.member = member;
    }
}
