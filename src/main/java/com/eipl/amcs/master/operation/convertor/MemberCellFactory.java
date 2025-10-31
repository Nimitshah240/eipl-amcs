package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.Member;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MemberCellFactory implements Callback<ListView<Member>, ListCell<Member>> {
    @Override
    public ListCell<Member> call(ListView<Member> param) {

        return new ListCell<Member>() {
            @Override
            protected void updateItem(Member item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.toMemberNameWithExCode());
                }
            }
        };
    }
}