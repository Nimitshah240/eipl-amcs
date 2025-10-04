package com.eipl.amcs.config;

import com.eipl.amcs.auth.bootcontroller.IdentityController;
import com.eipl.amcs.auth.repository.RolePermissionRepository;
import com.eipl.amcs.auth.repository.UserRepository;
import com.eipl.amcs.auth.repository.UserRoleRepository;
import com.eipl.amcs.auth.service.IdentityService;
import com.eipl.amcs.base.repository.IdentityRepository;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.repository.NotificationRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.master.geo.repository.*;
import com.eipl.amcs.master.global.repository.*;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.global.service.RateTypeService;
import com.eipl.amcs.master.global.service.ShiftService;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailSummaryRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceMasterRepository;
import com.eipl.amcs.master.inventory.repository.ProductGroupRepository;
import com.eipl.amcs.master.inventory.repository.ProductPurchaseRateRepository;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.inventory.repository.ProductSaleRateRepository;
import com.eipl.amcs.master.operation.repository.*;
import com.eipl.amcs.master.org.repository.*;
import com.eipl.amcs.master.procurement.repository.*;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.operation.billing.repository.*;
import com.eipl.amcs.operation.inventory.repository.*;
import com.eipl.amcs.operation.inventory.service.ProductDispatchService;
import com.eipl.amcs.operation.inventory.service.ProductDispatchTransactionService;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionTransactionService;
import com.eipl.amcs.operation.procurement.repository.*;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.operation.share.repository.ShareDividendRepository;
import com.eipl.amcs.operation.share.repository.ShareRateRepository;
import com.eipl.amcs.operation.share.repository.ShareRepository;
import com.eipl.amcs.setting.repository.GeneralConfigAuditRepository;
import com.eipl.amcs.setting.repository.GeneralConfigRepository;
import com.eipl.amcs.setting.repository.HardwareDeviceConfigRepository;
import com.eipl.amcs.sync.producer.BroadcastedProducer;
import com.eipl.amcs.sync.repository.BroadcastedLogRepository;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.eipl.amcs.sync.repository.NavigationBookRepository;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static com.eipl.amcs.MainApp.context;

public class BeanConfig {
    public static DockRepository dockRepository;
    public static SocietyRepository societyRepository;
    public static UnionRepository unionRepository;
    public static IdentityRepository identityRepository;

    public static SubLedgerOpeningBalanceRepository subLedgerOpeningBalanceRepository;
    public static NextCodeService nextCodeService;
    public static ProductSaleInstallmentRepository installmentRepository;
    public static UserRoleRepository userRoleRepository;
    public static RolePermissionRepository rolePermissionRepository;

    public static LedgerOpeningBalanceRepository ledgerOpeningBalanceRepository;
    public static MemberRepository memberRepository;
    public static UserRepository userRepository;
    public static NextCodeRepository nextCodeRepository;


    public static MilkCollectionRepository milkCollectionRepository;
    public static SocietyPaymentCycleRepository paymentCycleRepository;
    public static HardwareDeviceConfigRepository hardwareRepository;
    public static MemberMilkPurchaseRateApplicabilityRepository memberRateAppRepository;
    public static ShiftRepository shiftRepository;
    public static MemberMilkPurchaseRateRepository rateRepository;
    public static MilkQualityTypeRepository milkQualityRepository;
    public static MilkTypeRepository milkTypeRepository;

    public static LedgerMappingEventRepository ledgerMappingEventRepository;
    public static FinancialYearRepository financialYearRepository;
    public static SubLedgerRepository subLedgerRepository;
    public static VoucherRepository voucherRepository;
    public static VoucherTransactionRepository voucherTxnRepository;
    public static VoucherSubLedgerRepository voucherSubLedgerRepository;

    public static NotificationRepository notificationRepository;
    public static VillageRepository villageRepository;
    public static SubDistrictRepository subDistRepository;

    public static StateRepository stateRepository;

    public static HamletRepository hamletRepository;

    public static BasicTaxRepository basicTaxRepository;

    public static CashAdvanceRepository cashAdvanceRepository;

    public static DistrictRepository districtRepository;

    public static CommitteeMembersRepository committeeMembersRepository;
    public static SocietyRepository socRepository;
    public static DesignationRepository designationRepository;

    public static EventRepository eventRepository;

    public static UnitRepository unitRepository;

    public static LedgerGroupRepository ledgerGroupRepository;
    public static LedgerTypeRepository typeRepository;

    public static LedgerMappingBillHeadRepository ledgerMappingBillHeadRepository;

    public static SocietyYearClosingRepository closingRepository;

    public static UnitConversionRepository unitConversionRepository;

    public static LedgerMappingProductGroupRepository ledgerMappingProductGroupRepository;

    public static LedgerMappingTaxDetailRepository ledgerMappingTaxDetailRepository;

    public static LedgerRepository ledgerRepository;
    public static LedgerSubLedgerMappingRepository ledgerSubLedgerMappingRepository;
    public static LedgerService ledgerService;

    public static MeetingAgendaRepository meetingAgendaRepository;
    public static MomRepository momRepository;
    public static MomActionRepository momActionRepository;

    public static StaffSalaryRepository staffSalaryRepository;

    public static StaffMemberRepository staffMemberRepository;

    public static StaffSalaryHeadRepository staffSalaryHeadRepository;

    public static StaffSalaryProcessRepository staffSalaryProcessRepository;

    public static StaffSalaryMappingRepository staffSalaryMappingRepository;

    public static SubLedgerLedgerConfigRepository subLedgerLedgerConfigRepository;

    public static SubLedgerService subLedgerService;

    public static TaxRepository taxRepository;
    public static TaxDetailRepository taxDetailRepository;
    public static VoucherTypeLedgerConfigRepository voucherTypeLedgerConfigRepository;

    public static SocietyMilkPurchaseRateDetailRepository societyMilkPurchaseRateDetailRepository;

    public static MemberMilkPurchaseRateApplicabilityRepository memberMilkPurchaseRateApplicabilityRepository;

    public static VoucherTypeRepository voucherTypeRepository;

    public static GenderRepository genderRepository;

    public static MemberTypeRepository memberTypeRepository;

    public static MilkClassRepository milkClassRepository;

    public static RateTypeRepository rateTypeRepository;

    public static InsuranceMasterRepository insuranceMasterRepository;
    public static InsuranceDetailSummaryRepository insuranceDetailSummaryRepository;
    public static InsuranceDetailRepository insuranceDetailRepository;

    public static NavigationBookRepository navigationBookRepository;

    public static ProductGroupRepository productGroupRepository;


    public static ProductPurchaseRateRepository productPurchaseRateRepository;
    public static ProductRepository productRepository;


    public static BroadcastedProducer producer;
    public static BroadcastedRepository broadcastedRepository;
    public static SubscribedRepository subscribedRepository;
    public static BroadcastedLogRepository logRepository;
    public static ProductRequisitionService productRequisitionService;
    public static ProductRequisitionTransactionService productRequisitionTransactionService;
    public static ProductDispatchTransactionService productDispatchTransactionService;
    public static ProductDispatchService productDispatchService;
    public static ManualRequestRepository manualRequestRepository;
    public static DpuIncentiveRequestRepository dpuIncentiveRequestRepository;
    public static AllowDcsManualCollectionRangeRepository allowDcsManualCollectionRangeRepository;
    public static MemberDetailRepository memberDetailRepository;
    public static MilkDispatchRepository dispatchRepository;
    public static MilkDispatchTransactionRepository milkDispatchTransactionRepository;
    public static ProductRequisitionRepository productRequisitionRepository;
    public static ProductRequisitionTransactionRepository productRequisitionTransactionRepository;
    public static ProductDispatchRepository productDispatchRepository;
    public static CustomerRepository customerRepository;
    public static MilkQualityTypeRepository milkQualityTypeRepository;
    public static MilkReceiptRepository milkReceiptRepository;
    public static MilkReceiptTransactionRepository milkReceiptTransactionRepository;
    public static MilkDispatchService milkDispatchService;
    public static SocietyMilkPurchaseRateService societyMilkPurchaseRateService;
    public static MilkCollectionService milkCollectionService;
    public static BillCriteriaRepository billCriteriaRepository;


    public static MemberBillSummaryRepository summaryRepository;
    public static MemberBillRepository billRepository;
    public static MemberBillTransactionRepository transactionRepository;
    public static BillHeadRepository billHeadRepository;

    public static CustomerDetailsRepository customerDetailrepository;
    public static LedgerSubLedgerMappingRepository mappingRepository;

    public static RestTemplate restTemplate;

    public static ProductSaleRateRepository saleRateRepository;

    public static ProductSaleRateRepository productSaleRateRepository;

    public static MemberDetailRepository memberDetailrepository;
    public static MemberCreditLimitRepository memberCreditLimitRepository;
    public static MemberCreditLimitTransactionRepository memberCreditLimitTxnRepository;
    public static MilkCollectionRepository collectionRepository;

    public static BankRepository bankRepository;

    public static DockMilkTypeRepository dockMilkTypeRepository;

    public static BmcRepository bmcRepository;

    public static BranchRepository branchRepository;

    public static MccRepository mccRepository;
    public static PlantRepository plantRepository;

    public static RouteRepository routeRepository;

    public static SocietyPaymentCycleRepository societyPaymentCycleRepository;

    public static LocalMilkSaleRateRepository localMilkSaleRateRepository;

    public static MemberMilkPurchaseRateRepository memberMilkPurchaseRateRepository;
    public static MemberMilkPurchaseRateApplicabilityRepository appRepository;
    public static MemberMilkPurchaseRateDetailRepository dtlRepository;
    public static MemberMilkPurchaseRateBasedRepository basedRepository;

    public static SocietyMilkPurchaseRateRepository societyMilkPurchaseRateRepository;

    public static SocietyMilkPurchaseRateBasedRepository societyMilkPurchaseRateBasedRepository;

    public static SocietyMilkPurchaseRateApplicabilityRepository societyMilkPurchaseRateApplicabilityRepository;

    public static BonusRepository bonusRepository;
    public static BonusSummaryRepository bonusSummaryRepository;

    public static ProductDispatchTransactionRepository productDispatchTransactionRepository;

    public static GeneralConfigRepository generalConfigRepository;
    public static GeneralConfigAuditRepository auditRepository;

    public static ShareRateRepository shareRateRepository;
    public static ShareRepository shareRepository;
    public static ShareDividendRepository shareDividendRepository;


    public static ProductReceiptRepository productReceiptRepository;
    public static ProductReceiptTransactionRepository receiptTransRepository;
    public static ProductReceiptTaxRepository receiptTaxRepository;
    public static ProductStockRepository stockRepository;
    public static ProductStockTransactionRepository stockTxnRepository;

    public static HardwareDeviceRepository hardwareDeviceRepository;


    public static ProductSaleRepository productSaleRepository;
    public static ProductSaleTransactionRepository saleTransRepository;
    public static ProductSaleTaxRepository saleTaxRepository;

    public static LocalMilkSaleRepository localMilkSaleRepository;

    public static BmcRunningHoursRepository bmcRunningHoursRepository;

    public static BmcRecordingRepository bmcRecordingRepository;

    public static IdentityService identityService;
    public static ShiftService shiftService;
    public static RateTypeService rateTypeService;
    public static MilkTypeService milkTypeService;
    public static MilkQualityTypeService milkQualityTypeService;

    public static FormulaRepository formulaRepository;

    public static MemberMilkPurchaseRateService memberMilkPurchaseRateService;


    public static void settingBean() {
        try {

            genderRepository = context.getBean(GenderRepository.class);

            memberMilkPurchaseRateService = context.getBean(MemberMilkPurchaseRateService.class);

            formulaRepository = context.getBean(FormulaRepository.class);

            milkQualityTypeService = context.getBean(MilkQualityTypeService.class);

            milkTypeService = context.getBean(MilkTypeService.class);

            rateTypeService = context.getBean(RateTypeService.class);

            shiftService = context.getBean(ShiftService.class);

            identityService = context.getBean(IdentityService.class);

            bmcRunningHoursRepository = context.getBean(BmcRunningHoursRepository.class);

            bmcRecordingRepository = context.getBean(BmcRecordingRepository.class);

            localMilkSaleRepository = context.getBean(LocalMilkSaleRepository.class);

            hardwareDeviceRepository = context.getBean(HardwareDeviceRepository.class);

            productSaleRepository = context.getBean(ProductSaleRepository.class);
            saleTransRepository = context.getBean(ProductSaleTransactionRepository.class);
            saleTaxRepository = context.getBean(ProductSaleTaxRepository.class);

            productReceiptRepository = context.getBean(ProductReceiptRepository.class);
            receiptTransRepository = context.getBean(ProductReceiptTransactionRepository.class);
            receiptTaxRepository = context.getBean(ProductReceiptTaxRepository.class);
            stockRepository = context.getBean(ProductStockRepository.class);
            stockTxnRepository = context.getBean(ProductStockTransactionRepository.class);

            shareRepository = context.getBean(ShareRepository.class);

            shareDividendRepository = context.getBean(ShareDividendRepository.class);

            shareRateRepository = context.getBean(ShareRateRepository.class);

            generalConfigRepository = context.getBean(GeneralConfigRepository.class);
            auditRepository = context.getBean(GeneralConfigAuditRepository.class);


            productDispatchTransactionRepository = context.getBean(ProductDispatchTransactionRepository.class);

            bonusRepository = context.getBean(BonusRepository.class);
            bonusSummaryRepository = context.getBean(BonusSummaryRepository.class);

            societyPaymentCycleRepository = context.getBean(SocietyPaymentCycleRepository.class);

            societyMilkPurchaseRateApplicabilityRepository = context.getBean(SocietyMilkPurchaseRateApplicabilityRepository.class);

            societyMilkPurchaseRateBasedRepository = context.getBean(SocietyMilkPurchaseRateBasedRepository.class);

            societyMilkPurchaseRateRepository = context.getBean(SocietyMilkPurchaseRateRepository.class);

            localMilkSaleRateRepository = context.getBean(LocalMilkSaleRateRepository.class);

            plantRepository = context.getBean(PlantRepository.class);

            memberMilkPurchaseRateRepository = context.getBean(MemberMilkPurchaseRateRepository.class);
            appRepository = context.getBean(MemberMilkPurchaseRateApplicabilityRepository.class);
            dtlRepository = context.getBean(MemberMilkPurchaseRateDetailRepository.class);
            basedRepository = context.getBean(MemberMilkPurchaseRateBasedRepository.class);

            routeRepository = context.getBean(RouteRepository.class);

            memberMilkPurchaseRateApplicabilityRepository = context.getBean(MemberMilkPurchaseRateApplicabilityRepository.class);

            mccRepository = context.getBean(MccRepository.class);

            branchRepository = context.getBean(BranchRepository.class);

            bmcRepository = context.getBean(BmcRepository.class);

            dockMilkTypeRepository = context.getBean(DockMilkTypeRepository.class);

            bankRepository = context.getBean(BankRepository.class);

            memberDetailrepository = context.getBean(MemberDetailRepository.class);
            memberCreditLimitRepository = context.getBean(MemberCreditLimitRepository.class);
            memberCreditLimitTxnRepository = context.getBean(MemberCreditLimitTransactionRepository.class);
            collectionRepository = context.getBean(MilkCollectionRepository.class);

            saleRateRepository = context.getBean(ProductSaleRateRepository.class);

            productSaleRateRepository = context.getBean(ProductSaleRateRepository.class);

            restTemplate = context.getBean(RestTemplate.class);

            customerDetailrepository = context.getBean(CustomerDetailsRepository.class);
            mappingRepository = context.getBean(LedgerSubLedgerMappingRepository.class);

            summaryRepository = context.getBean(MemberBillSummaryRepository.class);
            billRepository = context.getBean(MemberBillRepository.class);
            transactionRepository = context.getBean(MemberBillTransactionRepository.class);
            billHeadRepository = context.getBean(BillHeadRepository.class);

            billCriteriaRepository = context.getBean(BillCriteriaRepository.class);
            producer = context.getBean(BroadcastedProducer.class);
            milkCollectionService = context.getBean(MilkCollectionService.class);
            broadcastedRepository = context.getBean(BroadcastedRepository.class);
            subscribedRepository = context.getBean(SubscribedRepository.class);
            logRepository = context.getBean(BroadcastedLogRepository.class);
            productRequisitionService = context.getBean(ProductRequisitionService.class);
            productRequisitionTransactionService = context.getBean(ProductRequisitionTransactionService.class);
            productDispatchTransactionService = context.getBean(ProductDispatchTransactionService.class);
            productDispatchService = context.getBean(ProductDispatchService.class);
            manualRequestRepository = context.getBean(ManualRequestRepository.class);
            dpuIncentiveRequestRepository = context.getBean(DpuIncentiveRequestRepository.class);
            allowDcsManualCollectionRangeRepository = context.getBean(AllowDcsManualCollectionRangeRepository.class);
            memberDetailRepository = context.getBean(MemberDetailRepository.class);
            dispatchRepository = context.getBean(MilkDispatchRepository.class);
            milkDispatchTransactionRepository = context.getBean(MilkDispatchTransactionRepository.class);
            productRequisitionRepository = context.getBean(ProductRequisitionRepository.class);
            productRequisitionTransactionRepository = context.getBean(ProductRequisitionTransactionRepository.class);
            productDispatchRepository = context.getBean(ProductDispatchRepository.class);
            customerRepository = context.getBean(CustomerRepository.class);
            milkQualityTypeRepository = context.getBean(MilkQualityTypeRepository.class);
            milkReceiptRepository = context.getBean(MilkReceiptRepository.class);
            milkReceiptTransactionRepository = context.getBean(MilkReceiptTransactionRepository.class);
            milkDispatchService = context.getBean(MilkDispatchService.class);

            navigationBookRepository = context.getBean(NavigationBookRepository.class);

            productRepository = context.getBean(ProductRepository.class);
            productPurchaseRateRepository = context.getBean(ProductPurchaseRateRepository.class);

            productGroupRepository = context.getBean(ProductGroupRepository.class);

            insuranceMasterRepository = context.getBean(InsuranceMasterRepository.class);
            insuranceDetailSummaryRepository = context.getBean(InsuranceDetailSummaryRepository.class);
            insuranceDetailRepository = context.getBean(InsuranceDetailRepository.class);

            rateTypeRepository = context.getBean(RateTypeRepository.class);

            milkClassRepository = context.getBean(MilkClassRepository.class);

            memberTypeRepository = context.getBean(MemberTypeRepository.class);

            voucherTypeRepository = context.getBean(VoucherTypeRepository.class);

            voucherTypeLedgerConfigRepository = context.getBean(VoucherTypeLedgerConfigRepository.class);

            taxRepository = context.getBean(TaxRepository.class);
            taxDetailRepository = context.getBean(TaxDetailRepository.class);
            subLedgerService = context.getBean(SubLedgerService.class);

            subLedgerLedgerConfigRepository = context.getBean(SubLedgerLedgerConfigRepository.class);

            staffSalaryProcessRepository = context.getBean(StaffSalaryProcessRepository.class);

            staffSalaryMappingRepository = context.getBean(StaffSalaryMappingRepository.class);

            staffSalaryRepository = context.getBean(StaffSalaryRepository.class);

            staffSalaryHeadRepository = context.getBean(StaffSalaryHeadRepository.class);

            staffMemberRepository = context.getBean(StaffMemberRepository.class);

            meetingAgendaRepository = context.getBean(MeetingAgendaRepository.class);
            momRepository = context.getBean(MomRepository.class);
            momActionRepository = context.getBean(MomActionRepository.class);

            ledgerRepository = context.getBean(LedgerRepository.class);
            ledgerSubLedgerMappingRepository = context.getBean(LedgerSubLedgerMappingRepository.class);
            ledgerService = context.getBean(LedgerService.class);

            ledgerMappingTaxDetailRepository = context.getBean(LedgerMappingTaxDetailRepository.class);

            ledgerMappingProductGroupRepository = context.getBean(LedgerMappingProductGroupRepository.class);

            unitConversionRepository = context.getBean(UnitConversionRepository.class);
            closingRepository = context.getBean(SocietyYearClosingRepository.class);

            ledgerMappingBillHeadRepository = context.getBean(LedgerMappingBillHeadRepository.class);

            ledgerGroupRepository = context.getBean(LedgerGroupRepository.class);
            typeRepository = context.getBean(LedgerTypeRepository.class);

            dockRepository = context.getBean(DockRepository.class);
            societyRepository = context.getBean(SocietyRepository.class);
            unionRepository = context.getBean(UnionRepository.class);
            identityRepository = context.getBean(IdentityRepository.class);
            subLedgerOpeningBalanceRepository = context.getBean(SubLedgerOpeningBalanceRepository.class);
            nextCodeService = context.getBean(NextCodeService.class);
            installmentRepository = context.getBean(ProductSaleInstallmentRepository.class);
            userRoleRepository = context.getBean(UserRoleRepository.class);
            rolePermissionRepository = context.getBean(RolePermissionRepository.class);
            ledgerOpeningBalanceRepository = context.getBean(LedgerOpeningBalanceRepository.class);
            memberRepository = context.getBean(MemberRepository.class);
            userRepository = context.getBean(UserRepository.class);
            nextCodeRepository = context.getBean(NextCodeRepository.class);

            milkCollectionRepository = context.getBean(MilkCollectionRepository.class);
            paymentCycleRepository = context.getBean(SocietyPaymentCycleRepository.class);
            hardwareRepository = context.getBean(HardwareDeviceConfigRepository.class);
            memberRateAppRepository = context.getBean(MemberMilkPurchaseRateApplicabilityRepository.class);
            shiftRepository = context.getBean(ShiftRepository.class);
            rateRepository = context.getBean(MemberMilkPurchaseRateRepository.class);
            milkQualityRepository = context.getBean(MilkQualityTypeRepository.class);
            milkTypeRepository = context.getBean(MilkTypeRepository.class);

            ledgerMappingEventRepository = context.getBean(LedgerMappingEventRepository.class);
            financialYearRepository = context.getBean(FinancialYearRepository.class);
            subLedgerRepository = context.getBean(SubLedgerRepository.class);
            voucherRepository = context.getBean(VoucherRepository.class);
            voucherTxnRepository = context.getBean(VoucherTransactionRepository.class);
            voucherSubLedgerRepository = context.getBean(VoucherSubLedgerRepository.class);

            notificationRepository = context.getBean(NotificationRepository.class);

            villageRepository = context.getBean(VillageRepository.class);
            subDistRepository = context.getBean(SubDistrictRepository.class);

            stateRepository = context.getBean(StateRepository.class);

            hamletRepository = context.getBean(HamletRepository.class);

            basicTaxRepository = context.getBean(BasicTaxRepository.class);

            cashAdvanceRepository = context.getBean(CashAdvanceRepository.class);

            districtRepository = context.getBean(DistrictRepository.class);

            committeeMembersRepository = context.getBean(CommitteeMembersRepository.class);
            socRepository = context.getBean(SocietyRepository.class);
            designationRepository = context.getBean(DesignationRepository.class);

            eventRepository = context.getBean(EventRepository.class);

            unitRepository = context.getBean(UnitRepository.class);

            societyMilkPurchaseRateDetailRepository = context.getBean(SocietyMilkPurchaseRateDetailRepository.class);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
