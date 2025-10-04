package com.eipl.amcs.operation.billing.service;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;

import java.time.LocalDate;
import java.util.List;

public interface MemberBillService {

    List<MemberBillSummary> findMemberBillSummaryBetWeen(LocalDate fromDate, LocalDate toDate);


    List<MemberBillSummary> findMemberBillSummaryBetWeenFromDateAndToDate(LocalDate fromDate, LocalDate toDate);

    MemberBillSummary findMemberBillSummary(SocietyPaymentCycle paymentCycle);

    List<MemberBill> findMemberBill(String societyCode, SocietyPaymentCycle paymentCycle, SocietyPaymentCycle prevPaymentCycle);

    List<MemberBillTransaction> findMemberBillTransaction(MemberBill memberBill);

//	List<MemberBill> callSp(String societyCode,SocietyPaymentCycle paymentCycle);

    MemberBill save(MemberBill memberBill);

    MemberBillTransaction save(MemberBillTransaction memberBillTransaction);

    MemberBillSummary save(MemberBillSummary memberBillSummary);

    MemberBillSummary checkTableData(SocietyPaymentCycle paymentCycle);

    List<MemberBill> fetchTableData(SocietyPaymentCycle paymentCycle);


    Boolean finalize(SocietyPaymentCycle paymentCycle, List<String> memberList);

    Boolean disburse(SocietyPaymentCycle paymentCycle, List<String> memberList, String identityHeader);

    List<MemberBillTransaction> saveTrans(List<MemberBillTransaction> list);

}
