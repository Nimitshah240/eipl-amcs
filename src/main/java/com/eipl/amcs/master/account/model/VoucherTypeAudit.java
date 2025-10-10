package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "voucher_types_audit")
public class VoucherTypeAudit extends BaseModelAudit {

    @Id
    private Integer code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @Override
    public String getTableName() {
        return "voucher_types_audit";
    }
}
