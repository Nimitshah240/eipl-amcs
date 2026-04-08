package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();

    CustomerDto save(CustomerDto customerDto, String identityInfo);

    Customer findByCustomerCode(String code);

    Customer findByCustomerCodeAndType(String code, Integer type);

    CustomerDto update(CustomerDto customer, String identityInfo);

    Optional<Customer> findById(String code);

    CustomerDetails findDetailByCustomerCode(String code);


    boolean checkCode(String str);

    boolean checkName(String str1, String str2);

    void delete(String code, String identityInfo);

    CustomerDetails findDetailByCustomer(Customer customer);

    List<Customer> findAllBySociety(String societyCode);
}
