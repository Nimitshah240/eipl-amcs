package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.model.Branch;

import java.util.List;

public interface BranchService {
	List<Branch> findAll();

	List<Branch> findAll(String bankCode);

}
