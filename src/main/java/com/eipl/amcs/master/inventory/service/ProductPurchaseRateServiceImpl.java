package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.repository.ProductPurchaseRateRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductPurchaseRateServiceImpl implements ProductPurchaseRateService {
    private static final Logger log = LoggerFactory.getLogger(ProductPurchaseRateServiceImpl.class);
    @Autowired
    private ProductPurchaseRateRepository productPurchaseRateRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @Override
    public List<ProductPurchaseRate> findAll() {
        List<ProductPurchaseRate> list = productPurchaseRateRepository.findAll(Sort.by("wefDate").descending());
        log.info("ProductPurchaseRates findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductPurchaseRate save(ProductPurchaseRate productPurchaseRate, String identityInfo) {

        LocalDate chk = checkWefDate(productPurchaseRate.getProduct().getCode());
        if (chk == null || chk.isBefore(productPurchaseRate.getWefDate())) {

        } else {
            FieldError wefdateNotValid = CommonUtils.getFieldError("productpurchaserate", "wefdate",
                    productPurchaseRate.getCode(), "wefdate.not.valid");
            throw new BusinessValidationFailException(getClass(), wefdateNotValid);
        }
        String code = nextCodeRepository.getNextCode("ProductPurchaseRate", "code",
                productPurchaseRate.getSociety().getCode(), 0);
        productPurchaseRate.setCode(code);
        ProductPurchaseRate newData = productPurchaseRateRepository.customSave(productPurchaseRate, identityInfo);
        newData.setProduct(productPurchaseRate.getProduct());
        newData.setUnion(productPurchaseRate.getUnion());
        newData.setSociety(productPurchaseRate.getSociety());
        productPurchaseRate.setInitData();
        return newData;
    }

    @Override
    public ProductPurchaseRate update(ProductPurchaseRate productPurchaseRate, String identityInfo) {
        ProductPurchaseRate newData = productPurchaseRateRepository.customUpdate(productPurchaseRate, identityInfo);
        newData.setProduct(productPurchaseRate.getProduct());
        newData.setUnion(productPurchaseRate.getUnion());
        newData.setSociety(productPurchaseRate.getSociety());
        productPurchaseRate.setupdateData();
        return newData;
    }

    @Override
    public Optional<ProductPurchaseRate> findById(String code) {
        return productPurchaseRateRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) {
        productPurchaseRateRepository.customDelete(productPurchaseRateRepository.findById(code).get(), identityInfo);
    }

    @Override
    @Transactional
    public void delete(ProductPurchaseRate productPurchaseRate, String identityInfo) {
        productPurchaseRateRepository.customDelete(productPurchaseRate.getCode(), identityInfo);
    }

    @Override
    public LocalDate checkWefDate(String str) {
        return productPurchaseRateRepository.checkWefDate(str);
    }

    @Override
    public ProductPurchaseRate findProductRate(Product product, LocalDate date) {
        ProductPurchaseRate rate = productPurchaseRateRepository.findTop1ByProductAndWefDateLessThanEqualOrderByWefDateDesc(product, date);
//        rate.setUnion(Hibernate.unproxy(rate.getUnion(), Union.class));
        return rate;
    }
}