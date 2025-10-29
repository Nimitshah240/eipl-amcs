package com.eipl.amcs.sync.producer;

import com.eipl.amcs.base.model.Notification;
import com.eipl.amcs.base.repository.NotificationRepository;
import com.eipl.amcs.base.service.NextCodeService;
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
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.SchemeRate;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.repository.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
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
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.AppConstant;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BroadcastedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedService.class);

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


    ObjectMapper mapper = new ObjectMapper();
    Map<Integer, LocalDate> insuranceStartDateMap = new HashMap<>(); // Added on 22/5 by Nimit; Usage - to calculate correct age on the basis of birthdate of member and startdate of insurance;

    public String sendBroadcastedAll() {
        List<Broadcasted> list;
        LOGGER.info("In send broadcasted msg All");
        try {
            list = repository.findAllByTableNameNotInOrderByCreatedAt(List.of("tbl_insurance_detail", "tbl_insurance_detail_summary"));
            for (List<Broadcasted> part : ListUtils.partition(list, 10)) {
                producer.produce(part);
//                part.forEach(item -> {
//                    producer.produce(item);
//                });
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

    @Scheduled(fixedDelay = 10000)
    public void sendBroadcasted() {
        LOGGER.info("In send broadcasted msg");
        try {
            List<Broadcasted> list = repository.findTop50ByTableNameNotInOrderByCreatedAt(List.of("tbl_insurance_detail", "tbl_insurance_detail_summary"));
            if (list == null || list.isEmpty())
                return;
            producer.produce(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 10000)
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

    @Scheduled(fixedDelay = 10000)
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

    /**
     * @param list
     * @param society
     * @param shiftList
     * @param milkTypeList
     * @param milkQualityTypeList
     * @updatedBy Nimit Shah
     * @updatedOn - 23-07-2025
     * @update - update to get sysUpdatedBy column of insuranceDetail data and schemeRate & SchemeRateApplicability data from portal.
     */
    private void process(List<Subscribed> list, Society society, List<Shift> shiftList, List<MilkType> milkTypeList, List<MilkQualityType> milkQualityTypeList) throws JsonProcessingException {
        for (Subscribed subscribed : list) {
            Map jsonText = mapper.readValue(subscribed.getDataText(), Map.class);
            switch (subscribed.getTableName()) {
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
//                        requisitionTransaction.setCode((String) jsonText.get("requisitionTransactionCode"));
                            requisitionTransaction.setApprovedBy((String) jsonText.get("approvedBy"));
                            if (jsonText.get("approvedDate") != null)
                                requisitionTransaction.setApprovedDate(LocalDate.parse((String) jsonText.get("approvedDate"), CommonUtils.Formatter5));
                            if (jsonText.get("approvedQuantity") != null)
                                requisitionTransaction.setApprovedQuantity(new BigDecimal(String.valueOf(jsonText.get("approvedQuantity"))));
                            if (jsonText.get("cancelledAt") != null)
//                        requisitionTransaction.setCancelledAt(LocalDateTime.parse((String) jsonText.get("cancelledAt"), CommonUtils.Formatter4));
//                    if (jsonText.get("createdAt") != null)
//                        requisitionTransaction.setCreatedAt(LocalDateTime.parse((String) jsonText.get("createdAt"), CommonUtils.Formatter4));
//                    if (jsonText.get("createdBy") != null)
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
//                        if (jsonText.get("quantity") != null)
//                            requisitionTransaction.setQuantity(new BigDecimal(String.valueOf(jsonText.get("quantity"))));
//                    if (jsonText.get("requisitionDate") != null)
//                        requisitionTransaction.setRequisitionDate(LocalDateTime.parse((String) jsonText.get("requisitionDate"), CommonUtils.Formatter4));
//                    if (jsonText.get("expectedDeliveryDate") != null)
//                        requisitionTransaction.setExpectedDeliveryDate(LocalDate.parse((String) jsonText.get("expectedDeliveryDate"), CommonUtils.Formatter4));
                            requisitionTransaction.setSchemeAddType((String) jsonText.get("schemeAddType"));
                            requisitionTransaction.setStatus((String) jsonText.get("status"));
//                            requisitionTransaction.setProduct((String) jsonText.get("product"));
//                            requisitionTransaction.setProductRequisition((String) jsonText.get("productRequisition"));
                            requisitionTransaction.setProductSchemeCode((String) jsonText.get("productSchemeCode"));
                            if (jsonText.get("approvedQuantity") != null)
                                requisitionTransaction.setApprovedQuantity(new BigDecimal(String.valueOf(jsonText.get("approvedQuantity"))));
                            if (jsonText.get("passonToMember") != null)
                                requisitionTransaction.setPassonToMember(Integer.parseInt((String) jsonText.get("passonToMember")));
//                    requisitionTransaction.setUnionCode((String) jsonText.get("unionCode"));
//                    requisitionTransaction.setSocietyCode(society.getCode());
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
//                            productDispatch.setSociety((String) jsonText.get("society"));
                        productDispatch.setActive(true);
                        productDispatch.setSociety(society);
                        productDispatchService.save(productDispatch, "");

                        ProductReceipt receipt = new ProductReceipt();
                        receipt.setChallanNo(productDispatch.getChallanNo());
//                    receipt.setCustomer();

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
//                    product.setxCol1(String.valueOf(jsonText.get("productDesc")));
                        product.setReferenceCode(String.valueOf(jsonText.get("refCode")));
                        product.setActive(Boolean.parseBoolean(String.valueOf(jsonText.get("isActive"))));
                        ProductGroup group = productGroupRepository.findByCode(Integer.valueOf(String.valueOf(jsonText.get("productGroupCode"))));
                        product.setProductGroup(group);
                        product.setActive(true);
                        product.setCreatedBy("PORTAL");
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
                        if (jsonText.get("isQualityManual").toString().equalsIgnoreCase("1")) {
                            allowDcsManualCollectionRange.setQualityManual(true);
                        } else {
                            allowDcsManualCollectionRange.setQualityManual(false);
                        }
                        if (jsonText.get("isWeightManual").toString().equalsIgnoreCase("1")) {
                            allowDcsManualCollectionRange.setWeightManual(true);
                        } else {
                            allowDcsManualCollectionRange.setWeightManual(false);
                        }
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
//                                insuranceDetailSummary.setFromDate(jsonText.get("fromDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("fromDate"))) : null);
                                insuranceDetailSummary.setFromDate(jsonText.get("fromDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("fromDate")), CommonUtils.Formatter5) : null);
//                                insuranceDetailSummary.setToDate(jsonText.get("toDate") != null ? LocalDate.parse(String.valueOf(jsonText.get("toDate"))) : null);
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
//                                insuranceDetail.setMemberCode(
//                                        jsonText.get("memberCode") != null
//                                                ? String.valueOf(jsonText.get("memberCode")).replaceAll("^.*(?=\\d{4})", "")
//                                                : null
//                                );
                                insuranceDetail.setMemberName(jsonText.get("memberName") != null ? String.valueOf(jsonText.get("memberName")) : null);
                                insuranceDetail.setAdharNo(jsonText.get("adharNo") != null ? String.valueOf(jsonText.get("adharNo")) : null);
                                insuranceDetail.setDob(jsonText.get("dob") != null ? String.valueOf(jsonText.get("dob")) : null);
                                try {
                                    int age = Period.between(LocalDate.parse(EncryptionUtil.decrypt(jsonText.get("dob") != null ? String.valueOf(jsonText.get("dob")) : null)), insuranceStartDateMap.get(jsonText.get("insuranceMasterCode") != null ? (Integer) jsonText.get("insuranceMasterCode") : null)).getYears();
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
                                insuranceDetailRepository.deleteById(String.valueOf((String) jsonText.get("insuranceDetailCode")));
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
                                schemeRate.setIsActive(jsonText.get("isMccWiseRate") != null ? String.valueOf(jsonText.get("isMccWiseRate")).equalsIgnoreCase("1") : null);
                                schemeRate.setIsActive(jsonText.get("isMemberRate") != null ? String.valueOf(jsonText.get("isMemberRate")).equalsIgnoreCase("1") : null);
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
                                schemeRateApplicability.setFromDate(fromDate != null ? fromDate : null);
                                LocalDateTime toDate = jsonText.get("toDate") != null ? LocalDateTime.parse((String) jsonText.get("toDate"), CommonUtils.Formatter4) : null;
                                shift = shiftRepository.findById(schemeRateApplicability.getToShift()).orElseThrow(() -> null);
                                toDate = toDate.with(CommonUtils.getTimeFromShift(shift));
                                schemeRateApplicability.setToDate(toDate != null ? toDate : null);
                                schemeRateApplicability.setRtpl(jsonText.get("rtpl") != null ? new BigDecimal(String.valueOf(jsonText.get("rtpl"))) : null);
                                schemeRateApplicability.setRateClass(jsonText.get("rateClass") != null ? (String) jsonText.get("rateClass") : null);
                                schemeRateApplicability.setApplicableFor(jsonText.get("applicableFor") != null ? (String) jsonText.get("applicableFor") : null);
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
//                    formula = formula.replace("RATE", kgRate.toString());
                        if (basedSnf.get().getVal().compareTo(BigDecimal.ZERO) > 0) {
                            formula = formula.replace("RATE", kgRate.multiply(basedSnf.get().getVal()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString());
                        } else {
                            formula = formula.replace("RATE", kgRate.toString());
                        }
                        formula = formula.replace("RATE", kgRate.toString());
                        formula = formula.replace("KGEQFAT", kgEqFat.toString());
                        formula = formula.replace("KGFAT", kgFat.toString());
                        BigDecimal val = CommonUtils.evaluate(formula).setScale(2, RoundingMode.HALF_UP);
//                    setAmount(val.toString());
//                    setRate(CommonUtils.calculateAvgRate(val, qty).toString());

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
