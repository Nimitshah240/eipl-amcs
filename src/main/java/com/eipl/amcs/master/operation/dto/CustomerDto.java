package com.eipl.amcs.master.operation.dto;
import com.eipl.amcs.master.operation.model.Customer;

public class CustomerDto {
    private Customer customer;
    private CustomerDetails customerDetail;

    public CustomerDto() {

    }

    public CustomerDto(Customer customer, CustomerDetails customerDetail) {
        super();
        this.customer = customer;
        this.customerDetail = customerDetail;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerDetails getCustomerDetail() {
        return customerDetail;
    }

    public void setCustomerDetail(CustomerDetails customerDetail) {
        this.customerDetail = customerDetail;
    }
}
