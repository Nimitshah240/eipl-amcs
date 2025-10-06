package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.repository.ShareRateRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShareRateServiceImpl implements ShareRateService {

    @Autowired
    ShareRateRepository shareRateRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @Override
    public List<ShareRate> findAll() {
        List<ShareRate> shareRateList = shareRateRepository.findAll();
        for (ShareRate shareRate : shareRateList) {
            shareRate.setSociety(Hibernate.unproxy(shareRate.getSociety(), Society.class));
        }
        return shareRateList;
    }

    @Override
    public Optional<ShareRate> findById(String id) {
        return shareRateRepository.findById(id);
    }

    @Override
    public ShareRate save(ShareRate localMilkSaleRate, String identityInfo)
            throws BusinessValidationFailException {
        localMilkSaleRate.setCode(nextCodeRepository.getNextCode("ShareRate", "code",
                localMilkSaleRate.getSociety().getCode(), 0));
        localMilkSaleRate.setInitData();
        ShareRate rateNew = shareRateRepository.customSave(localMilkSaleRate, identityInfo);
        rateNew.setSociety(localMilkSaleRate.getSociety());
        return rateNew;
    }


    @Override
    public ShareRate update(ShareRate shareRate, String identityHeader) {
        return shareRateRepository.customUpdate(shareRate, identityHeader);
    }

    @Override
    public void delete(ShareRate shareRate, String identityInfo) {
        shareRateRepository.delete(shareRate);
    }

    @Override
    public void delete(String id, String identityInfo) {
        shareRateRepository.customDelete(id, identityInfo);
    }

    @Override
    public ShareRate fetchRate(LocalDate dt) {
        return shareRateRepository.findTop1ByWefDateLessThanEqualOrderByWefDate(dt);
    }
}
