package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.Member;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MemberEditPasswordController implements MyInitialization {
    public PopupCallback callback;
    @FXML
    StackPane root;
    @FXML
    Button btnOk, btnClose;
    @FXML
    private TextField txtPassword;
    private Stage stage;
    private Member member;
    @FXML
    private Label lblincorrectpassword;
    private ResourceBundle resourceBundle;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblincorrectpassword.setVisible(false);
        this.resourceBundle = resourceBundle;
        btnOk.setOnAction(e -> {
            if (txtPassword.getText().equalsIgnoreCase(LocalDate.now().format(DateTimeFormatter.ofPattern("ddyyMM")))) {
                this.stage.close();
                MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
                controller.setMember(member);
                MainApp.getContentPane().setCenter(controller.getRoot());
            } else {
                lblincorrectpassword.setVisible(true);
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
