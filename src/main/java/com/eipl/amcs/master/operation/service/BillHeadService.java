package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.BillHead;

import java.util.List;
import java.util.Optional;

public interface BillHeadService {

    List<BillHead> findAll();

    BillHead save(BillHead BillHead) throws BusinessValidationFailException;

    BillHead update(BillHead BillHead) throws BusinessValidationFailException;

    Optional<BillHead> findById(String BillHead);

    void delete(String code);

    void delete(BillHead BillHead);

    BillHead saveBillHead(BillHead billHead, String identityInfo);

    BillHead updateBillHead(BillHead billHead, String identityInfo);

    void delete(String billHeadCode, String identityInfo);

}
