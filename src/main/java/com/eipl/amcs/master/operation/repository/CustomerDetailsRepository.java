package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailsRepository extends BaseRepository<CustomerDetails, String> {

	@EntityGraph(attributePaths = { "union", "customer", "bank", "branch", "state", "district", "subDistrict",
			"village", "hamlet" })
	CustomerDetails findByCustomer(Customer customer);

	@Override
	@EntityGraph(attributePaths = { "union", "customer", "bank", "branch", "state", "district", "subDistrict",
			"village", "hamlet" })
	Optional<CustomerDetails> findById(String id);

}
