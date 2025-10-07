package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.repository.BillHeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillHeadServiceImpl implements BillHeadService {
    @Autowired
    private BillHeadRepository repository;

    @Override
    public List<BillHead> findAll() {
        return repository.findAll();
    }

    @Override
    public BillHead save(BillHead BillHead) throws BusinessValidationFailException {
        return null;
    }

    @Override
    public BillHead update(BillHead BillHead) throws BusinessValidationFailException {
        return null;
    }

    @Override
    public Optional<BillHead> findById(String BillHead) {
        return null;
    }

    @Override
    public void delete(String code) {

    }

    @Override
    public void delete(BillHead BillHead) {
    }

    /**
     * Method acts as service method to save the bill head.
     *
     * @param billHead
     * @param identityInfo
     * @return BillHead
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public BillHead saveBillHead(BillHead billHead, String identityInfo) {
        try {
            billHead.setInitData();
            return repository.customSave(billHead, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as service method to update the bill head.
     *
     * @param billHead
     * @param identityInfo
     * @return BillHead
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public BillHead updateBillHead(BillHead billHead, String identityInfo) {
        try {
            billHead.setupdateData();
            return repository.customUpdate(billHead, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as service method to delete the bill head.
     *
     * @param billHeadCode
     * @param identityInfo
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void delete(String billHeadCode, String identityInfo) {
        try {
            repository.customDelete(repository.findById(billHeadCode).get(), identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
