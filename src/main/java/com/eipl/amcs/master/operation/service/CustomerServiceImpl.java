package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerSubLedgerMapping;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.repository.LedgerSubLedgerMappingRepository;
import com.eipl.amcs.master.account.repository.SubLedgerLedgerConfigRepository;
import com.eipl.amcs.master.account.repository.SubLedgerRepository;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.repository.CustomerDetailsRepository;
import com.eipl.amcs.master.operation.repository.CustomerRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.utils.CommonUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;
    @Autowired
    private CustomerDetailsRepository customerDetailrepository;
    @Autowired
    private SocietyRepository socRepository;
    @Autowired
    private NextCodeService nextCodeService;
    @Autowired
    private LedgerSubLedgerMappingRepository mappingRepository;
    @Autowired
    private SubLedgerRepository subLedgerRepository;
    @Autowired
    private SubLedgerLedgerConfigRepository subLedgerLedgerConfigRepository;

    @Override
    public List<Customer> findAll() {
        return repository.findAll(Sort.by("code"));
    }

    @Override
    public List<Customer> findAllBySociety(String societyCode) {
        Society society = socRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Society.class, "societycode", "invalid.society"));
        return repository.findAllBySociety(society, Sort.by("code"));
    }

    @Override
    @Transactional
    public CustomerDto save(CustomerDto customerDto, String identityInfo) {
        Optional<Customer> customerData = repository.findById(customerDto.getCustomer().getCode());
        if (customerData.isPresent()) {
            throw new BusinessValidationFailException(Customer.class, CommonUtils.getFieldError("Customer", "code",
                    customerDto.getCustomer().getCode(), "code.not.valid"));
        }
        CustomerDto customerDtoNew = new CustomerDto();
        Customer customer = customerDto.getCustomer();
        customer.setInitData();
        customerDtoNew.setCustomer(repository.customSave(customer, identityInfo));
        CustomerDetails customerDetail = customerDto.getCustomerDetail();
        customerDetail.setCode(customer.getCode());
        customerDetail.setInitData();
        customerDtoNew.setCustomerDetail(customerDetailrepository.customSave(customerDetail, identityInfo));

        SubLedger subLedger = new SubLedger();
        String code = nextCodeService.getNextCode("SubLedger", "code", customer.getSociety().getCode(), 0);
        subLedger.setCode(code);
        subLedger.setReferenceCode(customer.getCode());
        subLedger.setType(customer.getType().shortValue());
        subLedger.setName(customer.getName());
        subLedger.setNameLocal(customer.getNameLocal());
        subLedger.setSociety(customer.getSociety());
        subLedger.setUnionCode(customer.getSociety().getUnion() != null ? customer.getSociety().getUnion().getCode() : null);
        subLedger.setInitData();
        subLedgerRepository.customSave(subLedger, identityInfo);

        List<SubLedgerLedgerConfig> listConfig = subLedgerLedgerConfigRepository.findBySubLedgerType(1);
        if (listConfig != null && !listConfig.isEmpty()) {
            for (Ledger ledger : listConfig.stream().map(m -> m.getLedger()).collect(Collectors.toList())) {
                LedgerSubLedgerMapping mapping = new LedgerSubLedgerMapping();
                mapping.setCode(subLedger.getCode() + "-" + ledger.getCode());
                mapping.setSubLedger(subLedger);
                mapping.setLedger(ledger);
                mapping.setSociety(customer.getSociety());
                mapping.setUnionCode(customer.getSociety().getUnion() != null ? customer.getSociety().getUnion().getCode() : null);
                mappingRepository.customSave(mapping, identityInfo);
            }
        }
        return customerDtoNew;
    }

    @Override
    @Transactional
    public CustomerDto update(CustomerDto customerDto, String identityInfo) {
        Optional<Customer> old = repository.findById(customerDto.getCustomer().getCode());
        CustomerDto dtoNew = new CustomerDto();

        Customer customer = customerDto.getCustomer();
        customer.setupdateData();
        dtoNew.setCustomer(repository.customUpdate(customer, identityInfo));

        CustomerDetails customerDetail = customerDto.getCustomerDetail();
        customerDetail.setupdateData();
        dtoNew.setCustomerDetail(customerDetailrepository.customUpdate(customerDetail, identityInfo));

        Optional<SubLedger> sl = subLedgerRepository.findByReferenceCodeAndType(old.get().getCode(), old.get().getType().shortValue());
        if (sl.isPresent()) {
            sl.get().setName(customer.getName());
            sl.get().setType(customer.getType().shortValue());
            sl.get().setNameLocal(customer.getNameLocal());
            sl.get().setupdateData();
            subLedgerRepository.customUpdate(sl.get(), identityInfo);
        }

        return dtoNew;
    }

    @Override
    public Optional<Customer> findById(String code) {
        return repository.findById(code);
    }

    @Override
    @Transactional
    public Customer findByCustomerCode(String code) {
        Customer customer = repository.findByCode(code);
        if (code.endsWith("0001") && customer == null) {
            try {
                customer = new Customer();
                customer.setCode(code);
//                Society society = socRepository.findById(code.substring(0, 7)).orElseThrow(() -> new EntityNotFoundException(Society.class, "code", code.substring(0, 7)));
                Society society = MainApp.identityDto.getSociety();
                customer.setSociety(society);
                customer.setUnion(society.getUnion());
                customer.setType(5);
                customer.setCreditLimit(BigDecimal.ZERO);
                customer.setRegistrationDate(LocalDate.now());
                customer.setMobileNo("0000000000");
                customer.setPaymentMode(0);
                customer.setName("Retail Sale Consumer");
                customer.setNameLocal("Retail Sale Consumer");
                customer.setActive(true);
                customer = repository.save(customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customer;
    }

    @Override
    @Transactional
    public Customer findByCustomerCodeAndType(String code, Integer type) {
        //        if (code.endsWith("0001") && customer == null) {
//            try {
//                customer = new Customer();
//                customer.setCode(code);
////                Society society = socRepository.findById(code.substring(0, 7)).orElseThrow(() -> new EntityNotFoundException(Society.class, "code", code.substring(0, 7)));
//                Society society = MainApp.identityDto.getSociety();
//                customer.setSociety(society);
//                customer.setUnion(society.getUnion());
//                customer.setType(5);
//                customer.setCreditLimit(BigDecimal.ZERO);
//                customer.setRegistrationDate(LocalDate.now());
//                customer.setMobileNo("0000000000");
//                customer.setPaymentMode(0);
//                customer.setName("Retail Sale Consumer");
//                customer.setNameLocal("Retail Sale Consumer");
//                customer.setActive(true);
//                customer = repository.save(customer);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return repository.findByCodeAndType(code, type);
    }

    @Override
    public CustomerDetails findDetailByCustomerCode(String code) {
        Customer customer = repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, "invalid.customercode"));
        return customerDetailrepository.findByCustomer(customer);
    }

    @Override
    @Transactional
    public void delete(String code, String identityInfo) {
        Customer customer = repository.findById(code)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, "invalid.customercode"));
        CustomerDetails detail = customerDetailrepository.findByCustomer(customer);
        customerDetailrepository.customDelete(detail, identityInfo);
        repository.customDelete(customer, identityInfo);
    }

    @Override
    public boolean checkCode(String str) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkName(String str1, String str2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDetails findDetailByCustomer(Customer customer) {
        CustomerDetails dtl = customerDetailrepository.findByCustomer(customer);
        dtl.setState(Hibernate.unproxy(dtl.getState(), State.class));
        dtl.setDistrict(Hibernate.unproxy(dtl.getDistrict(), District.class));
        dtl.setSubDistrict(Hibernate.unproxy(dtl.getSubDistrict(), SubDistrict.class));
        dtl.setVillage(Hibernate.unproxy(dtl.getVillage(), Village.class));
        dtl.setHamlet(Hibernate.unproxy(dtl.getHamlet(), Hamlet.class));

        return dtl;
    }
}
