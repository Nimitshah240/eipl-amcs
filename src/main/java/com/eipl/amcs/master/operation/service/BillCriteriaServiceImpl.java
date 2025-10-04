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

import static com.eipl.amcs.config.BeanConfig.billCriteriaRepository;

@Service
public class BillCriteriaServiceImpl implements BillCriteriaService {
//    @Autowired
//    private BillCriteriaRepository billCriteriaRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(BillCriteriaController.class);

    @Override
    public List<BillCriteria> findAll() {
        return billCriteriaRepository.findAll();
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

    /**
     * Method acts as service method to save the bill criteria.
     *
     * @param billCriteria
     * @param identityInfo
     * @return BillCriteria
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public BillCriteria saveBillCriteria(BillCriteria billCriteria, String identityInfo) {
        try {
            billCriteria.setInitData();
            return billCriteriaRepository.customSave(billCriteria, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as service method to update the bill criteria.
     *
     * @param billCriteria
     * @param identityInfo
     * @return BillCriteria
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public BillCriteria updateBillCriteria(BillCriteria billCriteria, String identityInfo) {
        try {
            billCriteria.setupdateData();
            return billCriteriaRepository.customUpdate(billCriteria, identityInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method acts as service method to delete the bill criteria.
     *
     * @param billCriteriaCode
     * @param identityInfo
     * @author Nimit Shah
     * @createdOn 30-06-2025
     */
    @Override
    public void delete(String billCriteriaCode, String identityInfo) {
        try {
            LOGGER.info("Deleting BillCriteria with id: {}", billCriteriaCode);
            billCriteriaRepository.customDelete(billCriteriaRepository.findById(billCriteriaCode).get(), identityInfo);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new RuntimeException(e);
        }
    }


}
