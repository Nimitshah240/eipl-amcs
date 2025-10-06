package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkClassRepository;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.repository.LocalMilkSaleRateRepository;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class LocalMilkSaleRateServiceImpl implements LocalMilkSaleRateService {

	@Autowired
	private LocalMilkSaleRateRepository localMilkSaleRateRepository;
	@Autowired
	private NextCodeRepository nextCodeRepository;
	@Autowired
	private MilkTypeRepository milkTypeRepository;
	@Autowired
	private MilkClassRepository milkClassRepository;

	private static final Logger log = LoggerFactory.getLogger(LocalMilkSaleRateServiceImpl.class);

	@Override
	public List<LocalMilkSaleRate> findAll() {
		List<LocalMilkSaleRate> list = localMilkSaleRateRepository.findAll(Sort.by("wefDate").descending());
		log.info("LocalMilkSaleRates findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public LocalMilkSaleRate save(LocalMilkSaleRate localMilkSaleRate, String identityInfo)
			throws BusinessValidationFailException {
		Optional<LocalMilkSaleRate> rate = localMilkSaleRateRepository.findTop1BySocietyAndMilkTypeAndMilkClassAndWefDateGreaterThan(
				localMilkSaleRate.getSociety(), localMilkSaleRate.getMilkType(), localMilkSaleRate.getMilkClass(),
				localMilkSaleRate.getWefDate());
		if (rate.isPresent()) {
			FieldError nameNotValid = CommonUtil.getFieldError("LocalMilkSaleRate", "wefDate",
					localMilkSaleRate.getWefDate(), "wefdate.not.valid");
			throw new BusinessValidationFailException(getClass(), nameNotValid);
		}
		localMilkSaleRate.setCode(nextCodeRepository.getNextCode("LocalMilkSaleRate", "code",
				localMilkSaleRate.getSociety().getCode(), 0));
		localMilkSaleRate.setInitData();
		LocalMilkSaleRate rateNew = localMilkSaleRateRepository.customSave(localMilkSaleRate, identityInfo);
		rateNew.setSociety(localMilkSaleRate.getSociety());
		rateNew.setMilkClass(localMilkSaleRate.getMilkClass());
		rateNew.setMilkType(localMilkSaleRate.getMilkType());
		return rateNew;
	}

	@Override
	public LocalMilkSaleRate update(LocalMilkSaleRate localMilkSaleRate, String identityInfo) {
		LocalDate date = fetchLastestDate(localMilkSaleRate.getSociety().getCode(),
				localMilkSaleRate.getMilkType().getCode(), localMilkSaleRate.getMilkClass().getCode());
		if (date == null || date.isEqual(localMilkSaleRate.getWefDate())
				|| date.isBefore(localMilkSaleRate.getWefDate())) {

		} else {
			FieldError nameNotValid = CommonUtil.getFieldError("localmilksalerate", "name", localMilkSaleRate.getCode(),
					"wefdate.not.valid");
			throw new BusinessValidationFailException(getClass(), nameNotValid);
		}
		return localMilkSaleRateRepository.customSave(localMilkSaleRate, identityInfo);
	}

	@Override
	public Optional<LocalMilkSaleRate> findById(String code) {
		return localMilkSaleRateRepository.findById(code);
	}

	@Override
	public void delete(String code, String identityInfo) {
		localMilkSaleRateRepository.customDelete(localMilkSaleRateRepository.findById(code).get(), identityInfo);
	}

	@Override
	@Transactional
	public void delete(LocalMilkSaleRate localMilkSaleRate, String identityInfo) {
		localMilkSaleRateRepository.customDelete(localMilkSaleRate, identityInfo);
	}

	@Override
	public LocalDate fetchLastestDate(String str1, Integer i1, Integer i2) {
		// TODO Auto-generated method stub
		return localMilkSaleRateRepository.fetchLatestDate(str1, i1, i2);
	}

	@Override
	public LocalMilkSaleRate fetchRate(LocalDate date, Integer milkType, Integer milkClass) {
		MilkType mt = milkTypeRepository.findByCode(milkType);
		MilkClass mc = milkClassRepository.findByCode(milkClass);
		return localMilkSaleRateRepository.findTop1RateByWefDateLessThanEqualAndMilkTypeAndMilkClassOrderByWefDate(date, mt, mc);
	}
}