package com.eipl.amcs.operation.billing.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.repository.BillHeadRepository;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.operation.billing.repository.MemberBillRepository;
import com.eipl.amcs.operation.billing.repository.MemberBillSummaryRepository;
import com.eipl.amcs.operation.billing.repository.MemberBillTransactionRepository;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.repository.ProductSaleInstallmentRepository;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.VoucherUtil;
import com.udojava.evalex.Expression;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberBillServiceImpl implements MemberBillService {

    @Autowired
    private MemberBillSummaryRepository summaryRepository;
    @Autowired
    private MemberBillRepository billRepository;
    @Autowired
    private MemberBillTransactionRepository transactionRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDetailRepository memberDetailRepository;
    @Autowired
    private BillHeadRepository billHeadRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private SocietyRepository societyRepository;
    @Autowired
    private ProductSaleInstallmentRepository installmentRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    @Autowired
    private LedgerMappingBillHeadRepository ledgerMappingBillHeadRepository;
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

    @Override
    @Transactional
    public List<MemberBillSummary> findMemberBillSummaryBetWeen(LocalDate fromDate, LocalDate toDate) {
        List<SocietyPaymentCycle> list = paymentCycleRepository.findByFromDateBetween(
                LocalDateTime.of(fromDate, LocalTime.MIN), LocalDateTime.of(toDate, LocalTime.MAX));

        List<MemberBillSummary> memberBillSummaryList = summaryRepository.findAllByOrderByPaymentCycleDesc();
//        for (MemberBillSummary memberBillSummary : memberBillSummaryList) {
//            memberBillSummary.setPaymentCycle(Hibernate.unproxy(memberBillSummary.getPaymentCycle(), SocietyPaymentCycle.class));
//        }
        return memberBillSummaryList;
    }

    @Override
    @Transactional
    public List<MemberBillSummary> findMemberBillSummaryBetWeenFromDateAndToDate(LocalDate fromDate, LocalDate toDate) {
        List<SocietyPaymentCycle> list = paymentCycleRepository.findByFromDateBetween(
                LocalDateTime.of(fromDate, LocalTime.MIN), LocalDateTime.of(toDate, LocalTime.MAX));

        List<MemberBillSummary> memberBillSummaryList = summaryRepository.findByPaymentCycleIn(list);
//        for (MemberBillSummary memberBillSummary : memberBillSummaryList) {
//            memberBillSummary.setPaymentCycle(Hibernate.unproxy(memberBillSummary.getPaymentCycle(), SocietyPaymentCycle.class));
//        }
        return memberBillSummaryList;
    }


    @Override
    public MemberBillSummary findMemberBillSummary(SocietyPaymentCycle paymentCycle) {
        return summaryRepository.findByPaymentCycle(paymentCycle)
                .orElseThrow(() -> new EntityNotFoundException(MemberBillSummary.class, "invalid.paymentcycle"));
    }

    @Override
    public MemberBillSummary checkTableData(SocietyPaymentCycle paymentCycle) {
        Optional<MemberBillSummary> obj = summaryRepository.findByPaymentCycle(paymentCycle);
        if (obj.isPresent())
            return obj.get();
        return null;
    }

    @Override
    public List<MemberBill> fetchTableData(SocietyPaymentCycle paymentCycle) {
        List<MemberBill> list = billRepository.findByPaymentCycle(paymentCycle);
        for (MemberBill a : list) {
            a.setUnion(Hibernate.unproxy(a.getUnion(), Union.class));
            a.setSociety(Hibernate.unproxy(a.getSociety(), Society.class));
            a.setPaymentCycle(paymentCycle);
        }
        return list;
    }

    @Override
    public List<MemberBillTransaction> findMemberBillTransaction(MemberBill memberBill) {
        return transactionRepository.findByMemberBill(memberBill);
    }

    @Override
    @Transactional
    public List<MemberBill> findMemberBill(String societyCode, SocietyPaymentCycle paymentCycle,
                                           SocietyPaymentCycle prevPaymentCycle) {
        List<String> duplicate = new ArrayList<>();
        List<Map<String, Object>> spResult = transactionRepository.findBillTransaction(paymentCycle.getCode(),
                paymentCycle.getCode(), paymentCycle.getFromDate(), paymentCycle.getToDate(), 1, societyCode, "SYS");

        if (spResult == null || spResult.isEmpty())
            return null;

        List<Member> memberList = memberRepository.findAll();
        List<MemberDetail> memberDetailList = memberDetailRepository.findAll();
        List<BillHead> headList = billHeadRepository.findAll();
        List<MemberBill> memberBillList = new ArrayList<>();
        List<MemberBillTransaction> billTransactionList = new ArrayList<>();

        Society society = societyRepository.findById(societyCode).get();

        String billCode = nextCodeService.getNextCode("MemberBill", "code", societyCode, 1);
        for (Map<String, Object> map : spResult) {
            Member member = memberList.stream().filter(p -> map.get("member_code").toString().equals(p.getCode()))
                    .findAny().orElse(null);
            if (duplicate.contains(member.getCode()))
                continue;
            MemberBill mb = new MemberBill();
            if (member.getxCol1() != null && !member.getxCol1().equalsIgnoreCase("")) {
                String second = member.getxCol1();
                duplicate.add(second);
                if (second != null && !second.equalsIgnoreCase("") && !second.equalsIgnoreCase(member.getCode())) {
                    for (Map<String, Object> map1 : spResult) {
                        if (second.equalsIgnoreCase(map1.get("member_code").toString())) {
                            mb.setMilkAmount(new BigDecimal(map1.get("amount").toString()));
                            mb.setMilkQty(new BigDecimal(map1.get("quantity").toString()));
                            mb.setLoanAmount(new BigDecimal(map1.get("loan_amount").toString()));
                            mb.setProductSaleAmount(new BigDecimal(map1.get("ps_amount").toString()));
                            mb.setLocalSaleAmount(new BigDecimal(map1.get("ls_amount").toString()));
                        }
                    }
                }
            }
            System.out.println();
            MemberDetail memberDetail = memberDetailList.stream().filter(p -> map.get("member_code").toString().equals(p.getCode()))
                    .findAny().orElse(null);
            member.setMemberType(Hibernate.unproxy(member.getMemberType(), MemberType.class));
            member.setSociety(Hibernate.unproxy(member.getSociety(), Society.class));
            member.setMilkType(Hibernate.unproxy(member.getMilkType(), MilkType.class));
            mb.setCode(billCode);
            mb.setMember(member);
            mb.setPaymentMode(memberDetail.getPaymentMode() != null ? memberDetail.getPaymentMode() : 0);
            mb.setStatus((short) 1);
            mb.setPaymentCycle(paymentCycle);
            mb.setSociety(society);
            mb.setUnion(society.getUnion());
            mb.setMilkAmount(new BigDecimal(map.get("amount").toString()));
            mb.setMilkQty(new BigDecimal(map.get("quantity").toString()));
            if (map.get("avg_fat") != null) {
                mb.setAvgFat(new BigDecimal(map.get("avg_fat").toString()));
            } else {
                mb.setAvgFat(new BigDecimal(5));
            }
            if (map.get("avg_snf") != null) {
                mb.setAvgSnf(new BigDecimal(map.get("avg_snf").toString()));
            } else {
                mb.setAvgSnf(new BigDecimal("0"));
            }
            mb.setAvgClr(BigDecimal.ZERO);
            mb.setKgFat(BigDecimal.ZERO);
            mb.setKgSnf(BigDecimal.ZERO);
            mb.setLocalSaleAmount(new BigDecimal(map.get("ls_amount").toString()));
            mb.setProductSaleAmount(new BigDecimal(map.get("ps_amount").toString()));
            mb.setLoanAmount(new BigDecimal(map.get("loan_amount").toString()));
            mb.setDisbursed(false);
            mb.setBankAcno(map.get("account_no") != null ? map.get("account_no").toString() : null);
            mb.setIfsc(map.get("ifsc") != null ? map.get("ifsc").toString() : null);

            BillHead billHeadNetPay = null;
            BigDecimal netPay = BigDecimal.ZERO;
            BigDecimal otherAdd = BigDecimal.ZERO;
            BigDecimal otherDed = BigDecimal.ZERO;
            for (BillHead billHead : headList) {
                switch (billHead.getCode()) {
                    case "101":
                        if (mb.getMilkAmount().compareTo(BigDecimal.ZERO) > 0) {
                            MemberBillTransaction txnMa = createMemberBillTxn(mb, billHead, mb.getMilkAmount(), (short) 1,
                                    mb.getSociety().getCode());
                            billTransactionList.add(txnMa);
                            netPay = netPay.add(mb.getMilkAmount());
                        }
                        break;
                    case "102":
                        if (mb.getProductSaleAmount().compareTo(BigDecimal.ZERO) > 0) {
                            MemberBillTransaction txnPs = createMemberBillTxn(mb, billHead, mb.getProductSaleAmount(),
                                    (short) 2, mb.getSociety().getCode());
                            billTransactionList.add(txnPs);
                            netPay = netPay.subtract(mb.getProductSaleAmount());
                        }
                        break;
                    case "103":
                        if (mb.getLocalSaleAmount().compareTo(BigDecimal.ZERO) > 0) {
                            MemberBillTransaction txnLs = createMemberBillTxn(mb, billHead, mb.getLocalSaleAmount(),
                                    (short) 2, mb.getSociety().getCode());
                            billTransactionList.add(txnLs);
                            netPay = netPay.subtract(mb.getLocalSaleAmount());
                        }
                        break;
                    case "104":
                        if (mb.getLoanAmount().compareTo(BigDecimal.ZERO) > 0) {
                            MemberBillTransaction txnLa = createMemberBillTxn(mb, billHead, mb.getLoanAmount(), (short) 2,
                                    mb.getSociety().getCode());
                            billTransactionList.add(txnLa);
                            netPay = netPay.subtract(mb.getLoanAmount());
                        }
                        break;
                    case "105":
                        billHeadNetPay = billHead;
                        break;
                    default:
                        String formula = billHead.getxCol1() != null ? billHead.getxCol1() : billHead.getXCol1();
                        if (formula != null && !formula.isEmpty()) {
                            formula = formula.replace("AMT", mb.getMilkAmount().toString());
                            formula = formula.replace("QTY", mb.getMilkQty().toString());
                            formula = formula.replace("FAT", mb.getAvgFat().toString());
                            formula = formula.replace("SNF", mb.getAvgSnf().toString());
                            try {
                                Expression exp = new Expression(formula);
                                BigDecimal val = exp.eval();

                                MemberBillTransaction txn = createMemberBillTxn(mb, billHead, val,
                                        billHead.getHeadType() == (short) 1 ? (short) 1 : (short) 2,
                                        mb.getSociety().getCode());
                                txn.setFormula(formula);
                                billTransactionList.add(txn);
                                if (billHead.getHeadType() == (short) 1) {
                                    netPay = netPay.add(val);
                                    otherAdd = otherAdd.add(val);
                                } else {
                                    netPay = netPay.subtract(val);
                                    otherDed = otherDed.add(val);
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                }
            }
            mb.setOtherAddAmount(otherAdd.setScale(2, RoundingMode.FLOOR));
            mb.setOtherDedAmount(otherDed.setScale(2, RoundingMode.FLOOR));

            // Net pay
            MemberBillTransaction txn = createMemberBillTxn(mb, billHeadNetPay, netPay, (short) 1,
                    mb.getSociety().getCode());
            billTransactionList.add(txn);
            mb.setNetAmount(netPay.setScale(2, RoundingMode.HALF_UP));

            memberBillList.add(mb);
            // bill code setup for next
            billCode = societyCode + (Integer.parseInt(billCode.replace(societyCode, "")) + 1);
        }

        // Member bill summary
        BigDecimal loanAmount = BigDecimal.ZERO;
        BigDecimal localSaleAmount = BigDecimal.ZERO;
        BigDecimal productSaleAmount = BigDecimal.ZERO;
        BigDecimal milkAmount = BigDecimal.ZERO;
        BigDecimal netAmount = BigDecimal.ZERO;
        BigDecimal milkQty = BigDecimal.ZERO;
        BigDecimal otherAdd = BigDecimal.ZERO;
        BigDecimal otherDed = BigDecimal.ZERO;
        for (MemberBill memberBill : memberBillList) {
            loanAmount = loanAmount.add(memberBill.getLoanAmount());
            localSaleAmount = localSaleAmount.add(memberBill.getLocalSaleAmount());
            productSaleAmount = productSaleAmount.add(memberBill.getProductSaleAmount());
            milkAmount = milkAmount.add(memberBill.getMilkAmount());
            netAmount = netAmount.add(memberBill.getNetAmount());
            otherAdd = otherAdd.add(memberBill.getOtherAddAmount());
            otherDed = otherDed.add(memberBill.getOtherDedAmount());
            if (memberBill.getMilkQty() != null) {
                milkQty = milkQty.add(memberBill.getMilkQty());
            }
        }
        MemberBillSummary mbs = new MemberBillSummary();
        String summaryCode = nextCodeService.getNextCode("MemberBillSummary", "code", societyCode, 1);
        mbs.setCode(summaryCode);
        mbs.setLoanAmount(loanAmount);
        mbs.setMilkAmount(milkAmount);
        mbs.setLocalSaleAmount(localSaleAmount);
        mbs.setProductSaleAmount(productSaleAmount);
        mbs.setNetAmount(netAmount);
        mbs.setMilkQty(milkQty);
        mbs.setPaymentCycle(paymentCycle);
        mbs.setOtherAddAmount(otherAdd);
        mbs.setOtherDedAmount(otherDed);
        mbs.setInitData();

        // save data
        summaryRepository.save(mbs);
        List<MemberBill> list = billRepository.saveAll(memberBillList);
        transactionRepository.saveAll(billTransactionList);

        for (MemberBill memberBill : list) {
            memberBill.setUnion(Hibernate.unproxy(memberBill.getUnion(), Union.class));
            memberBill.setSociety(Hibernate.unproxy(memberBill.getSociety(), Society.class));
        }
        return list;
    }

    private MemberBillTransaction createMemberBillTxn(MemberBill bill, BillHead billhead, BigDecimal amount, short type,
                                                      String societyCode) {
        MemberBillTransaction txn = new MemberBillTransaction();
        txn.setMemberBill(bill);
        txn.setCode(bill.getCode() + "-" + billhead.getCode());
        txn.setAmount(amount);
        txn.setType(type);
        txn.setUnionCode("101");
        txn.setSocietyCode(societyCode);
        txn.setAdjustment(amount);
        txn.setBillHead(billhead);
        txn.setPrevDue(BigDecimal.ZERO);
        txn.setDue(BigDecimal.ZERO);
        return txn;
    }

    @Override
    public MemberBill save(MemberBill memberBill) {
        memberBill.setUnion(Hibernate.unproxy(memberBill.getUnion(), Union.class));
        memberBill.setMember(Hibernate.unproxy(memberBill.getMember(), Member.class));
        memberBill.setPaymentCycle(Hibernate.unproxy(memberBill.getPaymentCycle(), SocietyPaymentCycle.class));
        memberBill.setSociety(Hibernate.unproxy(memberBill.getSociety(), Society.class));

        return billRepository.save(memberBill);
    }

    @Override
    @Transactional
    public List<MemberBillTransaction> saveTrans(List<MemberBillTransaction> list) {
        MemberBill mb = billRepository.findById(list.get(0).getMemberBill().getCode()).get();

        BigDecimal oldPS = mb.getProductSaleAmount();
        BigDecimal oldLS = mb.getLocalSaleAmount();
        BigDecimal oldLA = mb.getLoanAmount();
        BigDecimal oldOthAdd = mb.getOtherAddAmount();
        BigDecimal oldOthDed = mb.getOtherDedAmount();

        BigDecimal otherAdd = BigDecimal.ZERO;
        BigDecimal otherDed = BigDecimal.ZERO;
        BigDecimal netAmt = BigDecimal.ZERO;
        for (MemberBillTransaction memberBillTransaction : list) {
            memberBillTransaction.setupdateData();
            switch (memberBillTransaction.getBillHead().getCode()) {
                case "101":
                    netAmt = netAmt.add(memberBillTransaction.getAdjustment());
                    break;
                case "102":
                    mb.setProductSaleAmount(memberBillTransaction.getAdjustment());
                    netAmt = netAmt.subtract(memberBillTransaction.getAdjustment());
                    break;
                case "103":
                    mb.setLocalSaleAmount(memberBillTransaction.getAdjustment());
                    netAmt = netAmt.subtract(memberBillTransaction.getAdjustment());
                    break;
                case "104":
                    mb.setLoanAmount(memberBillTransaction.getAdjustment());
                    netAmt = netAmt.subtract(memberBillTransaction.getAdjustment());
                    break;
                case "105":
                    break;
                default:
                    if (memberBillTransaction.getBillHead().getHeadType() == (short) 0) {
                        otherDed = otherDed.add(memberBillTransaction.getAdjustment());
                        netAmt = netAmt.subtract(memberBillTransaction.getAdjustment());
                    } else {
                        otherAdd = otherAdd.add(memberBillTransaction.getAdjustment());
                        netAmt = netAmt.add(memberBillTransaction.getAdjustment());
                    }
                    break;
            }
            transactionRepository.save(memberBillTransaction);
        }
        mb.setOtherAddAmount(otherAdd.setScale(2, RoundingMode.HALF_UP));
        mb.setOtherDedAmount(otherDed.setScale(2, RoundingMode.HALF_UP));

        mb.setNetAmount(netAmt.setScale(2, RoundingMode.HALF_UP));
        billRepository.save(mb);

        MemberBillSummary summary = summaryRepository.findByPaymentCycle(mb.getPaymentCycle()).get();
        summary.setProductSaleAmount(summary.getProductSaleAmount().subtract(oldPS).add(mb.getProductSaleAmount())
                .setScale(2, RoundingMode.HALF_UP));
        summary.setLocalSaleAmount(summary.getLocalSaleAmount().subtract(oldLS).add(mb.getLocalSaleAmount()).setScale(2,
                RoundingMode.HALF_UP));
        summary.setLoanAmount(
                summary.getLoanAmount().subtract(oldLA).add(mb.getLoanAmount()).setScale(2, RoundingMode.HALF_UP));
        summary.setOtherDedAmount(summary.getOtherDedAmount().subtract(oldOthDed).add(mb.getOtherDedAmount())
                .setScale(2, RoundingMode.HALF_UP));
        summary.setOtherAddAmount(summary.getOtherAddAmount().subtract(oldOthAdd).add(mb.getOtherAddAmount())
                .setScale(2, RoundingMode.HALF_UP));
        summary.setNetAmount(summary.getMilkAmount().subtract(summary.getLoanAmount()).subtract(summary.getLocalSaleAmount()).subtract(summary.getProductSaleAmount()).subtract(summary.getOtherDedAmount().add(summary.getOtherAddAmount())));
        summaryRepository.save(summary);
        return list;
    }

    @Override
    public MemberBillTransaction save(MemberBillTransaction memberBillTransaction) {
        return transactionRepository.save(memberBillTransaction);
    }

    @Override
    public MemberBillSummary save(MemberBillSummary memberBillSummary) {
        return summaryRepository.save(memberBillSummary);
    }

    @Override
    @Transactional
    public Boolean finalize(SocietyPaymentCycle paymentCycle, List<String> memberList) {
        List<MemberBill> memberBillList = billRepository.findByPaymentCycle(paymentCycle);
        for (MemberBill memberBill : memberBillList) {
            if (!memberList.contains(memberBill.getMember().getCode()))
                continue;

            memberBill.setStatus((short) 2);

            // Product sale installment adjustment
            if (memberBill.getProductSaleAmount().compareTo(BigDecimal.ZERO) >= 0) {
                List<MemberBillTransaction> txns = transactionRepository.findByMemberBill(memberBill);
                MemberBillTransaction txnPs = txns.stream().filter(p -> p.getBillHead().getCode().equals("102"))
                        .findFirst().orElse(null);
                if (txnPs != null && !txnPs.getAmount().equals(txnPs.getAdjustment())) {
                    List<ProductSaleInstallment> unpaidInstallments = installmentRepository
                            .findByMemberAndBillingFalseAndType(memberBill.getMember(), 1);
                    List<ProductSaleInstallment> currentInstallments = unpaidInstallments.stream()
                            .filter(p -> p.getSocietyPaymentCycle().getCode().equals(paymentCycle.getCode()))
                            .collect(Collectors.toList());
                    BigDecimal totalAmt = BigDecimal.ZERO;
                    for (ProductSaleInstallment inst : currentInstallments)
                        totalAmt = totalAmt.add(inst.getInstallmentAmount()).add(inst.getPreviousPendingAmount());
                    totalAmt = totalAmt.setScale(2, RoundingMode.HALF_UP);
                    if (txnPs.getAdjustment().compareTo(totalAmt) > 0) {
                        // TODO Paid more than inst amount will cover later
                    } else {
                        BigDecimal temp = BigDecimal.valueOf(txnPs.getAdjustment().doubleValue());
                        for (int i = 0; i < currentInstallments.size(); i++) {
                            ProductSaleInstallment inst = currentInstallments.get(i);
                            BigDecimal bg = inst.getInstallmentAmount().add(inst.getPreviousPendingAmount());
                            BigDecimal ps_amount = txnPs.getAdjustment().multiply(bg).divide(totalAmt, RoundingMode.HALF_UP)
                                    .setScale(2, RoundingMode.HALF_UP);
                            if (i > 0 && i == currentInstallments.size() - 1)
                                ps_amount = temp;
                            if (ps_amount.compareTo(bg) < 0) {
                                BigDecimal dueForNext = inst.getInstallmentAmount().subtract(ps_amount).setScale(2, RoundingMode.HALF_UP);
                                if (!dueForNext.equals(BigDecimal.valueOf(0))) {
                                    ProductSaleInstallment nextInst = unpaidInstallments.stream()
                                            .filter(p -> p.getInvoiceNo().equals(inst.getInvoiceNo())
                                                    && !p.getSocietyPaymentCycle().getCode()
                                                    .equals(inst.getSocietyPaymentCycle().getCode()))
                                            .findFirst().orElse(null);
                                    if (nextInst == null) {
                                        LocalDateTime dt = LocalDateTime
                                                .of(paymentCycle.getToDate().toLocalDate().plusDays(3), LocalTime.NOON);
                                        SocietyPaymentCycle nextPaymentCycle = paymentCycleRepository
                                                .findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(dt, dt);
                                        long cnt = unpaidInstallments.stream()
                                                .filter(p -> p.getInvoiceNo().equals(inst.getInvoiceNo())).count();
                                        nextInst = new ProductSaleInstallment();
                                        nextInst.setActualInstallment(dueForNext);
                                        nextInst.setType(1);
                                        nextInst.setBilling(false);
                                        nextInst.setPreviousPendingAmount(BigDecimal.ZERO);
                                        nextInst.setInstallmentAmount(dueForNext);
                                        nextInst.setInvoiceNo(inst.getInvoiceNo());
                                        nextInst.setDeductionDate(nextPaymentCycle.getToDate().toLocalDate());
                                        nextInst.setUnionCode(inst.getUnionCode());
                                        nextInst.setSocietyCode(inst.getSocietyCode());
                                        nextInst.setSocietyPaymentCycle(nextPaymentCycle);
                                        nextInst.setMember(inst.getMember());
                                        nextInst.setCode(inst.getInvoiceNo() + "-" + (Integer.parseInt(inst.getCode().split("-")[3]) + 1));
                                        nextInst.setInitData();
                                        installmentRepository.save(nextInst);
                                    } else {
                                        nextInst.setPreviousPendingAmount(dueForNext);
                                        nextInst.setupdateData();
                                        installmentRepository.save(nextInst);
                                    }
                                }
                            }
                            inst.setBilling(true);
                            inst.setupdateData();
                            installmentRepository.save(inst);
                            temp = temp.subtract(ps_amount).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                }
            }


            // cash ad
            if (memberBill.getLoanAmount().compareTo(BigDecimal.ZERO) >= 0) {
                List<MemberBillTransaction> txns = transactionRepository.findByMemberBill(memberBill);
                MemberBillTransaction txnPs = txns.stream().filter(p -> p.getBillHead().getCode().equals("104"))
                        .findFirst().orElse(null);
                if (txnPs != null && !txnPs.getAmount().equals(txnPs.getAdjustment())) {
                    List<ProductSaleInstallment> unpaidInstallments = installmentRepository
                            .findByMemberAndBillingFalseAndType(memberBill.getMember(), 3);
                    List<ProductSaleInstallment> currentInstallments = unpaidInstallments.stream()
                            .filter(p -> p.getSocietyPaymentCycle().getCode().equals(paymentCycle.getCode()))
                            .collect(Collectors.toList());
                    BigDecimal totalAmt = BigDecimal.ZERO;
                    for (ProductSaleInstallment inst : currentInstallments)
                        totalAmt = totalAmt.add(inst.getInstallmentAmount()).add(inst.getPreviousPendingAmount());
                    totalAmt = totalAmt.setScale(2, RoundingMode.HALF_UP);
                    if (txnPs.getAdjustment().compareTo(totalAmt) > 0) {
                        // TODO Paid more than inst amount will cover later
                    } else {
                        BigDecimal temp = BigDecimal.valueOf(txnPs.getAdjustment().doubleValue());
                        for (int i = 0; i < currentInstallments.size(); i++) {
                            ProductSaleInstallment inst = currentInstallments.get(i);
                            BigDecimal bg = inst.getInstallmentAmount().add(inst.getPreviousPendingAmount());
                            BigDecimal cs_amount = txnPs.getAdjustment().multiply(bg).divide(totalAmt, RoundingMode.HALF_UP)
                                    .setScale(2, RoundingMode.HALF_UP);
                            if (i > 0 && i == currentInstallments.size() - 1)
                                cs_amount = temp;
                            if (cs_amount.compareTo(bg) < 0) {
                                BigDecimal dueForNext = inst.getInstallmentAmount().subtract(cs_amount).setScale(2, RoundingMode.HALF_UP);
                                if (!dueForNext.equals(BigDecimal.valueOf(0))) {
                                    ProductSaleInstallment nextInst = unpaidInstallments.stream()
                                            .filter(p -> p.getInvoiceNo().equals(inst.getInvoiceNo())
                                                    && !p.getSocietyPaymentCycle().getCode()
                                                    .equals(inst.getSocietyPaymentCycle().getCode()))
                                            .findFirst().orElse(null);
                                    if (nextInst == null) {
                                        LocalDateTime dt = LocalDateTime
                                                .of(paymentCycle.getToDate().toLocalDate().plusDays(3), LocalTime.NOON);
                                        SocietyPaymentCycle nextPaymentCycle = paymentCycleRepository
                                                .findTop1ByFromDateLessThanEqualAndToDateGreaterThanEqual(dt, dt);
                                        long cnt = unpaidInstallments.stream()
                                                .filter(p -> p.getInvoiceNo().equals(inst.getInvoiceNo())).count();
                                        nextInst = new ProductSaleInstallment();
                                        nextInst.setActualInstallment(dueForNext);
                                        nextInst.setType(3);
                                        nextInst.setBilling(false);
                                        nextInst.setPreviousPendingAmount(BigDecimal.ZERO);
                                        nextInst.setInstallmentAmount(dueForNext);
                                        nextInst.setInvoiceNo(inst.getInvoiceNo());
                                        nextInst.setDeductionDate(nextPaymentCycle.getToDate().toLocalDate());
                                        nextInst.setUnionCode(inst.getUnionCode());
                                        nextInst.setSocietyCode(inst.getSocietyCode());
                                        nextInst.setSocietyPaymentCycle(nextPaymentCycle);
                                        nextInst.setMember(inst.getMember());
                                        nextInst.setCode(inst.getInvoiceNo() + "-" + (Integer.parseInt(inst.getCode().split("-")[1]) + 1));
                                        nextInst.setInitData();
                                        installmentRepository.save(nextInst);
                                    } else {
                                        nextInst.setPreviousPendingAmount(dueForNext);
                                        nextInst.setupdateData();
                                        installmentRepository.save(nextInst);
                                    }
                                }
                            }
                            inst.setBilling(true);
                            inst.setupdateData();
                            installmentRepository.save(inst);
                            temp = temp.subtract(cs_amount).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                }
            }
        }
        billRepository.saveAll(memberBillList);

        paymentCycle.setLockBillingProcess(true);
        paymentCycleRepository.customUpdate(paymentCycle, "");
        List<MemberBill> mb = billRepository.findByPaymentCycle(paymentCycle);
        for (MemberBill memberBill : mb) {
            memberBill.setStatus((short) 2);
        }
        List<ProductSaleInstallment> list = installmentRepository.findBySocietyPaymentCycle(paymentCycle);
        for (ProductSaleInstallment productSaleInstallment : list) {
            productSaleInstallment.setBilling(true);
            installmentRepository.customUpdate(productSaleInstallment, "");
        }
        return true;
    }

    @Override
    public Boolean disburse(SocietyPaymentCycle paymentCycle, List<String> memberList, Bank bank, String identityHeader) {
        paymentCycle.setBilling(true);
        paymentCycleRepository.customUpdate(paymentCycle, "");
        List<MemberBill> memberBillList = billRepository.findByPaymentCycle(paymentCycle);
        MemberBillSummary memberBillSummary = summaryRepository.findByPaymentCycle(paymentCycle).get();
        if (bank != null)
            memberBillSummary.setBank(bank);

        String voucherNo = nextCodeRepository.getNextCode("Voucher", "code", memberBillList.get(0).getSociety().getCode(), 1);
        BigDecimal disburseAmount = BigDecimal.ZERO;
        for (MemberBill memberBill : memberBillList) {
            if (!memberList.contains(memberBill.getMember().getCode())) {
                continue;
            }

            // fetch data and update
            memberBill.setStatus((short) 6);
            memberBill.setDisbursed(true);
            memberBill.setDisbursedDate(LocalDate.now());
            memberBill.setUnion(Hibernate.unproxy(memberBill.getUnion(), Union.class));
            memberBill.setMember(Hibernate.unproxy(memberBill.getMember(), Member.class));
            memberBill.setPaymentCycle(Hibernate.unproxy(memberBill.getPaymentCycle(), SocietyPaymentCycle.class));
            memberBill.setSociety(Hibernate.unproxy(memberBill.getSociety(), Society.class));

            // create voucher
            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(memberBill.getDisbursedDate());

            voucherNo = nextCodeService.getNextCode("Voucher", "code",
                    memberBill.getSociety().getCode() + "/" + financialYear.get().getCode() + "/", 0);
            memberBill.setVoucherNo(voucherNo);
            createVoucher(memberBill, ledgerMappingBillHeadRepository.findAll(Sort.by("code")), identityHeader, voucherNo, bank);
            disburseAmount = disburseAmount.add(memberBill.getNetAmount());
            billRepository.save(memberBill);
        }
        memberBillSummary.setDisbursedAmount(disburseAmount);
        memberBillSummary.setStatus((short) 6);
        summaryRepository.save(memberBillSummary);
        return true;
    }

    private String createVoucher(MemberBill memberBill, List<LedgerMappingBillHead> ledgerMappingBillHeads, String identityInfo, String voucherNo, Bank bank) {
        try {
            if (ledgerMappingBillHeads == null || ledgerMappingBillHeads.isEmpty())
                return null;
//            if (ledgerMappingBillHeads.stream().anyMatch(e -> e.getXCol1().equalsIgnoreCase("0"))) return null;

            List<LedgerMappingEvent> eventsList = ledgerMappingEventRepository.findByEventcode(AppConstant.EventCode.MEMBER_BILL);
            if (eventsList == null || eventsList.isEmpty())
                return null;

            Optional<FinancialYear> financialYear = financialYearRepository.findCurrentFinancialYear(memberBill.getDisbursedDate());

            if (voucherNo == null)
                return null;

            Voucher voucher = VoucherUtil.getVoucherInstance(voucherNo, null, memberBill.getDisbursedDate(),
                    memberBill.getDisbursedDate(), "Member Bill Auto Posting For: " + memberBill.getMember().getCode(),
                    eventsList.get(0).getVoucherType(), financialYear.isPresent() ? financialYear.get().getCode() : null,
                    memberBill.getSociety(), memberBill.getUnion().getCode(), null);

            voucher.setVoucherTransactions(new ArrayList<>());
            int cc = 1, ss = 1;
            // Debit Txn
            LedgerMappingEvent ledgerMapping = null;
            // cash
            if (memberBill.getPaymentMode() == 0) {
                ledgerMapping = eventsList.stream().filter(p -> p.getEvents().getCode() == 20)
                        .findFirst().orElse(null);
            } else {
                ledgerMapping = eventsList.stream().filter(p -> p.getEvents().getCode() == 21)
                        .findFirst().orElse(null);
            }

            Ledger creditLedger = new Ledger();
            if (memberBill.getPaymentMode() == (short) 1 && bank != null) {
                creditLedger = bank.getLedger();
            } else {
                if (ledgerMapping != null) {
                    creditLedger = ledgerMapping.getCreditLedger();
                }
            }

            VoucherTransaction debitTxn = VoucherUtil.getVoucherTxn(voucher, memberBill.getNetAmount(), true,
                    creditLedger,
                    "Net Payable Amount of Rs: " + memberBill.getNetAmount(), String.valueOf(cc));
            if (debitTxn != null)
                voucher.getVoucherTransactions().add(debitTxn);

            // Credit/debit Txn from event ledger mapping
            List<MemberBillTransaction> txn = transactionRepository.findByMemberBillAndBillHead_CodeNot(memberBill, "105");
            for (MemberBillTransaction memberBillTransaction : txn) {
                BigDecimal amt = null;
                if ("101".equals(memberBillTransaction.getBillHead().getCode())) {
                    amt = memberBill.getMilkAmount();
                } else if ("102".equals(memberBillTransaction.getBillHead().getCode())) {
                    amt = memberBill.getProductSaleAmount();
                } else if ("103".equals(memberBillTransaction.getBillHead().getCode())) {
                    amt = memberBill.getLocalSaleAmount();
                } else if ("104".equals(memberBillTransaction.getBillHead().getCode())) {
                    amt = memberBill.getLoanAmount();
                } else if ("105".equals(memberBillTransaction.getBillHead().getCode())) {
                    amt = memberBill.getNetAmount();
                } else {
                    amt = memberBillTransaction.getAdjustment();
                }

                LedgerMappingBillHead ledgerMappingBillHead = ledgerMappingBillHeads.stream()
                        .filter(p -> p.getBillHead().getCode().equals(memberBillTransaction.getBillHead().getCode()))
                        .findFirst().orElse(null);
                if (ledgerMappingBillHead == null || ledgerMappingBillHead.getLedger() == null)
                    continue;
                cc = cc + 1;
                VoucherTransaction vTxn = VoucherUtil.getVoucherTxn(voucher, amt, ledgerMappingBillHead.getCreditDebit(),
                        ledgerMappingBillHead.getLedger(), memberBillTransaction.getBillHead().getName() + " of Amount: " + amt.toString()
                        , String.valueOf(cc));
                voucher.getVoucherTransactions().add(vTxn);
                if (ledgerMappingBillHead.getHasSubLedger()) {
                    VoucherSubLedger voucherSubLedger = null;
                    Optional<SubLedger> subLedger = subLedgerRepository.findByTypeAndReferenceCode(AppConstant.SubLedgerType.MEMBER, memberBill.getMember().getCode());
                    if (subLedger.isPresent()) {
                        vTxn.setVoucherSubLedgers(new ArrayList<>());
                        voucherSubLedger = VoucherUtil.getVoucherSubLedger(voucher, vTxn, String.valueOf(ss), amt, ledgerMappingBillHead.getCreditDebit(),
                                memberBillTransaction.getBillHead().getName() + " of Amount: " + amt, subLedger.get());
                        if (voucherSubLedger != null)
                            vTxn.getVoucherSubLedgers().add(voucherSubLedger);
                        ss++;
                    }
                }
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
            return voucherNo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
