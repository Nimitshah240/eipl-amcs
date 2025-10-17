package com.eipl.amcs.report.bootcontroller;

import com.eipl.amcs.master.account.repository.LedgerRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.operation.billing.repository.BonusRepository;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.repository.MilkDispatchRepository;
import com.eipl.amcs.report.bootdto.LedgerBalance;
import com.eipl.amcs.report.bootdto.ProductStockValuationWithSaleAndPurchase;
import com.eipl.amcs.report.bootdto.ShiftReportCode;
import com.eipl.amcs.report.dto.*;
import com.eipl.amcs.utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
public class GeneralReportController {

    @Autowired
    private MilkCollectionRepository milkCollectionRepository;
    @Autowired
    private MilkDispatchRepository milkDispatchRepository;
    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private MemberBillRepository billRepository;
    @Autowired
    private BonusRepository bonusRepository;
    @Autowired
    private MemberRepository memberCollectionRepository;

    @GetMapping("/shift-report-codewise")
    public ResponseEntity<?> shiftReportCodewise(@RequestParam("societyCode") String societyCode, @RequestParam("collectionDateTime") String collectionDateTime) {
        LocalDateTime collectionDt = LocalDateTime.parse(collectionDateTime, AppConstant.DATE_TIME_FMT);
        List<ShiftReportCode> list = milkCollectionRepository.findShiftReport(societyCode, collectionDt);
        return new ResponseEntity<List<ShiftReportCode>>(list, HttpStatus.OK);
    }

    @GetMapping("/milk-dispatch-challan")
    public ResponseEntity<?> milkDispatchChallan(@RequestParam("societyCode") String societyCode, @RequestParam("challanNo") String challanNo) {
        List<MilkDispatchChallan> list = milkDispatchRepository.findDispatchChallanReport(societyCode, challanNo);
        return new ResponseEntity<List<MilkDispatchChallan>>(list, HttpStatus.OK);

    }

    @GetMapping("/payment-register")
    public ResponseEntity<?> paymentRegister(@RequestParam("societyCode") String societyCode, @RequestParam("societyPaymentCycleCode") String societyPaymentCycleCode, @RequestParam("paymentMode") Integer paymentMode) {
//        List<Map<String, Object>> list = billRepository.findPaymentRegisterReport(societyCode, societyPaymentCycleCode);
        List<PaymentRegisterForCash> list = billRepository.findPaymentRegisterReport(societyCode, societyPaymentCycleCode, paymentMode);
        return new ResponseEntity<List<PaymentRegisterForCash>>(list, HttpStatus.OK);
//        return null;
    }

    @GetMapping("/bonus-register")
    public ResponseEntity<?> bonusRegister(@RequestParam("societyCode") String societyCode, @RequestParam("bonusSummaryCode") String bonusSummaryCode) {
        List<BonusRegister> list = bonusRepository.findBonus(societyCode, bonusSummaryCode);
//                List<Map<String, Object>> list = bonusRepository.findBonus(societyCode, bonusSummaryCode);

        return new ResponseEntity<List<BonusRegister>>(list, HttpStatus.OK);
//        return null;
    }

    @GetMapping("/member-collection-summary")
    public ResponseEntity<?> memberCollectionSummary(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("qtyMode") Integer qtyMode) {
        LocalDateTime fromDate1 = LocalDateTime.parse(fromDate, AppConstant.DATE_TIME_FMT);
        LocalDateTime toDate1 = LocalDateTime.parse(toDate, AppConstant.DATE_TIME_FMT);
        List<MemberCollectionSummary> list = memberCollectionRepository.findMemberCollectionReport(fromDate1, toDate1, societyCode, qtyMode);
//        List<List<Map<String, Object>>> list = memberCollectionRepository.findMemberCollectionReport(
//                fromDate1, toDate1, societyCode, qtyMode);
//        return null;
        return new ResponseEntity<List<MemberCollectionSummary>>(list, HttpStatus.OK);
    }

    @GetMapping("/member-register")
    public ResponseEntity<?> MemberRegister(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") String fromDate, @RequestParam("memberType") Integer memberType) {
        LocalDate fromDate1 = LocalDate.parse(fromDate);
        List<MemberRegister> list = memberCollectionRepository.findMemberRegisterData(societyCode, fromDate1, memberType);
        return new ResponseEntity<List<MemberRegister>>(list, HttpStatus.OK);
    }

    @GetMapping("/dairy-register")
    public ResponseEntity<?> dairySaleRegister(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("milkType") Integer milkTypeCode) {
        LocalDateTime fromDate1 = LocalDateTime.parse(fromDate);
        LocalDateTime toDate1 = LocalDateTime.parse(toDate);
        List<DairySaleRegister> list = milkDispatchRepository.findDairySaleRegisterData(societyCode, fromDate1, toDate1, milkTypeCode);
        return new ResponseEntity<List<DairySaleRegister>>(list, HttpStatus.OK);

    }

    @GetMapping("/society-puchase")
    public ResponseEntity<?> societyPurchase(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
        LocalDateTime fromDate1 = LocalDateTime.parse(fromDate);
        LocalDateTime toDate1 = LocalDateTime.parse(toDate);
        List<SocietyPurchase> list = milkDispatchRepository.fndSocietyPurchase(societyCode, fromDate1, toDate1);
        return new ResponseEntity<List<SocietyPurchase>>(list, HttpStatus.OK);
    }

    @GetMapping("/stock_valuation")
    public ResponseEntity<List<ProductStockValuation>> fetchCurrentStockByProduct(@RequestParam("fromDate") Date fromDate, @RequestParam("societyCode") String societyCode, @RequestParam("locale") String locale) {
        return new ResponseEntity<List<ProductStockValuation>>(fetchStockValuation(fromDate, societyCode, locale), HttpStatus.OK);
    }

    @GetMapping("/stock_valuation_with_product")
    public ResponseEntity<List<ProductStockValuation>> fetchCurrentStockByPfroduct(@RequestParam("fromDate") Date fromDate, @RequestParam("societyCode") String societyCode, @RequestParam("locale") String locale, @RequestParam("productCode") String productCode) {
        return new ResponseEntity<List<ProductStockValuation>>(fetchStockValuationWithProduct(fromDate, societyCode, locale, productCode), HttpStatus.OK);
    }

    @GetMapping("/stock_valuation/with_sale_purchase")
    public ResponseEntity<List<ProductStockValuationWithSaleAndPurchase>> fetchCurrentStockByProductWithSaleAndPurchase(@RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("societyCode") String societyCode, @RequestParam("locale") String locale, @RequestParam("productCode") String productCode) {
        return new ResponseEntity<List<ProductStockValuationWithSaleAndPurchase>>(fetchStockValuationWithSaleAndPurchase(fromDate, toDate, societyCode, locale, productCode), HttpStatus.OK);
    }

    @GetMapping("/trading")
    public ResponseEntity<List<LedgerBalance>> fetchCurrentStockByProduct(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("locale") String locale) {

        return new ResponseEntity<List<LedgerBalance>>(fetchTrading(societyCode, fromDate, toDate, locale), HttpStatus.OK);

    }

    @GetMapping("/profit_loss")
    public ResponseEntity<List<LedgerBalance>> fetchProfitLoss(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("incomeExpense") int incomeExpense, @RequestParam("locale") String locale) {
        double tradingProfit = 0;
        try {
            if (incomeExpense == 1 && tradingProfit == 0) {
                List<LedgerBalance> listTrading = fetchTrading(societyCode, fromDate, toDate, locale);
                if (listTrading != null) tradingProfit = listTrading.stream().mapToDouble(m -> m.getBalance()).sum();
            }
            List<Object[]> list = ledgerRepository.fetchProfitLoss(societyCode, fromDate, toDate, incomeExpense, locale);
            List<LedgerBalance> listResp = new ArrayList<>();

            if (list != null && !list.isEmpty()) {
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
                });
                if (incomeExpense == 1 && tradingProfit > 0)
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                else if (incomeExpense == 0 && tradingProfit < 0)
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
            } else {
                if (incomeExpense == 1 && tradingProfit > 0) {
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                } else if (incomeExpense == 0 && tradingProfit < 0) {
                    listResp.add(new LedgerBalance("", "trading", 0, 0, tradingProfit));
                }
            }
            return new ResponseEntity<List<LedgerBalance>>(listResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/balance_sheet")
    public ResponseEntity<List<LedgerBalance>> fetchBalanceSheet(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("liabilityAsset") int liabilityAsset, @RequestParam("locale") String locale) {
        try {
            List<Object[]> list = ledgerRepository.fetchBalanceSheet(societyCode, fromDate, toDate, liabilityAsset, locale);
            if (list != null && !list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], 0, 0, ((BigDecimal) item[2]).doubleValue()));
                });
                return new ResponseEntity<List<LedgerBalance>>(listResp, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/fy_sub_ledger_opening_balance")
    public ResponseEntity<List<LedgerClose>> fetchSubLedgerOpeningBalance(@RequestParam("ledgerCode") String ledgerCode, @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("locale") String locale) {
        try {
            List<LedgerClose> listResp = new ArrayList<>();
            List<Object[]> list = ledgerRepository.fetchSubLedgerOpeningBalance(ledgerCode, fromDate, toDate, locale);
            if (list != null && !list.isEmpty()) {
                for (Object[] arr : list) {
                    LedgerClose bal = new LedgerClose((String) arr[0], (String) arr[1], (!(((BigDecimal) arr[2]).doubleValue() < 0)), ((BigDecimal) arr[2]).doubleValue());
                    listResp.add(bal);
                }
            }
            return new ResponseEntity<List<LedgerClose>>(listResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/ledger_closing")
    public ResponseEntity<List<LedgerClose>> fetchBalanceSheet(@RequestParam("societyCode") String societyCode, @RequestParam("fromDate") Date fromDate, @RequestParam("toDate") Date toDate, @RequestParam("locale") String locale) {
        try {
            List<LedgerClose> listResp = new ArrayList<>();

            List<Object[]> list = ledgerRepository.fetchLedgerClosing(societyCode, fromDate, toDate, locale);
            if (list != null && !list.isEmpty()) {
                for (Object[] arr : list) {
                    LedgerClose bal = new LedgerClose((String) arr[0], (String) arr[1], (!(((BigDecimal) arr[2]).doubleValue() < 0)), ((BigDecimal) arr[2]).doubleValue());
                    listResp.add(bal);
                }
            }
            return new ResponseEntity<List<LedgerClose>>(listResp, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ProductStockValuation> fetchStockValuation(Date endDate, String societyCode, String locale) {
        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProduct(endDate, societyCode, locale);
        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            List<Object[]> listReceipt = ledgerRepository.fetchProductReceipt((String) item[0], endDate);
            if (listReceipt != null && !listReceipt.isEmpty()) {

                double stock = Double.parseDouble(item[2].toString());
                for (Object[] arrReceipt : listReceipt) {
                    if (stock <= 0) {
                        break;
                    }
                    if (Double.parseDouble(arrReceipt[1].toString()) >= stock) {
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[1].toString()));
                        break;
                    } else {
                        stockValue = Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
                        stock -= Double.parseDouble(arrReceipt[1].toString());
                    }
                }
                list.add(new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), stockValue, (String) item[3]));
            } else {
                list.add(new ProductStockValuation((String) item[0], (String) item[1], 0, 0, (String) item[3]));
            }
        });
        return list;
    }


    private List<ProductStockValuation> fetchStockValuationWithProduct(Date endDate, String societyCode, String locale, String productCode) {
        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProductWithProduct(endDate, societyCode, locale, productCode);
        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            List<Object[]> listReceipt = ledgerRepository.fetchProductReceipt((String) item[0], endDate);
            if (listReceipt != null && !listReceipt.isEmpty()) {

                double stock = Double.parseDouble(item[2].toString());
                for (Object[] arrReceipt : listReceipt) {
                    if (stock <= 0) {
                        break;
                    }
                    if (Double.parseDouble(arrReceipt[1].toString()) >= stock) {
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[1].toString()));
                        break;
                    } else {
                        stockValue = Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
                        stock -= Double.parseDouble(arrReceipt[1].toString());
                    }
                }
                list.add(new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), stockValue, (String) item[3]));
            } else {
                list.add(new ProductStockValuation((String) item[0], (String) item[1], 0, 0, (String) item[3]));
            }
        });
        return list;
    }


    private List<ProductStockValuationWithSaleAndPurchase> fetchStockValuationWithSaleAndPurchase(Date fromDate, Date toDate, String societyCode, String locale, String productCode) {
        List<ProductStockValuation> list = new ArrayList<>();
        List<Object[]> listCurrentStock = ledgerRepository.fetchCurrentStockByProductWithProduct(Date.valueOf(fromDate.toLocalDate().minusDays(1)), societyCode, locale, productCode);
        List<Map<String, Object>> listPurchase = ledgerRepository.fetchPurchaseProduct(fromDate, toDate, societyCode, locale, productCode);
        List<Map<String, Object>> listSale = ledgerRepository.fetchSaleProduct(fromDate, toDate, societyCode, locale, productCode);
        Map<String, Map<String, Object>> listPurchaseMap = new HashMap<>();
        for (Map<String, Object> map : listPurchase) {
            listPurchaseMap.put(String.valueOf(map.get("productCode")), map);
        }
        Map<String, Map<String, Object>> listSaleMap = new HashMap<>();
        for (Map<String, Object> map : listSale) {
            listSaleMap.put(String.valueOf(map.get("productCode")), map);
        }

        listCurrentStock.forEach(item -> {
            double stockValue = 0;
            List<Object[]> listReceipt = ledgerRepository.fetchProductReceipt((String) item[0], fromDate);
            if (listReceipt != null && !listReceipt.isEmpty()) {

                double stock = Double.parseDouble(item[2].toString());
                for (Object[] arrReceipt : listReceipt) {
                    if (stock <= 0) {
                        break;
                    }
                    if (Double.parseDouble(arrReceipt[1].toString()) >= stock) {
                        stockValue = stockValue + (stock * Double.parseDouble(arrReceipt[1].toString()));
                        break;
                    } else {
                        stockValue = Double.parseDouble(arrReceipt[1].toString()) * Double.parseDouble(arrReceipt[0].toString());
                        stock -= Double.parseDouble(arrReceipt[1].toString());
                    }
                }
                list.add(new ProductStockValuation((String) item[0], (String) item[1], Double.parseDouble(item[2].toString()), stockValue, (String) item[3]));
            } else {
                list.add(new ProductStockValuation((String) item[0], (String) item[1], 0, 0, (String) item[3]));
            }
        });

        List<ProductStockValuationWithSaleAndPurchase> finalList = new ArrayList<>();
        for (ProductStockValuation productStockValuation : list) {
            ProductStockValuationWithSaleAndPurchase obj = new ProductStockValuationWithSaleAndPurchase();
            obj.setProductCode(productStockValuation.getProductCode());
            obj.setProductName(productStockValuation.getProductName());
            obj.setStock(productStockValuation.getStock());
            obj.setValuation(productStockValuation.getValuation());
            obj.setUnit(productStockValuation.getUnit());

            if (listPurchaseMap.get(productStockValuation.getProductCode()) != null) {
                obj.setPurchaseProductCode(productStockValuation.getProductCode());
                obj.setPurchaseProductName(productStockValuation.getProductName());
                obj.setPurchaseStock(Double.parseDouble(String.valueOf(listPurchaseMap.get(productStockValuation.getProductCode()).get("stock"))));
                obj.setPurchaseValuation(Double.parseDouble(String.valueOf(listPurchaseMap.get(productStockValuation.getProductCode()).get("valuation"))));
                obj.setPurchaseUnit(productStockValuation.getUnit());
            }
            if (listSaleMap.get(productStockValuation.getProductCode()) != null) {
                obj.setSaleProductCode(productStockValuation.getProductCode());
                obj.setSaleProductName(productStockValuation.getProductName());
                obj.setSaleStock(Double.parseDouble(String.valueOf(listSaleMap.get(productStockValuation.getProductCode()).get("stock"))));
                obj.setSaleValuation(Double.parseDouble(String.valueOf(listSaleMap.get(productStockValuation.getProductCode()).get("valuation"))));
                obj.setSaleUnit(productStockValuation.getUnit());
            }
            obj.setTotalStock(obj.getStock() + obj.getPurchaseStock());
            obj.setTotalValuation(obj.getValuation() + obj.getPurchaseValuation());
            obj.setTotalProductCode(obj.getProductCode());
            obj.setTotalProductName(obj.getProductName());
            obj.setTotalUnit(obj.getUnit());


            obj.setClosingStock(obj.getStock() + obj.getPurchaseStock() - obj.getSaleStock());
            obj.setClosingValuation(obj.getValuation() + obj.getPurchaseValuation() - obj.getSaleValuation());
            obj.setClosingProductCode(obj.getProductCode());
            obj.setClosingProductName(obj.getProductName());
            obj.setClosingUnit(obj.getUnit());
            finalList.add(obj);
        }
        return finalList;
    }


    private List<LedgerBalance> fetchTrading(String societyCode, Date fromDate, Date toDate, String locale) {
        double stockValuation = 0;
        try {
            List<ProductStockValuation> listStockValuation = fetchStockValuation(toDate, societyCode, locale);
            if (listStockValuation != null)
                stockValuation = listStockValuation.stream().mapToDouble(m -> m.getValuation()).sum();

            List<Object[]> list = ledgerRepository.fetchTrading(societyCode, fromDate, toDate, locale);
            if (list == null || list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                listResp.add(new LedgerBalance("", "stockvaluation", 0, stockValuation, stockValuation));
                return listResp;
            } else if (list != null && !list.isEmpty()) {
                List<LedgerBalance> listResp = new ArrayList<>();
                list.forEach(item -> {
                    listResp.add(new LedgerBalance((String) item[0], (String) item[1], Double.parseDouble(item[3].toString()), Double.parseDouble(item[2].toString()), Double.parseDouble(item[4].toString())));
                });
                listResp.add(new LedgerBalance("", "stockvaluation", 0, stockValuation, stockValuation));
                return listResp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/subledgerbalance")
    private List<Object[]> fetchSubLedgerOpeningBalaneceSecond(@RequestParam String societyCode, @RequestParam String fromDate, @RequestParam String toDate, @RequestParam String locale) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);


        List<Object[]> listStockValuation = ledgerRepository.fetchSubLedgerOpeningBalanceSecond(societyCode, from, to, locale);
        return listStockValuation;

    }


//	rpt_payment_register_bank`(IN p_society_code varchar(10),IN p_society_payment_cycle_code varchar(15),IN p_payment_mode INT,IN p_bank_code varchar(4),IN p_locale varchar(50))

    @GetMapping("/findPaymentRegisterReportExcel")
    public ResponseEntity<?> findPaymentRegisterReportExcel(@RequestParam("societyCode") String societyCode, @RequestParam("societyPaymentCycleCode") String societyPaymentCycleCode, @RequestParam("paymentmode") Integer paymentMode, @RequestParam("bankcode") String bankCode, @RequestParam("locale") String locale) {
        List<PaymentForBank> list = billRepository.findPaymentRegisterReportExcel(societyCode, societyPaymentCycleCode, paymentMode, bankCode, locale);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findBonus")
    public ResponseEntity<?> findBonusForAll(@RequestParam("societyCode") String societyCode, @RequestParam("memberCode") String memberCode, @RequestParam("paymentmode") Integer paymentMode, @RequestParam("bankcode") String bankCode, @RequestParam("locale") String locale, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("bonusType") Integer bonusType, @RequestParam("milkTypeCode") String milkTypeCode) {

        LocalDate fd = LocalDate.parse(fromDate);
        LocalDate td = LocalDate.parse(toDate);
        List<Map<String, Object>> list = bonusRepository.findAllBonus(societyCode, fd, td, memberCode, locale, bankCode, paymentMode, bonusType, milkTypeCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findExcel")
    public ResponseEntity<?> findBonusExcel(@RequestParam("societyCode") String societyCode, @RequestParam("memberCode") String memberCode, @RequestParam("paymentmode") Integer paymentMode, @RequestParam("bankcode") String bankCode, @RequestParam("locale") String locale, @RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, @RequestParam("bonusType") Integer bonusType) {

        LocalDate fd = LocalDate.parse(fromDate);
        LocalDate td = LocalDate.parse(toDate);
        List<Map<String, Object>> list = bonusRepository.findAllExcel(societyCode, fd, td, memberCode, locale, bankCode, paymentMode, bonusType);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
