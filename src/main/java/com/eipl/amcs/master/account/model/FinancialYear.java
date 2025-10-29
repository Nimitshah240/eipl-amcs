package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "financial_years")
public class FinancialYear extends BaseModel {

    @Id
    @Size(max = 15)
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public String getTableName() {
        return "financial_years";
    }

    @Override
    public String toString() {
        return startDate.getYear() + "-" + endDate.getYear();
    }
}
