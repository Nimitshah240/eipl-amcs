package com.eipl.amcs.setting.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "milk_collection_account_posting")
@Data
public class MilkCollectionAccountPosting extends BaseModelTxn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer fromShift;
    private Integer toShift;
    private Integer postingType; // 1 - Consolidate, 2 - Day Wise
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "milk_collection_account_posting";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }
}