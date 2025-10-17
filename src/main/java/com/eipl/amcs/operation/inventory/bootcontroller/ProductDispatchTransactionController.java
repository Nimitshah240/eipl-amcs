package com.eipl.amcs.operation.inventory.bootcontroller;

import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.service.ProductDispatchTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/product-dispatch-transaction")
public class ProductDispatchTransactionController {

    @Autowired
    private ProductDispatchTransactionService service;

    @GetMapping
    List<ProductDispatchTransaction> findAll() {
        return service.findAll();
    }

    @GetMapping("/{code}")
    Optional<ProductDispatchTransaction> findByID(@PathVariable String code) {
        return service.findById(code);
    }

    @PostMapping
    ProductDispatchTransaction save(@RequestBody ProductDispatchTransaction productDispatch) {
        return service.save(productDispatch);
    }


    @PutMapping("/{code}")
    ProductDispatchTransaction update(@PathVariable String code, @RequestBody ProductDispatchTransaction productDispatch) {
        productDispatch.setCode(code);
        return service.save(productDispatch);
    }

    @DeleteMapping("/{code}")
    void delete(@PathVariable String code) {
        service.delete(code);
    }

}
