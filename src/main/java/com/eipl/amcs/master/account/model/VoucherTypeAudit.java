package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "voucher_types_audit")
public class VoucherTypeAudit extends BaseModelAudit {

    @Id
    private Integer code;
    private String name;
    private String nameLocal;

    @Override
    public String getTableName() {
        return "voucher_types_audit";
    }
}
