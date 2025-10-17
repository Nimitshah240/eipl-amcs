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
    public static final String EIPL_DB_PASS = "EAmcs2021";
    public static final String EIPL_DB_NAME = "eipl_amcs_db";

    public static final String versionNo = "4.4";

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
        String PERMISSION = "permissions/";
        String CUSTOMER_NUMBER = "customers/next-code";
        String TAX = "tax";
        String STATE = "states";
        String PRODUCT_SALE_TRANSACTION = "product-sale-transactions";
        String DISTRICT = "districts";
        String HAMLET = "hamlets";
        String CUSTOMER = "customers";
        String CUSTOMER_DETAIL = "customers/customer-details";
        String SUBDISTRICT = "sub-districts";
        String VILLAGE = "villages";
        String DOCK = "docks";
        String PRODUCT_SALE_TO_MEMBER_NUMBER = "product-sale/next-code";
        String VOUCHER_NEXT_CODE = "voucher/next-code";
        String SHARE = "share";
        String SHARE_DIVIDEND = "share_dividend";
        String SHARE_RATE = "share_rate";
        String MEETING_TO_MEETING_ADD_EDIT = "meeting-agenda/next-code";
        String SOCIETY = "societies";
        String MILK_TYPE = "milk-types";
        String PRODUCT_SALE_TOMEMBER = "product-sale";
        String DOCK_NUMBER = "docks/next-code";
        String BANK = "banks";
        String PRODUCT_STOCK = "product-stocks";
        String BRANCH = "branches";
        String BMC = "bmcs";
        String MCC = "mccs";
        String PLANT = "plants";
        String ROUTE = "routes";
        String UNION = "unions";
        String GENDER = "genders";
        String RATE_TYPE = "rate-types";
        String MEMBERTYPE = "member-types";
        String MILKCLASS = "milk-classes";
        String MILKQUALITYTYPE = "milk-quality-types";
        String FORMULA = "formula";
        String SHIFT = "shifts";
        String UNIT = "units";
        String UNITCONVERSION = "unit-conversions";
        String HARDWAREDEVICE = "hardware-devices";
        String LOCAL_MILK_SALE_RATE = "local-milk-sale-rates";
        String MEMBER_MILK_PURCHASE_RATE = "member-milk-purchase-rates";
        String SOCIETY_MILK_PURCHASE_RATE = "society-milk-purchase-rates";
        String MEMBER_MILK_PURCHASE_RATE_DETAIL = "member-milk-purchase-rate-details";
        String SOCIETY_MILK_PURCHASE_RATE_DETAIL = "society-milk-purchase-rate-details";
        String MEMBER_MILK_PURCHASE_RATE_APPLICABILITY = "member-milk-purchase-rate-applicability";
        String SOCIETY_MILK_PURCHASE_RATE_APPLICABILITY = "society-milk-purchase-rate-applicability";
        String SOCIETY_PAYMENT_CYCLE = "society-payment-cycles";
        String HARDWARE_DEVICE_CONFIG = "hardware-device-config";
        String GENERAL_CONFIG = "general-config";
        String MEETING = "meeting-agenda";
        String CASHADVANCE = "/cash_advance";
        String BMC_RUNNING_HRS = "/bmc-running-hours";
        String RECORDING_PARAMETER = "/bmc-recordings";
        String ALLOWDCSMANUALCOLLECTIONRANGE = "/allow_dcs_manual_collection_range";
        String DPUINCENTIVEREQUEST = "/dpu_incentive_master";
        String LEDGER_OPENING_BALANCE = "/ledger_opening_balance";
        String SUB_LEDGER_OPENING_BALANCE = "/sub_ledger_opening_balance";
        String IDENTITY_CALL = "identity_details";
        String NOTIFICATION = "/notification";
        String MOM = "meeting-agenda/mom";
        String MOM_ACTION = "meeting-agenda/mom_action";
        String STAFF_SALARY_HEAD_NUMBER = "staff-salary-head/next-code";
        String STAFF_SALARY_HEAD = "staff-salary-head";
        String STAFF_SALARY_MAPPING = "staff-salary-mapping";
        String LEDGER_MAPPING_TAX_DETAIL = "ledger-mapping-tax-detail";


        String COMMITTEE_MEMBERS = "committee-members";
        String STAFF_MEMBERS = "staff-member";
        String DESIGNATION = "designation";
        String LEDGER_NUMBER = "ledgers/next-code";
        String SUBLEDGER_NUMBER = "sub-ledgers/next-code";
        String STAFF_NUMBER = "staff-member/next-code";


        String FINANCIAL_YEAR = "financial-years";
        String AUTH = "auth";
        String LEDGER = "ledgers";
        String EVENT = "event";
        String LEDGER_TYPE = "ledger-types";
        String LEDGER_MAPPING_PRODUCT_GROUP = "ledger-mapping-product-group";
        String LEDGER_MAPPING_EVENT = "ledger-mapping-event";
        String SUB_LEDGER_LEDGER_CONFIG = "sub_ledger_ledger_config";
        String LEDGER_MAPPING_BILL_HEAD = "ledger-mapping-bill-head";
        String LEDGER_MAPPING_VOUCHER_TYPE = "voucher-type-ledger-mapping";
        String LEDGER_GROUP = "ledger-groups";
        String VOUCHER = "voucher";
        String SUB_LEDGER = "sub-ledgers";
        String LEDGER_SUB_LEDGER_MAPPING = "ledgers/ledger-subledger-mapping";
        String VOUCHER_TYPE = "voucher-types";
        String VOUCHER_TYPE_LEDGER_CONFIG = "voucher-type-ledger-config";
        String PRODUCT_SALE_RATE = "product-sale-rates";
        String PRODUCT_SALE_RATE_NUMBER = "product-sale-rates/next-code";
        String PRODUCT_PURCHASE_RATE = "product-purchase-rates";
        String PRODUCT_PURCHASE_RATE_NUMBER = "product-purchase-rates/next-code";
        String PRODUCT_GROUP = "product-groups";
        String PRODUCT = "products";
        String PRODUCT_NUMBER = "products/next-code";
        String MEMBER = "members";
        String MEMBER_EKYC = "member-ekyc";
        String MEMBER_DETAIL = "members/member-detail";
        String PRODUCT_RECEIPT_MATERIAL = "product-receipt";
        String PRODUCT_REQUISITION = "product-requisition";
        String PRODUCT_DISPATCH = "product-dispatch";
        String PRODUCT_SALE = "product-sale";
        String MEMBER_TYPE = "member-types";
        String MEMBER_NUMBER = "members/next-code";
        String PRODUCT_RECEIPT_TAX_CALCULATED = "product-receipt-tax";
        String PRODUCT_RECEIPT_TRANSACTION = "product-receipt-transactions";
        String PRODUCT_REQUISITION_TRANSACTION = "product-requisition-transaction";
        String PRODUCT_SALE_TO_MEMBER_INSTALLMENT = "product-sale-to-member-installments";
        String PRODUCT_SALE_TO_MEMBER_TAX_CALCULATED = "product-sale-to-member-tax-calculates";
        String PRODUCT_SALE_TO_MEMBER_TRANSACTION = "product-sale-to-member-transactions";
        String SOCIETY_VENDOR_MAPPING = "society-vendor-mappings";
        String VENDOR = "vendors";
        String VENDOR_FILE_LOG = "vendor-file-logs";
        String VENDOR_RULES = "vendor-rules";
        String LOCAL_MILK_SALE = "local-milk-sale";
        String INSURANCE = "insurance";
        String IDENTITY = "identity";
        String MILK_COLLECTION = "milk_collection";
        String RATE = "local-milk-sale-rates/rate";
        String BILLHEAD = "bill-head";
        String BILLCRITERIA = "bill-criteria";
        String MEMBER_BILLING = "member-bill";
        String MILK_DISPATCH = "milk-dispatch";
        String MILK_RECEIPT = "milk-receipt";
        String REPORT_SHIFT_CODE = "report/shift-report-codewise";
        String REPORT_MILK_CHALLAN = "report/milk-dispatch-challan";
        String REPORT_MEMBER_COLLECTION = "report/member-collection-summary";
        String PAYMENT_REGISTER = "report/payment-register";
        String MEETING_REGISTER = "report/MeetingDetails";
        String BONUS = "bonus";
        String BONUS_REGISTER = "report/bonus-register";
        String MILK_SUMMARY_DATA_ENTRY = "milk_collection/collection-summary-data";
        String MEMBER_REGISTER = "report/member-register";
        String REPORT_DAIRY_SALE_REGISTER = "report/dairy-register";
        String SOCIETY_PURCHASE = "report/society-puchase";
        String PAYMENT_FOR_BANK_REPORT_EXCEL = "report/findPaymentRegisterReportExcel";
        String BONUS_REPORT_EXCEL = "report/findBonus";
        String BONUS_REPORT_ALL = "report/findExcel";
        String STOCK_VALUATION = "report/stock_valuation";
        String STOCK_VALUATION_WITH_PRODUCT = "report/stock_valuation_with_product";
        String STOCK_VALUATION_WITH_SALE_PURCHASE = "report/stock_valuation/with_sale_purchase";
        String TRADING = "report/trading";
        String PROFITLOSS = "report/profit_loss";
        String BALANCESHEET = "report/balance_sheet";
        String MILK_COLLECTION_AUDIT = "report/MilkCollectionAudit";
        String STAFF_SALARY_ADDITION = "report/StaffSalaryAddition";
        String STAFF_SALARY_DEDUCTION = "report/StaffSalaryDeduction";
        String STAFF_SALARY = "report/StaffSalaryPayment";
        String PATRAK_ONE = "report/PatrakOne";
        String UPDATE_CHECK = MainApp.getProperty("updater.url", "");
        String AMULAMCS = "AMULAMCSS";
        String GOKARNA = "GOKARNA";
        String JAIPUR = "JAIPUR";
        String IDENTITY_CHECK = "amcs-desktop/register";
        String NOTIFICATON = "realtime-services/notification";
        String SYNC_CHECK = "realtime-services/sync-request";
        String SENT_BOX_CHECK = "realtime-services/sentbox";
        String SENT_BOX_COUNT = "realtime-services/sentbox-count";
        String NOTIFICATON_ACK = "realtime-services/notification-acknowledgement";
        String SENT_BOX_ACK = "realtime-services/acknowledgement";
        String SYNC_CHECK_ACK = "realtime-services/sync-request-acknowledgement";

        String RATE_DOWNLOAD = "realtime-services/purchase-rate";
        String RATE_DETAIL_DOWNLOAD = "realtime-services/purchase-rate-detail";
        String RATE_DOWNLOAD_ACK = "realtime-services/rate-download-acknowledgement";


        // String COMMITTEE_MEMBERS = "committee-members";
        String SUB_LEDGER_TYPE = "sub-ledger-type";
        String LEDGER_CLOSE = "report/ledger_closing";
        String NEXT_FY_DATE = "financial-years/next_fy_date";
        String FY_SUB_LEDGER_OPENING_BALANCE = "report/fy_sub_ledger_opening_balance";
        String FY_FETCH_BY_CODE = "financial-years/fetch_code";
        String SOCIETY_YEAR_CLOSING = "society_year_closing";
        String SUB_LEDGER_BALANCES = "report/subledgerbalance";
        String LEDGER_FETCH_BY_CODE = "report/ledger_fetch_by_code";
        String SUB_LEDGER_FETCH_BY_CODE = "report/sub_ledger_fetch_by_code";
        // String COMMITTEE_MEMBERS = "committee-members";
        // String DESIGNATION ="designation";
        String BILL_HEAD_NUMBER = "bill-head/next-code";
        String BILL_CRITERIA_NUMBER = "bill-criteria/next-code";
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
        //        String RPT_CDA="CdaReport";
//        String RptCDADateWiseWithMilkType="RptCDADateWiseWithMilkType";
//        String RptCDAShiftWiseWithMilkType="RptCDAShiftWiseWithMilkType";
//        String RptCDADateWiseWithoutMilkType="RptCDADateWiseWithoutMilkType";
//        String RptCDAShiftWiseWithoutMilkType="RptCDAShiftWiseWithoutMilkType";
//        String RptCDAKgFatKgSNFDateWiseWithMilkType="RptCDAKgFatKgSNFDateWiseWithMilkType";
//        String RptCDAKgFatKgSNFShiftWiseWithMilkType="RptCDAKgFatKgSNFShiftWiseWithMilkType";
//        String RptCDAKgFatKgSNFDateWiseWithoutMilkType="RptCDAKgFatKgSNFDateWiseWithoutMilkType";
//        String RptCDAKgFatKgSNFShiftWiseWithoutMilkType="RptCDAKgFatKgSNFShiftWiseWithoutMilkType";
//        String RtpMilkCollectionProfitloss="RtpMilkCollectionProfitloss";
        String RtpMilkCollectionProfitloss2 = "RtpMilkCollectionProfitloss2";
        String RtpMilkCollectionProfitloss2WithOutMilkType = "RtpMilkCollectionProfitloss2WithOutMilkType";

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

}
