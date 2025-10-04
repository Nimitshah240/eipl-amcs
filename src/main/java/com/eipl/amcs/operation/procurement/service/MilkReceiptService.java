package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptDto;
import com.eipl.amcs.operation.procurement.dto.MilkReceiptSummaryDto;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface MilkReceiptService {
	List<MilkReceipt> findAll();

	MilkReceipt save(MilkReceiptDto MilkReceipt, String identityInfo);

	MilkReceipt update(MilkReceiptDto MilkReceipt, String identityInfo);

	Optional<MilkReceipt> findById(String code);

	void delete(String code, String identityInfo);

	void delete(MilkReceipt MilkReceipt, String identityInfo);
	
	SocietyMilkPurchaseRate fetchPurchaseRateCode(LocalDateTime date, Integer shiftCode, String societyCode);

	List<MilkReceiptTransaction> findDetailByChallanNo(String challanNo);

	Optional<MilkReceiptTransaction> findTransactionById(String code);

	void deleteTransaction(MilkReceiptTransaction milkReceiptTransaction, String identityInfo);

	List<MilkReceiptSummaryDto> fetchMilkReceiptSummary(LocalDateTime fromDt, LocalDateTime toDt);

}