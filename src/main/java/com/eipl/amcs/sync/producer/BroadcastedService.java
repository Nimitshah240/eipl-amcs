package com.eipl.amcs.sync.producer;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.repository.IdentityRepository;
import com.eipl.amcs.base.repository.NotificationRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.global.repository.ShiftRepository;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailSummaryRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceMasterRepository;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.repository.ProductGroupRepository;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.operation.model.*;
import com.eipl.amcs.master.operation.repository.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Route;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.BankRepository;
import com.eipl.amcs.master.org.repository.BmcRepository;
import com.eipl.amcs.master.org.repository.RouteRepository;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateDetailRepository;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.inventory.model.*;
import com.eipl.amcs.operation.inventory.repository.ProductDispatchRepository;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionRepository;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionTransactionRepository;
import com.eipl.amcs.operation.inventory.service.ProductDispatchService;
import com.eipl.amcs.operation.inventory.service.ProductDispatchTransactionService;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionTransactionService;
import com.eipl.amcs.operation.procurement.model.*;
import com.eipl.amcs.operation.procurement.repository.*;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.sync.model.Broadcasted;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.sync.repository.BroadcastedLogRepository;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.EncryptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BroadcastedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedService.class);
    ObjectMapper mapper = new ObjectMapper();
    Map<Integer, LocalDate> insuranceStartDateMap = new HashMap<>(); // Added on 22/5 by Nimit; Usage - to calculate correct age on the basis of birthdate of member and startdate of insurance;
    @Autowired
    private BroadcastedProducer producer;
    @Autowired
    private BroadcastedRepository repository;
    @Autowired
    private SubscribedRepository subscribedRepository;
    @Autowired
    private BroadcastedLogRepository logRepository;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private ProductRequisitionService productRequisitionService;
    @Autowired
    private ProductRequisitionTransactionService productRequisitionTransactionService;
    @Autowired
    private ProductDispatchTransactionService productDispatchTransactionService;
    @Autowired
    private ProductDispatchService productDispatchService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProductGroupRepository productGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ManualRequestRepository manualRequestRepository;
    @Autowired
    private DpuIncentiveRequestRepository dpuIncentiveRequestRepository;
    @Autowired
    private AllowDcsManualCollectionRangeRepository allowDcsManualCollectionRangeRepository;
    @Autowired
    private MilkCollectionService milkCollectionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDetailRepository memberDetailRepository;
    @Autowired
    private MilkDispatchRepository dispatchRepository;
    @Autowired
    private MilkDispatchTransactionRepository milkDispatchTransactionRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;
    @Autowired
    private ProductRequisitionRepository productRequisitionRepository;
    @Autowired
    private ProductRequisitionTransactionRepository productRequisitionTransactionRepository;
    @Autowired
    private ProductDispatchRepository productDispatchRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;
    @Autowired
    private MilkQualityTypeRepository milkQualityTypeRepository;
    @Autowired
    private MilkReceiptRepository milkReceiptRepository;
    @Autowired
    private MilkReceiptTransactionRepository milkReceiptTransactionRepository;
    @Autowired
    private MilkDispatchService milkDispatchService;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private SocietyMilkPurchaseRateService societyMilkPurchaseRateService;
    @Autowired
    private InsuranceDetailRepository insuranceDetailRepository;
    @Autowired
    private InsuranceMasterRepository insuranceMasterRepository;
    @Autowired
    private InsuranceDetailSummaryRepository insuranceDetailSummaryRepository;
    @Autowired
    private SchemeRateRepository schemeRateRepository;
    @Autowired
    private SchemeRateApplicabilityRepository schemeRateApplicabilityRepository;
    @Autowired
    private IdentityRepository identityRepository;
    @Autowired
    private SocietyPaymentCycleRepository societyPaymentCycleRepository;
    @Autowired
    private MemberMilkPurchaseRateDetailRepository memberMilkPurchaseRateDetailRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private BmcRepository bmcRepository;
    @Autowired
    private LedgerGroupRepository ledgerGroupRepository;
    @Autowired
    private LedgerTypeRepository ledgerTypeRepository;
    @Autowired
    private LedgerRepository ledgerRepository;
    @Autowired
    private BillHeadRepository billHeadRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private VoucherTypeRepository voucherTypeRepository;
    @Autowired
    private FinancialYearRepository financialYearRepository;
    @Autowired
    private LedgerMappingProductGroupRepository ledgerMappingProductGroupRepository;
    @Autowired
    private LedgerMappingBillHeadRepository ledgerMappingBillHeadRepository;
    @Autowired
    private BillCriteriaRepository billCriteriaRepository;
    @Autowired
    private LedgerMappingEventRepository ledgerMappingEventRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TaxRepository taxRepository;
    @Autowired
    private TaxDetailRepository taxDetailRepository;
    @Autowired
    private BasicTaxRepository basicTaxRepository;
    @Autowired
    private TaxGroupRepository taxGroupRepository;
    @Autowired
    private LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;
    @Autowired
    private FormulaRepository formulaRepository;

    public String sendBroadcastedAll() {
        List<Broadcasted> list;
        LOGGER.info("In send broadcasted msg All");
        try {
            list = repository.findAllByTableNameNotInOrderByCreatedAt(List.of("tbl_insurance_detail", "tbl_insurance_detail_summary"));
            for (List<Broadcasted> part : ListUtils.partition(list, 10)) {
                producer.produce(part);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }

    public Map<String, List<Broadcasted>> getGroupedByTableName() {
        List<Broadcasted> all = repository.findAll();
        return all.stream().collect(Collectors.groupingBy(Broadcasted::getTableName));
    }

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void sendBroadcasted() {
        LOGGER.info("In send broadcasted msg");
        try {
            List<Broadcasted> list = repository.findTop50ByTableNameNotInOrderByCreatedAt(List.of("tbl_insurance_detail", "tbl_insurance_detail_summary"));
            if (list == null || list.isEmpty())
                return;
            LOGGER.info("SENDING BROADCAST COUNT : {}", list.size());
            producer.produce(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void sendInbox() {
        LOGGER.info("Sending Inbox");
        try {
            List<Broadcasted> list = repository.findTop100ByTableNameInOrderByCreatedAt(List.of("tbl_insurance_detail", "tbl_insurance_detail_summary"));
            if (list == null || list.isEmpty())
                return;
            producer.produceInbox(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 2 * 60 * 1000)
    public void processSubscribed() {
        LOGGER.info("In Process Subscribed Message");
        try {
            List<Subscribed> list = subscribedRepository.findTop100ByTableNameInOrderByCreatedAt(AppConstant.prioritizedTableNameList);
            if (list == null || list.isEmpty()) {
                list = subscribedRepository.findTop100ByOrderByCreatedAt();
                if (list == null || list.isEmpty())
                    return;
            }
            Society society = societyRepository.findAll().get(0);
            List<Shift> shiftList = shiftRepository.findAll();
            List<MilkType> milkTypeList = milkTypeRepository.findAll();
            List<MilkQualityType> milkQualityTypeList = milkQualityTypeRepository.findAll();
            process(list, society, shiftList, milkTypeList, milkQualityTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void process(List<Subscribed> list, Society society, List<Shift> shiftList, List<MilkType> milkTypeList, List<MilkQualityType> milkQualityTypeList) throws JsonProcessingException {
        for (Subscribed subscribed : list) {
            Map jsonText = mapper.readValue(subscribed.getDataText(), Map.class);
            switch (subscribed.getTableName()) {
                case "tbl_formula":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                Formula formula = new Formula();
                                formula.setCode(jsonText.get("formulaCode") != null ? String.valueOf(jsonText.get("formulaCode")) : null);
                                formula.setFormula(jsonText.get("formula") != null ? String.valueOf(jsonText.get("formula")) : null);
                                formula.setDescription(jsonText.get("formulaDescription") != null ? String.valueOf(jsonText.get("formulaDescription")) : null);
                                formula.setName(jsonText.get("formulaDescription") != null ? String.valueOf(jsonText.get("formulaDescription")) : null);
                                formula.setType("1");
                                formula.setUnion(MainApp.identityDto.getUnion());
                                formula.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))) || "true".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                formula.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                formula.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                formula.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                formula.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                formula.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                formula.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                formula.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);

                                formulaRepository.save(formula);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_product_requisition":
                    try {
                        ProductRequisition requisition = new ProductRequisition();
                        requisition.setCode((String) jsonText.get("productRequisitionCode"));
                        if (jsonText.get("cancelledAt") != null)
                            requisition.setCancelledAt(LocalDateTime.parse((String) jsonText.get("cancelledAt"), CommonUtils.Formatter4));
                        requisition.setCancelledBy((String) jsonText.get("cancelledBy"));
                        if (jsonText.get("requisitionDate") != null)
                            requisition.setRequisitionDate(LocalDateTime.parse((String) jsonText.get("requisitionDate"), CommonUtils.Formatter4));
                        requisition.setDescription((String) jsonText.get("description"));
                        if (jsonText.get("entryType") != null)
                            requisition.setEntryType(Integer.parseInt((String) jsonText.get("entryType")));
                        if (jsonText.get("isCancel") != null)
                            requisition.setCancel(Boolean.valueOf((String) jsonText.get("isCancel")));
                        requisition.setStatus((String) jsonText.get("status"));
                        if (jsonText.get("createdAt") != null)
                            requisition.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4));
                        if (jsonText.get("createdBy") != null)
                            requisition.setCreatedBy((String) jsonText.get("createdBy"));
                        if (jsonText.get("updatedAt") != null)
                            requisition.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4));
                        if (jsonText.get("updatedBy") != null)
                            requisition.setCreatedBy((String) jsonText.get("updatedBy"));
                        requisition.setUnionCode((String) jsonText.get("unionCode"));
                        productRequisitionService.save(requisition, "");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_product_requisition_transaction":
                    try {
                        Optional<ProductRequisitionTransaction> productRequisitionTransaction = productRequisitionTransactionRepository.findById(String.valueOf(jsonText.get("requisitionTransactionCode")));
                        if (productRequisitionTransaction.isPresent()) {
                            ProductRequisitionTransaction requisitionTransaction = productRequisitionTransaction.get();
                            requisitionTransaction.setApprovedBy((String) jsonText.get("approvedBy"));
                            if (jsonText.get("approvedDate") != null)
                                requisitionTransaction.setApprovedDate(LocalDate.parse((String) jsonText.get("approvedDate"), CommonUtils.Formatter5));
                            if (jsonText.get("approvedQuantity") != null)
                                requisitionTransaction.setApprovedQuantity(new BigDecimal(String.valueOf(jsonText.get("approvedQuantity"))));
                            if (jsonText.get("cancelledAt") != null)
                                requisitionTransaction.setCreatedBy((String) jsonText.get("createdBy"));
                            if (jsonText.get("updatedAt") != null)
                                requisitionTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4));
                            if (jsonText.get("updatedBy") != null)
                                requisitionTransaction.setCreatedBy((String) jsonText.get("updatedBy"));
                            requisitionTransaction.setCancelledBy((String) jsonText.get("cancelledBy"));
                            if (jsonText.get("discountAmount") != null)
                                requisitionTransaction.setDiscountAmount(new BigDecimal(String.valueOf(jsonText.get("discountAmount"))));
                            if (jsonText.get("isApproved") != null)
                                requisitionTransaction.setIsApproved((int) jsonText.get("isApproved"));
                            requisitionTransaction.setCancel(Boolean.valueOf((String) jsonText.get("isCancel")));
                            if (jsonText.get("provisionalAmount") != null)
                                requisitionTransaction.setAmount(new BigDecimal(String.valueOf(jsonText.get("provisionalAmount"))));
                            if (jsonText.get("provisionalRate") != null)
                                requisitionTransaction.setRate(new BigDecimal(String.valueOf(jsonText.get("provisionalRate"))));
                            requisitionTransaction.setSchemeAddType((String) jsonText.get("schemeAddType"));
                            requisitionTransaction.setStatus((String) jsonText.get("status"));
                            requisitionTransaction.setProductSchemeCode((String) jsonText.get("productSchemeCode"));
                            if (jsonText.get("approvedQuantity") != null)
                                requisitionTransaction.setApprovedQuantity(new BigDecimal(String.valueOf(jsonText.get("approvedQuantity"))));
                            if (jsonText.get("passonToMember") != null)
                                requisitionTransaction.setPassonToMember(Integer.parseInt((String) jsonText.get("passonToMember")));
                            productRequisitionTransactionService.save(requisitionTransaction);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_product_dispatch":
                    try {
                        ProductDispatch productDispatch = new ProductDispatch();
                        productDispatch.setChallanNo((String) jsonText.get("challanNo"));
                        if (jsonText.get("challanVerified") != null)
                            productDispatch.setChallanVerified(Boolean.valueOf((String) jsonText.get("challanVerified")));
                        if (jsonText.get("requisitionDate") != null)
                            productDispatch.setRequisitionDate(LocalDateTime.parse((String) jsonText.get("requisitionDate"), CommonUtils.Formatter4));
                        if (jsonText.get("dispatchDate") != null)
                            productDispatch.setDispatchDate(LocalDate.parse((String) jsonText.get("dispatchDate"), CommonUtils.Formatter5));

                        productDispatch.setReferenceNo((String) jsonText.get("referenceNo"));
                        productDispatch.setVehicleNo((String) jsonText.get("vehicleNo"));
                        if (jsonText.get("updatedAt") != null)
                            productDispatch.setUpdatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4));
                        if (jsonText.get("updatedBy") != null)
                            productDispatch.setUpdatedBy((String) jsonText.get("updatedBy"));
                        productDispatch.setUnionCode((String) jsonText.get("unionCode"));

                        if (jsonText.get("createdAt") != null)
                            productDispatch.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4));
                        if (jsonText.get("createdBy") != null)
                            productDispatch.setCreatedBy((String) jsonText.get("createdBy"));
                        productDispatch.setActive(true);
                        productDispatch.setSociety(society);
                        productDispatchService.save(productDispatch, "");

                        ProductReceipt receipt = new ProductReceipt();
                        receipt.setChallanNo(productDispatch.getChallanNo());
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                case "tbl_product_dispatch_transaction":
                    try {
                        ProductDispatchTransaction productDispatchTransaction = new ProductDispatchTransaction();
                        productDispatchTransaction.setCode((String) jsonText.get("dispatchTransactionCode"));
                        if (jsonText.get("challanNo") != null)
                            productDispatchTransaction.setProductDispatch(productDispatchRepository.findById(String.valueOf(jsonText.get("challanNo"))).get());
                        if (jsonText.get("status") != null)
                            productDispatchTransaction.setStatus(String.valueOf(jsonText.get("status")));
                        if (jsonText.get("amount") != null)
                            productDispatchTransaction.setAmount(new BigDecimal(String.valueOf(jsonText.get("amount"))));
                        if (jsonText.get("dispatchDate") != null)
                            productDispatchTransaction.setDispatchDate(LocalDate.parse((String) jsonText.get("dispatchDate"), CommonUtils.Formatter5));
                        if (jsonText.get("discount") != null)
                            productDispatchTransaction.setDiscountAmount(new BigDecimal(String.valueOf(jsonText.get("discount"))));
                        if (jsonText.get("dispatchQty") != null)
                            productDispatchTransaction.setDispatchQty(new BigDecimal(String.valueOf(jsonText.get("dispatchQty"))));
                        if (jsonText.get("rate") != null)
                            productDispatchTransaction.setRate(new BigDecimal(String.valueOf(jsonText.get("rate"))));
                        if (jsonText.get("updatedAt") != null)
                            productDispatchTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4));
                        if (jsonText.get("updatedBy") != null)
                            productDispatchTransaction.setCreatedBy((String) jsonText.get("updatedBy"));
                        if (jsonText.get("createdAt") != null)
                            productDispatchTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4));
                        if (jsonText.get("createdBy") != null)
                            productDispatchTransaction.setCreatedBy((String) jsonText.get("createdBy"));
                        productDispatchTransaction.setUnionCode((String) jsonText.get("unionCode"));
                        if (jsonText.get("productCode") != null)
                            productDispatchTransaction.setProduct(
                                    (productRepository.findById(String.valueOf(jsonText.get("productCode"))).get()));
                        if (jsonText.get("requisitionTransactionCode") != null)
                            productDispatchTransaction.setProductRequisitionTransaction
                                    (productRequisitionTransactionRepository.findById(String.valueOf(jsonText.get("requisitionTransactionCode"))).get());
                        if (jsonText.get("productRequisitionCode") != null)
                            productDispatchTransaction.setProductRequisition(
                                    (productRequisitionRepository.findById(String.valueOf(jsonText.get("productRequisitionCode"))).get()));
                        productDispatchTransaction.setSociety(society);

                        if (jsonText.get("createdAt") != null)
                            productDispatchTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4));
                        if (jsonText.get("createdBy") != null)
                            productDispatchTransaction.setCreatedBy((String) jsonText.get("createdBy"));
                        productDispatchTransactionService.save(productDispatchTransaction);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_tax":
                    try {
                        Tax tax = new Tax();
                        tax.setCode(String.valueOf(jsonText.get("taxCode")));
                        tax.setName(String.valueOf(jsonText.get("taxName")));
                        tax.setActive("1".equals(String.valueOf(jsonText.get("isActive"))));
                        String localName = String.valueOf(jsonText.get("nameLocal"));
                        tax.setNameLocal((localName == null || localName.equalsIgnoreCase("null") || localName.isEmpty())
                                ? String.valueOf(jsonText.get("taxName"))
                                : localName);
                        tax.setUnion(MainApp.identityDto.getUnion());
                        tax.setCreatedBy("PORTAL");
                        taxRepository.save(tax);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_tax_group":
                    try {
                        Taxgroup taxGroup = new Taxgroup();
                        taxGroup.setCode(jsonText.get("taxGroupCode") != null ? Integer.valueOf(String.valueOf(jsonText.get("taxGroupCode"))) : null);
                        taxGroup.setName(jsonText.get("taxGroupName") != null ? String.valueOf(jsonText.get("taxGroupName")) : null);
                        taxGroup.setUnionCode(MainApp.identityDto.getUnion().getCode());
                        taxGroup.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))) || "true".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                        taxGroup.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        taxGroup.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : "PORTAL");
                        taxGroup.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                        taxGroup.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                        taxGroup.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                        taxGroup.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                        taxGroup.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);

                        taxGroupRepository.save(taxGroup);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_tax_detail":
                    try {
                        TaxDetail taxDetail = new TaxDetail();
                        taxDetail.setCode(jsonText.get("taxDetailCode") != null ? String.valueOf(jsonText.get("taxDetailCode")) : null);
                        taxDetail.setPercentage(jsonText.get("percentage") != null ? Double.parseDouble(String.valueOf(jsonText.get("percentage"))) : 0.0);
                        taxDetail.setType(jsonText.get("type") != null ? (short) Integer.parseInt(String.valueOf(jsonText.get("type"))) : 0);
                        if (jsonText.get("basicTaxCode") != null) {
                            taxDetail.setBasicTax(basicTaxRepository.findById(Integer.valueOf(String.valueOf(jsonText.get("basicTaxCode")))).orElse(null));
                        }
                        if (jsonText.get("taxGroupCode") != null) {
                            taxDetail.setTaxGroup(taxGroupRepository.findById(Integer.valueOf(String.valueOf(jsonText.get("taxGroupCode")))).orElse(null));
                        }
                        if (jsonText.get("taxCode") != null) {
                            taxDetail.setTax(taxRepository.findById(String.valueOf(jsonText.get("taxCode"))).orElse(null));
                        }
                        taxDetail.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                        taxDetail.setUnionCode(MainApp.identityDto.getUnion().getCode());
                        taxDetail.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        taxDetail.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : "PORTAL");
                        taxDetail.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                        taxDetail.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                        taxDetail.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                        taxDetail.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                        taxDetail.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                        taxDetailRepository.save(taxDetail);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_bulk_notification":
                    try {
                        Notification notification = new Notification();
                        notification.setBulkNotificationId((Integer) jsonText.get("bulkNotificationId"));
                        notification.setUnionCode((String) jsonText.get("unionCode"));
                        notification.setPlantCode((String) jsonText.get("plantCode"));
                        notification.setMccPlantCode((String) jsonText.get("mccPlantCode"));
                        notification.setBmcCode((String) jsonText.get("bmcCode"));
                        notification.setSocietyCode(society.getCode());
                        notification.setMemberCode((String) jsonText.get("memberCode"));
                        notification.setAppType((String) jsonText.get("appType"));
                        notification.setLoginType((String) jsonText.get("loginType"));
                        notification.setWefDate(jsonText.get("wefDate") != null ? LocalDateTime.parse((String) jsonText.get("wefDate"), CommonUtils.Formatter4) : null);
                        notification.setTitle((String) jsonText.get("title"));
                        notification.setMessage((String) jsonText.get("message"));
                        notification.setCampaignName((String) jsonText.get("campaignName"));
                        notification.setReceiverType((Integer) jsonText.get("bulkNotificationId"));
                        notification.setStatus((Integer) jsonText.get("status"));
                        notification.setOriginatingOrgCode((String) jsonText.get("originatingOrgCode"));
                        notification.setOriginatingOrgType((String) jsonText.get("originatingOrgType"));
                        notification.setOriginatingType((Integer) jsonText.get("originatingType"));
                        notification.setFromDate(jsonText.get("fromDate") != null ? LocalDateTime.parse((String) jsonText.get("fromDate"), CommonUtils.Formatter4) : null);
                        notification.setToDate(jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), CommonUtils.Formatter4) : null);
                        notification.setFromShift((Integer) jsonText.get("fromShiftCode"));
                        notification.setToShift((Integer) jsonText.get("toShiftCode"));
                        notification.setNotificationType((Integer) jsonText.get("notificationType"));
                        notification.setFileName((String) jsonText.get("filename"));
                        notification.setFilePath((String) jsonText.get("filePath"));
                        notification.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        notification.setCreatedBy((String) jsonText.get("createdBy"));
                        notificationRepository.save(notification);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_product_group":
                    try {
                        ProductGroup productGroup = new ProductGroup();
                        productGroup.setCode(Integer.parseInt(String.valueOf(jsonText.get("productGroupCode"))));
                        productGroup.setName(String.valueOf(jsonText.get("productGroupName")));
                        productGroup.setActive(Boolean.parseBoolean(String.valueOf(jsonText.get("isActive"))));
                        productGroup.setxCol1(String.valueOf(jsonText.get("refCode")));
                        productGroup.setNameLocal(String.valueOf(jsonText.get("localName")).equalsIgnoreCase("") ? String.valueOf(jsonText.get("productGroupName")) : String.valueOf(jsonText.get("localName")));
                        productGroup.setCreatedBy("PORTAL");
                        productGroup.setActive(true);
                        productGroupRepository.save(productGroup);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_product":
                    try {
                        Product product = new Product();
                        product.setCode(String.valueOf(jsonText.get("productCode")));
                        product.setName(String.valueOf(jsonText.get("productName")));
                        product.setNameLocal(String.valueOf(jsonText.get("localName")));
                        product.setReferenceCode(String.valueOf(jsonText.get("refCode")));
                        product.setActive(Boolean.parseBoolean(String.valueOf(jsonText.get("isActive"))));
                        ProductGroup group = productGroupRepository.findByCode(Integer.valueOf(String.valueOf(jsonText.get("productGroupCode"))));
                        product.setProductGroup(group);
                        product.setSociety(MainApp.identityDto.getSociety());
                        product.setUnion(MainApp.identityDto.getUnion());
                        product.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                        product.setCreatedBy("PORTAL");
                        product.setOriginatingOrgCode((String.valueOf(jsonText.get("originatingOrgCode"))));
                        product.setOriginatingType(Integer.valueOf((String.valueOf(jsonText.get("originatingType")))));
                        product.setOriginatingOrgType((String.valueOf(jsonText.get("originatingOrgType"))));
                        product.setMilk("1".equalsIgnoreCase(String.valueOf(jsonText.get("isMilk"))));
                        product.setSaleLedger(jsonText.get("saleLedger") != null ? ledgerRepository.findById(String.valueOf(jsonText.get("saleLedger"))).orElse(null) : null);
                        product.setPurchaseLedger(jsonText.get("purchaseLedger") != null ? ledgerRepository.findById(String.valueOf(jsonText.get("purchaseLedger"))).orElse(null) : null);
                        product.setStockLedger(jsonText.get("stockLedger") != null ? ledgerRepository.findById(String.valueOf(jsonText.get("stockLedger"))).orElse(null) : null);
                        product.setLocalSaleLedger(jsonText.get("localSaleLedger") != null ? ledgerRepository.findById(String.valueOf(jsonText.get("localSaleLedger"))).orElse(null) : null);
                        product.setMilkType(jsonText.get("milkType") != null ? milkTypeRepository.findByCode((int) jsonText.get("milkType")) : null);
                        product.setTax(jsonText.get("taxCode") != null ? taxRepository.findById(String.valueOf(jsonText.get("taxCode"))).orElse(null) : null);
                        product.setOtherStateTax(jsonText.get("otherStateTaxCode") != null ? taxRepository.findById(String.valueOf(jsonText.get("otherStateTaxCode"))).orElse(null) : null);
                        productRepository.save(product);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_allow_dcs_manual_collection_range":
                    try {
                        AllowDcsManualCollectionRange allowDcsManualCollectionRange = new AllowDcsManualCollectionRange();
                        allowDcsManualCollectionRange.setBmcCode(String.valueOf(jsonText.get("bmcCode")));
                        allowDcsManualCollectionRange.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        allowDcsManualCollectionRange.setCreatedBy(String.valueOf(jsonText.get("createdBy")));
                        allowDcsManualCollectionRange.setCode(Long.valueOf(String.valueOf(jsonText.get("manualCollectionCode"))));
                        allowDcsManualCollectionRange.setRemarks(String.valueOf(jsonText.get("remark")));
                        allowDcsManualCollectionRange.setSociety(society);
                        allowDcsManualCollectionRange.setStatus(Integer.parseInt(String.valueOf(jsonText.get("status"))));
                        allowDcsManualCollectionRange.setMccPlantCode(String.valueOf(jsonText.get("mccPlantCode")));
                        allowDcsManualCollectionRange.setUnionCode(String.valueOf(jsonText.get("unionCode")));
                        allowDcsManualCollectionRange.setFromShift(shiftList.stream().filter(e -> e.getCode() == Integer.parseInt(String.valueOf(jsonText.get("fromShift")))).findFirst().get());
                        allowDcsManualCollectionRange.setToShift(shiftList.stream().filter(e -> e.getCode() == Integer.parseInt(String.valueOf(jsonText.get("toShift")))).findFirst().get());
                        allowDcsManualCollectionRange.setQualityManual(jsonText.get("isQualityManual").toString().equalsIgnoreCase("1"));
                        allowDcsManualCollectionRange.setWeightManual(jsonText.get("isWeightManual").toString().equalsIgnoreCase("1"));
                        allowDcsManualCollectionRange.setUpdatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                        allowDcsManualCollectionRange.setUpdatedBy(String.valueOf(jsonText.get("updatedBy")));
                        allowDcsManualCollectionRange.setFromDate(jsonText.get("fromDate") != null ? LocalDateTime.parse((String) jsonText.get("fromDate"), CommonUtils.Formatter4) : null);
                        allowDcsManualCollectionRange.setToDate(jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), CommonUtils.Formatter4) : null);
                        allowDcsManualCollectionRange.setxCol1(String.valueOf(jsonText.get("xCol1")));
                        allowDcsManualCollectionRange.setxCol2(String.valueOf(jsonText.get("xCol2")));
                        allowDcsManualCollectionRange.setxCol3(String.valueOf(jsonText.get("xCol3")));
                        allowDcsManualCollectionRange.setXCol4(String.valueOf(jsonText.get("xCol4")));
                        allowDcsManualCollectionRange.setXCol5(String.valueOf(jsonText.get("xCol5")));
                        allowDcsManualCollectionRangeRepository.save(allowDcsManualCollectionRange);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case "tbl_dpu_incentive_master":
                    try {
                        dpuIncentiveRequestRepository.deleteAll();
                        DpuIncentiveRequest dpuIncentiveRequest = new DpuIncentiveRequest();
                        dpuIncentiveRequest.setIncentiveMasterCode(jsonText.get("incentiveMasterCode") != null ? Long.valueOf(String.valueOf(jsonText.get("incentiveMasterCode"))) : null);
                        dpuIncentiveRequest.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        dpuIncentiveRequest.setCreatedBy(String.valueOf(jsonText.get("createdBy")));
                        dpuIncentiveRequest.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt")) : null);
                        dpuIncentiveRequest.setUpdatedBy(jsonText.get("updatedBy") != null ? String.valueOf(LocalDateTime.parse((String) jsonText.get("updatedBy"))) : null);
                        dpuIncentiveRequest.setSociety(society);
                        dpuIncentiveRequest.setUnionCode(String.valueOf(jsonText.get("unionCode")));
                        dpuIncentiveRequest.setFromDate(jsonText.get("fromDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("fromDate")), CommonUtils.Formatter5) : null);
                        dpuIncentiveRequest.setToDate(jsonText.get("toDate") != null ? LocalDate.parse((String.valueOf(jsonText.get("toDate"))), CommonUtils.Formatter5) : null);
                        dpuIncentiveRequest.setMctime(jsonText.get("mCutoffTime") != null ? LocalTime.parse((String) jsonText.get("mCutoffTime")) : null);
                        dpuIncentiveRequest.setEctime(jsonText.get("eCutoffTime") != null ? LocalTime.parse((String) jsonText.get("eCutoffTime")) : null);
                        dpuIncentiveRequest.setMltime(jsonText.get("mLockTime") != null ? LocalTime.parse((String) jsonText.get("mLockTime")) : null);
                        dpuIncentiveRequest.setEltime(jsonText.get("eLockTime") != null ? LocalTime.parse((String) jsonText.get("eLockTime")) : null);
                        dpuIncentiveRequest.setMstime(jsonText.get("mStartTime") != null ? LocalTime.parse((String) jsonText.get("mStartTime")) : null);
                        dpuIncentiveRequest.setEstime(jsonText.get("eStartTime") != null ? LocalTime.parse((String) jsonText.get("eStartTime")) : null);
                        if (jsonText.get("incRate") == null || jsonText.get("incRate").toString().equalsIgnoreCase("null") || jsonText.get("incRate").toString().equalsIgnoreCase("0"))
                            dpuIncentiveRequest.setIncRate(1);
                        else
                            dpuIncentiveRequest.setIncRate((int) Double.parseDouble(jsonText.get("incRate").toString()));
                        if (dpuIncentiveRequest.getIncRate() == 0) {
                            dpuIncentiveRequest.setIncRate(1);
                        }
                        dpuIncentiveRequest.setIncDeduction(0);
                        dpuIncentiveRequest.setOriginatingOrgCode((String.valueOf(jsonText.get("originatingOrgCode"))));
                        dpuIncentiveRequest.setOriginatingType(Integer.valueOf((String.valueOf(jsonText.get("originatingType")))));
                        dpuIncentiveRequest.setOriginatingOrgType((String.valueOf(jsonText.get("originatingOrgType"))));
                        dpuIncentiveRequest.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                        dpuIncentiveRequest.setCreatedBy((String.valueOf(jsonText.get("createdBy"))));
                        dpuIncentiveRequest.setCreatedBy("PORTAL");
                        dpuIncentiveRequest.setxCol1(String.valueOf(jsonText.get("xCol1")));
                        dpuIncentiveRequest.setxCol2(String.valueOf(jsonText.get("xCol2")));
                        dpuIncentiveRequest.setxCol3(String.valueOf(jsonText.get("xCol3")));
                        dpuIncentiveRequest.setXCol4(String.valueOf(jsonText.get("xCol4")));
                        dpuIncentiveRequest.setXCol5(String.valueOf(jsonText.get("xCol5")));
                        dpuIncentiveRequestRepository.save(dpuIncentiveRequest);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                case "tbl_insurance_master":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                InsuranceMaster insuranceMaster = new InsuranceMaster();
                                insuranceMaster.setInsuranceMasterCode(jsonText.get("insuranceMasterCode") != null ? (Integer) jsonText.get("insuranceMasterCode") : null);
                                insuranceMaster.setInsuranceStartDate(jsonText.get("insuranceStartDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("insuranceStartDate"))) : null);
                                insuranceStartDateMap.put(jsonText.get("insuranceMasterCode") != null ? (Integer) jsonText.get("insuranceMasterCode") : null, jsonText.get("insuranceStartDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("insuranceStartDate"))) : null); // Added on 22/5 by Nimit; Usage - to calculate correct age on the basis of birthdate of member and startdate of insurance;
                                insuranceMaster.setInsuranceEndDate(jsonText.get("insuranceEndDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("insuranceEndDate")), AppConstant.DATE_FMT) : null);
                                insuranceMaster.setDcsEditStartDate(jsonText.get("dcsEditStartDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("dcsEditStartDate")), AppConstant.DATE_FMT) : null);
                                insuranceMaster.setDcsEditEndDate(jsonText.get("dcsEditEndDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("dcsEditEndDate")), AppConstant.DATE_FMT) : null);
                                insuranceMaster.setMemberMinAge(jsonText.get("memberMinAge") != null ? (Integer) jsonText.get("memberMinAge") : null);
                                insuranceMaster.setMemberMaxAge(jsonText.get("memberMaxAge") != null ? (Integer) jsonText.get("memberMaxAge") : null);
                                insuranceMaster.setInsuranceFinalDate(jsonText.get("insuranceFinalDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("insuranceFinalDate")), AppConstant.DATE_FMT) : null);
                                insuranceMaster.setInsuranceDescription(jsonText.get("insuranceDescription") != null ? String.valueOf(jsonText.get("insuranceDescription")) : null);
                                insuranceMaster.setUnionCode(jsonText.get("unionCode") != null ? String.valueOf(jsonText.get("unionCode")) : null);
                                insuranceMaster.setActive(jsonText.get("isActive") != null ? String.valueOf(jsonText.get("isActive")).equalsIgnoreCase("1") : null);
                                insuranceMaster.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("createdAt")), AppConstant.DATE_TIME_FMT_SSSSSS) : null);
                                insuranceMaster.setCreatedBy(jsonText.get("createdBy") != null ? String.valueOf(jsonText.get("createdBy")) : null);
                                insuranceMaster.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("updatedAt"))) : null);
                                insuranceMaster.setUpdatedBy(jsonText.get("updatedBy") != null ? String.valueOf(jsonText.get("updatedBy")) : null);
                                insuranceMaster.setOriginatingOrgCode(jsonText.get("originatingOrgCode") != null ? String.valueOf(jsonText.get("originatingOrgCode")) : null);
                                insuranceMaster.setOriginatingOrgType(jsonText.get("originatingOrgType") != null ? String.valueOf(jsonText.get("originatingOrgType")) : null);
                                insuranceMaster.setOriginatingType(jsonText.get("originatingType") != null ? (Integer) jsonText.get("originatingType") : null);
                                insuranceMaster.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                insuranceMaster.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                insuranceMaster.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                insuranceMaster.setxCol4(jsonText.get("xCol4") != null ? String.valueOf(jsonText.get("xCol4")) : null);
                                insuranceMaster.setxCol5(jsonText.get("xCol5") != null ? String.valueOf(jsonText.get("xCol5")) : null);
                                insuranceMasterRepository.save(insuranceMaster);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_insurance_detail_summary":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                InsuranceDetailSummary insuranceDetailSummary = new InsuranceDetailSummary();
                                insuranceDetailSummary.setInsuranceDetailSummaryCode(jsonText.get("insuranceDetailSummaryCode") != null ? (Integer) jsonText.get("insuranceDetailSummaryCode") : null);
                                insuranceDetailSummary.setInsuranceMasterCode(jsonText.get("insuranceMasterCode") != null ? (Integer) jsonText.get("insuranceMasterCode") : null);
                                insuranceDetailSummary.setUnionCode(jsonText.get("unionCode") != null ? String.valueOf(jsonText.get("unionCode")) : null);
                                insuranceDetailSummary.setPlantCode(jsonText.get("plantCode") != null ? String.valueOf(jsonText.get("plantCode")) : null);
                                insuranceDetailSummary.setBmcCode(jsonText.get("bmcCode") != null ? String.valueOf(jsonText.get("bmcCode")) : null);
                                insuranceDetailSummary.setMccPlantCode(jsonText.get("mccPlantCode") != null ? String.valueOf(jsonText.get("mccPlantCode")) : null);
                                insuranceDetailSummary.setDcsCode(jsonText.get("dcsCode") != null ? String.valueOf(jsonText.get("dcsCode")) : null);
                                insuranceDetailSummary.setDcsName(jsonText.get("dcsName") != null ? String.valueOf(jsonText.get("dcsName")) : null);
                                insuranceDetailSummary.setFromDate(jsonText.get("fromDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("fromDate")), CommonUtils.Formatter5) : null);
                                insuranceDetailSummary.setToDate(jsonText.get("toDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("toDate")), CommonUtils.Formatter5) : null);
                                insuranceDetailSummary.setStatus(jsonText.get("status") != null ? String.valueOf(jsonText.get("status")) : null);
                                insuranceDetailSummary.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("createdAt")), AppConstant.DATE_TIME_FMT_SSSSSS) : null);
                                insuranceDetailSummary.setCreatedBy(jsonText.get("createdBy") != null ? String.valueOf(jsonText.get("createdBy")) : null);
                                insuranceDetailSummary.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("updatedAt"))) : null);
                                insuranceDetailSummary.setUpdatedBy(jsonText.get("updatedBy") != null ? String.valueOf(jsonText.get("updatedBy")) : null);
                                insuranceDetailSummary.setOriginatingOrgCode(jsonText.get("originatingOrgCode") != null ? String.valueOf(jsonText.get("originatingOrgCode")) : null);
                                insuranceDetailSummary.setOriginatingOrgType(jsonText.get("originatingOrgType") != null ? String.valueOf(jsonText.get("originatingOrgType")) : null);
                                insuranceDetailSummary.setOriginatingType(jsonText.get("originatingType") != null ? (Integer) jsonText.get("originatingType") : null);
                                insuranceDetailSummary.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                insuranceDetailSummary.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                insuranceDetailSummary.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                insuranceDetailSummary.setxCol4(jsonText.get("xCol4") != null ? String.valueOf(jsonText.get("xCol4")) : null);
                                insuranceDetailSummary.setxCol5(jsonText.get("xCol5") != null ? String.valueOf(jsonText.get("xCol5")) : null);
                                insuranceDetailSummaryRepository.save(insuranceDetailSummary);
                                break;
                            case "DELETE":
                                insuranceDetailSummaryRepository.deleteById((Integer) jsonText.get("insuranceDetailSummaryCode"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_insurance_detail":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                if (insuranceStartDateMap.get(jsonText.get("insuranceMasterCode")) == null) {
                                    Integer inMasterCode = (Integer) jsonText.get("insuranceMasterCode");
                                    Optional<InsuranceMaster> insuranceMaster = insuranceMasterRepository.findById(inMasterCode);
                                    insuranceStartDateMap.put(inMasterCode, insuranceMaster.get().getInsuranceStartDate());
                                }
                                InsuranceDetail insuranceDetail = new InsuranceDetail();
                                insuranceDetail.setInsuranceDetailCode(jsonText.get("insuranceDetailCode") != null ? String.valueOf(jsonText.get("insuranceDetailCode")) : null);
                                insuranceDetail.setInsuranceMasterCode(jsonText.get("insuranceMasterCode") != null ? (Integer) jsonText.get("insuranceMasterCode") : null);
                                insuranceDetail.setSrNo(jsonText.get("srNo") != null ? String.valueOf(jsonText.get("srNo")) : null);
                                insuranceDetail.setUnionCode(jsonText.get("unionCode") != null ? String.valueOf(jsonText.get("unionCode")) : null);
                                insuranceDetail.setPlantCode(jsonText.get("plantCode") != null ? String.valueOf(jsonText.get("plantCode")) : null);
                                insuranceDetail.setBmcCode(jsonText.get("bmcCode") != null ? String.valueOf(jsonText.get("bmcCode")) : null);
                                insuranceDetail.setMccPlantCode(jsonText.get("mccPlantCode") != null ? String.valueOf(jsonText.get("mccPlantCode")) : null);
                                insuranceDetail.setDcsCode(jsonText.get("dcsCode") != null ? String.valueOf(jsonText.get("dcsCode")) : null);
                                insuranceDetail.setDcsName(jsonText.get("dcsName") != null ? String.valueOf(jsonText.get("dcsName")) : null);
                                insuranceDetail.setMemberId(jsonText.get("memberId") != null ? String.valueOf(jsonText.get("memberId")) : null);
                                insuranceDetail.setMemberCode(jsonText.get("memberCode") != null ? String.valueOf(jsonText.get("memberCode")) : null);
                                insuranceDetail.setMemberName(jsonText.get("memberName") != null ? String.valueOf(jsonText.get("memberName")) : null);
                                insuranceDetail.setAdharNo(jsonText.get("adharNo") != null ? String.valueOf(jsonText.get("adharNo")) : null);
                                insuranceDetail.setDob(jsonText.get("dob") != null ? String.valueOf(jsonText.get("dob")) : null);
                                try {
                                    int age = Period.between(LocalDate.parse(EncryptionUtil.decrypt(jsonText.get("dob") != null ? String.valueOf(jsonText.get("dob")) : null)), insuranceStartDateMap.get(jsonText.get("insuranceMasterCode") != null ? jsonText.get("insuranceMasterCode") : null)).getYears();
                                    insuranceDetail.setAge(age != 0 ? age : null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                insuranceDetail.setAge(jsonText.get("age") != null ? (Integer) jsonText.get("age") : null);
                                insuranceDetail.setGenderCode(jsonText.get("genderCode") != null ? String.valueOf(jsonText.get("genderCode")) : null);
                                insuranceDetail.setNomineeAdharNo(jsonText.get("nomineeAdharNo") != null ? String.valueOf(jsonText.get("nomineeAdharNo")) : null);
                                insuranceDetail.setNomineeMemberName(jsonText.get("nomineeMemberName") != null ? String.valueOf(jsonText.get("nomineeMemberName")) : null);
                                insuranceDetail.setDateOfJoiningScheme(jsonText.get("dateOfJoiningScheme") != null ? LocalDate.parse(String.valueOf(jsonText.get("dateOfJoiningScheme")), AppConstant.DATE_FMT) : null);
                                insuranceDetail.setStatus(jsonText.get("status") != null ? String.valueOf(jsonText.get("status")) : null);
                                insuranceDetail.setIsDelete(jsonText.get("isDelete") != null ? (Integer.parseInt((String.valueOf(jsonText.get("isDelete")))) == 1) : null);
                                insuranceDetail.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("createdAt")), AppConstant.DATE_TIME_FMT_SSSSSS) : null);
                                insuranceDetail.setCreatedBy(jsonText.get("createdBy") != null ? String.valueOf(jsonText.get("createdBy")) : null);
                                insuranceDetail.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse(String.valueOf(jsonText.get("updatedAt"))) : null);
                                insuranceDetail.setUpdatedBy(jsonText.get("updatedBy") != null ? String.valueOf(jsonText.get("updatedBy")) : null);
                                insuranceDetail.setOriginatingOrgCode(jsonText.get("originatingOrgCode") != null ? String.valueOf(jsonText.get("originatingOrgCode")) : null);
                                insuranceDetail.setOriginatingOrgType(jsonText.get("originatingOrgType") != null ? String.valueOf(jsonText.get("originatingOrgType")) : null);
                                insuranceDetail.setOriginatingType(jsonText.get("originatingType") != null ? (Integer) jsonText.get("originatingType") : null);
//                                insuranceDetail.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                insuranceDetail.setxCol1(String.valueOf(jsonText.get("xCol1")));
                                insuranceDetail.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                insuranceDetail.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                insuranceDetail.setxCol4(jsonText.get("xCol4") != null ? String.valueOf(jsonText.get("xCol4")) : null);
                                insuranceDetail.setxCol5(jsonText.get("xCol5") != null ? String.valueOf(jsonText.get("xCol5")) : null);
                                insuranceDetail.setSysUpdatedBy(jsonText.get("sysUpdatedBy") != null ? String.valueOf(jsonText.get("sysUpdatedBy")) : null);

                                insuranceDetailRepository.save(insuranceDetail);
                                break;
                            case "DELETE":
                                insuranceDetailRepository.deleteById(String.valueOf(jsonText.get("insuranceDetailCode")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "rfc_call":
                    try {
//                    LocalDate fromDate = LocalDate.parse(String.valueOf(jsonText.get("prodate")), AppConstant.RFC_CALL_FORMAT);
                        LocalDateTime fromDate = CommonUtils.getLocalDateTimeFromDateAndShift(LocalDate.parse(String.valueOf(jsonText.get("prodate")), AppConstant.RFC_CALL_FORMAT), shiftList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("shift")).charAt(0)).findFirst().get());
                        Optional<MilkReceipt> receiptOptional = milkReceiptRepository.findBySocietyAndFromDateAndFromShift(society,
                                fromDate,
                                shiftList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("shift")).charAt(0)).findFirst().get());
                        if (receiptOptional.isEmpty()) {
                            MilkReceipt milkReceipt = new MilkReceipt();
                            milkReceipt.setCode(nextCodeService.getNextCode("MilkReceipt", "code", society.getCode(), 1));
//                    milkReceipt.setMilkDispatch();
                            milkReceipt.setSociety(society);
                            milkReceipt.setUnion(society.getUnion());
                            milkReceipt.setFromShift(shiftList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("shift")).charAt(0)).findFirst().get());
                            milkReceipt.setToShift(milkReceipt.getFromShift());
                            milkReceipt.setReceiptDate(LocalDate.parse(String.valueOf(jsonText.get("prodate")), AppConstant.RFC_CALL_FORMAT));
                            milkReceipt.setFromDate(fromDate);
                            milkReceipt.setToDate(milkReceipt.getFromDate());
                            milkReceipt.setInitData();
                            milkReceiptRepository.customSave(milkReceipt, "");


                            MilkReceiptTransaction transaction = new MilkReceiptTransaction();
                            transaction.setTxnCode(milkReceipt.getCode() + "T" + 1);
                            transaction.setMilkReceipt(milkReceipt);
                            transaction.setMilkType(milkTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mtype")).charAt(0)).findFirst().get());
                            transaction.setMilkQualityType(milkQualityTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mcat")).charAt(0)).findFirst().get());
                            SocietyMilkPurchaseRate purchaseRateCode = milkDispatchService.fetchPurchaseRateCode(milkReceipt.getFromDate(),
                                    milkReceipt.getFromShift().getCode(), society.getCode());
                            transaction.setSocietyPurchaseRateCode(purchaseRateCode.getCode());
                            transaction.setAvgFat(new BigDecimal(String.valueOf(jsonText.get("fat"))));
                            transaction.setAvgSnf(new BigDecimal(String.valueOf(jsonText.get("snf"))));
                            transaction.setConvertedQuantity(new BigDecimal(String.valueOf(jsonText.get("weight"))));
                            transaction.setQty(new BigDecimal(String.valueOf(jsonText.get("quan"))));
                            if (transaction.getQty().compareTo(BigDecimal.ZERO) == 0) {
                                transaction.setQty(new BigDecimal(String.valueOf(jsonText.get("weight"))).divide(BigDecimal.valueOf(1.03), RoundingMode.HALF_UP));
                            }
                            if (transaction.getConvertedQuantity().compareTo(BigDecimal.ZERO) == 0) {
                                transaction.setConvertedQuantity(new BigDecimal(String.valueOf(jsonText.get("quan"))).multiply(BigDecimal.valueOf(1.03)));
                            }
                            transaction.setAvgClr(CommonUtils.calculateClr(transaction.getAvgFat(), transaction.getAvgSnf()));
                            transaction.setConvertedQuantityMode(BigDecimal.ZERO);

                            if (transaction.getQty().compareTo(BigDecimal.ZERO) > 0) {
                                //setRate and Amount
                                fetchRate(String.valueOf(transaction.getAvgFat()),
                                        String.valueOf(transaction.getAvgSnf()), String.valueOf(transaction.getConvertedQuantity()),
                                        transaction.getMilkType(), transaction.getMilkQualityType(), purchaseRateCode, transaction, milkReceipt);
                            }
                            transaction.setAcidity(BigDecimal.ZERO);
                            transaction.setDensity(BigDecimal.ZERO);
                            transaction.setFreezingPoint(BigDecimal.ZERO);
                            transaction.setLactose(BigDecimal.ZERO);
                            transaction.setProtein(BigDecimal.ZERO);
                            transaction.setWater(BigDecimal.ZERO);
                            transaction.setQuantityMode(1);
                            transaction.setConvertedQuantityMode(CommonUtils.convertQty(AppConstant.CollectionType.RECEIPT, transaction.getQty().toString()));
                            transaction.setInitData();
                            milkReceiptTransactionRepository.customSave(transaction, "");
                        } else {

                            Optional<MilkReceiptTransaction> transactionOptional = milkReceiptTransactionRepository.findByMilkReceiptAndMilkTypeAndMilkQualityTypeAndQty(receiptOptional.get(),
                                    milkTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mtype")).charAt(0)).findFirst().get(),
                                    milkQualityTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mcat")).charAt(0)).findFirst().get(),

                                    BigDecimal.valueOf(
                                            Long.parseLong(
                                                    String.valueOf(
                                                            !jsonText.get("quan").toString().equalsIgnoreCase("0") ? jsonText.get("quan") : new BigDecimal(String.valueOf(jsonText.get("weight"))).divide(BigDecimal.valueOf(1.03), RoundingMode.HALF_UP)
                                                    ))

                                    ));

                            if (transactionOptional.isEmpty()) {
                                int count = 1;
                                List<MilkReceiptTransaction> transactionList = milkReceiptTransactionRepository.findByMilkReceipt(receiptOptional.get());
                                if (transactionList != null && !transactionList.isEmpty()) {
                                    count = transactionList.size();
                                }
                                count++;
                                MilkReceiptTransaction transaction = new MilkReceiptTransaction();
                                transaction.setTxnCode(receiptOptional.get().getCode() + "T" + count);
                                transaction.setMilkReceipt(receiptOptional.get());
                                transaction.setMilkType(milkTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mtype")).charAt(0)).findFirst().get());
                                transaction.setMilkQualityType(milkQualityTypeList.stream().filter(e -> e.getName().charAt(0) == String.valueOf(jsonText.get("mcat")).charAt(0)).findFirst().get());
                                SocietyMilkPurchaseRate purchaseRateCode = milkDispatchService.fetchPurchaseRateCode(receiptOptional.get().getFromDate(),
                                        receiptOptional.get().getFromShift().getCode(), society.getCode());
                                transaction.setSocietyPurchaseRateCode(purchaseRateCode.getCode());
                                transaction.setAvgFat(new BigDecimal(String.valueOf(jsonText.get("fat"))));
                                transaction.setAvgSnf(new BigDecimal(String.valueOf(jsonText.get("snf"))));
                                transaction.setConvertedQuantity(new BigDecimal(String.valueOf(jsonText.get("weight"))));
                                transaction.setQty(new BigDecimal(String.valueOf(jsonText.get("quan"))));
                                if (transaction.getQty().compareTo(BigDecimal.ZERO) == 0) {
                                    transaction.setQty(new BigDecimal(String.valueOf(jsonText.get("weight"))).divide(BigDecimal.valueOf(1.03), RoundingMode.HALF_UP));
                                }
                                if (transaction.getConvertedQuantity().compareTo(BigDecimal.ZERO) == 0) {
                                    transaction.setQty(new BigDecimal(String.valueOf(jsonText.get("quan"))).multiply(BigDecimal.valueOf(1.03)));
                                }
                                transaction.setAvgClr(CommonUtils.calculateClr(transaction.getAvgFat(), transaction.getAvgSnf()));
                                transaction.setConvertedQuantityMode(BigDecimal.ZERO);

                                if (transaction.getQty().compareTo(BigDecimal.ZERO) > 0) {
                                    //setRate and Amount
                                    fetchRate(String.valueOf(transaction.getAvgFat()),
                                            String.valueOf(transaction.getAvgSnf()), String.valueOf(transaction.getConvertedQuantity()),
                                            transaction.getMilkType(), transaction.getMilkQualityType(), purchaseRateCode, transaction, receiptOptional.get());
                                }
                                transaction.setAcidity(BigDecimal.ZERO);
                                transaction.setDensity(BigDecimal.ZERO);
                                transaction.setFreezingPoint(BigDecimal.ZERO);
                                transaction.setLactose(BigDecimal.ZERO);
                                transaction.setProtein(BigDecimal.ZERO);
                                transaction.setWater(BigDecimal.ZERO);
                                transaction.setQuantityMode(1);
                                transaction.setConvertedQuantityMode(CommonUtils.convertQty(AppConstant.CollectionType.RECEIPT, transaction.getQty().toString()));
                                transaction.setInitData();
                                milkReceiptTransactionRepository.customSave(transaction, "");
                            }
                        }

                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_scheme_rate":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                SchemeRate schemeRate = new SchemeRate();
                                schemeRate.setSchemeRateCode(jsonText.get("schemeRateCode") != null ? (String) jsonText.get("schemeRateCode") : null);
                                schemeRate.setFromDate(jsonText.get("fromDate") != null ? LocalDateTime.parse((String) jsonText.get("fromDate"), CommonUtils.Formatter4) : null);
                                schemeRate.setToDate(jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), CommonUtils.Formatter4) : null);
                                schemeRate.setFromShift(jsonText.get("fromShift") != null ? (Integer) jsonText.get("fromShift") : null);
                                schemeRate.setToShift(jsonText.get("toShift") != null ? (Integer) jsonText.get("toShift") : null);
                                schemeRate.setRtpl(jsonText.get("rtpl") != null ? new BigDecimal(String.valueOf(jsonText.get("rtpl"))) : null);
                                schemeRate.setRateClass(jsonText.get("rateClass") != null ? (String) jsonText.get("rateClass") : null);
                                schemeRate.setDescription(jsonText.get("description") != null ? (String) jsonText.get("description") : null);
                                schemeRate.setIsMccWiseRate(jsonText.get("isMccWiseRate") != null ? String.valueOf(jsonText.get("isMccWiseRate")).equalsIgnoreCase("1") : null);
                                schemeRate.setIsMemberRate(jsonText.get("isMemberRate") != null ? String.valueOf(jsonText.get("isMemberRate")).equalsIgnoreCase("1") : null);
                                schemeRate.setUnionCode(jsonText.get("unionCode") != null ? (String) jsonText.get("unionCode") : null);
                                schemeRate.setIsActive(jsonText.get("isActive") != null ? String.valueOf(jsonText.get("isActive")).equalsIgnoreCase("1") : null);
                                schemeRate.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                schemeRate.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                schemeRate.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                schemeRate.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                schemeRate.setOriginatingOrgCode(jsonText.get("originatingOrgCode") != null ? (String) jsonText.get("originatingOrgCode") : null);
                                schemeRate.setOriginatingOrgType(jsonText.get("originatingOrgType") != null ? (String) jsonText.get("originatingOrgType") : null);
                                schemeRate.setOriginatingType(jsonText.get("originatingType") != null ? (Integer) jsonText.get("originatingType") : null);
                                schemeRate.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                schemeRate.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                schemeRate.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                schemeRate.setxCol4(jsonText.get("xCol4") != null ? String.valueOf(jsonText.get("xCol4")) : null);
                                schemeRate.setxCol5(jsonText.get("xCol5") != null ? String.valueOf(jsonText.get("xCol5")) : null);
                                schemeRateRepository.save(schemeRate);
                                break;
                            case "DELETE":
                                schemeRateRepository.deleteBySchemeRateCode((String) jsonText.get("schemeRateCode"));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_scheme_rate_applicability":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                SchemeRateApplicability schemeRateApplicability = new SchemeRateApplicability();
                                schemeRateApplicability.setSchemeRateAppCode(jsonText.get("schemeRateAppCode") != null ? (Integer) jsonText.get("schemeRateAppCode") : null);
                                schemeRateApplicability.setSchemeRateCode(jsonText.get("schemeRateCode") != null ? (String) jsonText.get("schemeRateCode") : null);
                                schemeRateApplicability.setFromShift(jsonText.get("fromShift") != null ? (Integer) jsonText.get("fromShift") : null);
                                schemeRateApplicability.setToShift(jsonText.get("toShift") != null ? (Integer) jsonText.get("toShift") : null);
                                LocalDateTime fromDate = jsonText.get("fromDate") != null ? LocalDateTime.parse((String) jsonText.get("fromDate"), CommonUtils.Formatter4) : null;
                                Shift shift = shiftRepository.findById(schemeRateApplicability.getFromShift()).orElseThrow(() -> null);
                                fromDate = fromDate.with(CommonUtils.getTimeFromShift(shift));
                                schemeRateApplicability.setFromDate(fromDate);
                                LocalDateTime toDate = jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), CommonUtils.Formatter4) : null;
                                shift = shiftRepository.findById(schemeRateApplicability.getToShift()).orElseThrow(() -> null);
                                toDate = toDate.with(CommonUtils.getTimeFromShift(shift));
                                schemeRateApplicability.setToDate(toDate);
                                schemeRateApplicability.setRtpl(jsonText.get("rtpl") != null ? new BigDecimal(String.valueOf(jsonText.get("rtpl"))) : null);
                                schemeRateApplicability.setRateClass(jsonText.get("rateClass") != null ? (String) jsonText.get("rateClass") : null);
                                schemeRateApplicability.setApplicableFor("DCS"); // NIMIT - KEEP THIS BECAUSE PHP TEAM CHANGE REFCODE TO DCSCODE, WHICH CREATE ISSUE IN REPORT
//                                schemeRateApplicability.setApplicableFor(jsonText.get("applicableFor") != null ? MainApp.identityDto.getSociety().getCode() : null); // NIMIT - KEEP THIS BECAUSE PHP TEAM CHANGE REFCODE TO DCSCODE, WHICH CREATE ISSUE IN REPORT
                                schemeRateApplicability.setApplicableCode(jsonText.get("applicableCode") != null ? (String) jsonText.get("applicableCode") : null);
                                schemeRateApplicability.setIsMemberRate(jsonText.get("isMemberRate") != null ? String.valueOf(jsonText.get("isMemberRate")).equalsIgnoreCase("1") : null);
                                schemeRateApplicability.setUnionCode(jsonText.get("unionCode") != null ? (String) jsonText.get("unionCode") : null);
                                schemeRateApplicability.setIsActive(jsonText.get("isActive") != null ? String.valueOf(jsonText.get("isActive")).equalsIgnoreCase("1") : null);
                                schemeRateApplicability.setApprovedAt(jsonText.get("approvedAt") != null ? LocalDateTime.parse((String) jsonText.get("approvedAt"), CommonUtils.Formatter4) : null);
                                schemeRateApplicability.setApprovedBy(jsonText.get("approvedBy") != null ? (String) jsonText.get("approvedBy") : null);
                                schemeRateApplicability.setTabDownloadDatetime(jsonText.get("tabDownloadDatetime") != null ? LocalDateTime.parse((String) jsonText.get("tabDownloadDatetime"), CommonUtils.Formatter4) : null);
                                schemeRateApplicability.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                schemeRateApplicability.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                schemeRateApplicability.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                schemeRateApplicability.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                schemeRateApplicability.setOriginatingOrgCode(jsonText.get("originatingOrgCode") != null ? (String) jsonText.get("originatingOrgCode") : null);
                                schemeRateApplicability.setOriginatingOrgType(jsonText.get("originatingOrgType") != null ? (String) jsonText.get("originatingOrgType") : null);
                                schemeRateApplicability.setOriginatingType(jsonText.get("originatingType") != null ? (Integer) jsonText.get("originatingType") : null);
                                schemeRateApplicability.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                schemeRateApplicability.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                schemeRateApplicability.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                schemeRateApplicability.setxCol4(jsonText.get("xCol4") != null ? String.valueOf(jsonText.get("xCol4")) : null);
                                schemeRateApplicability.setxCol5(jsonText.get("xCol5") != null ? String.valueOf(jsonText.get("xCol5")) : null);
                                schemeRateApplicability.setDescription(jsonText.get("description") != null ? (String) jsonText.get("description") : null);
                                schemeRateApplicabilityRepository.save(schemeRateApplicability);
                                break;
                            case "DELETE":
                                schemeRateApplicabilityRepository.deleteBySchemeRateAppCode((Integer) jsonText.get("schemeRateAppCode"));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_force_sync_request":
                    switch ((String) jsonText.get("tableName")) {
                        case "tbl_milk_collection":
                            try {
                                milkCollectionRepository.findAllCollectionByDate(LocalDateTime.parse((String) jsonText.get("fromDatetime"), CommonUtils.Formatter4), LocalDateTime.parse((String) jsonText.get("toDatetime"), CommonUtils.Formatter4), "");
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        case "tbl_member":
                            try {
                                for (Member member : memberRepository.findAll()) {
                                    member.setSociety(society);
                                    member.setMilkType(Hibernate.unproxy(member.getMilkType(), MilkType.class));
                                    member.setMemberType(Hibernate.unproxy(member.getMemberType(), MemberType.class));
                                    memberRepository.customSaveForSync(member, "");
                                }
                                for (MemberDetail detail : memberDetailRepository.findAll()) {
                                    detail.setState(Hibernate.unproxy(detail.getState(), State.class));
                                    detail.setDistrict(Hibernate.unproxy(detail.getDistrict(), District.class));
                                    detail.setSubDistrict(Hibernate.unproxy(detail.getSubDistrict(), SubDistrict.class));
                                    detail.setVillage(Hibernate.unproxy(detail.getVillage(), Village.class));
                                    detail.setHamlet(Hibernate.unproxy(detail.getHamlet(), Hamlet.class));
                                    detail.setBank(Hibernate.unproxy(detail.getBank(), Bank.class));
                                    detail.setBranch(Hibernate.unproxy(detail.getBranch(), Branch.class));
                                    memberDetailRepository.customSaveForSync(detail, "");
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        case "tbl_dcs_payment_cycle":
                            try {
                                for (SocietyPaymentCycle societyPaymentCycle : paymentCycleRepository.findAll()) {
                                    societyPaymentCycle.setFromShift(Hibernate.unproxy(societyPaymentCycle.getFromShift(), Shift.class));
                                    societyPaymentCycle.setToShift(Hibernate.unproxy(societyPaymentCycle.getToShift(), Shift.class));
                                    paymentCycleRepository.customSaveForSync(societyPaymentCycle, "");
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        case "tbl_milk_dispatch":
                            try {
                                for (MilkDispatch md : dispatchRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(LocalDateTime.parse((String) jsonText.get("fromDatetime"), AppConstant.SYNC_DATE_TIME_FMT), LocalDateTime.parse((String) jsonText.get("toDatetime"), AppConstant.SYNC_DATE_TIME_FMT))) {
                                    dispatchRepository.customSaveForSync(md, "");
                                    for (MilkDispatchTransaction dispatchTransaction : milkDispatchTransactionRepository.findByMilkDispatch(md)) {
                                        milkDispatchTransactionRepository.customSaveForSync(dispatchTransaction, "");
                                    }
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        default:
                            System.out.println("Default case");
                            break;
                    }
                    break;
                case "tbl_milk_collection":
                    try {
                        DateTimeFormatter CODE_DATE_FMT = DateTimeFormatter.ofPattern("yyMMdd");
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                MilkCollection milkCollection = new MilkCollection();
                                milkCollection.setCode(MainApp.identityDto.getDock().getDockNo() + "-" + (LocalDateTime.parse((String) jsonText.get("dateTimeOfCollection")).format(CODE_DATE_FMT)
                                        + jsonText.get("shiftCode") + "-" + milkCollection.getSampleNo()));
                                milkCollection.setDock(MainApp.identityDto.getDock());
                                milkCollection.setAmount(jsonText.get("amount") != null ? new BigDecimal(String.valueOf(jsonText.get("amount"))) : new BigDecimal(0));
                                milkCollection.setCollectionDate(LocalDateTime.parse(jsonText.get("dateTimeOfCollection").toString()));
                                milkCollection.setConvertedQty(jsonText.get("convertedQty") != null ? new BigDecimal(String.valueOf(jsonText.get("convertedQty"))) : new BigDecimal(0));
                                milkCollection.setConvertedQtyMode(jsonText.get("convertedQtyMode") != null ? (int) jsonText.get("convertedQtyMode") : null);
                                milkCollection.setClr(jsonText.get("clr") != null ? new BigDecimal(String.valueOf(jsonText.get("clr"))) : new BigDecimal(0));
                                milkCollection.setSociety(jsonText.get("dcsCode") != null ? societyRepository.findById(identityRepository.findBySocietyRefCode((String) jsonText.get("dcsCode")).getSocietyCode()).get() : null);
                                milkCollection.setFat(jsonText.get("fat") != null ? new BigDecimal(String.valueOf(jsonText.get("fat"))) : null);
                                milkCollection.setSnf(jsonText.get("snf") != null ? new BigDecimal(String.valueOf(jsonText.get("snf"))) : null);
                                milkCollection.setWater(jsonText.get("water") != null ? new BigDecimal(String.valueOf(jsonText.get("water"))) : null);
                                milkCollection.setWsCode(jsonText.get("wsCode") != null ? (String) jsonText.get("wsCode") : null);
                                milkCollection.setLectose(jsonText.get("lactose") != null ? new BigDecimal(String.valueOf(jsonText.get("lactose"))) : null);
                                milkCollection.setMember(jsonText.get("memberCode") != null ? memberRepository.findByCode((String) jsonText.get("memberCode")) : null);
                                milkCollection.setAnalyserCode(jsonText.get("milkAnalyserTypeCode") != null ? (String) jsonText.get("milkAnalyserTypeCode") : null);
                                milkCollection.setMilkQualityType((jsonText.get("milkQualityTypeCode") != null ? milkQualityTypeRepository.findById((int) jsonText.get("milkQualityTypeCode")).get() : null));
                                milkCollection.setMilkType(jsonText.get("milkTypeCode") != null ? milkTypeRepository.findByCode((int) jsonText.get("milkTypeCode")) : null);
                                milkCollection.setShift(jsonText.get("shiftCode") != null ? shiftRepository.findById((int) jsonText.get("shiftCode")).get() : null);
                                milkCollection.setProtein(jsonText.get("protein") != null ? new BigDecimal(String.valueOf(jsonText.get("protein"))) : null);
                                milkCollection.setRateCode(jsonText.get("purchaseRateCode") != null ? jsonText.get("purchaseRateCode").toString() : null);
                                milkCollection.setQualityAuto(jsonText.get("qltyAuto") != null ? (boolean) jsonText.get("qltyAuto") : null);
                                milkCollection.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                milkCollection.setQualityAt(jsonText.get("qltyTime") != null ? LocalDateTime.parse((String) jsonText.get("qltyTime")) : null);
                                milkCollection.setQtyMode(jsonText.get("qtyMode") != null ? (int) jsonText.get("qtyMode") : null);
                                milkCollection.setQty(jsonText.get("qty") != null ? new BigDecimal(String.valueOf(jsonText.get("qty"))) : null);
                                milkCollection.setRtpl(jsonText.get("rtpl") != null ? new BigDecimal(String.valueOf(jsonText.get("rtpl"))) : null);
                                milkCollection.setSampleNo(jsonText.get("sampleNo") != null ? (int) (jsonText.get("sampleNo")) : null);
                                milkCollection.setSocietyPaymentCycle(societyPaymentCycleRepository.findSocietyPaymentCycle(LocalDateTime.parse((String) jsonText.get("dateTimeOfCollection"))));
                                milkCollection.setRateCode(memberMilkPurchaseRateDetailRepository.findRateCode(new BigDecimal(String.valueOf(jsonText.get("fat"))), new BigDecimal(String.valueOf(jsonText.get("snf")))));
                                milkCollectionRepository.desktopCollectionSave(milkCollection, CommonUtils.setIdentityHeader());
                                break;
                            case "DELETE":
//                                milkCollectionRepository.delete((String) jsonText.get("schemeRateAppCode"));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_route":
                    Route route = new Route();
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                route.setCode(String.valueOf(jsonText.get("routeCode")));
                                route.setCodeEx(String.valueOf(jsonText.get("routeCodeEx")));
                                route.setName(String.valueOf(jsonText.get("routeName")));
                                route.setNameLocal(String.valueOf(jsonText.get("localName")));
                                route.setCapacity(jsonText.get("capacity") != null ? Integer.valueOf(jsonText.get("capacity").toString()) : null);
                                route.setLengthKms(jsonText.get("routeLengthKms") != null ? Integer.valueOf(String.valueOf(jsonText.get("routeLengthKms"))) : null);
                                route.setStartTime(jsonText.get("morningStartTime") != null ? LocalTime.parse((String) jsonText.get("morningStartTime")) : null);
                                route.setReturnTime(jsonText.get("morningEndTime") != null ? LocalTime.parse((String) jsonText.get("morningEndTime")) : null);
                                route.setUnion(MainApp.identityDto.getUnion());
                                route.setBmc(bmcRepository.findAll().get(0));
                                route.setActive(Boolean.parseBoolean(String.valueOf(jsonText.get("isActive"))));
                                route.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                route.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                route.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                route.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                route.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                route.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                route.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                routeRepository.save(route);
                                break;
                            case "DELETE":
                                route.setCode(String.valueOf(jsonText.get("routeCode")));
                                route.setActive(false);
                                routeRepository.save(route);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_types":
                    LedgerType ledgerType = new LedgerType();
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                ledgerType.setCode(jsonText.get("ledgerTypeCode") != null ? String.valueOf(jsonText.get("ledgerTypeCode")) : null);
                                ledgerType.setName(jsonText.get("ledgerTypeName") != null ? String.valueOf(jsonText.get("ledgerTypeName")) : null);
                                ledgerType.setNameLocal(jsonText.get("localName") != null ? String.valueOf(jsonText.get("localName")) : null);
                                ledgerType.setBalanceSheet(jsonText.get("balanceSheet") != null ? Boolean.parseBoolean(String.valueOf(jsonText.get("balanceSheet"))) : null);
                                ledgerType.setProfitLoss(jsonText.get("profitLoss") != null ? Boolean.parseBoolean(String.valueOf(jsonText.get("profitLoss"))) : null);
                                ledgerType.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                ledgerType.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                ledgerType.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                ledgerType.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                ledgerType.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                ledgerType.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                ledgerType.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                ledgerType.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                ledgerType.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                ledgerTypeRepository.save(ledgerType);
                                break;
                            case "DELETE":
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_groups":
                    LedgerGroup ledgerGroup = new LedgerGroup();
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                ledgerGroup.setCode(jsonText.get("ledgerGroupCode") != null ? String.valueOf(jsonText.get("ledgerGroupCode")) : null);
                                ledgerGroup.setName(jsonText.get("ledgerGroupName") != null ? String.valueOf(jsonText.get("ledgerGroupName")) : null);
                                ledgerGroup.setNameLocal(jsonText.get("localName") != null ? String.valueOf(jsonText.get("localName")) : null);
                                ledgerGroup.setLedgerType(ledgerTypeRepository.findById(String.valueOf(jsonText.get("ledgerTypeCode"))).orElse(null));
                                ledgerGroup.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                ledgerGroup.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                ledgerGroup.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                ledgerGroup.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                ledgerGroup.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                ledgerGroup.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                ledgerGroup.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                ledgerGroup.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                ledgerGroup.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                ledgerGroupRepository.save(ledgerGroup);
                                break;
                            case "DELETE":
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledgers":
                    Ledger ledger = new Ledger();
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                ledger.setCode(jsonText.get("ledgerCode") != null ? String.valueOf(jsonText.get("ledgerCode")) : null);
                                ledger.setName(jsonText.get("ledgerName") != null ? String.valueOf(jsonText.get("ledgerName")) : null);
                                ledger.setNameLocal(jsonText.get("localName") != null ? String.valueOf(jsonText.get("localName")) : null);
                                ledger.setHasSubLedger("1".equalsIgnoreCase(String.valueOf(jsonText.get("hasSubLedger"))));
                                ledger.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                ledger.setSociety(MainApp.identityDto.getSociety());
                                ledger.setLedgerGroup(ledgerGroupRepository.findById(String.valueOf(jsonText.get("ledgerGroupCode"))).orElse(null));
                                ledger.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                ledger.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                ledger.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                ledger.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                ledger.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                ledger.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                ledger.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                ledger.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                ledger.setPlantCode(jsonText.get("plantCode") != null ? String.valueOf(jsonText.get("plantCode")) : null);
                                ledger.setMccCode(jsonText.get("mccPlantCode") != null ? String.valueOf(jsonText.get("mccPlantCode")) : null);
                                ledger.setBmcCode(jsonText.get("bmcCode") != null ? String.valueOf(jsonText.get("bmcCode")) : null);
                                ledgerRepository.save(ledger);
                                break;
                            case "DELETE":
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_voucher_types":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                VoucherType vType = new VoucherType();
//                                vType.setCode(Long.valueOf(String.valueOf(jsonText.get("voucherTypeCode"))));
                                vType.setCode(jsonText.get("voucherTypeCode") != null ? Long.valueOf(jsonText.get("voucherTypeCode").toString()) : null);
                                vType.setName(jsonText.get("voucherTypeName") != null ? String.valueOf(jsonText.get("voucherTypeName")) : null);
                                vType.setNameLocal(jsonText.get("localName") != null ? String.valueOf(jsonText.get("localName")) : null);
                                vType.setVoucherType(jsonText.get("voucherType") != null ? Integer.valueOf(String.valueOf(jsonText.get("voucherType"))) : null);
                                vType.setCreditDebit("1".equalsIgnoreCase(String.valueOf(jsonText.get("creditDebit"))));
                                vType.setLedger(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerCode"))).orElse(null));
                                vType.setUnionCode((String) jsonText.get("unionCode"));
                                vType.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                vType.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                vType.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                vType.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                vType.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                vType.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                vType.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                vType.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                voucherTypeRepository.save(vType);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_bill_head":
                    BillHead billHead = new BillHead();
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                billHead.setCode(jsonText.get("billHeadCode") != null ? String.valueOf(jsonText.get("billHeadCode")) : null);
                                billHead.setName(jsonText.get("billHeadName") != null ? String.valueOf(jsonText.get("billHeadName")) : null);
                                billHead.setNameLocal(jsonText.get("billHeadName") != null ? String.valueOf(jsonText.get("billHeadName")) : null);
                                billHead.setDefaultHead(Boolean.valueOf((String) jsonText.get("isDefault")));
                                billHead.setDisburseAllowed("1".equalsIgnoreCase(String.valueOf(jsonText.get("isDisburseAllowed"))));
                                billHead.setUnion(MainApp.identityDto.getUnion());
                                billHead.setSociety(MainApp.identityDto.getSociety());
                                billHead.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                billHead.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                billHead.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                billHead.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                billHead.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                billHead.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                billHead.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                billHead.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                billHead.setHeadType(jsonText.get("billHeadType") != null ? Short.parseShort(String.valueOf(jsonText.get("billHeadType"))) : 0);
                                billHead.setCalculationBasedOn(jsonText.get("calculationBasedOn") != null ? String.valueOf(jsonText.get("calculationBasedOn")) : null);
                                billHead.setBillHeadFor(jsonText.get("billHeadFor") != null ? String.valueOf(jsonText.get("billHeadFor")) : null);
                                billHead.setDefaultBillHeadCode(jsonText.get("defaultBillHeadCode") != null ? Integer.parseInt(String.valueOf(jsonText.get("defaultBillHeadCode"))) : null);
                                billHead.setDisburseAllowed(Boolean.parseBoolean(String.valueOf(jsonText.get("isDisburseAllowed"))));
                                billHead.setGeneralFormul(jsonText.get("generalFormula") != null ? String.valueOf(jsonText.get("generalFormula")) : null);
                                billHead.setGeneralFormulaCode(jsonText.get("generalFormulaCode") != null ? String.valueOf(jsonText.get("generalFormulaCode")) : null);
                                billHead.setGeneralFormulaComma(jsonText.get("generalFormulaComma") != null ? String.valueOf(jsonText.get("generalFormulaComma")) : null);
                                billHead.setHasSlab(Boolean.parseBoolean(String.valueOf(jsonText.get("hasSlab"))));
                                billHead.setHold(Boolean.parseBoolean(String.valueOf(jsonText.get("isHold"))));
                                billHead.setReserved(Boolean.parseBoolean(String.valueOf(jsonText.get("isReserved"))));
                                billHead.setPaymentCycleType(jsonText.get("paymentCycleType") != null ? String.valueOf(jsonText.get("paymentCycleType")) : null);
                                billHead.setSapSeqNo(jsonText.get("sapSeqNo") != null ? Integer.parseInt(String.valueOf(jsonText.get("sapSeqNo"))) : null);
                                billHead.setSequenceNo(jsonText.get("sequenceNo") != null ? Integer.parseInt(String.valueOf(jsonText.get("sequenceNo"))) : null);
                                billHead.setMilkType(jsonText.get("milkTypeCode") != null ? milkTypeRepository.findByCode((int) jsonText.get("milkTypeCode")) : null);
                                billHeadRepository.save(billHead);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_banks":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                Bank bank = new Bank();

                                bank.setCode(jsonText.get("bankCode") != null ? String.valueOf(jsonText.get("bankCode")) : null);
                                bank.setName(jsonText.get("bankName") != null ? String.valueOf(jsonText.get("bankName")) : null);
                                bank.setNameLocal(jsonText.get("localName") != null ? String.valueOf(jsonText.get("localName")) : null);
//                                bank.setAcNoLength(jsonText.get("acNoLength") != null ? Short.parseShort(String.valueOf(jsonText.get("acNoLength"))) : null);
                                if (jsonText.get("acNoLength") != null) {
                                    double len = Double.parseDouble(String.valueOf(jsonText.get("acNoLength")));
                                    bank.setAcNoLength((short) len);
                                }
                                bank.setCheckedAcNoLength("1".equals(String.valueOf(jsonText.get("checkedAcNo"))));
                                bank.setNationalizedBank("1".equals(String.valueOf(jsonText.get("nationalizedBank"))));
                                bank.setLedger(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerCode"))).orElse(null));
                                bank.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                bank.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                bank.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                bank.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                bank.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                bank.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                bank.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                bank.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);

                                bankRepository.save(bank);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_financial_year":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                FinancialYear financialYear = new FinancialYear();
                                financialYear.setCode(jsonText.get("code") != null ? String.valueOf(jsonText.get("code")) : null);
                                financialYear.setStartDate(jsonText.get("startingDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("startingDate"))) : null);
                                financialYear.setEndDate(jsonText.get("endingDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("endingDate"))) : null);
                                financialYear.setActive("1".equalsIgnoreCase(String.valueOf(jsonText.get("isActive"))));
                                financialYear.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                financialYear.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                financialYear.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                financialYear.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);

                                financialYearRepository.save(financialYear);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_mapping_product_group":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                LedgerMappingProductGroup mapping = new LedgerMappingProductGroup();

                                mapping.setCode(jsonText.get("ledgerMappingProductGroupCode") != null ? String.valueOf(jsonText.get("ledgerMappingProductGroupCode")) : null);
                                mapping.setLedgerSaleCode(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerSaleCode"))).orElse(null));
                                mapping.setLedgerPurchaseCode(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerPurchaseCode"))).orElse(null));
                                mapping.setSociety(MainApp.identityDto.getSociety());
                                mapping.setProductGroup(productGroupRepository.findById(Integer.parseInt(String.valueOf(jsonText.get("productGroupCode")))).orElse(null));
                                mapping.setUnionCode(jsonText.get("unionCode") != null ? String.valueOf(jsonText.get("unionCode")) : null);
                                mapping.setPlantCode(jsonText.get("plantCode") != null ? String.valueOf(jsonText.get("plantCode")) : null);
                                mapping.setMccCode(jsonText.get("mccPlantCode") != null ? String.valueOf(jsonText.get("mccPlantCode")) : null);
                                mapping.setBmcCode(jsonText.get("bmcCode") != null ? String.valueOf(jsonText.get("bmcCode")) : null);
                                mapping.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                mapping.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                mapping.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                mapping.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);

                                ledgerMappingProductGroupRepository.save(mapping);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_mapping_bill_head":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                LedgerMappingBillHead mapping = new LedgerMappingBillHead();
                                mapping.setCode(jsonText.get("ledgerMappingBillHeadCode") != null ? String.valueOf(jsonText.get("ledgerMappingBillHeadCode")) : null);
                                mapping.setType(jsonText.get("type") != null ? Integer.parseInt(String.valueOf(jsonText.get("type"))) : 0);
                                mapping.setHasSubLedger("1".equalsIgnoreCase(String.valueOf(jsonText.get("hasSubLedger"))));
                                mapping.setCreditDebit("1".equalsIgnoreCase(String.valueOf(jsonText.get("creditDebit"))));
                                mapping.setLedger(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerCode"))).orElse(null));
                                mapping.setBillCriteria(billCriteriaRepository.findById(String.valueOf(jsonText.get("billCriteriaCode"))).orElse(null));
                                mapping.setSociety(MainApp.identityDto.getSociety());
                                mapping.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                if (jsonText.get("billHeadCode") != null) {
                                    String code = String.valueOf(jsonText.get("billHeadCode"));
                                    try {
                                        code = String.valueOf(Long.valueOf(code));
                                    } catch (Exception e) {
                                    }
                                    mapping.setBillHead(billHeadRepository.findById(code).orElse(null));
                                } else {
                                    mapping.setBillHead(null);
                                }
                                mapping.setPlantCode(jsonText.get("plantCode") != null ? String.valueOf(jsonText.get("plantCode")) : null);
                                mapping.setMccCode(jsonText.get("mccPlantCode") != null ? String.valueOf(jsonText.get("mccPlantCode")) : null);
                                mapping.setBmcCode(jsonText.get("bmcCode") != null ? String.valueOf(jsonText.get("bmcCode")) : null);
                                mapping.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                mapping.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                mapping.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                mapping.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                mapping.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                mapping.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                mapping.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);
                                ledgerMappingBillHeadRepository.save(mapping);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_mapping_event":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                LedgerMappingEvent eventMapping = new LedgerMappingEvent();

                                eventMapping.setCode(jsonText.get("code") != null ? Integer.parseInt(String.valueOf(jsonText.get("code"))) : null);
                                eventMapping.setEventcode(jsonText.get("eventcode") != null ? Integer.parseInt(String.valueOf(jsonText.get("eventcode"))) : 0);
                                eventMapping.setCreditSubLedger("1".equalsIgnoreCase(String.valueOf(jsonText.get("creditSubLedger"))) || "true".equalsIgnoreCase(String.valueOf(jsonText.get("creditSubLedger"))));
                                eventMapping.setDebitSubLedger("1".equalsIgnoreCase(String.valueOf(jsonText.get("debitSubLedger"))) || "true".equalsIgnoreCase(String.valueOf(jsonText.get("debitSubLedger"))));
                                eventMapping.setSociety(MainApp.identityDto.getSociety());
                                eventMapping.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                if (jsonText.get("event_code") != null) {
                                    eventMapping.setEvents(eventRepository.findById(Integer.parseInt(String.valueOf(jsonText.get("event_code")))).orElse(null));
                                }
                                if (jsonText.get("credit_ledger_code") != null) {
                                    eventMapping.setCreditLedger(ledgerRepository.findById(String.valueOf(jsonText.get("credit_ledger_code"))).orElse(null));
                                }
                                if (jsonText.get("debit_ledger_code") != null) {
                                    eventMapping.setDebitLedger(ledgerRepository.findById(String.valueOf(jsonText.get("debit_ledger_code"))).orElse(null));
                                }
                                if (jsonText.get("voucher_type_code") != null) {
                                    eventMapping.setVoucherType(voucherTypeRepository.findById(String.valueOf(Integer.parseInt(String.valueOf(jsonText.get("voucher_type_code"))))).orElse(null));
                                }
                                eventMapping.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                eventMapping.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                eventMapping.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                eventMapping.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);

                                ledgerMappingEventRepository.save(eventMapping);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tbl_ledger_mapping_tax_detail":
                    try {
                        switch (subscribed.getOperation()) {
                            case "INSERT":
                            case "UPDATE":
                                LedgerMappingTaxDetail mapping = new LedgerMappingTaxDetail();

                                mapping.setCode(jsonText.get("ledgerMappingTaxDetailCode") != null ? String.valueOf(jsonText.get("ledgerMappingTaxDetailCode")) : null);
                                mapping.setSociety(MainApp.identityDto.getSociety());
                                mapping.setUnionCode(MainApp.identityDto.getUnion().getCode());
                                if (jsonText.get("taxDetailCode") != null) {
                                    mapping.setTaxDetail(taxDetailRepository.findById(String.valueOf(jsonText.get("taxDetailCode"))).orElse(null));
                                }
                                if (jsonText.get("ledgerCode") != null) {
                                    mapping.setLedger(ledgerRepository.findById(String.valueOf(jsonText.get("ledgerCode"))).orElse(null));
                                }
                                mapping.setCreatedAt(jsonText.get("createdAt") != null ? LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4) : null);
                                mapping.setCreatedBy(jsonText.get("createdBy") != null ? (String) jsonText.get("createdBy") : null);
                                mapping.setUpdatedAt(jsonText.get("updatedAt") != null ? LocalDateTime.parse((String) jsonText.get("updatedAt"), CommonUtils.Formatter4) : null);
                                mapping.setUpdatedBy(jsonText.get("updatedBy") != null ? (String) jsonText.get("updatedBy") : null);
                                mapping.setxCol1(jsonText.get("xCol1") != null ? String.valueOf(jsonText.get("xCol1")) : null);
                                mapping.setxCol2(jsonText.get("xCol2") != null ? String.valueOf(jsonText.get("xCol2")) : null);
                                mapping.setxCol3(jsonText.get("xCol3") != null ? String.valueOf(jsonText.get("xCol3")) : null);

                                ledgerMappingTaxDetailRepository.save(mapping);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
            logRepository.save(subscribed.toSubscribedLog(subscribed));
            subscribedRepository.delete(subscribed);
        }

    }


    protected void fetchRate(String fat, String snf, String qty, MilkType milkType, MilkQualityType
            milkQualityType, SocietyMilkPurchaseRate societyMilkPurchaseRate, MilkReceiptTransaction
                                     transaction, MilkReceipt milkReceipt) {
        if (!fat.isEmpty() && !snf.isEmpty() && milkType != null && milkQualityType != null) {
            List<SocietyMilkPurchaseRateBased> listBased;
            listBased = societyMilkPurchaseRateService.fetchRateBased(societyMilkPurchaseRate.getCode());

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
                        BigDecimal kgEqFat = CommonUtils.calculateEqKgFat(eqFat, qty);
                        BigDecimal kgFat = CommonUtils.calculateKgFat(fatVal, qty);
                        String formula = basedFat.get().getFormula() != null ?
                                basedFat.get().getFormula().getFormula() : null;
                        if (formula == null || formula.isEmpty()) {
                            transaction.setRate(BigDecimal.ZERO);
                            transaction.setAmount(BigDecimal.ZERO);
                            return;
                        }
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("RATE", kgRate.toString());
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula).setScale(2, RoundingMode.HALF_UP);

                        transaction.setRate(new BigDecimal(CommonUtils.calculateAvgRate(val, qty).toString()));
                        transaction.setAmount(val);
                    } else {
                        transaction.setRate(BigDecimal.ZERO);
                        transaction.setAmount(BigDecimal.ZERO);
                    }
                }
            } else {
                LocalDateTime date = milkReceipt.getFromDate();
                Optional<MilkReceipt> milkReceiptOptional = milkReceiptRepository.findPreviousRecordOfGoodMilkType(date);
                if (milkReceiptOptional.isPresent()) {
                    List<MilkReceiptTransaction> transactionList = milkReceiptTransactionRepository.findByMilkReceipt(milkReceiptOptional.get());
                    transaction.setRate(transactionList.get(0).getRate().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
                    transaction.setAmount(transactionList.get(0).getAmount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
                } else {
                    transaction.setRate(BigDecimal.ZERO);
                    transaction.setAmount(BigDecimal.ZERO);
                }
            }
        }
    }
}
