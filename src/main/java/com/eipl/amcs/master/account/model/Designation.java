package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "designation")
public class Designation extends BaseModelTxn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    private String name;
    // 0 - Staff, 1 - Commitee(from nddb)
    private Integer type;

    @Override
    public String getTableName() {
        return "designation";
    }
}
