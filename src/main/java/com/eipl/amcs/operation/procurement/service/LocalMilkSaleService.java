package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LocalMilkSaleService {


	List<LocalMilkSale> findAll(LocalDateTime fromDt, LocalDateTime toDt);

	LocalMilkSale save(LocalMilkSale localMilkSale, String identityInfo) throws BusinessValidationFailException;

	LocalMilkSale update(LocalMilkSale localMilkSale, String identityInfo) throws BusinessValidationFailException;

	Optional<LocalMilkSale> findById(String localMilkSale);
	
	void delete(String code, String identityInfo);

	void delete(LocalMilkSale localMilkSale, String identityInfo);

	List<LocalMilkSale> migrateCollections(List<LocalMilkSale> dtoList, String header);


}
