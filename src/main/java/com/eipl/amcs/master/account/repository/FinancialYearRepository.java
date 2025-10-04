package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.FinancialYear;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinancialYearRepository extends BaseRepository<FinancialYear, String> {

	@Override
	List<FinancialYear> findAll(Sort sort);

	@Override
	Optional<FinancialYear> findById(String s);

	@Query("select fy from FinancialYear fy where fy.startDate <= ?1 and fy.endDate >= ?1")
	Optional<FinancialYear> findCurrentFinancialYear(LocalDate date);


	@Query(nativeQuery = true, value = "select code ,start_date ,end_date from financial_years WHERE start_date >= ?1 and end_date <= ?1  AND is_active=true ")
	List<LocalDate> fetchByDate(LocalDate currentDate);

	@Query(nativeQuery = true, value = "select count(*) as cnt from society_year_closing dc where dc.financial_years_code = ?1 ")
	int fetchByCode(String financialYearCode);

}
