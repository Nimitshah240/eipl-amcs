package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.eipl.amcs.master.account.model.LedgerGroup;
import lombok.Getter;
import lombok.Setter;

/*
 * NO USAGE, SHIFT TO MODEL
 */
@Setter
@Getter
public class Ledger extends BaseModel {

    private String code;
    private String name;
    private String nameLocal;
    private Boolean hasSubLedger;
    private Short entryType;
    private LedgerGroup ledgerGroup;
    private Society society;
    private String unionCode;

    private BooleanProperty selected;

    public Ledger() {
        selected = new SimpleBooleanProperty(false);
    }
    public Ledger(String name) {
        selected = new SimpleBooleanProperty(false);

        this.name = name;
    }

    public final BooleanProperty selectedProperty() {
        return this.selected;
    }

    public final boolean isSelected() {
        return this.selectedProperty().get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public String toString() {
        return  CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
