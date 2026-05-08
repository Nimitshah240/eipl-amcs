package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherService {
    List<VoucherDto> findAll();

    List<VoucherDto> findAllBetweenDates(LocalDate fromDate, LocalDate toDate);

    //List<VoucherDto> findAllBetweenDates(String societyCode, LocalDate fromDate, LocalDate toDate);
    List<VoucherTransaction> findAllTransaction(Voucher voucher);

    List<VoucherSubLedger> findAllVoucherSubLedger(VoucherTransaction voucherTransaction);

    Voucher save(VoucherDto dto, String identityInfo);

    Voucher update(VoucherDto dto, String identityInfo);

    Optional<Voucher> findById(String voucherNo);

    void delete(String voucherNo, String identityInfo);

    void delete(Voucher voucher, String identityInfo);

    List<VoucherTransaction> loadVoucherByVoucherDateBetween(LocalDate fromDate, LocalDate toDate);
}
