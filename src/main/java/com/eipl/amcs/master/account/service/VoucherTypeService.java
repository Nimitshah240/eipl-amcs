package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.VoucherType;

import java.util.List;
import java.util.Optional;

public interface VoucherTypeService {
    List<VoucherType> findAll();


    VoucherType save(VoucherType voucherType, String identityInfo);

    VoucherType update(VoucherType voucherType, String identityInfo);


    Optional<VoucherType> findById(String voucherTypeNo);

    void delete(Integer voucherTypeNo, String identityInfo);

    void delete(VoucherType voucherType, String identityInfo);


}
