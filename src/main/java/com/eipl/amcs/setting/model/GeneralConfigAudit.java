package com.eipl.amcs.setting.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "general_config_audit")
public class GeneralConfigAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    @Column(name = "json_key")
    private String key;
    private String value;

    private String societyCode;

    @Override
    public String getTableName() {
        return "general_config_audit";
    }


}
