package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.repository.RateTypeRepository;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.NumberUtil;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GenerateFromParamTask extends Task<MemberMilkPurchaseRate> {
    private MemberMilkPurchaseRate memberMilkPurchaseRate;
    private List<MemberMilkPurchaseRateBased> memberMilkPurchaseRateBasedList;

    private RateTypeRepository rateTypeRepository;

    public GenerateFromParamTask(MemberMilkPurchaseRate memberMilkPurchaseRate, List<MemberMilkPurchaseRateBased> memberMilkPurchaseRateBased) {
        this.memberMilkPurchaseRate = memberMilkPurchaseRate;
        this.memberMilkPurchaseRateBasedList = memberMilkPurchaseRateBased;
        rateTypeRepository = EmcsAppContext.getContext().getBean(RateTypeRepository.class);
    }

    @Override
    protected MemberMilkPurchaseRate call() throws Exception {
        try {
            // Single Axis Rate Chart
            for (MemberMilkPurchaseRateBased tempBased : memberMilkPurchaseRateBasedList) {
                if (tempBased.getRateType() == 1) {
                    for (BigDecimal p1 = tempBased.getStartVal();
                         p1.compareTo(tempBased.getEndVal()) <= 0;
                         p1 = NumberUtil.round(p1.add(BigDecimal.valueOf(0.1)), 1)) {
                        MemberMilkPurchaseRateDetail dtl = new MemberMilkPurchaseRateDetail();
                        dtl.setFat(p1);
                        dtl.setMilkQualityType(tempBased.getMilkQualityType());
                        dtl.setMilkType(tempBased.getMilkType());
                        dtl.setMemberMilkPurchaseRate(memberMilkPurchaseRate);
                        dtl.setRate(NumberUtil.round(getRtplWithAdditionDeduction(p1, new BigDecimal(0),
                                String.valueOf(tempBased.getRateType()), "", tempBased, null), 2));
                        dtl.setSnf(BigDecimal.valueOf(0));
                        log.info("{} # {} # {} # {} # {}",tempBased.getMilkType(), tempBased.getMilkQualityType(), dtl.getFat(),dtl.getSnf(), dtl.getRate() );
                        memberMilkPurchaseRate.getListDetails().add(dtl);
                    }
                }
            }

            // Dual Axis Rate Chart
            for (MemberMilkPurchaseRateBased tempBased : memberMilkPurchaseRateBasedList) {

                if (tempBased.getQualityParam() != 1)
                    continue;

                if (tempBased.getRateType() == 2) { //TODO CHANGE LATER, MAKE IT DYNAMIC
                    List<MemberMilkPurchaseRateBased> tempRange2 = memberMilkPurchaseRateBasedList.stream()
                            .filter(p -> p.getMilkType().getCode() == tempBased.getMilkType()
                                    .getCode() && p.getQualityParam() != 1)
                            .collect(Collectors.toList());
                    for (BigDecimal p1 = tempBased.getStartVal(); p1.compareTo(tempBased.getEndVal()) <= 0; p1 = NumberUtil.round(p1.add(BigDecimal.valueOf(0.1)), 1)) {
                        for (MemberMilkPurchaseRateBased tempBased2 : tempRange2) {
                            for (BigDecimal p2 = tempBased2.getStartVal(); p2.compareTo(tempBased2
                                    .getEndVal()) <= 0; p2 = NumberUtil.round(p2.add(new BigDecimal(0.1)), 1)) {
                                MemberMilkPurchaseRateDetail dtl = new MemberMilkPurchaseRateDetail();
                                dtl.setFat(p1);
                                dtl.setMilkQualityType(tempBased.getMilkQualityType());
                                dtl.setMilkType(tempBased.getMilkType());
                                dtl.setMemberMilkPurchaseRate(memberMilkPurchaseRate);
                                dtl.setRate(NumberUtil.round(getRtplWithAdditionDeduction(p1, p2,
                                        String.valueOf(tempBased.getRateType()), "", tempBased, tempBased2), 2));
                                dtl.setSnf(p2);
                                log.info("{} # {} # {} # {} # {}",tempBased.getMilkType(), tempBased.getMilkQualityType(), dtl.getFat(),dtl.getSnf(), dtl.getRate() );
                                memberMilkPurchaseRate.getListDetails().add(dtl);
                            }
                        }
                    }
                }
            }
            return memberMilkPurchaseRate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BigDecimal getRate(BigDecimal p1, BigDecimal p2, String param1, String param2, MilkType milkType,
                               MemberMilkPurchaseRateBased based) {
        String str = getFormula(p1, p2, milkType);
        if (str.length() > 0) {
            RateType rateType = rateTypeRepository.findById(based.getRateType()).get();
            if (rateType.getRateType().contains("+")) {
                /**
                 * Replace KGPARAM
                 */
                str = str.replaceAll("KG" +
                                rateType.getRateType().substring(0, rateType.getRateType().indexOf('+')),
                        getKgRateOfParam(p1, milkType,
                                rateType.getRateType().substring(0, rateType.getRateType().indexOf('+'))));
                str = str.replaceAll("KG" +
                                rateType.getRateType().substring(rateType.getRateType().indexOf('+') + 1),
                        getKgRateOfParam(p2, milkType,
                                rateType.getRateType().substring(rateType.getRateType().indexOf('+') + 1)));
                /**
                 * Replace PARAM
                 */
                str = str.replaceAll(rateType.getRateType().substring(0, rateType.getRateType().indexOf('+')),
                        p1 + "");
                str = str.replaceAll(rateType.getRateType().substring(rateType.getRateType().indexOf('+') + 1),
                        p2 + "");
            } else {
                /**
                 * Replace KGPARAM
                 */
                str = str.replaceAll("KG" + rateType.getRateType(),
                        getKgRateOfParam(p1, milkType, rateType.getRateType()));
                /**
                 * Replace PARAM
                 */
                str = str.replaceAll(rateType.getRateType(), p1 + "");
            }
            return NumberUtil.round(CommonUtils.evaluate(str), 2);
        } else {
            return new BigDecimal(0);
        }
    }

    public BigDecimal getRtplWithAdditionDeduction(BigDecimal p1, BigDecimal p2, String param1, String param2,
                                                   MemberMilkPurchaseRateBased based, MemberMilkPurchaseRateBased based2) {
        BigDecimal rtpl = getRate(p1, p2, param1, param2, based.getMilkType(), based);
        if ((based.getDeductionType() > 0 && based.getRefType() > 0)
                || (based2 != null && based2.getDeductionType() > 0 && based2.getRefType() > 0)) {
            int pt = 0;
            if (based.getRateType() == 2) {
                switch (based.getDeductionType()) {
                    case 1: // Value Addition
                        BigDecimal tempRate = new BigDecimal(0);
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == based.getFixedPoint() && p.getSnf() == p2
                                                        && p.getMilkType().getCode() == based
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;

                                else
                                    pt = 1;
                                rtpl = tempRate.add(BigDecimal.valueOf(pt).multiply(based.getVal()));

                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(new BigDecimal(pt).multiply(based.getVal()));
                                break;
                        }
                        break;
                    case 2: // Value deduction
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == based.getFixedPoint() && p.getSnf() == p2
                                                        && p.getMilkType().getCode() == based
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(new BigDecimal(pt).multiply(based.getVal()));
                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(new BigDecimal(pt).multiply(based.getVal()));
                                break;
                        }
                        break;
                    case 3:// Percentage addition
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == based.getFixedPoint() && p.getSnf() == p2
                                                        && p.getMilkType().getCode() == based
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)

                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.add(tempRate.multiply(new BigDecimal(pt).multiply(based.getVal()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));
                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = ((NumberUtil.round(p1.subtract(based.getStartVal()), 1)).multiply(new BigDecimal(10)).divide(BigDecimal.valueOf(based.getStep()))).intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(rtpl.multiply(new BigDecimal(pt).multiply(based.getVal()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));
                                break;
                        }
                        break;
                    case 4: // Percentage Deduction
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == based.getFixedPoint() && p.getSnf() == p2
                                                        && p.getMilkType().getCode() == based
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = ((NumberUtil.round(based.getEndVal().subtract(p1), 1)).multiply(new BigDecimal(10)).divide(new BigDecimal(based.getStep()))).intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(tempRate.multiply(new BigDecimal(pt)).multiply(based.getVal()).divide(new BigDecimal(100), RoundingMode.HALF_UP));
                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(
                                        rtpl.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );
                                break;
                        }
                        break;
                }

                //////////////
                // based-2
                /////////////
                switch (based2.getDeductionType()) {
                    case 1: // Value Addition
                        BigDecimal tempRate = new BigDecimal(0);
                        switch (based2.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == p1 && p.getSnf() == based2.getFixedPoint()
                                                        && p.getMilkType().getCode() == based2
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(p2.subtract(based2.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.add(BigDecimal.valueOf(pt).multiply(based2.getVal()));

                                break;
                            case 2: // Actual
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(p2.subtract(based2.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(BigDecimal.valueOf(pt).multiply(based2.getVal()));
                                break;
                        }
                        break;
                    case 2: // Value deduction
                        switch (based2.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == p1 && p.getSnf() == based2.getFixedPoint()
                                                        && p.getMilkType().getCode() == based2
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(based2.getEndVal().subtract(p2), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(BigDecimal.valueOf(pt).multiply(based2.getVal()));

                                break;
                            case 2: // Actual
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(based2.getEndVal().subtract(p2), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(BigDecimal.valueOf(pt).multiply(based2.getVal()));

                                break;
                        }
                        break;
                    case 3:// Percentage addition
                        switch (based2.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == p1 && p.getSnf() == based2.getFixedPoint()
                                                        && p.getMilkType().getCode() == based2
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(p2.subtract(based2.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.add(
                                        tempRate.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based2.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );
                                break;
                            case 2: // Actual
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(p2.subtract(based2.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(
                                        rtpl.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based2.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                        }
                        break;
                    case 4: // Percentage Deduction
                        switch (based2.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate
                                        .getListDetails().stream().filter(
                                                p -> p.getFat() == p1 && p.getSnf() == based2.getFixedPoint()
                                                        && p.getMilkType().getCode() == based2
                                                        .getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(based2.getEndVal().subtract(p2), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(
                                        tempRate.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based2.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                            case 2: // Actual
                                if (based2.getStep() > 0)
                                    pt = NumberUtil.round(based2.getEndVal().subtract(p2), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based2.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(
                                        rtpl.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based2.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                        }
                        break;
                }
            } else { // Single Axis Rate Chart
                switch (based.getDeductionType()) {
                    case 1: // Value Addition
                        BigDecimal tempRate = new BigDecimal(0);
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate.getListDetails().stream()
                                        .filter(p -> p.getFat() == based.getFixedPoint() && p.getMilkType()
                                                .getCode() == based.getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.add(BigDecimal.valueOf(pt).multiply(based.getVal()));

                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(BigDecimal.valueOf(pt).multiply(based.getVal()));
                                break;
                        }
                        break;
                    case 2: // Value deduction
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate.getListDetails().stream()
                                        .filter(p -> p.getFat() == based.getFixedPoint() && p.getMilkType()
                                                .getCode() == based.getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(BigDecimal.valueOf(pt).multiply(based.getVal()));
                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;

                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(BigDecimal.valueOf(pt).multiply(based.getVal()));
                                break;
                        }
                        break;
                    case 3:// Percentage addition
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate.getListDetails().stream()
                                        .filter(p -> p.getFat() == based.getFixedPoint() && p.getMilkType()
                                                .getCode() == based.getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.add(
                                        tempRate.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(p1.subtract(based.getStartVal()), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.add(
                                        rtpl.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );
                                break;
                        }
                        break;
                    case 4: // Percentage Deduction
                        switch (based.getRefType()) {
                            case 1: // Fixed point
                                MemberMilkPurchaseRateDetail tempDtl = memberMilkPurchaseRate.getListDetails().stream()
                                        .filter(p -> p.getFat() == based.getFixedPoint() && p.getMilkType()
                                                .getCode() == based.getMilkType().getCode())
                                        .findAny().orElse(null);
                                tempRate = tempDtl != null ? tempDtl.getRate() : new BigDecimal(0);
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = tempRate.subtract(
                                        tempRate.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                            case 2: // Actual
                                if (based.getStep() > 0)
                                    pt = NumberUtil.round(based.getEndVal().subtract(p1), 1)
                                            .multiply(BigDecimal.valueOf(10))
                                            .divide(BigDecimal.valueOf(based.getStep()), RoundingMode.HALF_UP)
                                            .intValue() + 1;
                                else
                                    pt = 1;
                                rtpl = rtpl.subtract(
                                        rtpl.multiply(BigDecimal.valueOf(pt))
                                                .multiply(based.getVal())
                                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                                );

                                break;
                        }
                        break;
                }
            }
        }
        return NumberUtil.round(rtpl.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : rtpl, 2);
    }

    private String getKgRateOfParam(BigDecimal p1, MilkType milkType, String param) {
        for (MemberMilkPurchaseRateBased prb : memberMilkPurchaseRateBasedList) {
            if (prb.getMilkType().getCode() == milkType.getCode()
                    && prb.getQualityParam() == (param.equals("FAT") ? 1 : 2) && p1.compareTo(prb.getStartVal()) >= 0
                    && p1.compareTo(prb.getEndVal()) <= 0) {
                return prb.getKgRate() + "";
            }
        }
        return "0";
    }

    private String getFormula(BigDecimal p1, BigDecimal p2, MilkType milkType) {
        String formula = "";
        for (MemberMilkPurchaseRateBased prb : memberMilkPurchaseRateBasedList) {
            if (Objects.equals(prb.getMilkType().getCode(), milkType.getCode())
                    && p1.compareTo(prb.getStartVal()) >= 0 && p1.compareTo(prb.getEndVal()) <= 0) {
                formula = prb.getFormula().getFormula();
                break;
            }
        }
        if (formula.length() > 0)
            return formula;
        else {
            for (MemberMilkPurchaseRateBased prb : memberMilkPurchaseRateBasedList) {
                if (prb.getMilkType().getCode() == milkType.getCode()
                        && p2.compareTo(prb.getStartVal()) >= 0 && p2.compareTo(prb.getEndVal()) <= 0) {
                    formula = prb.getFormula().getFormula();
                    break;
                }
            }
        }
        return formula;
    }
}