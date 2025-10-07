package com.eipl.amcs.util;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.udojava.evalex.Expression;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

import com.eipl.amcs.utils.AppConstant;

public class CommonUtil {


    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter Formatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
    public static final DateTimeFormatter Formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter Formatter3 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter Formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    public static final DateTimeFormatter Formatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int SCALE = 2;
    private static final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
//    public static final MathContext MY_DECIMAL32 = MathContext.DECIMAL128;//new MathContext(4, RoundingMode.HALF_UP);
    public static final MathContext MY_DECIMAL32 = new MathContext(34, RoundingMode.HALF_UP);


    public static FieldError getFieldError(String objName, String field, Object rejectedVal, String defMessage) {
        return new FieldError(objName, field, rejectedVal, false, null, null, defMessage);
    }

    public static LocalTime getTimeFromShift(Shift shift) {
        if (shift == null || shift.getName().equals("Morning"))
            return LocalTime.of(6, 0);

        return LocalTime.of(18, 0);
    }

    // 1-Member, 2-Non member, 3-Institute, 4-Vendor, 5-Consumer
    public static short getMemberNonMemberTypeValue(String str) {
        switch (str.toLowerCase()) {
            case "member":
                return (short) 1;
            case "non Member":
                return (short) 2;
            case "institute":
                return (short) 3;
            case "vendor":
                return (short) 4;
            case "consumer":
                return (short) 5;
        }
        return 0;
    }

    public static String getIdentityHeader(Map<String, String> headers) {
        if (headers == null)
            return null;
        return headers.get(AppConstant.HEADER_IDENTITY);
    }

    public static String setIdentityHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("SOCIETY");
        sb.append("#");
        sb.append(MainApp.identityDto.getSociety().getCode());
        sb.append("#");
        sb.append(MainApp.systemId);
        sb.append("#");
        sb.append(MainApp.getProperty(AppConstant.Props.VERSION, "1.0"));
        sb.append("#");
        sb.append(MainApp.locale);
        return new String(Base64.getEncoder().encode(sb.toString().getBytes()));
    }

    public static LocalDateTime getLocalDateTimeFromDateAndShift(LocalDate date, Shift shift) {
        if (date == null || shift == null)
            return null;
        return LocalDateTime.of(date, shift.getCode() == 1 ? LocalTime.of(6, 0) : LocalTime.of(18, 0));
    }

    public static int strToInt(String str) {
        if (str == null || str.isEmpty())
            return 0;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static BigDecimal calculateClr(BigDecimal fat, BigDecimal snf) {
        // Expr: CLR = (SNF - (FAT X LR1) - LR2) X 4
        try {
            String expr = "(" + snf + "-(" + fat + "*" + "0.21" + ")-" +
                    "0.66" + ")*4";
            Expression expression = new Expression(expr);
            return expression.eval();
        } catch (Exception e) {
            return null;
        }
    }


    public static BigDecimal calculateEqFat(BigDecimal fatVal, BigDecimal snfVal) {
        return scale2RoundDown(BigDecimal.valueOf(0.6667).multiply(snfVal).add(fatVal));
    }

    public static BigDecimal calculateEqKgFat(BigDecimal eqFat, String quantity) {
        if (quantity == null || quantity.isEmpty())
            return BigDecimal.ZERO;
        BigDecimal qtyVal = new BigDecimal(quantity);
        return scale2RoundDown(qtyVal.multiply(eqFat).divide(BigDecimal.valueOf(100), MY_DECIMAL32));
    }


    public static BigDecimal convertQty(String collectionType, String qty) {
        try {
            switch (collectionType) {
                case AppConstant.CollectionType.MEMBER_COLL:
                case AppConstant.CollectionType.SOCIETY_COLL:
                case AppConstant.CollectionType.DISPATCH:
                case AppConstant.CollectionType.RECEIPT:
                case AppConstant.CollectionType.LOCAL_SALE:
                    return convertQuantity("1", qty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BigDecimal scale2RoundDown(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(SCALE, RATE_ROUND);
    }

    private static BigDecimal convertQuantity(String mode, String qty) {
        if ("0".equalsIgnoreCase(mode))
            return new BigDecimal(qty).multiply(new BigDecimal("1"))
                    .setScale(2, RoundingMode.HALF_UP);

        return new BigDecimal(qty).divide(new BigDecimal("1"), 3, RoundingMode.HALF_UP);
    }


    public static BigDecimal fetchEffectiveRate(BigDecimal kgRate, SocietyMilkPurchaseRateBased basedSnf) {
        switch (basedSnf.getDeductionType()) {
            case 0: // NA
                return kgRate;
            case 1: // Val Ad
                return kgRate;
            case 2: // Val ded
                return kgRate;
            case 3: // Per add
                return kgRate;
            case 4: // Per ded
                return kgRate;
            case 5: // Per rate add
            case 6: // Per rate ded
                return scale2RoundDown(kgRate.multiply(basedSnf.getVal()).divide(BigDecimal.valueOf(100), MY_DECIMAL32));
        }
        return kgRate;
    }

    public static BigDecimal calculateKgFat(BigDecimal fatVal, String quantity) {
        if (quantity == null || quantity.isEmpty())
            return BigDecimal.ZERO;
        BigDecimal qtyVal = new BigDecimal(quantity);
        return scale2RoundDown(qtyVal.multiply(fatVal).divide(BigDecimal.valueOf(100), MY_DECIMAL32));
    }

    public static BigDecimal evaluate(String formula) {
        try {
            if (formula == null || formula.isEmpty())
                return BigDecimal.ZERO;

            Expression expression = new Expression(formula);
            expression.setPrecision(10);
            return expression.eval().setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal scale2RoundUp(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(SCALE, RATE_ROUND);
    }

    public static BigDecimal scale1RoundUp(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(1, RATE_ROUND);
    }

    public static BigDecimal calculateAvgRate(BigDecimal val, String quantity) {
        if (quantity == null || quantity.isEmpty())
            return BigDecimal.ZERO;
        return scale2RoundUp(val.divide(new BigDecimal(quantity), MY_DECIMAL32));
    }

}
