package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NewGeneratedRateViewTask extends Task<List<String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewGeneratedRateViewTask.class);
    private final RateViewDto rateViewDto;
    private final MilkType milkType;
    private final MilkQualityType milkQualityType;

    public NewGeneratedRateViewTask(RateViewDto rateViewDto, MilkType milkType, MilkQualityType milkQualityType) {
        this.rateViewDto = rateViewDto;
        this.milkType = milkType;
        this.milkQualityType = milkQualityType;
    }

    @Override
    protected List<String> call() throws Exception {
        try {
            List<String> listStr = null;
            if (rateViewDto.getRateType() == (short) 0) {
                List<MemberMilkPurchaseRateDetail> details = rateViewDto.getMemberRate().getListDetails();
                List<MemberMilkPurchaseRateDetail> listDetails = details.stream()
                        .filter(d -> Objects.equals(d.getMilkType().getCode(), milkType.getCode()))
                        .filter(d -> Objects.equals(d.getMilkQualityType().getCode(), milkQualityType.getCode()))
                        .sorted(
                                Comparator.comparing(MemberMilkPurchaseRateDetail::getFat,
                                                Comparator.nullsLast(BigDecimal::compareTo))
                                        .thenComparing(MemberMilkPurchaseRateDetail::getSnf,
                                                Comparator.nullsLast(BigDecimal::compareTo))
                        )
                        .collect(Collectors.toList());

                listStr = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#"
                        + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode()).collect(Collectors.toList());

                if (listStr == null || listStr.isEmpty()) {
                    return null;
                }
            } else {
//                TODO NIMIT - FOR FUTURE SOCIETY MILK PURHCASE RATE DETAIL.... DON'T DELETE
//                List<SocietyMilkPurchaseRateDetail> details = rateViewDto.getSocietyRate().getListDetails();
//                List<SocietyMilkPurchaseRateDetail> listDetails = details.stream()
//                        .filter(d -> Objects.equals(d.getMilkType(), milkType))
//                        .filter(d -> Objects.equals(d.getMilkQualityType(), milkQualityType))
//                        .sorted(
//                                Comparator.comparing(SocietyMilkPurchaseRateDetail::getFat,
//                                                Comparator.nullsLast(BigDecimal::compareTo))
//                                        .thenComparing(SocietyMilkPurchaseRateDetail::getSnf,
//                                                Comparator.nullsLast(BigDecimal::compareTo))
//                        )
//                        .collect(Collectors.toList());
//
//                listStr = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#"
//                        + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode()).collect(Collectors.toList());
//
//                if (listStr == null || listStr.isEmpty()) {
//                    return null;
//                }
            }
            return listStr;
        } catch (Exception e) {
            LOGGER.error("Rate Fetch fetch", e);
        }
        return null;
    }

}
