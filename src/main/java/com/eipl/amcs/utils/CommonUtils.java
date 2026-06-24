package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.udojava.evalex.Expression;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonUtils {

    //    public static final MathContext MY_DECIMAL32 =  MathContext.DECIMAL128; ///new MathContext(10, RoundingMode.HALF_UP);
    public static final MathContext MY_DECIMAL32 = new MathContext(34, RoundingMode.HALF_UP);
    public static final DateTimeFormatter FMT_DATE_EXCEL = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter Formatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
    public static final DateTimeFormatter Formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter Formatter3 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter Formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    public static final DateTimeFormatter Formatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter MONTH_SHORT_FORMATTER = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
    final static int CHAR_PER_LINE = 40;
    final static String SPACE = " ";
    final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    private static final int SCALE = 2;
    private static final RoundingMode RATE_ROUND = RoundingMode.HALF_UP;
    private static final List<CustomerTypeKeyValDto> customerTypeList;
    public static List<String> meetingTypeList;

    static {
        meetingTypeList = new ArrayList<>();
        meetingTypeList.add("Managing Committee Meeting");
        meetingTypeList.add("Annual General Meeting");
        meetingTypeList.add("Special General Meeting");
        meetingTypeList.add("Extension Meeting");
        meetingTypeList.add("Gram Sabha");
    }

    static {
        customerTypeList = new ArrayList<>();
        customerTypeList.add(new CustomerTypeKeyValDto((short) 1, getResourceString(MainApp.getBundle(), "member"), true, true, false));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 2, getResourceString(MainApp.getBundle(), "nonmember"), true, true, false));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 3, getResourceString(MainApp.getBundle(), "vendor"), true, true, true));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 4, getResourceString(MainApp.getBundle(), "institute"), true, true, true));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 5, getResourceString(MainApp.getBundle(), "retailsale"), true, false, true));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 6, getResourceString(MainApp.getBundle(), "consumer"), true, true, true));
        customerTypeList.add(new CustomerTypeKeyValDto((short) 7, getResourceString(MainApp.getBundle(), "other"), false, false, true));
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.isEmpty()) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static String getLocalString(String main, String local) {
        if ("en".equalsIgnoreCase(MainApp.getLocale()))
            return main;

        if (local == null || local.isEmpty())
            return main;
        return local;
    }

    public static LocalDateTime getLocalDateTimeFromDateAndShift(LocalDate date, Shift shift) {
        if (date == null || shift == null)
            return null;
        return LocalDateTime.of(date, shift.getCode() == 1 ? LocalTime.of(6, 0) : LocalTime.of(18, 0));
    }

    public static String getLocalDateTimeFromDateAndShiftText(LocalDate date, Shift shift) {
        if (date == null || shift == null)
            return null;
        return date.toString().replace("-", "") + (shift.getCode() == 1 ? "6" : "18");
    }

    public static String getRateGenerationMethod(short code) {
        switch (code) {
            case 1:
                return "Excel";
            case 2:
                return "Manual";
            default:
                throw new IllegalStateException("Unexpected value: " + code);
        }
    }

    public static List<Shift> removeAllShift(List<Shift> list) {
        return list.stream().filter(p -> p.getCode() != 3).collect(Collectors.toList());
    }

    public static String getFileExtension(File file) {
        if (file == null || !file.exists())
            return null;
        return file.getName().substring(file.getName().lastIndexOf('.') + 1);
    }

    public static Short getRateGenerationMethodCode(String val) {
        switch (val) {
            case "Excel":
                return (short) 1;
            case "Manual":
                return (short) 2;
            default:
                throw new IllegalStateException("Unexpected value: " + val);
        }
    }

    public static Integer strToInteger(String strNum) {
        if (!isNumeric(strNum))
            return 0;
        return Integer.valueOf(strNum);
    }

    public static Double strToDouble(String strNum) {
        if (!isNumeric(strNum))
            return 0d;
        return Double.valueOf(strNum);
    }

    public static String getDeviceType(Short deviceType) {
        switch (deviceType) {
            case 0:
                return "Weight Scale";
            case 1:
                return "Milk Analyser";
            case 2:
                return "Display";
            case 3:
                return "Splitter";
            default:
                throw new IllegalStateException("Unexpected value: " + deviceType);
        }
    }

    public static LocalTime getTimeFromShift(Shift shift) {
        if (shift == null || shift.getName().equals("Morning"))
            return LocalTime.of(6, 0);

        return LocalTime.of(18, 0);
    }

    public static String getMemberShortCode(String code) {
        if (code == null)
            return null;
        code = code.replaceFirst(MainApp.identityDto.getSociety().getCode(), "");
        return String.format("%04d", strToInteger(code));
    }

    public static BigDecimal calculateClr(String fat, String snf) {
        // Expr: CLR = (SNF - (FAT X LR1) - LR2) X 4
        try {
            String expr = "(" + snf + "-(" + fat + "*" + MainApp.getProperty(AppConstant.Props.CLR_CONST1, "0") + ")-" +
                    MainApp.getProperty(AppConstant.Props.CLR_CONST2, "0") + ")*4";
            Expression expression = new Expression(expr);
            return expression.eval();
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal convertQty(String collectionType, String qty) {
        try {
            switch (collectionType) {
                case AppConstant.CollectionType.MEMBER_COLL:
                    return convertQuantity(MainApp.getProperty(AppConstant.Props.MEMBER_COLLECTION_QTY_MODE, "0"), qty);
                case AppConstant.CollectionType.SOCIETY_COLL:
                    return convertQuantity(MainApp.getProperty(AppConstant.Props.SOCIETY_COLLECTION_QTY_MODE, "0"), qty);
                case AppConstant.CollectionType.DISPATCH:
                    return convertQuantity(MainApp.getProperty(AppConstant.Props.DISPATCH_QTY_MODE, "0"), qty);
                case AppConstant.CollectionType.RECEIPT:
                    return convertQuantity(MainApp.getProperty(AppConstant.Props.RECEIPT_QTY_MODE, "0"), qty);
                case AppConstant.CollectionType.LOCAL_SALE:
                    return convertQuantity(MainApp.getProperty(AppConstant.Props.MILKSALE_QTY_MODE, "0"), qty);
            }
        } catch (Exception e) {
            LOGGER.error("Convert Qty", e);
        }
        return null;
    }

    private static BigDecimal convertQuantity(String mode, String qty) {
        if ("0".equalsIgnoreCase(mode))
            return new BigDecimal(qty).multiply(new BigDecimal(MainApp.getProperty(AppConstant.Props.LTR_TO_KG, "0")))
                    .setScale(2, RoundingMode.HALF_UP);

        return new BigDecimal(qty).divide(new BigDecimal(MainApp.getProperty(AppConstant.Props.LTR_TO_KG, "0")), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertQuantityValue(BigDecimal value, Integer weightSetting) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        switch (weightSetting) {
            case 0: // single digit truncate
                return value.setScale(1, RoundingMode.DOWN);
            case 1: // Double digit truncate
                return value.setScale(2, RoundingMode.DOWN);
            case 2:// single digit round
                return value.setScale(1, RoundingMode.HALF_UP);
            case 3: // double digit round
                return value.setScale(2, RoundingMode.HALF_UP);
        }
        return value;
    }

    public static BigDecimal convertQualityValue(BigDecimal value, Integer qualityValue) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        switch (qualityValue) {
            case 0: // Round
                return value.setScale(1, RoundingMode.HALF_UP);
            case 1: // truncate
                return value.setScale(1, RoundingMode.DOWN);
            case 2: // Round
                return value.setScale(2, RoundingMode.HALF_UP);
            case 3: // truncate
                return value.setScale(2, RoundingMode.DOWN);
        }
        return value.setScale(1, RoundingMode.HALF_UP);
    }

    public static List<Tax> getTaxFromDto(List<TaxDto> dto) {
        List<Tax> list = dto.stream()
                .filter(m -> m.getTax().getName().equalsIgnoreCase("nil"))
                .map(TaxDto::getTax)
                .collect(Collectors.toList());

        list.addAll(dto.stream()
                .filter(m -> m.getTax() != null && m.getTax().getName() != null
                        && !m.getTax().getName().equalsIgnoreCase("nil"))
                .map(TaxDto::getTax)
                .collect(Collectors.toList()));
        return list;
    }

    public static File openExcelFileDialog(String dialogTitle) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(dialogTitle);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel", "*.xls"));
        return fileChooser.showOpenDialog(MainApp.getStage());
    }

    public static File openFileDialog(String dialogTitle) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(dialogTitle);
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("File", "*.mdb"));
        return fileChooser.showOpenDialog(MainApp.getStage());
    }

    public static File openDirectoryDialog(String dialogTitle) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle(dialogTitle);
        return fileChooser.showDialog(MainApp.getStage());
    }

    public static Map<TaxDetail, BigDecimal> calculateAndFetchTaxBifurcation(TaxDto taxDto, BigDecimal val) {
        Map<TaxDetail, BigDecimal> taxBifurcation = new HashMap<>();
        if (taxDto == null || taxDto.getTaxDetails() == null)
            return taxBifurcation;
        taxDto.getTaxDetails().forEach(item -> {
            taxBifurcation.put(item, calculateTaxAmount(item, val));
        });
        return taxBifurcation;
    }

    private static BigDecimal calculateTaxAmount(TaxDetail item, BigDecimal val) {
        BigDecimal amt = val.multiply(BigDecimal.valueOf(item.getPercentage())).divide(BigDecimal.valueOf(100)).setScale(SCALE, RATE_ROUND);
        if (item.getType() == (short) 1) {
            return amt;
        }
        return BigDecimal.ZERO.subtract(amt).setScale(SCALE, RATE_ROUND);
    }

    public static BigDecimal scale2RoundUp(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(SCALE, RATE_ROUND);
    }

    public static BigDecimal scale2RoundDown(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(SCALE, RATE_ROUND);
    }

    public static BigDecimal scale1RoundUp(BigDecimal val) {
        if (val == null)
            return null;
        return val.setScale(1, RATE_ROUND);
    }

    public static LocalDate excelDate(String str) {
        if (str == null || str.isEmpty())
            return null;
        return LocalDate.parse(str, FMT_DATE_EXCEL);
    }

    public static String getResourceString(ResourceBundle bundle, String key) {
        if (key == null)
            return "";
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getShiftShort(Shift shift) {
        if (shift == null)
            return "";
        return shift.getName().charAt(0) + "";
    }

    public static String getPaymentSummaryStatus(short status) {
        switch (status) {
            case 1:
                return "PENDING";
            default:
                return "";
        }
    }

    // //1-PENDING, 2-SENT, 3-SENT_DBT, 4-REJECT, 5-PROCESSING, 6-DISBURSED, 7-FAIL
    public static String getPaymentStatus(short status) {
        switch (status) {
            case 1:
                return "PENDING";
            case 2:
                return "SENT";
            case 3:
                return "SENT_DBT";
            case 4:
                return "REJECT";
            case 5:
                return "PROCESSING";
            case 6:
                return "DISBURSED";
            case 7:
                return "FAIL";
            default:
                return "";
        }
    }

    // //1-PENDING, 2-APPROVED, 3-REJECT, 4-CLOSE
    public static String getRequestStatus(int status) {
        switch (status) {
            case 1:
                return "PENDING";
            case 2:
                return "APPROVED";
            case 3:
                return "REJECT";
            case 4:
                return "CLOSE";
            default:
                return "";
        }
    }

    public static String getMeetingType(short type) {
        switch (type) {
            case 1:
                return "Managing Committee Meeting";
            case 2:
                return "Annual General Meeting";
            case 3:
                return "Special General Meeting";
            case 4:
                return "Extension Meeting";
            case 5:
                return "Gram Sabha";
            default:
                return "";
        }
    }

    public static short getMeetingTypeFromString(String val) {
        switch (val) {
            case "Managing Committee Meeting":
                return (short) 1;
            case "Annual General Meeting":
                return (short) 2;
            case "Special General Meeting":
                return (short) 3;
            case "Extension Meeting":
                return (short) 4;
            case "Gram Sabha":
                return (short) 5;
            default:
                return (short) 0;
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.1      This will return consumer type in local language
     */
    public static String getCustomerTypeString(short val) {
        switch (val) {
            case 1:
                return MainApp.getBundle().getString("member");
            case 2:
                return MainApp.getBundle().getString("nonmember");
            case 3:
                return MainApp.getBundle().getString("vendor");
            case 4:
                return MainApp.getBundle().getString("institute");
            case 5:
                return MainApp.getBundle().getString("retailsale");
            case 6:
                return MainApp.getBundle().getString("consumer");
            default:
                return MainApp.getBundle().getString("other");
        }
    }

    /**
     * @param val 0-Bank, 1-Cash
     * @return
     */
    public static String getPaymentModeString(Short val) {
        switch (val) {
            case 1:
                return "CASH";
            default:
                return "CREDIT";
        }
    }

    public static String getPaymentTypeString(Short val) {
        switch (val) {
            case 1:
                return "BANK";
            case 2:
                return "COUPON";
            case 0:
            default:
                return "CASH";
        }
    }

    public static Short getStringFromPaymentType(String val) {
        switch (val) {
            case "BANK":
            case "CREDIT":
                return (short) 1;
            case "COUPON":
                return (short) 2;
            case "CASH":
            default:
                return (short) 0;
        }
    }

    public static String getDispatchDestinationType(Integer val) {
        switch (val) {
            case 0:
                return MainApp.getBundle().getString("bmc");
            case 1:
                return MainApp.getBundle().getString("mcc");
            case 2:
                return MainApp.getBundle().getString("plant");
            case 3:
                return MainApp.getBundle().getString("union");
            default:
                return "";
        }
    }

    public static List<CustomerTypeKeyValDto> getCustomerTypesForLocalMilkSale() {
        return customerTypeList.stream()
                .filter(p -> p.isLocalSale()).collect(Collectors.toList());
    }

    public static String getCustomerTypeStrFromShort(short val) {
        Optional<CustomerTypeKeyValDto> op = customerTypeList.stream().filter(p -> p.getKey() == val).findFirst();
        if (op.isPresent())
            return op.get().getValue();
        return null;
    }

    public static List<CustomerTypeKeyValDto> getCustomerTypesForProductSale() {
        return customerTypeList.stream()
                .filter(p -> p.isProductSale()).collect(Collectors.toList());
    }

    public static List<CustomerTypeKeyValDto> getCustomerTypesForCustomerCreate() {
        return customerTypeList.stream()
                .filter(p -> p.isCustomerTypeCreate()).collect(Collectors.toList());
    }

    public static String getDate(LocalDateTime ld) {
        String[] a = ld.toString().split("-");
        String date = a[2].substring(0, 2) + "/" + a[1];
        return date;
    }

    public static String leftAlignObject(Object value, int columnWidth) {
        String str = value.toString();

        if (str.length() == columnWidth)
            return str;
        if (str.length() > columnWidth)
            return str.substring(0, columnWidth);
        if (str.length() < columnWidth)
            return str + (String.format("%0" + (columnWidth - str.length()) + "d", 0).replace("0", SPACE));

        return "";
    }

    public static String centerAlign(String str) {
        int cut = (CHAR_PER_LINE - str.length()) / 2;
        return (String.format("%0" + cut + "d", 0).replace("0", SPACE))
                + str
                + (String.format("%0" + cut + "d", 0).replace("0", SPACE));
    }

    public static String rightAlignString(String value, int columnWidth, String padChar) {
        if (value.length() == columnWidth)
            return value;
        return String.format("%0" + (columnWidth - value.length() == 0 ? columnWidth : Math.abs(columnWidth - value.length())) + "d", 0).replace("0", padChar) +
                value;
    }

    public static BigDecimal convertQtyToKg(String qty) {
        return new BigDecimal(qty).multiply(new BigDecimal(MainApp.getProperty(AppConstant.Props.LTR_TO_KG, "0"))).setScale(2, RoundingMode.HALF_UP);
    }

    public static String getDeviceId(String societyCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("AMUL");
        if (MainApp.identityDto != null && MainApp.identityDto.getSociety() != null)
            sb.append(MainApp.identityDto.getSociety().getCode());
        else
            sb.append(societyCode);
        sb.append("AMCS");
        return sb.toString();
    }

    public static String getVersionNo() {
        return AppConstant.versionNo;
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
            return scale2RoundUp(expression.eval());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal calculateAvgRate(BigDecimal val, String quantity) {
        if (quantity == null || quantity.isEmpty())
            return BigDecimal.ZERO;
        return scale2RoundUp(val.divide(new BigDecimal(quantity), MY_DECIMAL32));
    }

    public static String fetchCollectionSlipDateFormatted(LocalDate date) {
        if (date == null)
            return "";
        return date.format(fmt);
    }

    public static List<CustomerTypeKeyValDto> getAllCustomerTypes() {
        return customerTypeList;
    }

    public static Date convertToSqlDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.valueOf(localDate);
    }

    public static FieldError getFieldError(String objName, String field, Object rejectedVal, String defMessage) {
        return new FieldError(objName, field, rejectedVal, false, null, null, defMessage);
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
        String sb = "SOCIETY" +
                "#" +
                MainApp.identityDto.getSociety().getCode() +
                "#" +
                MainApp.systemId +
                "#" +
                MainApp.getProperty(AppConstant.Props.VERSION, "1.0") +
                "#" +
                MainApp.locale;
        return new String(Base64.getEncoder().encode(sb.getBytes()));
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
}
