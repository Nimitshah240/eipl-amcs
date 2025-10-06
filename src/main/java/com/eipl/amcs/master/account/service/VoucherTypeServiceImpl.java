package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.repository.VoucherTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class VoucherTypeServiceImpl implements VoucherTypeService {

    @Autowired
    private VoucherTypeRepository voucherTypeRepository;

    private static final Logger log = LoggerFactory.getLogger(VoucherTypeServiceImpl.class);

    @Override
    public List<VoucherType> findAll() {
        List<VoucherType> list = voucherTypeRepository.findAll(Sort.by("code"));
        log.info("VoucherTypes findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public VoucherType save(VoucherType voucherType, String identityInfo) {
        voucherType.setInitData();
        return voucherTypeRepository.customSave(voucherType, identityInfo);
    }

    @Override
    public VoucherType update(VoucherType voucherType, String identityInfo) {
        voucherType.setupdateData();
        return voucherTypeRepository.customUpdate(voucherType, identityInfo);
    }

    @Override
    public Optional<VoucherType> findById(String voucherTypeNo) {
        return Optional.empty();
    }

    @Override
    public void delete(Integer voucherTypeNo, String identityInfo) {
        VoucherType voucherType = voucherTypeRepository.findById(voucherTypeNo).get();
        voucherTypeRepository.customDelete(voucherType, identityInfo);
    }

    @Override
    public void delete(VoucherType voucherType, String identityInfo) {
        voucherTypeRepository.customDelete(voucherType, identityInfo);
    }

}