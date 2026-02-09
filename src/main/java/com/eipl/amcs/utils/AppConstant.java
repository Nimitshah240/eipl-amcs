package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class AppConstant {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter Formatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
    public static final DateTimeFormatter Formatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter Formatter3 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter Formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    public static final DateTimeFormatter Formatter6 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter Formatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final short ONE = 1;
    public static final short ZERO = 0;

    public static final String PARAM_CHK = "CHK";
    public static final String DB_ENCODE_CHAR = "#";

    public static final String PROMPT_DB_PASS = "PNM^$)&(%*";
    public static final int MIGRATION_LIST_SIZE = 50;
    public static String EIPL_DB_PASS = "";
    public static String EIPL_DB_NAME = "";
    public static String DB_LOC = "localhost";

    public static final String client = "JAIPURDUSS"; // OR JAIPURDUSS OR AMUL
    public static final String versionNo = "1.0";

    public static String baseUrlRealTime;
    public static String syncUrlRealTime;

    public static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            AppConstant.Formatter1, // Replace with your actual formatters
            AppConstant.Formatter2,
            AppConstant.Formatter3,
            AppConstant.Formatter4,
            AppConstant.Formatter5,
            AppConstant.Formatter6
    );
    public static final String HEADER_IDENTITY = "identity";
    public static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public static final DateTimeFormatter DATE_TIME_FMT_SSSSSS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    public static final DateTimeFormatter SYNC_DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter RFC_CALL_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // SPRING BOOTS CONSTANTS
    public static List<String> prioritizedTableNameList = Arrays.asList("tbl_bulk_notification", "rfc_call");

    public static LocalDate parseDateWithMultipleFormats(String dateStr) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
                // Ignore this exception and try the next formatter
            }
        }
        // Handle the case where none of the formatters could parse the date
        throw new IllegalArgumentException("Date format not recognized: " + dateStr);
    }

    public interface UrlPath {

        String SOCIETY_MILK_PURCHASE_RATE_DETAIL = "society-milk-purchase-rate-details";
        String SOCIETY_MILK_PURCHASE_RATE_APPLICABILITY = "society-milk-purchase-rate-applicability";

        String UPDATE_CHECK = MainApp.getProperty("updater.url", "");
        String AMULAMCS = "AMULAMCS";
        String GOKARNA = "GOKARNA";
        String JAIPUR = "JAIPUR";

        String NOTIFICATON = "realtime-services/notification";
        String NOTIFICATON_ACK = "realtime-services/notification-acknowledgement";
        String SYNC_CHECK = "realtime-services/sync-request";
        String SYNC_CHECK_ACK = "realtime-services/sync-request-acknowledgement";

        String IDENTITY_CHECK = "androiddpu/v5/android-dpu/register";
        String VERIFICATION = "androiddpu/v5/android-dpu/verification";
        String START_UP = "androiddpu/v5/android-dpu/start-up";
        String RATE_DOWNLOAD = "androiddpu/v5/realtime-services/purchase-rate";
        String RATE_DETAIL_DOWNLOAD = "androiddpu/v5/realtime-services/purchase-rate-detail";
        String RATE_DOWNLOAD_ACK = "androiddpu/v5/realtime-services/rate-download-acknowledgement";
        String SENT_BOX_COUNT = "androiddpu/v5/master-data/sentbox-count";
        String SENT_BOX_CHECK = "androiddpu/v5/master-data/sentbox";
        String SENT_BOX_ACK = "androiddpu/v5/master-data/acknowledgement";
        String INBOX = "androiddpu/v5/master-data/inbox";
        String APP_UPDATE = "app-update";
        String UPDATE_LOG = "/sync-app-update";
        String DATA_PROCESSOR = "http://192.168.1.86:6375/eipl-amcs-processor/";
        String PROCESSOR_SYNC = "sync";
        String MILK_COLLECTION = "milk-collection";
        String SENT_BOX_DESKTOP = "androiddpu/v5/master-data/sentbox-desktop";
        String SENT_BOX_DESKTOP_COUNT = "androiddpu/v5/master-data/sentbox-count-desktop";
        String SENT_BOX_DESKTOP_ACK = "androiddpu/v5/master-data/acknowledgement-desktop";
//        String LIVE_URL = "http://qaqc.emilkpro.in/";
//        String LIVE_URL = "http://amulamcsuat.emilkpro.in/webservice/amcs/v1/";
//        String LIVE_URL = "http://qaqc.emilkpro.in/webservice/amcs/v1/";
//        String LIVE_URL = "http://jaipurduss.emilkpro.in/webservice/amcs/v1/";
//        String LIVE_URL = "https://amulamcs.yamatech.app/webservice/amcs/v1/";
          String MEMBER_DOWNLOAD = "androiddpu/v5/realtime-services/member-download";
    }

    public interface CollectionType {
        String MEMBER_COLL = "member_collection";
        String SOCIETY_COLL = "society_collection";
        String LOCAL_SALE = "local_sale";
        String DISPATCH = "dispatch";
        String RECEIPT = "receipt";
    }

    public interface DEVICE_TAG {
        String WS_TAG = "ws";
        String ANALYSER_TAG = "ma";
        String ANALYSER2_TAG = "ma2";
        String ANALYSER3_TAG = "ma3";
        String ANALYSER4_TAG = "ma4";
        String SPLITTER_TAG = "sp";
    }

    public interface Props {
        String BASE_URL = "baseurl";
        String APP_REQUEST_DEBUG = "app.request.debug";
        String IDENTITY_DOCK = "identity.dock";
        String IDENTITY_SOCIETY = "identity.society";
        String IDENTITY_UNION = "identity.union";
        String IDENTITY_VERSION = "identity.version";
        String DEFAULT_CREDIT_LIMIT = "default.creditlimit";
        String CLR_CONST1 = "clr.const1";
        String CLR_CONST2 = "clr.const2";
        String LTR_TO_KG = "ltr.to.kg";
        String DEFAULT_SNF = "default.snf";
        String DEFAULT_SNF_VALUE = "deafult.snf.value";
        String QTY_READING_ROUND = "qty.reading.rounding";
        String QUALITY_READING_ROUND = "quality.reading.rounding";
        String SAMPLE_MILK_SIZE = "sample.milk.size";
        String ACCEPT_MILK_OTHERTHAN_DEFAULT_MILKTYPE = "accept.milktype.otherthen.default";
        String ALLOW_MULTIENTRY_SAMEMILKTYPE = "allow.multipleentry.samemilktype";
        String ALLOW_MULTIENTRY_DIFFMILKTYPE = "allow.multipleentry.diffmilktype";
        String MEMBER_COLLECTION_QTY_MODE = "member.collection.qty.mode";
        String SOCIETY_COLLECTION_QTY_MODE = "society.collection.qty.mode";
        String MILKSALE_QTY_MODE = "milksale.qty.mode";
        String DISPATCH_QTY_MODE = "dispatch.qty.mode";
        String RECEIPT_QTY_MODE = "receipt.qty.mode";
        String AVG_PARAM_PREV_SHIFTS = "avg.param.prev.shiftcount";
        String APP_LANGUAGE = "app.languages";
        String ALLOW_CASHPAYMENT = "allow.cashpayment";
        String SYSTEM_ID = "identity.id";
        String VERSION = "identity.version";
        String BASE_URL_REALTIME = "baseurl.realtime";
    }

    public interface ReportPath {
        String SHIFT_REPORT_CODEWISE = "ShiftReportCodeWise";
        String PRICE_DIFFERENCE = "PriceDifference";
        String MILK_DISPATCH_MONTH_WISE = "MilkDispatchMonthWise";
        String STAFF_SALARY_ADDITION = "StaffSalaryAddition";
        String PATRAK_ONE = "PatrakOne";
        String STAFF_SALARY_DEDUCTION = "StaffSalaryDeduction";
        String COMMITTEE_REGISTER = "CommitteeRegister";
        String STAFF_SALARY = "StaffSalaryPayment";
        String Milk_Collection_Year_Wise = "MilkCollectionYearWise";
        String Milk_Collection_Month_Wise = "MilkCollectionMonthWise";
        String Milk_Collection_Quarter_Wise = "MilkCollectionQuarterWise";
        String MILK_COLLECTION_AUDIT = "MilkCollectionAudit";
        String SHIFT_REPORT_MEMBERWISE = "ShiftReportMemberWise";
        String MILK_DISPATCH_CHALLAN = "MilkDispatchChallan";
        String MILK_DISPATCH_CHALLAN_FORMAT_TWO = "MilkDispatchChallanFormatTwo";
        String PAYMENT_REGISTER_CASH = "PaymentRegisterCash";
        String PAYMENT_REGISTER_BANK = "PaymentRegisterBank";
        String PAYMENT_REGISTER_BANK_IFSC = "PaymentRegisterBankWithIfsc";
        String BONUS_REGISTER_BANK = "BonusRegisterBank";
        String BONUS_REGISTER_CASH = "BonusRegisterCash";
        String BONUS_REGISTER_BANK1 = "BonusRegisterBank1";
        String BONUS_SUMMARY = "BonusSummary";
        String BONUS = "Bonus";
        String BONUS_REGISTER_CASH1 = "BonusRegisterCash1";
        String PAYMENT_REGISTER_ALL = "PaymentRegisterAll";
        String MEETING_REGISTER = "MeetingRegister";
        String BONUS_REGISTER = "BonusRegister";
        String BONUS_REGISTER1 = "BonusRegister1";
        String SHARE_DIVIDEND = "ShareDividend";
        String SHARE_ISSUE = "ShareIssue";
        String SHARE_ISSUE_2 = "ShareIssue2";

        String SHARE_CANCEL = "ShareCancel";
        String SHARE_TRANSFER = "ShareTransfer";
        String SHARE_MEMBER = "ShareMember";

        String MEMBER_COLLECTION_SUMMARY = "MemberCollectionSummary";
        String MEMBER_COLLECTION_SUMMARY1 = "MemberCollectionSummary1";
        String MEMBER_REGISTER = "MemberRegister";
        String MEMBER_REGISTER_ONE = "MemberRegister1";
        String DAIRY_SALE_REGISTER = "DairySaleRegister";
        String SUMMARY_2 = "Summary";
        String SOCIETY_PURCHASE = "SocietyPurchase";
        String SOCIETY_PURCHASE_MEMBER_WISE = "SocietyPurchaseMemberWise";
        String RPT_CDA = "CdaReport";
        String RptCDADateWiseWithMilkType = "RptCDADateWiseWithMilkType";
        String RptCDAShiftWiseWithMilkType = "RptCDAShiftWiseWithMilkType";
        String RptCDADateWiseWithoutMilkType = "RptCDADateWiseWithoutMilkType";
        String RptCDAShiftWiseWithoutMilkType = "RptCDAShiftWiseWithoutMilkType";
        String RptCDAKgFatKgSNFDateWiseWithMilkType = "RptCDAKgFatKgSNFDateWiseWithMilkType";
        String RptCDAKgFatKgSNFShiftWiseWithMilkType = "RptCDAKgFatKgSNFShiftWiseWithMilkType";
        String RptCDAKgFatKgSNFDateWiseWithoutMilkType = "RptCDAKgFatKgSNFDateWiseWithoutMilkType";
        String RptCDAKgFatKgSNFShiftWiseWithoutMilkType = "RptCDAKgFatKgSNFShiftWiseWithoutMilkType";
        String RtpMilkCollectionProfitloss = "RtpMilkCollectionProfitloss";

        String MEMBER_BILL_CONSOLIDATE = "MemberBillingConsolidated";
        String MEMBER_BILL_MONTH_YEAR = "MemberBillingMonthYearWise";
        String MEMBER_BILL_PAYMENT_CYCLE = "MemberBillingPaymentCycleWise";

        String MEMBER_BILL_BANK_DETAIL = "MemberBillingBankDetail";

        String MEMBER_LOCAL_MILK_SALE_DATE_SHIFT_WISE = "LocalMilkSaleDateShiftWise";
        String MEMBER_LOCAL_MILK_SALE_DATE_WISE = "LocalMilkSaleDateWise";

        String MEMBER_BILL_HEAD_SUMMARY = "MemberBillHeadSummary";
        String MEMBER_BILL_HEAD_DETAIL = "MemberBillHeadDetail";

        String CONSUMER_WISE_PRODUCT_SALE_DETAIL = "ConsumerWiseProductSaleDetail";
        String PRODUCT_SALE_DETAIL_CONSUMER_WISE = "ProductSaleDetailConsumerWise";
        String PRODUCT_SALE_DETAILS = "ProductSaleDetails";
        String CODE_WISE_PRODUCT_SALE_DETAIL = "CodeWiseProductSaleDetail";
        String MEMBER_WISE_HEAD_PIVOTING = "MemberWiseHeadPivoting";

        String MEMBER_MILK_COLLECTION_SLIP = "MemberMilkCollectionSlip";
        String MEMBER_BILLING_HEAD = "MemberBillingHead";

        String ELECTION_REGISTER = "ElectionRegister";
        String ELECTION_REGISTER_MILK_TYPE = "ElectionRegisterMilkType";
        String RPT_LEDGER_BOOK = "LedgerBook";
        String RPT_LEDGER_BOOK_SUB_LEDGER = "LedgerBookSubLedger";
        String RPT_LEDGER_BOOK_SUMMARY = "LedgerBookSummary";
        String RPT_SUB_LEDGER_BOOK_SUMMARY = "SubLedger Book Summary";
        String RPT_SUB_LEDGER_BOOK_DATE_WISE = "SubLedger Book Date Wise";
        String RPT_SUB_LEDGER_BOOK_CONSOLIDATE = "SubLedger Book Summary Consolidate";
        String RPT_STOCK_VALUATION = "Stock Valuation";
        String RPT_STOCK_VALUATION_WITH_SALE_PURCHASE = "Stock Valuation WIth Sale And Purchase";
        String RPT_TRADINGREPORT = "TradingReport";
        String RPT_PROFIT_LOSS_REPORT = "ProfitLossReport";
        String RPT_BALANCESHEET = "Balancesheet";
        String RPT_LEDGER_OPENING_BALANCE = "LedgerOpeningBalance";

        String RPT_DAY_BOOK = "DayBook";
        String RPT_CASH_BOOK = "CashBook";

        String MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION = "MemberWiseMilkTypeWiseConsolidateCollection";
        String MEMBER_WISE_CONSOLIDATE_COLLECTION = "MemberWiseConsolidateCollection";
        String MEMBER_WISE_CONSOLIDATE_COLLECTION2 = "MemberWiseConsolidateCollection2";
        String MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION = "MemberWiseMilkTypeWiseConsolidateCollectionWithDeduction";
        String MEMBER_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION = "MemberWiseConsolidateCollectionWithDeduction";
        String MEMBER_WISE_CONSOLIDATE_COLLECTION2_WITH_DEDUCTION = "MemberWiseConsolidateCollection2WithDeduction";
        String RtpMilkCollectionProfitloss2 = "RtpMilkCollectionProfitloss2";
        String RtpMilkCollectionProfitloss2WithOutMilkType = "RtpMilkCollectionProfitloss2WithOutMilkType";
        String SCHEME_RATE_SOCIETY_PURCHASE = "SchemeRateSocietyPurchaseReport";
        String SCHEME_RATE_SOCIETY_PURCHASE_MEMBER_WISE = "SchemeRateSocietyPurchaseMemberWise";
        String SCHEME_RATE_MEMBER_MILK_COLLECTION_SLIP = "SchemeRateMemberMilkCollectionSlip";
        String PURCHASE_REGISTER_MONTH_WISE = "PurchaseRegisterMonthWise";
        String PAYMENT_REGISTER_WITH_DEDUCTION = "PaymentRegisterWithDeduction";
        String MILK_DISPATCH_CHALLAN_FORMAT_THREE = "DispatchFormatThree";

//        -- MilkCollectionLocalSaleDispatchFormat2WithOutMilkType
    }

    public interface EventCode {
        int LOCAL_MILK_SALE = 103;
        int MILK_COLLECTION = 101;
        int MEMBER_BILL = 109;
        int CASH_ADVANCE = 104;
        int PRODUCT_SALE_CASH = 107;
        int PRODUCT_SALE_CREDIT = 108;
        int PRODUCT_RECEIPT = 106;
    }

    public interface SubLedgerType {
        short MEMBER = (short) 1;
    }

    public enum ClientCode {
        AMULAMCS,
        JAIPUR_AMCS,
        LACTALIS_QA,
        BANAS_AMCS,
        AMCS_QAQCJ;
    }
}
