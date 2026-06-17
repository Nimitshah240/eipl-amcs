package com.eipl.amcs.report.util;

import ch.qos.logback.classic.Logger;
import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.utils.AppConstant;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import static com.eipl.amcs.utils.AppConstant.DB_LOC;

public class ReportGenerate {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ReportGenerate.class);

    public static JasperPrint getReportDataSourceViewer(String path, Map<String, Object> params,
                                                        JRBeanCollectionDataSource jrBeanCollectionDataSource) {
        try {
            return JasperFillManager.fillReport(getPath(path), params, jrBeanCollectionDataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JasperPrint getReportDataSourceViewer(String path, Map<String, Object> params,
                                                        JREmptyDataSource jrBeanCollectionDataSource) {
        try {
            return JasperFillManager.fillReport(getPath(path), params, jrBeanCollectionDataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JasperPrint getReportDataSourceJasperPrint(String path, Map<String, Object> params,
                                                             JRBeanCollectionDataSource jrBeanCollectionDataSource) {
        try {
            return JasperFillManager.fillReport(getPath(path), params, jrBeanCollectionDataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JasperPrint getReportDataSourceJasperPrint(String path, Map<String, Object> params) {
        try {
            String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
            try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
                return JasperFillManager.fillReport(getPath(path), params, connection);
            }
        } catch (Exception e) {
            ReportGenerate.showError(e);
            e.printStackTrace();
        }
        return null;
    }

    private static String getPath(String path) {
        switch (path) {

            // Milk collection jaspe file
            case AppConstant.ReportPath.SHIFT_REPORT_CODEWISE:
                return "resources/report/milkcollection/ShiftReportCodeWise.jasper";
            case AppConstant.ReportPath.PRICE_DIFFERENCE:
                return "resources/report/milkcollection/PriceDifference.jasper";
            case AppConstant.ReportPath.MILK_DISPATCH_MONTH_WISE:
                return "resources/report/milkcollection/MilkSaleDispatchMonthWise.jasper";
            case AppConstant.ReportPath.SHIFT_REPORT_MEMBERWISE:
                return "resources/report/milkcollection/ShiftReportMemberWise.jasper";
            case AppConstant.ReportPath.MILK_DISPATCH_CHALLAN:
                return "resources/report/milkcollection/MilkDispatchNote.jasper";
            case AppConstant.ReportPath.PAYMENT_REGISTER_CASH:
                return "resources/report/milkcollection/PaymentForCash.jasper";
            case AppConstant.ReportPath.PAYMENT_REGISTER_BANK:
                return "resources/report/milkcollection/PaymentRegisterForBank.jasper";
            case AppConstant.ReportPath.PAYMENT_REGISTER_BANK_IFSC:
                return "resources/report/milkcollection/PaymentRegisterForBankWithIfsc.jasper";
            case AppConstant.ReportPath.PAYMENT_REGISTER_ALL:
                return "resources/report/milkcollection/PaymentForCashMilkTypeWise.jasper";
            case AppConstant.ReportPath.BONUS_REGISTER:
                return "resources/report/milkcollection/BonusRegister.jasper";
            case AppConstant.ReportPath.BONUS_REGISTER_CASH:
                return "resources/report/milkcollection/BonusRegisterPaymentTypeCash.jasper";
            case AppConstant.ReportPath.BONUS_REGISTER_BANK:
                return "resources/report/milkcollection/BonusRegisterPaymentTypeBank.jasper";
            case AppConstant.ReportPath.BONUS_SUMMARY:
                return "resources/report/milkcollection/BonusSummary.jasper";
            case AppConstant.ReportPath.BONUS:
                return "resources/report/milkcollection/Bonus.jasper";

            case AppConstant.ReportPath.BONUS_REGISTER1:
                return "resources/report/milkcollection/BonusRegisterConsolidated.jasper";
            case AppConstant.ReportPath.BONUS_REGISTER_CASH1:
                return "resources/report/milkcollection/BonusRegisterPaymentTypeCashConsolidated.jasper";
            case AppConstant.ReportPath.BONUS_REGISTER_BANK1:
                return "resources/report/milkcollection/BonusRegisterPaymentTypeBankConsolidated.jasper";

            case AppConstant.ReportPath.MEMBER_REGISTER:
                return "resources/report/milkcollection/MemberRegister.jasper";
            case AppConstant.ReportPath.MEMBER_REGISTER_ONE:
                return "resources/report/milkcollection/MemberRegister1.jasper";
            case AppConstant.ReportPath.MEMBER_COLLECTION_SUMMARY:
                return "resources/report/milkcollection/MemberCollectionSummary.jasper";
            case AppConstant.ReportPath.MEMBER_COLLECTION_SUMMARY1:
                return "resources/report/milkcollection/MemberCollectionSummary1.jasper";
            case AppConstant.ReportPath.SOCIETY_PURCHASE:
                return "resources/report/milkcollection/SocietyPurchase.jasper";
            case AppConstant.ReportPath.SOCIETY_PURCHASE_MEMBER_WISE:
                return "resources/report/milkcollection/SocietyPurchaseMemberWise.jasper";
            case AppConstant.ReportPath.DAIRY_SALE_REGISTER:
                return "resources/report/milkcollection/DairySaleRegister.jasper";
            case AppConstant.ReportPath.SUMMARY_2:
                return "resources/report/milkcollection/summary2.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_CONSOLIDATE:
                return "resources/report/milkcollection/MemberBillingConsolidated.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_MONTH_YEAR:
                return "resources/report/milkcollection/MemberBillingMonthYearWise.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_PAYMENT_CYCLE:
                return "resources/report/milkcollection/MemberBillingPaymentCycleWise.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_BANK_DETAIL:
                return "resources/report/milkcollection/MemberBillingBankDetail.jasper";
            case AppConstant.ReportPath.MEMBER_LOCAL_MILK_SALE_DATE_SHIFT_WISE:
                return "resources/report/milkcollection/LocalMilkSaleDateShiftWise.jasper";
            case AppConstant.ReportPath.MEMBER_LOCAL_MILK_SALE_DATE_WISE:
                return "resources/report/milkcollection/LocalMilkSaleDateWise.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_HEAD_SUMMARY:
                return "resources/report/milkcollection/MemberBillHeadSummary.jasper";
            case AppConstant.ReportPath.MEMBER_BILL_HEAD_DETAIL:
                return "resources/report/milkcollection/MemberBillHeadDetail.jasper";
            case AppConstant.ReportPath.CONSUMER_WISE_PRODUCT_SALE_DETAIL:
                return "resources/report/milkcollection/ConsumerWiseProductSaleDetail.jasper";
            case AppConstant.ReportPath.PRODUCT_SALE_DETAIL_CONSUMER_WISE:
                return "resources/report/milkcollection/ProductSaleDetailConsumerWise.jasper";
            case AppConstant.ReportPath.PRODUCT_SALE_DETAILS:
                return "resources/report/milkcollection/ProductSaleDetails.jasper";
            case AppConstant.ReportPath.CODE_WISE_PRODUCT_SALE_DETAIL:
                return "resources/report/milkcollection/CodeWiseProductSaleDetail.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_HEAD_PIVOTING:
                return "resources/report/milkcollection/MemberWiseHeadPivoting.jasper";
            case AppConstant.ReportPath.MEMBER_MILK_COLLECTION_SLIP:
                return "resources/report/milkcollection/MemberMilkCollectionSlip.jasper";
            case AppConstant.ReportPath.MEMBER_BILLING_HEAD:
                return "resources/report/milkcollection/MemberBilling.jasper";
            case AppConstant.ReportPath.MEETING_REGISTER:
                return "resources/report/milkcollection/MeetingDetails.jasper";
            case AppConstant.ReportPath.ELECTION_REGISTER:
                return "resources/report/milkcollection/ElectionRegister.jasper";
            case AppConstant.ReportPath.ELECTION_REGISTER_MILK_TYPE:
                return "resources/report/milkcollection/ElectionRegisterMilkType.jasper";
            case AppConstant.ReportPath.MILK_DISPATCH_CHALLAN_FORMAT_TWO:
                return "resources/report/milkcollection/DispatchNoteValva.jasper";
            case AppConstant.ReportPath.MILK_COLLECTION_AUDIT:
                return "resources/report/milkcollection/MilkCollectionAudit.jasper";
            case AppConstant.ReportPath.STAFF_SALARY:
                return "resources/report/milkcollection/StaffSalaryPayment.jasper";
            case AppConstant.ReportPath.STAFF_SALARY_ADDITION:
                return "resources/report/milkcollection/StaffSalaryAddition.jasper";
            case AppConstant.ReportPath.STAFF_SALARY_DEDUCTION:
                return "resources/report/milkcollection/StaffSalaryDeduction.jasper";
            case AppConstant.ReportPath.COMMITTEE_REGISTER:
                return "resources/report/milkcollection/CommitteeRegister.jasper";
            case AppConstant.ReportPath.PATRAK_ONE:
                return "resources/report/milkcollection/PatrakOne.jasper";
            case AppConstant.ReportPath.RPT_LEDGER_BOOK:
                return "//resources/report/milkcollection/LedgerBookSubLedger.jasper";
            case AppConstant.ReportPath.RPT_LEDGER_BOOK_SUB_LEDGER:
                return "resources/report/milkcollection/LedgerBook.jasper";
            case AppConstant.ReportPath.RPT_LEDGER_BOOK_SUMMARY:
                return "resources/report/milkcollection/LedgerBookSummary.jasper";

            case AppConstant.ReportPath.RPT_SUB_LEDGER_BOOK_DATE_WISE:
                return "resources/report/milkcollection/SubLedgerReport.jasper";
            case AppConstant.ReportPath.RPT_SUB_LEDGER_BOOK_CONSOLIDATE:
                return "resources/report/milkcollection/SubLedgerReportConsolidate.jasper";
            case AppConstant.ReportPath.RPT_SUB_LEDGER_BOOK_SUMMARY:
                return "resources/report/milkcollection/SubLedgerBookSummary.jasper";
            case AppConstant.ReportPath.RPT_STOCK_VALUATION:
                return "resources/report/milkcollection/StockValuation.jasper";
            case AppConstant.ReportPath.RPT_STOCK_VALUATION_WITH_SALE_PURCHASE:
                return "resources/report/milkcollection/Patrakone_B.jasper";
            case AppConstant.ReportPath.RPT_TRADINGREPORT:
                return "resources/report/milkcollection/TradingReport.jasper";
            case AppConstant.ReportPath.RPT_PROFIT_LOSS_REPORT:
                return "resources/report/milkcollection/ProfitLoss.jasper";

            case AppConstant.ReportPath.RPT_BALANCESHEET:
                return "resources/report/milkcollection/BalanceSheet.jasper";
            case AppConstant.ReportPath.RPT_LEDGER_OPENING_BALANCE:
                return "resources/report/milkcollection/LedgerOpeningBalance.jasper";

            case AppConstant.ReportPath.RPT_DAY_BOOK:
                return "resources/report/milkcollection/DayBook.jasper";
            case AppConstant.ReportPath.RPT_CASH_BOOK:
                return "resources/report/milkcollection/CashBook.jasper";

//            case AppConstant.ReportPath.RPT_CDA:
//                return "resources/report/milkcollection/CashBook.jasper";

            case AppConstant.ReportPath.RptCDADateWiseWithMilkType:
                return "resources/report/cda/CDADateWithMilkTypeWise.jasper";

            case AppConstant.ReportPath.RptCDADateWiseWithoutMilkType:
                return "resources/report/cda/CDADateWithoutMilkTypeWise.jasper";

            case AppConstant.ReportPath.RptCDAShiftWiseWithMilkType:
                return "resources/report/cda/CDAShiftWithMilkTyprWise.jasper";

            case AppConstant.ReportPath.RptCDAShiftWiseWithoutMilkType:
                return "resources/report/cda/CDAShiftWithoutMilkTyprWise.jasper";

            case AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithMilkType:
                return "resources/report/cda/CDADateKgFatKgSnfWithMilkTypeWise.jasper";

            case AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithoutMilkType:
                return "resources/report/cda/CDADateKgFatKgSnfWithoutMilkTypeWise.jasper";

            case AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithMilkType:
                return "resources/report/cda/CDAShiftKgFatKgSnfWithMilkTyprWise.jasper";
            case AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithoutMilkType:
                return "resources/report/cda/CDAShiftKgFatKgSnfWithoutMilkTyprWise.jasper";
            case AppConstant.ReportPath.RtpMilkCollectionProfitloss:
                return "resources/report/milkcollection/MilkProfitLoss.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION:
                return "resources/report/milkcollection/MemberWiseMilkTypeWiseConsolidateCollection.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION:
                return "resources/report/milkcollection/MemberWiseConsolidateCollection.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION2:
                return "resources/report/milkcollection/MemberWiseConsolidateCollection2.jasper";

            case AppConstant.ReportPath.MEMBER_WISE_MILK_TYPE_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION:
                return "resources/report/milkcollection/MemberWiseMilkTypeWiseConsolidateCollectionWithDeduction.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION_WITH_DEDUCTION:
                return "resources/report/milkcollection/MemberWiseConsolidateCollectionWithDeduction.jasper";
            case AppConstant.ReportPath.MEMBER_WISE_CONSOLIDATE_COLLECTION2_WITH_DEDUCTION:
                return "resources/report/milkcollection/MemberWiseConsolidateCollection2WithDeduction.jasper";
            case AppConstant.ReportPath.Milk_Collection_Year_Wise:
                return "resources/report/milkcollection/MilkCollectionYearWise.jasper";
            case AppConstant.ReportPath.Milk_Collection_Month_Wise:
                return "resources/report/milkcollection/MilkCollectionMonthWise.jasper";
            case AppConstant.ReportPath.Milk_Collection_Quarter_Wise:
                return "resources/report/milkcollection/MilkCollectionQuarterWise.jasper";

//            case AppConstant.ReportPath.RPT_CDA:
//                return "resources/report/milkcollection/CashBook.jasper";

//            case AppConstant.ReportPath.RptCDADateWiseWithMilkType:
//                return "resources/report/milkcollection/CDADateWithMilkTypeWise.jasper";
//
//            case AppConstant.ReportPath.RptCDADateWiseWithoutMilkType:
//                return "resources/report/milkcollection/CDADateWithoutMilkTypeWise.jasper";
//
//            case AppConstant.ReportPath.RptCDAShiftWiseWithMilkType:
//                return "resources/report/milkcollection/CDAShiftWithMilkTyprWise.jasper";
//
//            case AppConstant.ReportPath.RptCDAShiftWiseWithoutMilkType:
//                return "resources/report/milkcollection/CDAShiftWithoutMilkTyprWise.jasper";
//
//            case AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithMilkType:
//                return "resources/report/milkcollection/CDADateKgFatKgSnfWithMilkTypeWise.jasper";
//
//            case AppConstant.ReportPath.RptCDAKgFatKgSNFDateWiseWithoutMilkType:
//                return "resources/report/milkcollection/CDADateKgFatKgSnfWithoutMilkTypeWise.jasper";
//
//            case AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithMilkType:
//                return "resources/report/milkcollection/CDAShiftKgFatKgSnfWithMilkTyprWise.jasper";
//            case AppConstant.ReportPath.RptCDAKgFatKgSNFShiftWiseWithoutMilkType:
//                return "resources/report/milkcollection/CDAShiftKgFatKgSnfWithoutMilkTyprWise.jasper";
//            case AppConstant.ReportPath.RtpMilkCollectionProfitloss:
//                return "resources/report/milkcollection/MilkProfitLoss.jasper";
            case AppConstant.ReportPath.RtpMilkCollectionProfitloss2:
                return "resources/report/milkcollection/MilkCollectionLocalSaleDispatchFormat2.jasper";
            case AppConstant.ReportPath.RtpMilkCollectionProfitloss2WithOutMilkType:
                return "resources/report/milkcollection/MilkCollectionLocalSaleDispatchFormat2WithOutMilkType.jasper";


            case AppConstant.ReportPath.SHARE_DIVIDEND:
                return "resources/report/milkcollection/ShareDividend.jasper";
            case AppConstant.ReportPath.SHARE_ISSUE:
                return "resources/report/milkcollection/ShareIssue.jasper";
            case AppConstant.ReportPath.SHARE_ISSUE_2:
                return "resources/report/milkcollection/ShareIssue2.jasper";
            case AppConstant.ReportPath.SHARE_CANCEL:
                return "resources/report/milkcollection/ShareCancelled.jasper";
            case AppConstant.ReportPath.SHARE_TRANSFER:
                return "resources/report/milkcollection/ShareTransfer.jasper";

            case AppConstant.ReportPath.SHARE_MEMBER:
                return "resources/report/milkcollection/ShareMember.jasper";
            case AppConstant.ReportPath.SCHEME_RATE_SOCIETY_PURCHASE:
                return "resources/report/milkcollection/SchemeRateSocietyPurchaseReport.jasper";
            case AppConstant.ReportPath.SCHEME_RATE_SOCIETY_PURCHASE_MEMBER_WISE:
                return "resources/report/milkcollection/SchemeRateSocietyPurchaseMemberWise.jasper";
            case AppConstant.ReportPath.SCHEME_RATE_MEMBER_MILK_COLLECTION_SLIP:
                return "resources/report/milkcollection/SchemeRateMemberMilkCollectionSlip.jasper";
            case AppConstant.ReportPath.PURCHASE_REGISTER_MONTH_WISE:
                return "resources/report/milkcollection/PurchaseRegisterMonthWise.jasper";
            case AppConstant.ReportPath.PAYMENT_REGISTER_WITH_DEDUCTION:
                return "resources/report/milkcollection/PaymentRegisterWithDeduction.jasper";
            case AppConstant.ReportPath.MILK_DISPATCH_CHALLAN_FORMAT_THREE:
                return "resources/report/milkcollection/DispatchFormatThree.jasper";
            case AppConstant.ReportPath.EDIT_COLLECTION_REPORT:
                return "resources/report/milkcollection/EditCollectionReport.jasper";
            case AppConstant.ReportPath.MANUAL_COLLECTION_REPORT:
                return "resources/report/milkcollection/ManualCollnData.jasper";
            case AppConstant.ReportPath.ITEM_PURCHASE_REGISTER:
                return "resources/report/milkcollection/ItemPurReg.jasper";
            case AppConstant.ReportPath.ITEM_SALE_REGISTER:
                return "resources/report/milkcollection/ItemSaleReg.jasper";
            case AppConstant.ReportPath.PRODUCT_STOCK_STATEMENT:
                return "resources/report/milkcollection/StockStatementReport.jasper";
            case AppConstant.ReportPath.PRODUCT_STOCK_LEDGER:
                return "resources/report/milkcollection/ProductStockLedger.jasper";
            case AppConstant.ReportPath.LEDGER_SUMMARY:
                return "resources/report/milkcollection/TBReport.jasper";
            case AppConstant.ReportPath.FARMER_REGISTER:
                return "resources/report/milkcollection/FarmerRegister.jasper";
            case AppConstant.ReportPath.FARMER_PENDING_LIST:
                return "resources/report/milkcollection/FarmerPendingList.jasper";
            case AppConstant.ReportPath.Farmer_MAPPING:
                return "resources/report/milkcollection/MemberRegisterFarmerMapping.jasper";
            case AppConstant.ReportPath.ELECTION_REGISTER_ONE:
                return "resources/report/milkcollection/ElectionRegister1.jasper";
            case AppConstant.ReportPath.ROJMED:
                return "resources/report/milkcollection/Rojmed.jasper";
            case AppConstant.ReportPath.ProfitLossOne:
                return "resources/report/milkcollection/ProfitLossOne.jasper";
            case AppConstant.ReportPath.TradingReportOne:
                return "resources/report/milkcollection/TradingReportOne.jasper";
            case AppConstant.ReportPath.BalanceSheetOne:
                return "resources/report/milkcollection/BalanceSheetOne.jasper";
            case AppConstant.ReportPath.TBReportOne:
                return "resources/report/milkcollection/TBReportOne.jasper";
        }
        return null;
    }

    public static void showError(Exception e) {
        LOGGER.error("Report Error : ", e);
        MyAlert alert = new ErrorAlert(MainApp.getStage(), "Report Error", "Error in Generating Report.");
        alert.createAlert();
    }
}
