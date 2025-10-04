package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SocietyPaymentCycleService {
	List<SocietyPaymentCycle> findAll();
	List<SocietyPaymentCycle> findAll(LocalDateTime fromDt, LocalDateTime toDt);

	String save(List<SocietyPaymentCycle> societyPaymentCycle, String identityInfo) throws BusinessValidationFailException;

	SocietyPaymentCycle update(String code, SocietyPaymentCycle societyPaymentCycle, String identityInfo);

	Optional<SocietyPaymentCycle> findById(String code);

	void delete(String code, String identityInfo) throws EntityNotFoundException;

	void delete(SocietyPaymentCycle societyPaymentCycle);

	boolean checkDateRangeConflict(String str1, String str2, LocalDateTime fromDate, LocalDateTime toDate);

	SocietyPaymentCycle fetchCurrentPaymentCycle(LocalDateTime date, String code);
	
	List<SocietyPaymentCycle> findByToDateGreaterThanEqualOrderByToDate(LocalDateTime date,int limit);


}
