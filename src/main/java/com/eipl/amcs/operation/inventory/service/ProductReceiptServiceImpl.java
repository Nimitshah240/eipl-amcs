package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.inventory.dto.ProductReceiptDto;
import com.eipl.amcs.operation.inventory.dto.ProductSaleAcUtil;
import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.*;
import com.eipl.amcs.operation.inventory.repository.*;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.VoucherUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProductReceiptServiceImpl implements ProductReceiptService {
    @Autowired
    private ProductReceiptRepository productReceiptRepository;
    @Autowired
    private ProductReceiptTransactionRepository receiptTransRepository;
    @Autowired
    private ProductReceiptTaxRepository receiptTaxRepository;
    @Autowired
    private ProductStockRepository stockRepository;
    @Autowired
    private ProductStockTransactionRepository stockTxnRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherTransactionRepository voucherTxnRepository;
    @Autowired
    private VoucherSubLedgerRepository voucherSubLedgerRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private LedgerMappingProductGroupRepository ledgerMappingProductGroupRepository;
    @Autowired
    private LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductReceipt> findAll(LocalDate fromDt, LocalDate toDt) {
        return productReceiptRepository.findByGrnDateBetween(fromDt, toDt, Sort.by("grnDate").descending());
    }

    @Override
    @Transactional
    public ProductReceiptDto save(ProductReceiptDto productReceiptDto, String identityInfo) {
        ProductReceiptDto dtoNew = new ProductReceiptDto();

        productReceiptDto.getProductReceipt().setInitData();
        String voucherNo = createAutoPosting(productReceiptDto, null, identityInfo);
        productReceiptDto.getProductReceipt().setVoucherNo(voucherNo);
        productReceiptDto.getProductReceipt().setxCol1(UUID.randomUUID().toString());
        ProductReceipt receiptNew = productReceiptRepository.customSave(productReceiptDto.getProductReceipt(), identityInfo);
        receiptNew.setVendor(productReceiptDto.getProductReceipt().getVendor());
        receiptNew.setUnion(productReceiptDto.getProductReceipt().getUnion());
        receiptNew.setSociety(productReceiptDto.getProductReceipt().getSociety());

        List<ReceiptTxnTaxDto> listNew = new ArrayList<>();
        dtoNew.setProductReceipt(receiptNew);
        dtoNew.setReceiptTxnTaxDtoList(listNew);

        int txnCnt = 1;
        for (ReceiptTxnTaxDto dto : productReceiptDto.getReceiptTxnTaxDtoList()) {
            ReceiptTxnTaxDto temp = new ReceiptTxnTaxDto();
            dto.getTransaction().setInitData();
            dto.getTransaction().setProductReceipt(receiptNew);
            dto.getTransaction().setGrnTxnNo(receiptNew.getGrnNo() + "T" + txnCnt);
            if (MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO"))
                dto.getTransaction().setBatchNo(receiptNew.getChallanDate().format(DateTimeFormatter.ofPattern("yyMMdd")) + dto.getTransaction().getProduct().getCode());
            dto.getTransaction().setxCol1(UUID.randomUUID().toString());
            ProductReceiptTransaction t = receiptTransRepository.customSave(dto.getTransaction(), identityInfo);
            t.setProductReceipt(receiptNew);
            t.setProduct(dto.getTransaction().getProduct());
            t.setUnit(dto.getTransaction().getUnit());
            t.setTax(dto.getTransaction().getTax());

            temp.setTransaction(t);
            txnCnt++;

            if (dto.getReceiptTaxList() != null && !dto.getReceiptTaxList().isEmpty()) {
                List<ProductReceiptTax> receiptTaxListNew = new ArrayList<>();
                temp.setReceiptTaxList(receiptTaxListNew);

                int taxCnt = 1;
                for (ProductReceiptTax recTax : dto.getReceiptTaxList()) {
                    recTax.setCode(dto.getTransaction().getGrnTxnNo() + "T" + taxCnt);
                    recTax.setProductReceipt(receiptNew);
                    recTax.setProductReceiptTransaction(temp.getTransaction());
                    recTax.setInitData();

                    ProductReceiptTax tt = receiptTaxRepository.customSave(recTax, identityInfo);
                    tt.setProductReceipt(receiptNew);
                    tt.setProductReceiptTransaction(t);
                    tt.setTaxDetail(recTax.getTaxDetail());

                    receiptTaxListNew.add(tt);
                    taxCnt++;
                }
            }

            setupProductStock(dto.getTransaction(), productReceiptDto.getProductReceipt().getSociety(), "CREATE",
                    "Receipt Add", identityInfo);
            listNew.add(temp);
        }

        return dtoNew;
    }


    private String createAutoPosting(ProductReceiptDto dto, String voucherCode, String identityInfo) {
        try {
            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.PRODUCT_RECEIPT);
            if (eventsList == null || eventsList.isEmpty())
                return null;

//            if (eventsList.stream().anyMatch(e -> e.getXCol1().equalsIgnoreCase("0"))) return null;

            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(dto.getProductReceipt().getGrnDate());

            if (voucherCode == null) {
                voucherCode = nextCodeService.getNextCode("Voucher", "code",
                        dto.getProductReceipt().getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 0);
                if (voucherCode == null)
                    return null;

                // Voucher
                Voucher voucher = VoucherUtil.getVoucherInstance(voucherCode, dto.getProductReceipt().getChallanNo(), dto.getProductReceipt().getGrnDate(),
                        dto.getProductReceipt().getChallanDate(), "Product Receipt Auto Posting " + dto.getProductReceipt().getGrnDate(),
                        eventsList.get(0).getVoucherType(), financialYear.isPresent() ? financialYear.get().getCode() : null,
                        dto.getProductReceipt().getSociety(), dto.getProductReceipt().getUnion().getCode(), dto.getProductReceipt().getSociety().getCode() + "01");
                voucher.setVoucherTransactions(new ArrayList<>());
                voucher.setProcessReference(dto.getProductReceipt().getGrnNo());
                voucher.setProcessName("product_receipt");

                // calculate amt
                List<LedgerMappingProductGroup> listLmpg = ledgerMappingProductGroupRepository.findAll(Sort.by("code"));
                List<LedgerMappingTaxDetail> listTaxMapping = ledgerMappingTaxDetailRepository.findAll(Sort.by("code"));
                List<ProductSaleAcUtil> list = new ArrayList<>();
                for (ReceiptTxnTaxDto receiptTxnTaxDto : dto.getReceiptTxnTaxDtoList()) {
                    Product product = productRepository.findById(receiptTxnTaxDto.getTransaction().getProduct().getCode()).orElse(null);
                    if (product == null)
                        return null;
//                    ProductGroup group = product.getProductGroup();
//                    if (group == null)
//                        continue;
//
//                    LedgerMappingProductGroup lmpg = listLmpg.stream()
//                            .filter(p -> p.getProductGroup().getCode().intValue() == group.getCode().intValue())
//                            .findFirst().orElse(null);

                    ProductSaleAcUtil obj = list.stream()
                            .filter(p -> p.getLedger().getCode().equalsIgnoreCase(product.getPurchaseLedger().getCode()))
                            .findFirst().orElse(null);
                    if (obj == null) {
                        obj = new ProductSaleAcUtil();
                        obj.setAmount(receiptTxnTaxDto.getTransaction().getAmount()
                                .subtract(receiptTxnTaxDto.getTransaction().getDiscount()));
                        if (product.getPurchaseLedger() != null) {
                            obj.setLedger(product.getPurchaseLedger());
                        }
                        obj.setNarration("Product receipt: " + receiptTxnTaxDto.getTransaction().getProduct().getCode());
                        list.add(obj);
                    } else {
                        BigDecimal amt = receiptTxnTaxDto.getTransaction().getAmount()
                                .subtract(receiptTxnTaxDto.getTransaction().getDiscount());
                        obj.setAmount(obj.getAmount().add(amt));
                    }

                    // Tax ledgers
                    if (receiptTxnTaxDto.getReceiptTaxList() != null) {
                        for (ProductReceiptTax productSaleTax : receiptTxnTaxDto.getReceiptTaxList()) {
                            LedgerMappingTaxDetail taxMap = listTaxMapping.stream()
                                    .filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(productSaleTax.getTaxDetail().getCode()))
                                    .findFirst().orElse(null);
                            if (taxMap == null)
                                continue;

                            ProductSaleAcUtil obj1 = list.stream()
                                    .filter(p -> p.getLedger().getCode().equalsIgnoreCase(taxMap.getPurchaseLedger().getCode()))
                                    .findFirst().orElse(null);
                            if (obj1 == null) {
                                obj1 = new ProductSaleAcUtil();
                                obj1.setAmount(productSaleTax.getValue());
                                obj1.setLedger(taxMap.getPurchaseLedger());
                                obj1.setNarration("Tax : " + productSaleTax.getTaxDetail().getCode());
                                list.add(obj1);
                            } else {
                                obj1.setAmount(obj.getAmount().add(productSaleTax.getValue()));
                            }
                        }
                    }
                }

                //Credit Txn
                BigDecimal amt = BigDecimal.ZERO;
                for (ProductSaleAcUtil a : list) {
                    amt = amt.add(a.getAmount());
                }
                VoucherTransaction creditTxn = VoucherUtil.getVoucherTxn(voucher, amt, true, dto.getProductReceipt().getVendor().getLedger() == null ? eventsList.get(0).getCreditLedger() : dto.getProductReceipt().getVendor().getLedger(),
                        "Product receipt " + dto.getProductReceipt().getGrnNo(), "1");
                creditTxn.setAutoPostedScreen(false);
                voucher.getVoucherTransactions().add(creditTxn);
                if (eventsList.get(0).getCreditSubLedger()) {
                    VoucherSubLedger voucherSubLedger = null;
                    Optional<SubLedger> subLedger = subLedgerRepository.findByTypeAndReferenceCode(Short.valueOf(String.valueOf(dto.getProductReceipt().getVendor().getVendorType())), dto.getProductReceipt().getVendor().getCode());
                    if (subLedger.isPresent()) {
                        creditTxn.setVoucherSubLedgers(new ArrayList<>());
                        voucherSubLedger = VoucherUtil.getVoucherSubLedger(voucher, creditTxn, "1", amt, true,
                                "Product receipt " + dto.getProductReceipt().getGrnNo(), subLedger.get());
                        if (voucherSubLedger != null)
                            creditTxn.getVoucherSubLedgers().add(voucherSubLedger);
                    }
                }

                // Debit txn
                int sr = 2;
                for (ProductSaleAcUtil a : list) {
                    VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, a.getAmount(), false, a.getLedger(),
                            a.getNarration(), String.valueOf(sr));
                    debitTxn.setAutoPostedScreen(false);
                    sr++;
                    voucher.getVoucherTransactions().add(debitTxn);
                }

                voucherRepository.customSave(voucher, identityInfo);
                if (!voucher.getVoucherTransactions().isEmpty()) {
                    for (VoucherTransaction voucherTransaction : voucher.getVoucherTransactions()) {
                        voucherTxnRepository.customSave(voucherTransaction, identityInfo);
                        if (voucherTransaction.getVoucherSubLedgers() != null && !voucherTransaction.getVoucherSubLedgers().isEmpty()) {
                            for (VoucherSubLedger voucherSubLedger : voucherTransaction.getVoucherSubLedgers()) {
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

    @Transactional
    private void setupProductStock(ProductReceiptTransaction transaction, Society society, String operation,
                                   String trnsType, String identityInfo) {

        Optional<ProductStock> stockData;
        if (MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("fifo")) {
            stockData = stockRepository.findByProductAndBatchNo(transaction.getProduct(), transaction.getBatchNo());
        } else {
            stockData = stockRepository.findByProduct(transaction.getProduct());
        }
        String code = null;
        BigDecimal oldVal = null;
        if (stockData.isPresent()) {
            code = stockData.get().getCode();
            ProductStock stockOld = stockData.get();
            oldVal = stockOld.getStock();
            if (operation.equals("CREATE"))
                stockOld.setStock(stockOld.getStock()
                        .add(BigDecimal.valueOf(transaction.getQuantity()).setScale(3, RoundingMode.HALF_UP)));
            else if (operation.equals("DELETE"))
                stockOld.setStock(stockOld.getStock()
                        .subtract(BigDecimal.valueOf(transaction.getQuantity()).setScale(3, RoundingMode.HALF_UP)));
            stockOld.setupdateData();
            stockOld.setProduct(Hibernate.unproxy(stockOld.getProduct(), Product.class));
            stockOld.setSaleRate(transaction.getSaleRate());
            stockOld.setPurchaseRate(transaction.getRate());
            stockOld.setBatchNo(MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO") ? transaction.getBatchNo() : null);
            stockOld.setReferenceCode(transaction.getGrnTxnNo());
            if (stockOld.getStock().compareTo(BigDecimal.ZERO) < 0)
                throw new RuntimeException("StockIsLessThanZero");
            stockRepository.customSave(stockOld, identityInfo);
        } else {
            oldVal = BigDecimal.ZERO;

            code = nextCodeRepository.getNextCode("ProductStock", "code", transaction.getSocietyCode(), 0);
            ProductStock stock = new ProductStock();
            stock.setCode(code);
            stock.setBatchNo(MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO") ? transaction.getBatchNo() : null);

            stock.setSaleRate(transaction.getSaleRate());
            stock.setPurchaseRate(transaction.getRate());
            stock.setReferenceCode(transaction.getGrnTxnNo());


            if (operation.equals("CREATE"))
                stock.setStock(BigDecimal.valueOf(transaction.getQuantity()));
            else if (operation.equals("DELETE"))
                stock.setStock(BigDecimal.ZERO.subtract(BigDecimal.valueOf(transaction.getQuantity())));
            stock.setUnionCode(transaction.getUnionCode());
            stock.setProduct(transaction.getProduct());
            stock.setSociety(society);
            stock.setInitData();
            if (stock.getStock().compareTo(BigDecimal.ZERO) < 0)
                throw new RuntimeException("StockIsLessThanZero");
            stockRepository.customSave(stock, identityInfo);
        }

        // Stock transaction
        String txnCode = nextCodeRepository.getNextCode("ProductStockTransaction", "code", code + "T", 0);
        ProductStockTransaction txn = new ProductStockTransaction();
        txn.setCode(txnCode);
        txn.setNewValue(BigDecimal.valueOf(transaction.getQuantity()));
        txn.setOldValue(oldVal);

        txn.setBatchNo(MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO") ? transaction.getBatchNo() : null);

        txn.setSaleRate(transaction.getSaleRate());
        txn.setPurchaseRate(transaction.getRate());

        if (operation.equals("CREATE"))
            txn.setFinalValue(oldVal.add(txn.getNewValue()).setScale(3, RoundingMode.HALF_UP));
        else if (operation.equals("DELETE"))
            txn.setFinalValue(oldVal.subtract(txn.getNewValue()).setScale(3, RoundingMode.HALF_UP));
        txn.setReferenceCode(transaction.getGrnTxnNo());
        txn.setTransactionDate(transaction.getProductReceipt().getChallanDate());
        txn.setTransactionType(trnsType);
        txn.setProduct(transaction.getProduct());
        txn.setSociety(society);
        txn.setUnionCode(transaction.getUnionCode());
        txn.setInitData();
        stockTxnRepository.customSave(txn, identityInfo);
    }

    @Override
    @Transactional
    public ProductReceiptDto update(ProductReceiptDto productReceiptDto, String identityInfo) {

        ProductReceipt productReceipt = productReceiptRepository.findById(productReceiptDto.getProductReceipt().getGrnNo())
                .orElseThrow(() -> new EntityNotFoundException(ProductReceipt.class, "Invalid"));


        // Existing product receipt transactions
        List<ProductReceiptTransaction> listTxn = receiptTransRepository.findByProductReceipt(productReceipt);
        List<ProductReceiptTax> listTax = receiptTaxRepository.findByProductReceipt(productReceipt);

        // Delete old transaction and tax details
        for (ProductReceiptTax tax : listTax) {
            receiptTaxRepository.customDelete(tax, identityInfo);
        }
        for (ProductReceiptTransaction txn : listTxn) {
            setupProductStock(txn, productReceipt.getSociety(), "DELETE", "Receipt Delete", identityInfo);
            receiptTransRepository.customDelete(txn, identityInfo);
        }

        createAutoPosting(productReceiptDto, productReceiptDto.getProductReceipt().getVoucherNo(), identityInfo);
        String voucherNo = createAutoPosting(productReceiptDto, null, identityInfo);
        productReceiptDto.getProductReceipt().setVoucherNo(voucherNo);

        ProductReceiptDto dtoNew = new ProductReceiptDto();
        productReceiptDto.getProductReceipt().setupdateData();
        ProductReceipt receiptNew = productReceiptRepository.customUpdate(productReceiptDto.getProductReceipt(), identityInfo);
        receiptNew.setVendor(productReceiptDto.getProductReceipt().getVendor());
        receiptNew.setUnion(productReceiptDto.getProductReceipt().getUnion());
        receiptNew.setSociety(productReceiptDto.getProductReceipt().getSociety());

        List<ReceiptTxnTaxDto> listNew = new ArrayList<>();
        dtoNew.setProductReceipt(receiptNew);
        dtoNew.setReceiptTxnTaxDtoList(listNew);

        int txnCnt = listTxn.size() + 1;
        for (ReceiptTxnTaxDto dto : productReceiptDto.getReceiptTxnTaxDtoList()) {
            ReceiptTxnTaxDto temp = new ReceiptTxnTaxDto();
            dto.getTransaction().setupdateData();
            dto.getTransaction().setProductReceipt(receiptNew);
            dto.getTransaction().setGrnTxnNo(receiptNew.getGrnNo() + "T" + txnCnt);
            if (MainApp.getProperty("fifo.process", "fifo").equalsIgnoreCase("FIFO"))
                dto.getTransaction().setBatchNo(receiptNew.getChallanDate().format(DateTimeFormatter.ofPattern("yyMMdd")) + dto.getTransaction().getProduct().getCode());

            ProductReceiptTransaction t = receiptTransRepository.customSave(dto.getTransaction(), identityInfo);
            t.setProductReceipt(receiptNew);
            t.setProduct(dto.getTransaction().getProduct());
            t.setUnit(dto.getTransaction().getUnit());
            t.setTax(dto.getTransaction().getTax());

            temp.setTransaction(t);
            txnCnt++;

            if (dto.getReceiptTaxList() != null && !dto.getReceiptTaxList().isEmpty()) {
                List<ProductReceiptTax> receiptTaxListNew = new ArrayList<>();
                temp.setReceiptTaxList(receiptTaxListNew);

                int taxCnt = 1;
                for (ProductReceiptTax recTax : dto.getReceiptTaxList()) {
                    recTax.setCode(dto.getTransaction().getGrnTxnNo() + "T" + taxCnt);
                    recTax.setProductReceipt(receiptNew);
                    recTax.setProductReceiptTransaction(temp.getTransaction());
                    recTax.setupdateData();

                    ProductReceiptTax tt = receiptTaxRepository.customSave(recTax, identityInfo);
                    tt.setProductReceipt(receiptNew);
                    tt.setProductReceiptTransaction(t);
                    tt.setTaxDetail(recTax.getTaxDetail());

                    receiptTaxListNew.add(tt);
                    taxCnt++;
                }
            }

            setupProductStock(dto.getTransaction(), productReceiptDto.getProductReceipt().getSociety(), "CREATE",
                    "Receipt Add", identityInfo);
            listNew.add(temp);
        }


        return dtoNew;
    }

    @Override
    public Optional<ProductReceipt> findById(String grnNo) {
        return productReceiptRepository.findById(grnNo);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        ProductReceipt productReceipt = productReceiptRepository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(ProductReceipt.class, "Invalid"));

        if (productReceipt.getVoucherNo() != null) {
            Optional<Voucher> voucher = voucherRepository.findById(productReceipt.getVoucherNo());
            if (voucher.isEmpty()) return;
            List<VoucherTransaction> transactionList = voucherTxnRepository.findByVoucher(voucher.get());
            if (transactionList != null && !transactionList.isEmpty()) {
                for (VoucherTransaction voucherTransaction : transactionList) {
                    List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
                    if (voucherSubLedgerList != null && !voucherSubLedgerList.isEmpty()) {
                        for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                            voucherSubLedgerRepository.delete(voucherSubLedger);
                        }
                    }
                    voucherTxnRepository.delete(voucherTransaction);
                }
                voucherRepository.delete(voucher.get());
            }
        }
        List<ProductReceiptTax> listTaxTxn = receiptTaxRepository.findByProductReceipt(productReceipt);
        for (ProductReceiptTax txn : listTaxTxn) {

            receiptTaxRepository.customDelete(txn, identityInfo);
        }


        List<ProductReceiptTransaction> listTxn = receiptTransRepository.findByProductReceipt(productReceipt);
        for (ProductReceiptTransaction txn : listTxn) {
            setupProductStock(txn, productReceipt.getSociety(), "DELETE", "Receipt Delete", identityInfo);
            txn.setTax(null);
            txn.setUnit(null);
            txn.setProduct(null);
            receiptTransRepository.customDelete(txn, identityInfo);
        }

        productReceiptRepository.customDelete(productReceipt, identityInfo);
    }

    @Override
    @Transactional
    public void delete(ProductReceipt productReceipt, String identityInfo) {
        productReceiptRepository.customDelete(productReceipt.getGrnNo(), identityInfo);
    }

}