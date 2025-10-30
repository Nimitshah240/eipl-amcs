package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.controller.BillCriteriaController;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.repository.BillCriteriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BillCriteriaServiceImpl implements BillCriteriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillCriteriaController.class);
    @Autowired
    private BillCriteriaRepository repository;

    @Override
    public List<BillCriteria> findAll() {
        return repository.findAll();
    }

    @Override
    public BillCriteria save(BillCriteria BillCriteria) throws BusinessValidationFailException {
        return null;
    }

    @Override
    public BillCriteria update(BillCriteria BillCriteria) throws BusinessValidationFailException {
        return null;
    }

    @Override
    public Optional<BillCriteria> findById(String BillCriteria) {
        return null;
    }

    @Override
    public void delete(String code) {

    }

    @Override
    public void delete(BillCriteria BillCriteria) {

    }

    @Override
    public BillCriteria saveBillCriteria(BillCriteria billCriteria, String identityInfo) {
        try {
            billCriteria.setInitData();
            return repository.customSave(billCriteria, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BillCriteria updateBillCriteria(BillCriteria billCriteria, String identityInfo) {
        try {
            billCriteria.setupdateData();
            return repository.customUpdate(billCriteria, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String billCriteriaCode, String identityInfo) {
        try {
            LOGGER.info("Deleting BillCriteria with id: {}", billCriteriaCode);
            repository.customDelete(repository.findById(billCriteriaCode).get(), identityInfo);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
