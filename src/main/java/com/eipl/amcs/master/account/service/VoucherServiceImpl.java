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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.*;

@Service
public class VoucherServiceImpl implements VoucherService {

//    private NextCodeService nextCodeService;
//    private MemberRepository memberRepository;
//    private ProductSaleInstallmentRepository installmentRepository;
//    private LedgerMappingEventRepository ledgerMappingEventRepository;
//    private FinancialYearRepository financialYearRepository;
//    private SubLedgerRepository subLedgerRepository;
//    private VoucherRepository voucherRepository;
//    private VoucherTransactionRepository voucherTxnRepository;
//    private VoucherSubLedgerRepository voucherSubLedgerRepository;

    private static final Logger log = LoggerFactory.getLogger(VoucherServiceImpl.class);


    @Override
    public List<VoucherDto> findAll() {

        List<Voucher> list = voucherRepository.findAll(Sort.by("code").descending());
        List<VoucherTransaction> voucherTransactionList = new ArrayList<>();
        List<VoucherSubLedger> voucherSubLedgerList = new ArrayList<>();
        List<VoucherDto> dto = new ArrayList<>();
        for (Voucher voucher : list) {
            VoucherDto dto1 = new VoucherDto();
            voucherTransactionList.addAll(voucherTxnRepository.findByVoucher(voucher));
            for (VoucherTransaction voucherTransaction : voucherTransactionList) {
                voucherSubLedgerList.addAll(voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction));
            }
            dto1.setVoucher(voucher);
            dto1.setVoucherTransactions(voucherTransactionList);
            dto1.setVoucherSubLedgers(voucherSubLedgerList);
            dto.add(dto1);
        }
        log.info("Voucher findAll {} items fetched", list.size());
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
//        Voucher voucher1 = new Voucher();
//        String code = nextCodeService.getNextCode("Voucher", "code",
//                voucher.getSociety().getCode() + "/" + voucher.getFinancialYearsCode() + "/", 6);
//        voucher1.setCode(code);
//        voucher1.setAutoPosted(voucher.getAutoPosted());
//        voucher1.setBillNo(voucher.getBillNo());
//        voucher1.setBillDate(voucher.getBillDate());
//        voucher1.setVoucherDate(voucher.getVoucherDate());
//        voucher1.setInitData();
//        voucher1.setxCol1(voucher.getXCol1());
//        voucher1.setxCol2(voucher.getxCol2());
//        voucher1.setxCol3(voucher.getXCol3());
//        voucher1.setSociety(voucher.getSociety());
//        voucher1.setVoucherType(voucher.getVoucherType());
//        voucher1.setUnionCode(voucher.getUnionCode());
//        voucher1.setDockCode(voucher.getDockCode());
//        voucher1.setFinancialYearsCode(voucher.getFinancialYearsCode());
//
//        voucher1.setCancelled(false);
//        voucher1.setRemarks("Cancelled Against:-" + voucher.getCode());
//        voucherRepository.customDelete(voucher1, identityInfo);
        List<VoucherTransaction> list = voucherTxnRepository.findByVoucher(voucher);
        for (VoucherTransaction voucherTransaction : list) {
//            VoucherTransaction transaction = new VoucherTransaction();
//            String transCode = nextCodeService.getNextCode("voucherTransaction", "code", voucher.getSociety().getCode(), 0);
//            transaction.setCode(transCode);
//            transaction.setAmount(voucherTransaction.getAmount());
//            transaction.setCreditDebit(!voucherTransaction.getCreditDebit());
//            transaction.setNarration(voucherTransaction.getNarration());
//            transaction.setLedger(voucherTransaction.getLedger());
//            transaction.setVoucher(voucher1);
//            transaction.setInitData();
//            transaction.setxCol1(voucherTransaction.getXCol1());
//            transaction.setxCol2(voucherTransaction.getXCol2());
//            transaction.setxCol3(voucherTransaction.getXCol3());
            voucherTxnRepository.customDelete(voucherTransaction, identityInfo);

            List<VoucherSubLedger> voucherSubLedgerList = voucherSubLedgerRepository.findByVoucherTransaction(voucherTransaction);
            for (VoucherSubLedger voucherSubLedger : voucherSubLedgerList) {
//                VoucherSubLedger voucherSubLedger1 = new VoucherSubLedger();
//
//                String subCode = nextCodeService.getNextCode("VoucherSubLedger", "code", voucher.getSociety().getCode(), 0);
//                voucherSubLedger1.setCode(subCode);
//                voucherSubLedger1.setAmount(voucherSubLedger.getAmount());
//                voucherSubLedger1.setNarration(voucherSubLedger.getNarration());
//                if (voucherSubLedger.getCreditDebit() != null) {
//                    //TO-DO
//                    voucherSubLedger1.setCreditDebit(!voucherSubLedger.getCreditDebit());
//                }
//                voucherSubLedger1.setVoucherTransaction(transaction);
//                voucherSubLedger1.setVoucher(voucher1);
//                voucherSubLedger1.setSubLedger(voucherSubLedger.getSubLedger());
//                voucherSubLedger1.setxCol1(voucherSubLedger.getxCol1());
//                voucherSubLedger1.setxCol2(voucherSubLedger.getxCol2());
//                voucherSubLedger1.setxCol3(voucherSubLedger.getxCol3());
                voucherSubLedgerRepository.customDelete(voucherSubLedger, identityInfo);
            }

        }
    }


}