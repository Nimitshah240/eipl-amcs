package com.eipl.amcs.master.inventory.bootcontroller;

import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product-groups")
public class ProductGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductGroupController.class);
    @Autowired
    private ProductGroupService service;

    @GetMapping
    public ResponseEntity<List<ProductGroup>> index() {
        try {
            List<ProductGroup> list = service.findAll();
            if (list == null || list.isEmpty())
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<List<ProductGroup>>(list, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductGroup> findByProductGroupCode(@PathVariable("code") String code) {
        return new ResponseEntity<>(service.findByProductGroupCode(code), HttpStatus.OK);
    }
}
