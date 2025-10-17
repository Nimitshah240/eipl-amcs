package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.repository.SubLedgerOpeningBalanceRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubLedgerOpeningBalanceServiceImpl implements SubLedgerOpeningBalanceService {

    private static final Logger log = LoggerFactory.getLogger(SubLedgerOpeningBalanceServiceImpl.class);
    @Autowired
    private SubLedgerOpeningBalanceRepository subLedgerOpeningBalanceRepository;
    @Autowired
    private NextCodeService nextCodeService;

    @Override
    public List<SubLedgerOpeningBalance> findAll() {
        List<SubLedgerOpeningBalance> list = subLedgerOpeningBalanceRepository.findAll();
        for (SubLedgerOpeningBalance subLedgerOpeningBalance : list) {
            subLedgerOpeningBalance.setLedger(Hibernate.unproxy(subLedgerOpeningBalance.getLedger(), Ledger.class));
            subLedgerOpeningBalance.setSubLedger(Hibernate.unproxy(subLedgerOpeningBalance.getSubLedger(), SubLedger.class));
            subLedgerOpeningBalance.setSociety(Hibernate.unproxy(subLedgerOpeningBalance.getSociety(), Society.class));
        }
        log.info("cash findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public SubLedgerOpeningBalance save(SubLedgerOpeningBalance subLedgerOpeningBalance, String identityInfo) {
        String code = nextCodeService.getNextCode("SubLedgerOpeningBalance", "code", subLedgerOpeningBalance.getSociety().getCode(), 0);
        subLedgerOpeningBalance.setCode(code);
        subLedgerOpeningBalance.setInitData();
        subLedgerOpeningBalanceRepository.customSave(subLedgerOpeningBalance, identityInfo);
        return null;
    }


    @Override
    public SubLedgerOpeningBalance update(SubLedgerOpeningBalance subLedgerOpeningBalance, String identityInfo) {
        return subLedgerOpeningBalanceRepository.customUpdate(subLedgerOpeningBalance, identityInfo);
    }


    @Override
    public Optional<SubLedgerOpeningBalance> findById(String cashAdvanceNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String code, String identityInfo) {
        SubLedgerOpeningBalance balance = subLedgerOpeningBalanceRepository.findById(code).orElseThrow();
        balance.setSociety(Hibernate.unproxy(balance.getSociety(), Society.class));
        subLedgerOpeningBalanceRepository.customDelete(balance, identityInfo);
    }


    @Override
    public void delete(SubLedgerOpeningBalance ledgerOpening, String identityInfo) {
        subLedgerOpeningBalanceRepository.customDelete(ledgerOpening, identityInfo);
    }

    @Override
    public List<SubLedgerOpeningBalance> importSubLedgerBalance(List<SubLedgerOpeningBalance> subledgerbList, String header) {
        List<SubLedgerOpeningBalance> list = new ArrayList<>();

        subledgerbList.forEach(item -> {
            SubLedgerOpeningBalance subled = null;
            try {
                if (item.getCode() != null) {
                    Optional<SubLedgerOpeningBalance> slBalanceData = subLedgerOpeningBalanceRepository.findById(item.getCode());
                    if (slBalanceData.isPresent()) {
                        SubLedgerOpeningBalance subledBal = slBalanceData.get();
                        subledBal.setSubLedger(item.getSubLedger());
                        subledBal.setSociety(item.getSociety());
                        subledBal.setUnionCode(item.getUnionCode());
                        subledBal.setFinancialYearsCode(item.getFinancialYearsCode());
                        subledBal.setCreditDebit(item.getCreditDebit());

                        subledBal.setupdateData();
                        subLedgerOpeningBalanceRepository.customUpdate(subledBal, header);
                        list.add(subledBal);
                    }
                } else {
                    String code = nextCodeService.getNextCode("SubLedgerOpeningBalance", "code", item.getSociety().getCode(), 0);
                    item.setCode(code);
                    item.setSociety(Hibernate.unproxy(item.getSociety(), Society.class));
                    item.setInitData();
                    subled = subLedgerOpeningBalanceRepository.customSave(item, header);
                    list.add(subled);
                }
            } catch (Exception e) {
                list.add(subled);
            }
        });
        for (SubLedgerOpeningBalance subLedgerOpeningBalance : list) {
            subLedgerOpeningBalance.setSubLedger(Hibernate.unproxy(subLedgerOpeningBalance.getSubLedger(), SubLedger.class));
            subLedgerOpeningBalance.setSociety(Hibernate.unproxy(subLedgerOpeningBalance.getSociety(), Society.class));
            subLedgerOpeningBalance.setLedger(Hibernate.unproxy(subLedgerOpeningBalance.getLedger(), Ledger.class));
        }
        return list;
    }

//		}
//		return null;
//	}

}


