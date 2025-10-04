package com.eipl.amcs.master.operation.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomerDto implements Serializable {

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
