package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductAndSaleRateDto;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.inventory.repository.ProductSaleRateRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SocietyRepository socRepository;
    @Autowired
    private ProductSaleRateRepository saleRateRepository;

    @Override
    public List<Product> findAll() {
        List<Product> list = productRepository.findAll(Sort.by("name"));
        log.info("Products findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<Product> findAllBySociety(String societyCode) {
        Society society = socRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Society.class, "societycode", "invalid.society"));
        List<Product> list = new ArrayList<>();

        List<Product> listProductsUnion = productRepository.findAllBySocietyIsNull(Sort.by("code"));
        if (listProductsUnion != null && !listProductsUnion.isEmpty())
            list.addAll(listProductsUnion);

        List<Product> listProductsSociety = productRepository.findAllBySociety(society, Sort.by("code"));
        if (listProductsSociety != null && !listProductsSociety.isEmpty())
            list.addAll(listProductsSociety);

        return list;
    }

    @Override
    public Product save(Product product, String identityInfo) {

        boolean chk = checkName(product.getName(), product.getCode());
        if (chk) {
            FieldError nameNotValid = CommonUtils.getFieldError("product", "name", product.getName(), "name.not.valid");
            throw new BusinessValidationFailException(getClass(), nameNotValid);
        }
        product.setInitData();
        Product newData = productRepository.customSave(product, identityInfo);
        newData.setConversionUnit(product.getConversionUnit());
        newData.setPrimaryUom(product.getPrimaryUom());
        newData.setProductGroup(product.getProductGroup());
        newData.setTax(product.getTax());
        newData.setSociety(product.getSociety());
        newData.setUnion(product.getUnion());
        return newData;
    }

    @Override
    public Product update(Product product, String identityInfo) {
        boolean chk = checkName(product.getName(), product.getCode());
        if (chk) {
            FieldError nameNotValid = CommonUtils.getFieldError("product", "name", product.getName(), "name.not.valid");
            throw new BusinessValidationFailException(getClass(), nameNotValid);
        }
        Product newData = productRepository.save(product);
        newData.setConversionUnit(product.getConversionUnit());
        newData.setPrimaryUom(product.getPrimaryUom());
        newData.setProductGroup(product.getProductGroup());
        newData.setTax(product.getTax());
        newData.setSociety(product.getSociety());
        newData.setUnion(product.getUnion());
        newData.setupdateData();
        return newData;
    }

    @Override
    public Optional<Product> findById(String productNo) {
        return productRepository.findById(productNo);
    }

    @Override
    public void delete(String productNo, String identityInfo) {
        productRepository.customDelete(productRepository.findById(productNo).get(), identityInfo);
    }

    @Override
    @Transactional
    public void delete(Product product, String identityInfo) {
        productRepository.customDelete(product.getCode(), identityInfo);
    }

    @Override
    public boolean checkName(String name, String code) {
        List<Product> list = productRepository.checkName(name, code);
        return list == null;
    }

    @Override
    public List<ProductAndSaleRateDto> migrateCollections(List<ProductAndSaleRateDto> dtoList, String header) {
        for (ProductAndSaleRateDto dto : dtoList) {
            dto.getProduct().setInitData();
            dto.getProductSaleRate().setInitData();
            productRepository.save(dto.getProduct());
            saleRateRepository.save(dto.getProductSaleRate());
        }
        return dtoList;
    }
}
