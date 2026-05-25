package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberCreditLimit;
import com.eipl.amcs.master.operation.model.MemberCreditLimitTransaction;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitRepository;
import com.eipl.amcs.master.operation.repository.MemberCreditLimitTransactionRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.org.repository.UnionRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.repository.MemberBillSummaryRepository;
import com.eipl.amcs.operation.inventory.dto.ProductSaleAcUtil;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.dto.ProductSaleMigrateDto;
import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.*;
import com.eipl.amcs.operation.inventory.repository.*;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.VoucherUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProductSaleServiceImpl implements ProductSaleService {

    @Autowired
    private ProductSaleRepository productSaleRepository;
    @Autowired
    private ProductSaleTransactionRepository saleTransRepository;
    @Autowired
    private ProductSaleTaxRepository saleTaxRepository;
    @Autowired
    private ProductStockRepository stockRepository;
    @Autowired
    private ProductStockTransactionRepository stockTxnRepository;
    @Autowired
    private ProductSaleInstallmentRepository installmentRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberCreditLimitRepository memberCreditLimitRepository;
    @Autowired
    private MemberCreditLimitTransactionRepository memberCreditLimitTxnRepository;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private UnionRepository unionRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherTransactionRepository voucherTxnRepository;
    @Autowired
    private VoucherSubLedgerRepository voucherSubLedgerRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private LedgerMappingProductGroupRepository ledgerMappingProductGroupRepository;
    @Autowired
    private LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private MemberBillSummaryRepository summaryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductSale> findAll(LocalDate fromDt, LocalDate toDt) {
        return productSaleRepository.findByInvoiceDateBetween(fromDt, toDt, Sort.by("invoiceNo").descending());
    }

    @Override
    @Transactional
    public ProductSaleDto save(ProductSaleDto productSaleDto, String identityInfo) {
        String societyCode = productSaleDto.getSaleTxnTaxDtoList().get(0).getTransaction().getSocietyCode();
        String unionCode = productSaleDto.getSaleTxnTaxDtoList().get(0).getTransaction().getUnionCode();

        if (productSaleDto.getProductSale().getDeductionStartDate() != null) {
            SocietyPaymentCycle paymentCycle = paymentCycleRepository.findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(productSaleDto.getProductSale().getDeductionStartDate().atTime(13, 5, 5), productSaleDto.getProductSale().getDeductionStartDate().atTime(13, 5, 5));
            if (paymentCycle == null || paymentCycle.getLockBillingProcess())
                throw new BusinessValidationFailException(LocalMilkSale.class, CommonUtils.getFieldError("productsale", "Invoice Date", productSaleDto.getProductSale().getInvoiceDate(), "paymentcyclenotfound"));

            MemberBillSummary memberBillSummary = summaryRepository.findTop1ByDeductionFromDateLessThanEqualAndDeductionToDateGreaterThanEqual(productSaleDto.getProductSale().getDeductionStartDate(), productSaleDto.getProductSale().getDeductionStartDate());
            if (memberBillSummary != null)
                throw new BusinessValidationFailException(ProductSale.class, CommonUtils.getFieldError("productsale", "Invoice Date", productSaleDto.getProductSale().getInvoiceDate(), "billing.already.completed"));
        }


        ProductSaleDto dtoNew = new ProductSaleDto();

        String voucherNo = createAutoPosting(productSaleDto, null, identityInfo);
        productSaleDto.getProductSale().setInitData();
        productSaleDto.getProductSale().setSociety(societyRepository.findById(societyCode).get());
        productSaleDto.getProductSale().setUnion(unionRepository.findById(unionCode).get());
        productSaleDto.getProductSale().setUnion(Hibernate.unproxy(productSaleDto.getProductSale().getUnion(), Union.class));
        productSaleDto.getProductSale().setVoucherNo(voucherNo);
        productSaleDto.getProductSale().setxCol1(UUID.randomUUID().toString());
        ProductSale saleNew = productSaleRepository.customSave(productSaleDto.getProductSale(), identityInfo);
        saleNew.setSociety(productSaleDto.getProductSale().getSociety());
        saleNew.setUnion(productSaleDto.getProductSale().getUnion());
        saleNew.setDock(productSaleDto.getProductSale().getDock());
        saleNew.setxCol1(UUID.randomUUID().toString());
        List<SaleTxnTaxDto> listNew = new ArrayList<>();
        dtoNew.setProductSale(saleNew);
        dtoNew.setSaleTxnTaxDtoList(listNew);

        int txnCnt = 1;
        for (SaleTxnTaxDto dto : productSaleDto.getSaleTxnTaxDtoList()) {
            SaleTxnTaxDto temp = new SaleTxnTaxDto();
            dto.getTransaction().setInitData();
            dto.getTransaction().setProductSale(saleNew);
            dto.getTransaction().setInvoiceTxnNo(saleNew.getInvoiceNo() + "T" + txnCnt);

            ProductSaleTransaction t = saleTransRepository.customSave(dto.getTransaction(), identityInfo);
            t.setProductSale(saleNew);
            t.setProduct(dto.getTransaction().getProduct());
            t.setUnitCode(dto.getTransaction().getUnitCode());
            t.setTaxCode(dto.getTransaction().getTaxCode());
            t.setxCol1(UUID.randomUUID().toString());
            temp.setTransaction(t);
            txnCnt++;

            if (dto.getSaleTaxList() != null && !dto.getSaleTaxList().isEmpty()) {
                List<ProductSaleTax> saleTaxListNew = new ArrayList<>();
                temp.setSaleTaxList(saleTaxListNew);

                int taxCnt = 1;
                for (ProductSaleTax recTax : dto.getSaleTaxList()) {
                    recTax.setCode(dto.getTransaction().getInvoiceTxnNo() + "T" + taxCnt);
                    recTax.setProductSale(saleNew);
                    recTax.setProductSaleTransaction(temp.getTransaction());
                    recTax.setInitData();

                    ProductSaleTax tt = saleTaxRepository.customSave(recTax, identityInfo);
                    tt.setProductSale(saleNew);
                    tt.setProductSaleTransaction(t);
                    tt.setTaxDetail(recTax.getTaxDetail());
                    tt.setxCol1(UUID.randomUUID().toString());

                    saleTaxListNew.add(tt);
                    taxCnt++;
                }
            }

            setupProductStock(dto.getTransaction(), productSaleDto.getProductSale().getSociety(), "CREATE", "Product Sale Add", identityInfo);
            listNew.add(temp);
        }
        int txnInstallment = 1;
        for (ProductSaleInstallment dto : productSaleDto.getSaleInstallments()) {
            Member member = memberRepository.findByCode(productSaleDto.getProductSale().getConsumerCode());
            dto.setCode(productSaleDto.getProductSale().getInvoiceNo() + "-" + txnInstallment);
            dto.setMember(member);
            dto.setSocietyPaymentCycle(dto.getSocietyPaymentCycle());
            dto.setInvoiceNo(saleNew.getInvoiceNo());
            dto.setSocietyCode(dto.getSocietyCode());
            dto.setUnionCode(MainApp.identityDto.getUnion().getCode());

            dto.setPreviousPendingAmount(BigDecimal.ZERO);
            dto.setInitData();
            dto.setBilling(false);
            dto.setType(1);
            dto.setxCol1(UUID.randomUUID().toString());
            installmentRepository.customSave(dto, identityInfo);
            txnInstallment++;
        }

        dtoNew.setSaleInstallments(null);
        return dtoNew;
    }

    private String createAutoPosting(ProductSaleDto productSaleDto, String voucherCode, String identityInfo) {
        try {
            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(productSaleDto.getProductSale().getPaymentMode() == (short) 0 ? AppConstant.EventCode.PRODUCT_SALE_CASH : AppConstant.EventCode.PRODUCT_SALE_CREDIT);
            if (eventsList == null || eventsList.isEmpty()) return null;

//            if (eventsList.stream().anyMatch(e -> e.getXCol1().equalsIgnoreCase("0"))) return null;


            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(productSaleDto.getProductSale().getInvoiceDate());
            String societyCode = productSaleDto.getProductSale().getSociety().getCode();
            if (voucherCode == null) {
                voucherCode = nextCodeService.getNextCode("Voucher", "code", societyCode + "/" + financialYear.get().getCode() + "/", 0);
                if (voucherCode == null) return null;

                // Voucher
                Voucher voucher = VoucherUtil.getVoucherInstance(voucherCode, productSaleDto.getProductSale().getInvoiceNo(), productSaleDto.getProductSale().getInvoiceDate(), productSaleDto.getProductSale().getInvoiceDate(), "Product Sale Auto Posting " + productSaleDto.getProductSale().getInvoiceDate(), eventsList.get(0).getVoucherType(), financialYear.isPresent() ? financialYear.get().getCode() : null, productSaleDto.getProductSale().getSociety(), productSaleDto.getProductSale().getUnion().getCode(), productSaleDto.getProductSale().getDock().getDockNo());
                voucher.setProcessName("tbl_product_sale");
                voucher.setProcessReference(productSaleDto.getProductSale().getInvoiceNo());
                voucher.setVoucherTransactions(new ArrayList<>());
                voucher.setxCol1(UUID.randomUUID().toString());
                // Calculate amount of txns
                List<LedgerMappingProductGroup> listLmpg = ledgerMappingProductGroupRepository.findAll(Sort.by("code"));
                List<LedgerMappingTaxDetail> listTaxMapping = ledgerMappingTaxDetailRepository.findAll(Sort.by("code"));
                List<ProductSaleAcUtil> list = new ArrayList<>();
                for (SaleTxnTaxDto saleTxnTaxDto : productSaleDto.getSaleTxnTaxDtoList()) {
                    Product product = productRepository.findById(saleTxnTaxDto.getTransaction().getProduct().getCode()).get();
                    ProductGroup group = product.getProductGroup();
                    if (group == null) continue;

//                    LedgerMappingProductGroup lmpg = listLmpg.stream().filter(p -> p.getProductGroup().getCode().intValue() == group.getCode().intValue()).findFirst().orElse(null);
//                    if (lmpg == null) continue;

                    ProductSaleAcUtil obj = list.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(product.getSaleLedger().getCode())).findFirst().orElse(null);
                    if (obj == null) {
                        obj = new ProductSaleAcUtil();
                        obj.setAmount(saleTxnTaxDto.getTransaction().getAmount().subtract(saleTxnTaxDto.getTransaction().getDiscount()));
                        obj.setLedger(saleTxnTaxDto.getTransaction().getProduct().getSaleLedger());
                        obj.setNarration("Product sale: " + saleTxnTaxDto.getTransaction().getProduct().getCode());
                        list.add(obj);
                    } else {
                        BigDecimal amt = saleTxnTaxDto.getTransaction().getAmount().subtract(saleTxnTaxDto.getTransaction().getDiscount());
                        obj.setAmount(obj.getAmount().add(amt));
                    }

                    // Tax ledgers
                    if (saleTxnTaxDto.getSaleTaxList() != null) {
                        for (ProductSaleTax productSaleTax : saleTxnTaxDto.getSaleTaxList()) {
                            LedgerMappingTaxDetail taxMap = listTaxMapping.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(productSaleTax.getTaxDetail().getCode())).findFirst().orElse(null);
                            if (taxMap == null) continue;

                            ProductSaleAcUtil obj1 = list.stream().filter(p -> p.getLedger().getCode().equalsIgnoreCase(taxMap.getSaleLedger().getCode())).findFirst().orElse(null);
                            if (obj1 == null) {
                                obj1 = new ProductSaleAcUtil();
                                obj1.setAmount(productSaleTax.getValue());
                                obj1.setLedger(taxMap.getSaleLedger());
                                obj1.setNarration("Tax : " + productSaleTax.getTaxDetail().getCode());
                                list.add(obj1);
                            } else {
                                obj1.setAmount(obj1.getAmount().add(productSaleTax.getValue()));
                            }
                        }
                    }
                }

                //Debit Txn
                BigDecimal amt = BigDecimal.ZERO;
                for (ProductSaleAcUtil a : list) {
                    amt = amt.add(a.getAmount());
                }
                VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, amt, false, eventsList.get(0).getDebitLedger(), "Product Sale On Credit To " + productSaleDto.getProductSale().getConsumerType() + ": " + productSaleDto.getProductSale().getConsumerCode(), "1");
                voucher.setProcessName("tbl_product_sale");
                voucher.setProcessReference(productSaleDto.getProductSale().getInvoiceNo());
                voucher.getVoucherTransactions().add(debitTxn);
                debitTxn.setxCol1(UUID.randomUUID().toString());
                debitTxn.setAutoPostedScreen(false);
                if (eventsList.get(0).getDebitSubLedger()) {
                    VoucherSubLedger voucherSubLedger = null;
                    Optional<SubLedger> subLedger = subLedgerRepository.findByTypeAndReferenceCode(productSaleDto.getProductSale().getConsumerType(), productSaleDto.getProductSale().getConsumerCode());
                    if (subLedger.isPresent()) {
                        debitTxn.setVoucherSubLedgers(new ArrayList<>());
                        voucherSubLedger = VoucherUtil.getVoucherSubLedger(voucher, debitTxn, "1", amt, false, "Product Sale On Credit To " + productSaleDto.getProductSale().getConsumerType() + ": " + productSaleDto.getProductSale().getConsumerCode(), subLedger.get());
                        if (voucherSubLedger != null) debitTxn.getVoucherSubLedgers().add(voucherSubLedger);
                    }
                }

                // Credit txn
                int sr = 2;
                for (ProductSaleAcUtil a : list) {

                    VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, a.getAmount(), true, a.getLedger(), a.getNarration(), String.valueOf(sr));
                    sr++;
                    creditTxn.setxCol1(UUID.randomUUID().toString());
                    voucher.getVoucherTransactions().add(creditTxn);
                    creditTxn.setAutoPostedScreen(false);
                }

                voucherRepository.customSave(voucher, identityInfo);
                if (!voucher.getVoucherTransactions().isEmpty()) {
                    for (VoucherTransaction voucherTransaction : voucher.getVoucherTransactions()) {
                        voucherTxnRepository.customSave(voucherTransaction, identityInfo);
                        if (voucherTransaction.getVoucherSubLedgers() != null && !voucherTransaction.getVoucherSubLedgers().isEmpty()) {
                            for (VoucherSubLedger voucherSubLedger : voucherTransaction.getVoucherSubLedgers()) {
                                voucherSubLedger.setxCol1(UUID.randomUUID().toString());
                                voucherSubLedgerRepository.customSave(voucherSubLedger, identityInfo);
                            }
                        }
                    }
                }
                return voucher.getCode();
            } else {
                Optional<Voucher> voucher = voucherRepository.findById(voucherCode);
                List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
                for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                    for (VoucherSubLedger voucherSubLedger : voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction)) {
                        voucherSubLedgerRepository.delete(voucherSubLedger);
                    }
                    voucherTxnRepository.delete(voucherTransaction);
                }
                voucherRepository.delete(voucher.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateCreditLimit(ProductSale obj, String op, String val, String identityInfo) {
        Optional<MemberCreditLimit> opMemberCredit = memberCreditLimitRepository.findByConsumerCodeAndConsumerType(obj.getConsumerCode(), obj.getConsumerType());
        if (opMemberCredit.isPresent()) {
            MemberCreditLimit oldObj = opMemberCredit.get();
            oldObj.setSociety(Hibernate.unproxy(oldObj.getSociety(), Society.class));
            String code = oldObj.getCode();
            BigDecimal olVal = oldObj.getBalance();
            if (op.equals("CREATE")) {
                oldObj.setBalance(oldObj.getBalance().subtract(obj.getNetAmount()).setScale(2, RoundingMode.HALF_UP));
                if (oldObj.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                    FieldError creditlimiterror = CommonUtils.getFieldError("productsale", "creditlimt", obj.getAmount(), "creditlimiterror");
                    throw new BusinessValidationFailException(getClass(), creditlimiterror);
                }
            } else if (op.equals("DELETE")) {
                oldObj.setBalance(oldObj.getBalance().add(obj.getNetAmount()).setScale(2, RoundingMode.HALF_UP));
            }
            oldObj.setupdateData();
            memberCreditLimitRepository.customUpdate(oldObj, identityInfo);

            MemberCreditLimitTransaction txn = new MemberCreditLimitTransaction();
            String txnCode = nextCodeService.getNextCode("MemberCreditLimitTransaction", "code", code + "T", 0);
            txn.setCode(txnCode);
            txn.setOldValue(olVal);
            txn.setNewValue(obj.getNetAmount());
            txn.setBalance(oldObj.getBalance());
            txn.setConsumerCode(obj.getConsumerCode());
            txn.setConsumerType(obj.getConsumerType());
            txn.setReferenceCode(obj.getInvoiceNo());
            txn.setTransactionType(val);
            txn.setTransactionDate(obj.getInvoiceDate());
            txn.setSociety(obj.getSociety());
            txn.setUnionCode(obj.getUnion().getCode());
            txn.setInitData();
            memberCreditLimitTxnRepository.customSave(txn, identityInfo);
        }
    }


    @Transactional
    private void setupProductStock(ProductSaleTransaction transaction, Society society, String operation, String trnsType, String identityInfo) {
        Optional<ProductStock> stockData = stockRepository.findByProduct(transaction.getProduct());
        String code = null;
        BigDecimal oldVal = null;
        if (stockData.isPresent()) {
            code = stockData.get().getCode();
            ProductStock stockOld = stockData.get();
            oldVal = stockOld.getStock();
            if (operation.equals("CREATE"))
                stockOld.setStock(stockOld.getStock().subtract(transaction.getQuantity()).setScale(3, RoundingMode.HALF_UP));
            else if (operation.equals("DELETE"))
                stockOld.setStock(stockOld.getStock().add(transaction.getQuantity()).setScale(3, RoundingMode.HALF_UP));
            stockOld.setupdateData();
            stockData.get().setProduct(Hibernate.unproxy(stockData.get().getProduct(), Product.class));
            stockData.get().setSociety(Hibernate.unproxy(stockData.get().getSociety(), Society.class));
            stockRepository.customUpdate(stockOld, identityInfo);
        } else {
            oldVal = BigDecimal.ZERO;

            code = nextCodeRepository.getNextCode("ProductStock", "code", transaction.getSocietyCode(), 0);
            ProductStock stock = new ProductStock();
            stock.setCode(code);
            if (operation.equals("CREATE")) stock.setStock(BigDecimal.ZERO.subtract(transaction.getQuantity()));
            else if (operation.equals("DELETE")) stock.setStock(transaction.getQuantity());
            stock.setUnionCode(transaction.getUnionCode());
            stock.setProduct(transaction.getProduct());
            stock.setSociety(society);
            stock.setInitData();
            stock.setxCol1(UUID.randomUUID().toString());
            stockRepository.customSave(stock, identityInfo);
        }

        // Stock transaction
        String txnCode = nextCodeRepository.getNextCode("ProductStockTransaction", "code", code + "T", 0);
        ProductStockTransaction txn = new ProductStockTransaction();
        txn.setCode(txnCode);
        txn.setNewValue(BigDecimal.ZERO.subtract(transaction.getQuantity()));
        txn.setOldValue(oldVal);
        if (operation.equals("CREATE"))
            txn.setFinalValue(oldVal.add(txn.getNewValue()).setScale(3, RoundingMode.HALF_UP));
        else if (operation.equals("DELETE"))
            txn.setFinalValue(oldVal.subtract(txn.getNewValue()).setScale(3, RoundingMode.HALF_UP));
        // FIXME(NIMIT | 25.05.2026): In product receipt reference code in txn code because this indicate stock transaction is of which txn.
//        txn.setReferenceCode(code);
        txn.setReferenceCode(transaction.getInvoiceTxnNo());


        txn.setTransactionDate(transaction.getProductSale().getInvoiceDate());
        txn.setTransactionType(trnsType);
        txn.setProduct(transaction.getProduct());
        txn.setSociety(society);
        txn.setUnionCode(transaction.getUnionCode());
        txn.setInitData();
        txn.setxCol1(UUID.randomUUID().toString());
        stockTxnRepository.customSave(txn, identityInfo);
    }

    @Override
    @Transactional
    public ProductSaleDto update(ProductSaleDto productSaleDto, String identityInfo) {
        ProductSale productSale = productSaleRepository.findById(productSaleDto.getProductSale().getInvoiceNo()).orElseThrow(() -> new EntityNotFoundException(ProductSale.class, "Invalid"));
        // Existing product receipt transactions
        if (productSaleDto.getProductSale() != null && productSaleDto.getProductSale().getDeductionStartDate() != null) {
            MemberBillSummary memberBillSummary = summaryRepository.findTop1ByDeductionFromDateLessThanEqualAndDeductionToDateGreaterThanEqual(productSale.getDeductionStartDate(), productSale.getDeductionStartDate());
            if (memberBillSummary != null)
                throw new BusinessValidationFailException(ProductSale.class, CommonUtils.getFieldError("productsale", "Invoice Date", productSale.getInvoiceDate(), "billing.already.completed"));
        }
        List<ProductSaleTransaction> listTxn = saleTransRepository.findByProductSale(productSale);
        List<ProductSaleTax> listTax = saleTaxRepository.findByProductSale(productSale);
        List<ProductSaleInstallment> listInstallment = installmentRepository.findByInvoiceNo(productSale.getInvoiceNo());

        for (ProductSaleTax tax : listTax) {
            saleTaxRepository.customDelete(tax, identityInfo);
        }
        for (ProductSaleInstallment inst : listInstallment) {
            installmentRepository.customDelete(inst, identityInfo);
        }
        for (ProductSaleTransaction txn : listTxn) {
            setupProductStock(txn, productSale.getSociety(), "DELETE", "Product Sale Delete", identityInfo);
            saleTransRepository.customDelete(txn, identityInfo);
        }


        ProductSaleDto dtoNew = new ProductSaleDto();
        createAutoPosting(productSaleDto, productSaleDto.getProductSale().getVoucherNo(), identityInfo);
        String voucherNo = createAutoPosting(productSaleDto, null, identityInfo);


        productSaleDto.getProductSale().setInitData();
        productSaleDto.getProductSale().setVoucherNo(voucherNo);
        ProductSale saleNew = productSaleRepository.customSave(productSaleDto.getProductSale(), identityInfo);
        saleNew.setUnion(productSaleDto.getProductSale().getUnion());
        saleNew.setSociety(productSaleDto.getProductSale().getSociety());
        saleNew.setDock(productSaleDto.getProductSale().getDock());
        // check credit sale 0-Bank, 1-Cash
        List<SaleTxnTaxDto> listNew = new ArrayList<>();
        dtoNew.setProductSale(saleNew);
        dtoNew.setSaleTxnTaxDtoList(listNew);

        int txnCnt = listTxn.size() + 1;
        for (SaleTxnTaxDto dto : productSaleDto.getSaleTxnTaxDtoList()) {
            SaleTxnTaxDto temp = new SaleTxnTaxDto();
            dto.getTransaction().setInitData();
            dto.getTransaction().setProductSale(saleNew);
            dto.getTransaction().setInvoiceTxnNo(saleNew.getInvoiceNo() + "T" + txnCnt);

            ProductSaleTransaction t = saleTransRepository.customSave(dto.getTransaction(), identityInfo);
            t.setProductSale(saleNew);
            t.setProduct(dto.getTransaction().getProduct());
            t.setUnitCode(dto.getTransaction().getUnitCode());
            t.setTaxCode(dto.getTransaction().getTaxCode());


            temp.setTransaction(t);
            txnCnt++;

            if (dto.getSaleTaxList() != null && !dto.getSaleTaxList().isEmpty()) {
                List<ProductSaleTax> saleTaxListNew = new ArrayList<>();
                temp.setSaleTaxList(saleTaxListNew);

                int taxCnt = 1;
                for (ProductSaleTax recTax : dto.getSaleTaxList()) {
                    recTax.setCode(dto.getTransaction().getInvoiceTxnNo() + "T" + taxCnt);
                    recTax.setProductSale(saleNew);
                    recTax.setProductSaleTransaction(temp.getTransaction());
                    recTax.setInitData();

                    ProductSaleTax tt = saleTaxRepository.customSave(recTax, identityInfo);
                    tt.setProductSale(saleNew);
                    tt.setProductSaleTransaction(t);
                    tt.setTaxDetail(recTax.getTaxDetail());

                    saleTaxListNew.add(tt);
                    taxCnt++;
                }
            }

            setupProductStock(dto.getTransaction(), productSaleDto.getProductSale().getSociety(), "CREATE", "Product Sale Add", identityInfo);
            listNew.add(temp);
        }
        int txnInstallment = 1;
        for (ProductSaleInstallment dto : productSaleDto.getSaleInstallments()) {
            dto.setCode(String.valueOf(txnInstallment));
            dto.setMember(memberRepository.findByCode(productSaleDto.getProductSale().getConsumerCode()));
            dto.setInvoiceNo(productSale.getInvoiceNo());
            ProductSaleInstallment temp = installmentRepository.save(dto);
            dto.setInitData();
            dto.setMember(memberRepository.findByCode(productSaleDto.getProductSale().getConsumerCode()));
            dto.setSocietyPaymentCycle(dto.getSocietyPaymentCycle());
            txnInstallment++;
        }
        // Delete old transaction and tax details
        return dtoNew;
    }

    @Override
    public Optional<ProductSale> findById(String code) {
        return productSaleRepository.findById(code);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        ProductSale productSale = productSaleRepository.findById(code).orElseThrow(() -> new EntityNotFoundException(ProductReceipt.class, "Invalid"));

        if (productSale.getDeductionStartDate() != null) {
            MemberBillSummary memberBillSummary = summaryRepository.findTop1ByDeductionFromDateLessThanEqualAndDeductionToDateGreaterThanEqual(productSale.getDeductionStartDate(), productSale.getDeductionStartDate());
            if (memberBillSummary != null)
                throw new BusinessValidationFailException(ProductSale.class, CommonUtils.getFieldError("productsale", "Invoice Date", productSale.getInvoiceDate(), "billing.already.completed"));
        }
        List<ProductSaleInstallment> listIns = installmentRepository.findByInvoiceNo(productSale.getInvoiceNo());
        for (ProductSaleInstallment txn : listIns) {
            if (txn.getSocietyPaymentCycle().getLockBillingProcess()) {
                FieldError nameNotValid = CommonUtils.getFieldError("productsale", "date", txn.getTableName(), "can.not.delete");
                throw new BusinessValidationFailException(getClass(), nameNotValid);
            }
            installmentRepository.customDelete(txn, identityInfo);
        }

        List<ProductSaleTax> listTaxTxn = saleTaxRepository.findByProductSale(productSale);
        for (ProductSaleTax txn : listTaxTxn) {
            saleTaxRepository.customDelete(txn, identityInfo);
        }

        List<ProductSaleTransaction> listTxn = saleTransRepository.findByProductSale(productSale);
        for (ProductSaleTransaction txn : listTxn) {
            setupProductStock(txn, productSale.getSociety(), "DELETE", "Product Sale Delete", identityInfo);
            txn.setProduct(null);
            saleTransRepository.customDelete(txn, identityInfo);
        }

        if (productSale.getVoucherNo() != null) {
            Optional<Voucher> voucher = voucherRepository.findById(productSale.getVoucherNo());
            if (voucher.isPresent()) {
                List<VoucherTransaction> voucherTransactionList = voucherTxnRepository.findByVoucher(voucher.get());
                for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                    List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                    if (!voucherSubLedgerList.isEmpty()) {
                        for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                            voucherSubLedgerRepository.delete(voucherSubLedger);
                        }
                    }
                    voucherTxnRepository.delete(voucherTransaction);
                }
                voucherRepository.delete(voucher.get());
            }
        }
        productSaleRepository.customDelete(code, identityInfo);
    }

    @Override
    @Transactional
    public void delete(ProductSale productSale, String identityInfo) {

        productSaleRepository.customDelete(productSale.getInvoiceNo(), identityInfo);
    }

    @Override
    public List<ProductSaleMigrateDto> migrateCollections(List<ProductSaleMigrateDto> dtoList, String header) {
        for (ProductSaleMigrateDto dto : dtoList) {
            dto.getProductSale().setInitData();
            dto.getProductSaleTransaction().setInitData();
            dto.getProductSaleTransaction().setInvoiceTxnNo(dto.getProductSale().getInvoiceNo());
            productSaleRepository.save(dto.getProductSale());
            saleTransRepository.save(dto.getProductSaleTransaction());
        }
        return dtoList;
    }

    @Override
    public List<ProductSale> findByMemberCodeAndDate(String memberCode, LocalDate fromDate, LocalDate toDate) {
        try {
            return productSaleRepository.findByConsumerCodeAndConsumerTypeAndPaymentModeAndInvoiceDateBetween(memberCode, (short) 1, (short) 1, fromDate, toDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}