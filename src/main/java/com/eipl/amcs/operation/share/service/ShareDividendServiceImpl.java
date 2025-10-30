package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.repository.ShareDividendRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class ShareDividendServiceImpl implements ShareDividendService {
    @Autowired
    ShareDividendRepository shareDividendRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;


    @Override
    public List<ShareDividend> findAll() {
        return shareDividendRepository.findAll();
    }

    @Override
    public Optional<ShareDividend> findById(String id) {
        return shareDividendRepository.findById(id);
    }

    @Override
    public ShareDividend save(ShareDividend share, String identityInfo) {
        return shareDividendRepository.save(share);
    }

    @Override
    @Transactional
    public String save(List<ShareDividend> shareList, String identityInfo) throws BusinessValidationFailException {
        String nextCode = nextCodeRepository.getNextCode("ShareDividend", "code",
                shareList.get(0).getSociety().getCode(), 0);

        for (ShareDividend shareDividend : shareList) {
            shareDividend.setCode(nextCode);
            shareDividend.setInitData();
            shareDividendRepository.customSave(shareDividend, identityInfo);
            nextCode = String.valueOf(Integer.parseInt(nextCode) + 1);
        }
        return "Share Dividend saved successfully!";
    }

    @Override
    public ShareDividend update(ShareDividend share, String identityHeader) {
        return shareDividendRepository.save(share);
    }

    @Override
    public Share cancel(Share share) {
        return null;
    }

    @Override
    public Share cancel(String id, String identityInfo) {
        Optional<ShareDividend> share = shareDividendRepository.findById(id);
        if (share.isPresent()) {
            shareDividendRepository.customDelete(share.get(), identityInfo);
        }
        return null;
    }

    @Override
    public List<ShareDividend> findAllData(LocalDate fromDt, LocalDate toDt) {
        List<ShareDividend> list =

                shareDividendRepository.findByDisbursementDateBetween(fromDt, toDt, Sort.by("disbursementDate").descending());
        for (ShareDividend shareDividend : list) {
            shareDividend.setMember(Hibernate.unproxy(shareDividend.getMember(), Member.class));
            shareDividend.setSociety(Hibernate.unproxy(shareDividend.getSociety(), Society.class));
        }
        return list;
    }

    @Override
    public ShareDividend delete(String id, String identityInfo) {
        Optional<ShareDividend> shareDividend = shareDividendRepository.findById(id);
        if (shareDividend.isPresent()) {
            shareDividendRepository.customDelete(shareDividend.get(), identityInfo);
        }
        return null;
    }

    @Override
    public ShareDividend deleteAll(LocalDate fromDt, LocalDate toDt) {
        List<ShareDividend> list =
                shareDividendRepository.findByDisbursementDateBetween(fromDt, toDt, Sort.by("disbursementDate").descending());
        for (ShareDividend shareDividend : list) {
            shareDividend.setMember(Hibernate.unproxy(shareDividend.getMember(), Member.class));
            shareDividend.setSociety(Hibernate.unproxy(shareDividend.getSociety(), Society.class));
        }
        for (ShareDividend shareDividend : list) {
            shareDividendRepository.delete(shareDividend);
        }
        return null;
    }
}
