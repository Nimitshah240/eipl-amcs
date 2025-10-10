package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateApplicabilityRepository;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateBasedRepository;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateDetailRepository;
import com.eipl.amcs.master.procurement.repository.SocietyMilkPurchaseRateRepository;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchRateAndDetailsDto;
import com.eipl.amcs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SocietyMilkPurchaseRateServiceImpl implements SocietyMilkPurchaseRateService {

    @Autowired
    private SocietyMilkPurchaseRateRepository societyMilkPurchaseRateRepository;
    @Autowired
    private SocietyMilkPurchaseRateDetailRepository societyMilkPurchaseRateDetailRepository;
    @Autowired
    private SocietyMilkPurchaseRateApplicabilityRepository societyMilkPurchaseRateApplicabilityRepository;
    @Autowired
    private SocietyMilkPurchaseRateBasedRepository societyMilkPurchaseRateBasedRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;
    @Autowired
    private MilkQualityTypeRepository milkQualityRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;

    private Map<Integer, MilkType> mapMilkType;
    private Map<Integer, MilkQualityType> mapMilkQualityType;

    @Override
    public List<SocietyMilkPurchaseRate> findAll() {
        return societyMilkPurchaseRateRepository.findAll(Sort.by("wefDate").descending());
    }


    @Override
    @Transactional
    public String savePurchaseRate(SocietyMilkPurchaseRateDto dto) throws BusinessValidationFailException {
        Optional<SocietyMilkPurchaseRate> rate = societyMilkPurchaseRateRepository.findTop1ByWefDateGreaterThanEqual(dto.getPurchaseRate().getWefDate());
        if (rate.isPresent()) {
            FieldError wefDateNotValid = CommonUtil.getFieldError("SocietyMilkPurchaseRateDto", "wefDate",
                    dto.getPurchaseRate().getWefDate(), "wefdate.not.valid");
            throw new BusinessValidationFailException(getClass(), wefDateNotValid);
        }

        dto.getPurchaseRate().setCode(nextCodeRepository.getNextCode("SocietyMilkPurchaseRate", "code",
                "101", 0));
        dto.getPurchaseRate().setInitData();
        dto.setPurchaseRate(societyMilkPurchaseRateRepository.save(dto.getPurchaseRate()));

        if (dto.getListRateBased() != null) {
            int sr = 1;
            for (SocietyMilkPurchaseRateBased based : dto.getListRateBased()) {
                based.setInitData();
                based.setCode(dto.getPurchaseRate().getCode() + "B" + sr);
                sr++;
                based.setSocietyMilkPurchaseRate(dto.getPurchaseRate());
                societyMilkPurchaseRateBasedRepository.save(based);
            }
        }

        // Applicability
        AtomicInteger srno = new AtomicInteger(1);
        dto.getListApplicability().forEach(item -> {
            item.setCode(dto.getPurchaseRate().getCode() + "A" + (srno.getAndIncrement()));
            item.setSocietyMilkPurchaseRate(dto.getPurchaseRate());
            item.setInitData();
        });
        societyMilkPurchaseRateApplicabilityRepository.saveAll(dto.getListApplicability());

        // Details
        List<MilkType> listMilkType = milkTypeRepository.findAll();
        mapMilkType = new HashMap<>();
        listMilkType.forEach(item -> {
            mapMilkType.put(item.getCode(), item);
        });
        List<MilkQualityType> listMilkQualityType = milkQualityRepository.findAll();
        mapMilkQualityType = new HashMap<>();
        listMilkQualityType.forEach(item -> {
            mapMilkQualityType.put(item.getCode(), item);
        });

        AtomicInteger srno1 = new AtomicInteger(1);
        List<SocietyMilkPurchaseRateDetail> listDetails = new ArrayList<>();
        dto.getListDetail().forEach(item -> {
            String[] arr = item.split("#");
            SocietyMilkPurchaseRateDetail dtl = new SocietyMilkPurchaseRateDetail();
            dtl.setCode(dto.getPurchaseRate().getCode() + "D" + (srno1.getAndIncrement()));
            dtl.setFat(new BigDecimal(arr[0]));
            dtl.setSnf(new BigDecimal(arr[1]));
            dtl.setRate(new BigDecimal(arr[2]));
            dtl.setMilkType(getMilkType(arr[3]));
            dtl.setMilkQualityType(getMilkQualityType(arr[4]));
            dtl.setSocietyMilkPurchaseRate(dto.getPurchaseRate());
            dtl.setInitData();
            listDetails.add(dtl);
        });
        societyMilkPurchaseRateDetailRepository.saveAll(listDetails);
        return "Milk Purchase Rate Saved!";
    }

    @Override
    public List<String> fetchRateDetails(String code, Integer milkTypeCode, Integer milkQualityTypeCode) {
        Optional<SocietyMilkPurchaseRate> rate = societyMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        Optional<MilkType> milkType = milkTypeRepository.findById(milkTypeCode);
        if (!milkType.isPresent()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRate", "milkTypeCode", code,
                    "milktype.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        Optional<MilkQualityType> milkQualityType = milkQualityRepository.findById(milkQualityTypeCode);
        if (!milkQualityType.isPresent()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRate", "milkQualityTypeCode", code,
                    "milkqualitytype.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        List<SocietyMilkPurchaseRateDetail> listDetails = societyMilkPurchaseRateDetailRepository.findBySocietyMilkPurchaseRateAndMilkTypeAndMilkQualityType(rate.get(), milkType.get(),
                milkQualityType.get(), Sort.by("fat", "snf"));
        if (listDetails == null || listDetails.isEmpty()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRateDetails", null, null,
                    "ratedetails.not.found");
            throw new BusinessValidationFailException(getClass(), error);
        }

        List<String> list = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#"
                + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode()).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<SocietyMilkPurchaseRateBased> fetchRateBased(String code) {
        Optional<SocietyMilkPurchaseRate> rate = societyMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }
        List<SocietyMilkPurchaseRateBased> list = societyMilkPurchaseRateBasedRepository.findBySocietyMilkPurchaseRate(rate.get());
        for (SocietyMilkPurchaseRateBased based : list) {
            based.setSocietyMilkPurchaseRate(rate.get());
        }
        return list;
    }

    private MilkQualityType getMilkQualityType(String milkQualityTypeCode) {
        return mapMilkQualityType.get(Integer.parseInt(milkQualityTypeCode));
    }

    private MilkType getMilkType(String milkTypeCode) {
        return mapMilkType.get(Integer.parseInt(milkTypeCode));
    }


    @Override
    public MilkDispatchRateAndDetailsDto fetchRateAndDetails(String code) {
        Optional<SocietyMilkPurchaseRate> rate = societyMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("SocietyMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        MilkDispatchRateAndDetailsDto dto = new MilkDispatchRateAndDetailsDto();
        dto.setSocietyMilkPurchaseRate(rate.get());

        List<MilkType> milkTypeList = milkTypeRepository.findAll();
        Optional<MilkQualityType> milkQualityType = milkQualityRepository.findById(1);

        Map<String, BigDecimal> map = new HashMap<>();
        for (MilkType type : milkTypeList) {
            List<SocietyMilkPurchaseRateDetail> listDetails = societyMilkPurchaseRateDetailRepository
                    .findBySocietyMilkPurchaseRateAndMilkTypeAndMilkQualityType(rate.get(), type, milkQualityType.get(),
                            Sort.by("fat", "snf"));
            if (listDetails != null && !listDetails.isEmpty()) {
                listDetails.forEach(item -> {
                    map.put(item.getFat() + "#" + item.getSnf() + "#" + type.getCode() + "#"
                            + milkQualityType.get().getCode(), item.getRate());
                });
            }
        }

        dto.setDetails(map);
        return dto;
    }
}
