package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.BillCriteria;

import java.util.List;
import java.util.Optional;

public interface BillCriteriaService {

    List<BillCriteria> findAll();

    BillCriteria save(BillCriteria BillCriteria) throws BusinessValidationFailException;

    BillCriteria update(BillCriteria BillCriteria) throws BusinessValidationFailException;

    Optional<BillCriteria> findById(String BillCriteria);

    void delete(String code);

    void delete(BillCriteria BillCriteria);

    BillCriteria saveBillCriteria(BillCriteria billCriteria, String identityInfo);

    BillCriteria updateBillCriteria(BillCriteria billCriteria, String identityInfo);

    void delete(String billCriteriaCode, String identityInfo);
}
