package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.master.operation.model.SchemeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemeRateRepository extends JpaRepository<SchemeRate, String> {
    void deleteBySchemeRateCode(String schemeRateCode);

}
