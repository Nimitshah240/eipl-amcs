package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface MilkDispatchService {
	List<MilkDispatch> findAll();

	MilkDispatch save(MilkDispatchDto MilkDispatch, String identityInfo);

	MilkDispatch update(MilkDispatchDto MilkDispatch, String identityInfo);

	Optional<MilkDispatch> findById(String code);

	void delete(String code, String identityInfo);

	void delete(MilkDispatch MilkDispatch, String identityInfo);
	
	SocietyMilkPurchaseRate fetchPurchaseRateCode(LocalDateTime date, Integer shiftCode, String societyCode);

	List<MilkDispatchTransaction> findDetailByChallanNo(String challanNo);

	Optional<MilkDispatchTransaction> findTransactionById(String code);

	void deleteTransaction(MilkDispatchTransaction milkDispatchTransaction, String identityInfo);

	List<MilkDispatchSummaryDto> fetchMilkDispatchSummary(LocalDateTime fromDt, LocalDateTime toDt);

}