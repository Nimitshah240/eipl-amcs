package com.eipl.amcs.base.model;


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
@Table(name = "identity")
public class Identity extends BaseModelTxn {

    @Id
    private String code;
    private String token;
    private String societyRefCode;
    private String systemMac;
    private String syncStatus;
    private String syncUrl;
    private String unionCode;
    private String societyCode;
    private String dockNo;

    @Override
    public String getTableName() {
        return "identity";
    }
}
