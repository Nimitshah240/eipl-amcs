package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.repository.ProductSaleRateRepository;
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
public class ProductSaleRateServiceImpl implements ProductSaleRateService {
    private static final Logger log = LoggerFactory.getLogger(ProductSaleRateServiceImpl.class);
    @Autowired
    private ProductSaleRateRepository productSaleRateRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    @Override
    public List<ProductSaleRate> findAll() {
        List<ProductSaleRate> list = productSaleRateRepository.findAll(Sort.by("wefDate").descending());
        log.info("ProductSaleRates findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductSaleRate save(ProductSaleRate productSaleRate, String identityInfo) {
//		LocalDate chk = checkWefDate(productSaleRate.getProduct().getCode(), new ProductSaleRate().getCode());
//		if (chk == null || chk.isBefore(productSaleRate.getWefDate())) {
//
//		} else {
//			FieldError wefDateNotValid = CommonUtils.getFieldError("productsalerate", "wefDate",
//					productSaleRate.getCode(), "wefdate.not.valid");
//			throw new BusinessValidationFailException(getClass(), wefDateNotValid);
//		}
////		return productSaleRateRepository.save(productSaleRate);
//		String code = nextCodeRepository.getNextCode("ProductSaleRate", "code", productSaleRate.getSociety().getCode(),
//				0);
        String code = nextCodeRepository.getNextCode("ProductSaleRate", "code", productSaleRate.getSociety().getCode(),
                0);
        productSaleRate.setCode(code);
        LocalDate chk = checkWefDate(productSaleRate.getProduct().getCode(), code);
        if (chk == null || chk.isBefore(productSaleRate.getWefDate())) {

        } else {
            FieldError wefDateNotValid = CommonUtils.getFieldError("productsalerate", "wefDate",
                    productSaleRate.getCode(), "wefdate.not.valid");
            throw new BusinessValidationFailException(getClass(), wefDateNotValid);
        }
        productSaleRate.setCode(code);
        ProductSaleRate newData = productSaleRateRepository.customSave(productSaleRate, identityInfo);
        newData.setProduct(productSaleRate.getProduct());
        newData.setUnion(productSaleRate.getUnion());
        newData.setSociety(productSaleRate.getSociety());
        productSaleRate.setInitData();
        return newData;
    }

    @Override
    public ProductSaleRate update(ProductSaleRate productSaleRate, String identityInfo) {
        LocalDate chk = checkWefDate(productSaleRate.getProduct().getCode(), new ProductSaleRate().getCode());
        if (chk == null || chk.isBefore(productSaleRate.getWefDate())) {

        } else {
            FieldError wefDateNotValid = CommonUtils.getFieldError("productsalerate", "name", productSaleRate.getCode(),
                    "wefdate.not.valid");
            throw new BusinessValidationFailException(getClass(), wefDateNotValid);
        }
//		return productSaleRateRepository.save(productSaleRate);

        ProductSaleRate newData = productSaleRateRepository.customUpdate(productSaleRate, identityInfo);
        newData.setProduct(productSaleRate.getProduct());
        newData.setUnion(productSaleRate.getUnion());
        newData.setSociety(productSaleRate.getSociety());
        productSaleRate.setupdateData();
        return newData;
    }

    @Override
    public Optional<ProductSaleRate> findById(String code) {
        return productSaleRateRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) {
        productSaleRateRepository.customDelete(code, identityInfo);
    }

    @Override
    @Transactional
//	@CacheEvict(value = { "productSaleRatesCache" }, allEntries = true)
    public void delete(ProductSaleRate productSaleRate, String identityInfo) {
        productSaleRateRepository.customDelete(productSaleRate, identityInfo);
    }

    @Override
    public LocalDate checkWefDate(String str1, String str2) {
        return productSaleRateRepository.checkWefDate(str1, str2);
    }

    @Override
    public ProductSaleRate findByProduct(Product product, LocalDate date) {
        return productSaleRateRepository.findTop1ByProductAndWefDateLessThanEqualOrderByWefDateDesc(product, date);
    }

}
