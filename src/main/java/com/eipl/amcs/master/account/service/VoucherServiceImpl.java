package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.dto.VoucherDto;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.repository.*;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleInstallmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    private static final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductSaleInstallmentRepository installmentRepository;
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
    public List<VoucherDto> findAll() {
        List<Voucher> list = voucherRepository.findAll(Sort.by("code").descending());
        List<VoucherDto> dto = new ArrayList<>();
        for (Voucher voucher : list) {
            VoucherDto dto1 = new VoucherDto();
            dto1.setVoucher(voucher);
            dto.add(dto1);
        }
        log.info("Voucher findAll {} items fetched", list.size());
        return dto;
    }

    @Override
    public List<VoucherDto> findAllBetweenDates(LocalDate fromDate, LocalDate toDate) {
        List<Voucher> list = voucherRepository.findByCancelledFalseAndVoucherDateBetween(fromDate, toDate, Sort.by("voucherDate").ascending());


        List<VoucherDto> dto = new ArrayList<>();
        for (Voucher voucher : list) {
            VoucherDto dto1 = new VoucherDto();
            dto1.setVoucher(voucher);
            dto.add(dto1);
        }
        log.info("Voucher findAllBetweenDates {} items fetched", list.size());
        return dto;
    }

    @Override
    public List<VoucherTransaction> findAllTransaction(Voucher voucher) {
        return voucherTxnRepository.findByVoucher(voucher);
    }

    @Override
    public List<VoucherSubLedger> findAllVoucherSubLedger(VoucherTransaction voucherTransaction) {
        return voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
    }

    @Override
    @Transactional
    public Voucher save(VoucherDto dto, String identityInfo) {

        dto.getVoucher().setInitData();
        voucherRepository.customSave(dto.getVoucher(), identityInfo);
        for (VoucherTransaction voucherTransaction : dto.getVoucherTransactions()) {
            voucherTransaction.setInitData();
            voucherTxnRepository.customSave(voucherTransaction, identityInfo);
        }
        for (VoucherSubLedger voucherSubLedger : dto.getVoucherSubLedgers()) {
            voucherSubLedger.setInitData();
            voucherSubLedgerRepository.customSave(voucherSubLedger, identityInfo);
        }

        return null;
    }


    @Override
    public Voucher update(VoucherDto dto, String identityInfo) {
        dto.getVoucher().setupdateData();
        return voucherRepository.customUpdate(dto.getVoucher(), identityInfo);
    }


    @Override
    public Optional<Voucher> findById(String voucherNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String voucherNo, String identityInfo) {
        Optional<Voucher> voucher = voucherRepository.findById(voucherNo);
        if (voucher.isPresent()) {
            voucher.get().setCancelled(true);
            voucherRepository.customUpdate(voucher.get(), identityInfo);
        }
    }


    @Override
    @Transactional
    public void delete(Voucher voucher, String identityInfo) {
        voucher.setCancelled(true);
        voucherRepository.customDelete(voucher, identityInfo);

        List<VoucherTransaction> list = voucherTxnRepository.findByVoucher(voucher);
        for (VoucherTransaction voucherTransaction : list) {
            voucherTxnRepository.customDelete(voucherTransaction, identityInfo);

            List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
            for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
                voucherSubLedgerRepository.customDelete(voucherSubLedger, identityInfo);
            }
        }
    }
}
