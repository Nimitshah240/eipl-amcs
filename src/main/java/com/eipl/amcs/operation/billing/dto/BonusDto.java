package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BonusDto {
    private BonusSummary bonusSummary;
    private List<Bonus> bonusList;

//    public BonusSummary getBonusSummary() {
//        return bonusSummary;
//    }
//
//    public void setBonusSummary(BonusSummary bonusSummary) {
//        this.bonusSummary = bonusSummary;
//    }
//
//    public List<Bonus> getBonusList() {
//        return bonusList;
//    }
//
//    public void setBonusList(List<Bonus> bonusList) {
//        this.bonusList = bonusList;
//    }
}
