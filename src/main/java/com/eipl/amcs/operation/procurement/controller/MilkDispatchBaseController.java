package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.task.MilkDispatchPrevRecordGetTask;
import com.eipl.amcs.operation.procurement.task.MilkDispatchTransactionLoadTask;
import com.eipl.amcs.operation.procurement.task.MilkReceiptPrevRecordGetTask;
import com.eipl.amcs.operation.procurement.task.MilkReceiptTransactionLoadTask;
import com.eipl.amcs.utils.CommonUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class MilkDispatchBaseController {

    protected final int SCALE = 2;
    protected final RoundingMode ROUND = RoundingMode.HALF_UP;
    protected SocietyMilkPurchaseRate societyMilkPurchaseRate;
    protected Map<String, BigDecimal> mapRateDetails;
    protected List<SocietyMilkPurchaseRateBased> listBased;
    protected MilkDispatchRateAndDetailsDto milkDispatchRateAndDetailsDto;
    String mapKey = null;

    protected void calculateClr(String fat, String snf) {
        if (!fat.isEmpty() && !snf.isEmpty()) {
            BigDecimal clr = CommonUtils.calculateClr(fat, snf);
            setClr(clr != null ? clr.toString() : "0");
        }
    }

    protected abstract void setClr(String clr);

    protected void calculateAmount(String rate, String qty) {
        if (!rate.isEmpty() && !qty.isEmpty()) {
            mapKey = null;
            setAmount(new BigDecimal(rate).multiply(new BigDecimal(qty)).setScale(SCALE, ROUND).toString());
        }
    }

    protected abstract void setAmount(String amount);

    protected void fetchRate(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType) {
        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            if (!(milkType.getCode() == 3 && milkQualityType.getCode() == 3)) {

                if ((short) 1 == societyMilkPurchaseRate.getRateGenMethodCode() && listBased != null) {
                    BigDecimal fatVal = new BigDecimal(fat);
                    BigDecimal snfVal = new BigDecimal(snf);
                    Optional<SocietyMilkPurchaseRateBased> basedFat =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 1
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    Optional<SocietyMilkPurchaseRateBased> basedSnf =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 2
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    if (basedFat.isPresent() && basedSnf.isPresent()) {
                        BigDecimal kgRate = CommonUtils.fetchEffectiveRate(basedFat.get().getKgRate(), basedSnf.get());
                        BigDecimal eqFat = CommonUtils.calculateEqFat(fatVal, snfVal);
                        BigDecimal kgEqFat = CommonUtils.calculateEqKgFat(eqFat, getQuantity());
                        BigDecimal kgFat = CommonUtils.calculateKgFat(fatVal, getQuantity());
                        String formula = basedFat.get().getFormula() != null ?
                                basedFat.get().getFormula().getFormula() : null;
                        if (formula == null || formula.isEmpty()) {
                            setRate("0");
                            return;
                        }

//                    formula = formula.replace("RATE", kgRate.toString());
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula);
                        setAmount(val.toString());
                        setRate(CommonUtils.calculateAvgRate(val, getQuantity()).toString());
                    } else {
                        setRate("0");
                    }
                } else {
                    mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                            new BigDecimal(snf).setScale(SCALE, ROUND) + "#" +
                            milkType.getCode() + "#" + milkQualityType.getCode();
                    if (mapKey != null) {
                        BigDecimal val = mapRateDetails.get(mapKey);
                        setRate(val != null ? val.setScale(SCALE, ROUND).toString() : "0");
                    }
                }
            }
        }
    }

    protected void fetchRateForDispatch(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType, LocalDateTime dateTime) {
        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            if (!(milkType.getCode() == 3 && milkQualityType.getCode() == 3)) {

                if ((short) 1 == societyMilkPurchaseRate.getRateGenMethodCode() && listBased != null) {
                    BigDecimal fatVal = new BigDecimal(fat);
                    BigDecimal snfVal = new BigDecimal(snf);
                    Optional<SocietyMilkPurchaseRateBased> basedFat =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 1
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    Optional<SocietyMilkPurchaseRateBased> basedSnf =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 2
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    if (basedFat.isPresent() && basedSnf.isPresent()) {
                        BigDecimal kgRate = CommonUtils.fetchEffectiveRate(basedFat.get().getKgRate(), basedSnf.get());
                        BigDecimal eqFat = CommonUtils.calculateEqFat(fatVal, snfVal);
                        BigDecimal kgEqFat = CommonUtils.calculateEqKgFat(eqFat, getQuantity());
                        BigDecimal kgFat = CommonUtils.calculateKgFat(fatVal, getQuantity());
                        String formula = basedFat.get().getFormula() != null ?
                                basedFat.get().getFormula().getFormula() : null;
                        if (formula == null || formula.isEmpty()) {
                            setRate("0");
                            return;
                        }

//                    formula = formula.replace("RATE", kgRate.toString());
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula);
                        setAmount(val.toString());
                        setRate(CommonUtils.calculateAvgRate(val, getQuantity()).toString());
                    } else {
                        setRate("0");
                    }
                } else {
                    mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                            new BigDecimal(snf).setScale(SCALE, ROUND) + "#" +
                            milkType.getCode() + "#" + milkQualityType.getCode();
                    if (mapKey != null) {
                        BigDecimal val = mapRateDetails.get(mapKey);
                        setRate(val != null ? val.setScale(SCALE, ROUND).toString() : "0");
                    }
                }
            } else {
                MilkDispatchPrevRecordGetTask task = new MilkDispatchPrevRecordGetTask(dateTime);
                task.setOnSucceeded(e -> {
                    try {
                        if (task.get() != null) {
                            MilkDispatch receipt = task.get();
                            MilkDispatchTransactionLoadTask transactionLoadTask = new MilkDispatchTransactionLoadTask(receipt.getChallanNo());
                            transactionLoadTask.setOnSucceeded(ee -> {
                                try {
                                    List<MilkDispatchTransaction> transactionList = transactionLoadTask.get();
                                    if (transactionList != null && !transactionList.isEmpty()) {
                                        setRate(String.valueOf(transactionList.get(0).getRate().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)));
                                        setAmount(String.valueOf(transactionList.get(0).getAmount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)));
                                    }
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                } catch (ExecutionException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            new Thread(transactionLoadTask).start();
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                new Thread(task).start();
            }
        }
    }


    protected void fetchRateForDispatchTable(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType, LocalDateTime dateTime, MilkDispatchTransaction transaction) {
        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            if (!(milkType.getCode() == 3 && milkQualityType.getCode() == 3)) {

                if ((short) 1 == societyMilkPurchaseRate.getRateGenMethodCode() && listBased != null) {
                    BigDecimal fatVal = new BigDecimal(fat);
                    BigDecimal snfVal = new BigDecimal(snf);
                    Optional<SocietyMilkPurchaseRateBased> basedFat =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 1
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    Optional<SocietyMilkPurchaseRateBased> basedSnf =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 2
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    if (basedFat.isPresent() && basedSnf.isPresent()) {
                        BigDecimal kgRate = CommonUtils.fetchEffectiveRate(basedFat.get().getKgRate(), basedSnf.get());
                        BigDecimal eqFat = CommonUtils.calculateEqFat(fatVal, snfVal);
                        BigDecimal kgEqFat = CommonUtils.calculateEqKgFat(eqFat, transaction.getQty().toString());
                        BigDecimal kgFat = CommonUtils.calculateKgFat(fatVal, transaction.getQty().toString());
                        String formula = basedFat.get().getFormula() != null ?
                                basedFat.get().getFormula().getFormula() : null;
                        if (formula == null || formula.isEmpty()) {
                            setRate("0");
                            return;
                        }

//                    formula = formula.replace("RATE", kgRate.toString());
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula);
                        transaction.setAmount(val);
                        transaction.setRate(CommonUtils.calculateAvgRate(val, transaction.getQty().toString()));
                    } else {
                        transaction.setRate(BigDecimal.ZERO);
                    }
                } else {
                    mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                            new BigDecimal(snf).setScale(SCALE, ROUND) + "#" +
                            milkType.getCode() + "#" + milkQualityType.getCode();
                    if (mapKey != null) {
                        BigDecimal val = mapRateDetails.get(mapKey);
                        transaction.setRate(val != null ? val.setScale(SCALE, ROUND) : BigDecimal.ZERO);
                    }
                }
            } else {
                MilkDispatchPrevRecordGetTask task = new MilkDispatchPrevRecordGetTask(dateTime);
                task.setOnSucceeded(e -> {
                    try {
                        if (task.get() != null) {
                            MilkDispatch receipt = task.get();
                            MilkDispatchTransactionLoadTask transactionLoadTask = new MilkDispatchTransactionLoadTask(receipt.getChallanNo());
                            transactionLoadTask.setOnSucceeded(ee -> {
                                try {
                                    List<MilkDispatchTransaction> transactionList = transactionLoadTask.get();
                                    if (transactionList != null && !transactionList.isEmpty()) {
                                        transaction.setRate(transactionList.get(0).getRate().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
                                        transaction.setAmount(transactionList.get(0).getAmount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
                                    }
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                } catch (ExecutionException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            new Thread(transactionLoadTask).start();
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                new Thread(task).start();
            }
        }
    }

    protected void fetchRateForReceipt(String fat, String snf, MilkType milkType, MilkQualityType milkQualityType, LocalDateTime dateTime) {
        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            if (!(milkType.getCode() == 3 && milkQualityType.getCode() == 3)) {

                if ((short) 1 == societyMilkPurchaseRate.getRateGenMethodCode() && listBased != null) {
                    BigDecimal fatVal = new BigDecimal(fat);
                    BigDecimal snfVal = new BigDecimal(snf);
                    Optional<SocietyMilkPurchaseRateBased> basedFat =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 1
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && fatVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    Optional<SocietyMilkPurchaseRateBased> basedSnf =
                            listBased.stream().filter(p -> p.getMilkType().getCode().compareTo(milkType.getCode()) == 0
                                            && p.getQualityParam() == 2
                                            && p.getMilkQualityType().getCode() == milkQualityType.getCode()
                                            && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getStartVal()) >= 0 && snfVal.setScale(1, RoundingMode.DOWN).compareTo(p.getEndVal()) <= 0)
                                    .findFirst();
                    if (basedFat.isPresent() && basedSnf.isPresent()) {
                        BigDecimal kgRate = CommonUtils.fetchEffectiveRate(basedFat.get().getKgRate(), basedSnf.get());
                        BigDecimal eqFat = CommonUtils.calculateEqFat(fatVal, snfVal);
                        BigDecimal kgEqFat = CommonUtils.calculateEqKgFat(eqFat, getQuantity());
                        BigDecimal kgFat = CommonUtils.calculateKgFat(fatVal, getQuantity());
                        String formula = basedFat.get().getFormula() != null ?
                                basedFat.get().getFormula().getFormula() : null;
                        if (formula == null || formula.isEmpty()) {
                            setRate("0");
                            return;
                        }

//                    formula = formula.replace("RATE", kgRate.toString());
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula);
                        setAmount(val.toString());
                        setRate(CommonUtils.calculateAvgRate(val, getQuantity()).toString());
                    } else {
                        setRate("0");
                    }
                } else {
                    mapKey = new BigDecimal(fat).setScale(SCALE, ROUND) + "#" +
                            new BigDecimal(snf).setScale(SCALE, ROUND) + "#" +
                            milkType.getCode() + "#" + milkQualityType.getCode();
                    if (mapKey != null) {
                        BigDecimal val = mapRateDetails.get(mapKey);
                        setRate(val != null ? val.setScale(SCALE, ROUND).toString() : "0");
                    }
                }
            } else {
                MilkReceiptPrevRecordGetTask task = new MilkReceiptPrevRecordGetTask(dateTime);
                task.setOnSucceeded(e -> {
                    try {
                        if (task.get() != null) {
                            MilkReceipt receipt = task.get();
                            MilkReceiptTransactionLoadTask transactionLoadTask = new MilkReceiptTransactionLoadTask(receipt.getCode());
                            transactionLoadTask.setOnSucceeded(ee -> {
                                try {
                                    List<MilkReceiptTransaction> transactionList = transactionLoadTask.get();
                                    if (transactionList != null && !transactionList.isEmpty()) {
                                        setRate(String.valueOf(transactionList.get(0).getRate().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)));
                                        setAmount(String.valueOf(transactionList.get(0).getAmount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)));
                                    }
                                } catch (InterruptedException ex) {
                                    throw new RuntimeException(ex);
                                } catch (ExecutionException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                            new Thread(transactionLoadTask).start();
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                new Thread(task).start();
            }
        }
    }

    protected abstract void setRate(String rate);

    protected abstract String getQuantity();

    protected abstract void setQuantity(String val);
}
