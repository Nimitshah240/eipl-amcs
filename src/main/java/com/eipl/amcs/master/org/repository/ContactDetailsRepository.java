package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.master.org.model.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Integer> {


    List<ContactDetails> findByModuleNameIgnoreCaseAndModuleCodeAndDepartmentIgnoreCase(
            String moduleName,
            String moduleCode,
            String department
    );
}