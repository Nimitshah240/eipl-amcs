package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.repository.MilkQualityTypeRepository;
import com.eipl.amcs.master.global.repository.MilkTypeRepository;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateDetail;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateApplicabilityRepository;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateBasedRepository;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateDetailRepository;
import com.eipl.amcs.master.procurement.repository.MemberMilkPurchaseRateRepository;
import com.eipl.amcs.operation.procurement.dto.MilkRateAndDetailsDto;
import com.eipl.amcs.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MemberMilkPurchaseRateServiceImpl implements MemberMilkPurchaseRateService {

    private final static Logger logger = LoggerFactory.getLogger(MemberMilkPurchaseRateServiceImpl.class);
    @Autowired
    private MemberMilkPurchaseRateRepository memberMilkPurchaseRateRepository;
    @Autowired
    private MemberMilkPurchaseRateApplicabilityRepository appRepository;
    @Autowired
    private MemberMilkPurchaseRateDetailRepository dtlRepository;
    @Autowired
    private MemberMilkPurchaseRateBasedRepository basedRepository;
    @Autowired
    private MilkTypeRepository milkTypeRepository;
    @Autowired
    private MilkQualityTypeRepository milkQualityRepository;
    @Autowired
    private NextCodeRepository nextCodeRepository;
    private Map<Integer, MilkType> mapMilkType;
    private Map<Integer, MilkQualityType> mapMilkQualityType;

    @Override
    public List<MemberMilkPurchaseRate> findAll() {
        return memberMilkPurchaseRateRepository.findAll(Sort.by("wefDate").descending());
    }

    @Override
    @Transactional
    public String savePurchaseRate(MemberMilkPurchaseRateDto dto) throws BusinessValidationFailException {
        Optional<MemberMilkPurchaseRate> rate = memberMilkPurchaseRateRepository.findTop1BySocietyAndWefDateGreaterThanEqualOrderByWefDateDesc(
                dto.getPurchaseRate().getSociety(), dto.getPurchaseRate().getWefDate());
        if (rate.isPresent()) {
            logger.warn("Member milk purchase rate already available from: {}", rate.get().getWefDate().toString());
            FieldError wefDateNotValid = CommonUtil.getFieldError("MemberMilkPurchaseRateDto", "wefDate",
                    dto.getPurchaseRate().getWefDate(), "wefdate.not.valid");
            throw new BusinessValidationFailException(getClass(), wefDateNotValid);
        }

        dto.getPurchaseRate().setCode(nextCodeRepository.getNextCode("MemberMilkPurchaseRate", "code",
                dto.getPurchaseRate().getSociety().getCode(), 0));
        dto.getPurchaseRate().setInitData();
        dto.setPurchaseRate(memberMilkPurchaseRateRepository.save(dto.getPurchaseRate()));

        // Based
        if (dto.getListRateBased() != null) {
            int a = 1;
            for (MemberMilkPurchaseRateBased based : dto.getListRateBased()) {
                based.setCode(dto.getPurchaseRate().getCode() + "B" + a);
                a++;
                based.setInitData();
                based.setMemberMilkPurchaseRate(dto.getPurchaseRate());
                basedRepository.save(based);
            }
        }

        // Applicability
        AtomicInteger srno = new AtomicInteger(1);
        dto.getListApplicability().forEach(item -> {
            item.setCode(dto.getPurchaseRate().getCode() + "A" + (srno.getAndIncrement()));
            item.setMemberMilkPurchaseRate(dto.getPurchaseRate());
            item.setInitData();
        });
        appRepository.saveAll(dto.getListApplicability());

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
        List<MemberMilkPurchaseRateDetail> listDetails = new ArrayList<>();
        dto.getListDetail().forEach(item -> {
            String[] arr = item.split("#");
            MemberMilkPurchaseRateDetail dtl = new MemberMilkPurchaseRateDetail();
            dtl.setCode(dto.getPurchaseRate().getCode() + "D" + (srno1.getAndIncrement()));
            dtl.setFat(new BigDecimal(arr[0]));
            dtl.setSnf(new BigDecimal(arr[1]));
            dtl.setRate(new BigDecimal(arr[2]));
            dtl.setMilkType(getMilkType(arr[3]));
            dtl.setMilkQualityType(getMilkQualityType(arr[4]));
            dtl.setMemberMilkPurchaseRate(dto.getPurchaseRate());
            dtl.setInitData();
            listDetails.add(dtl);
        });
        dtlRepository.saveAll(listDetails);
        logger.info("Member milk purchase rate saved with code: {}-{}", dto.getPurchaseRate().getCode(),
                dto.getPurchaseRate().getWefDate().toString());
        return "Milk Purchase Rate Saved!";
    }

    @Override
    public List<String> fetchRateDetails(String code, Integer milkTypeCode, Integer milkQualityTypeCode) {
        Optional<MemberMilkPurchaseRate> rate = memberMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        Optional<MilkType> milkType = milkTypeRepository.findById(milkTypeCode);
        if (!milkType.isPresent()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRate", "milkTypeCode", code,
                    "milktype.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        Optional<MilkQualityType> milkQualityType = milkQualityRepository.findById(milkQualityTypeCode);
        if (!milkQualityType.isPresent()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRate", "milkQualityTypeCode", code,
                    "milkqualitytype.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        List<MemberMilkPurchaseRateDetail> listDetails = dtlRepository
                .findByMemberMilkPurchaseRateAndMilkTypeAndMilkQualityType(rate.get(), milkType.get(),
                        milkQualityType.get(), Sort.by("fat", "snf"));
        if (listDetails == null || listDetails.isEmpty()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRateDetails", null, null,
                    "ratedetails.not.found");
            throw new BusinessValidationFailException(getClass(), error);
        }

        List<String> list = listDetails.stream().map(m -> m.getFat() + "#" + m.getSnf() + "#" + m.getRate() + "#"
                + m.getMilkType().getCode() + "#" + m.getMilkQualityType().getCode()).collect(Collectors.toList());
        return list;
    }

    private MilkQualityType getMilkQualityType(String milkQualityTypeCode) {
        return mapMilkQualityType.get(Integer.parseInt(milkQualityTypeCode));
    }

    private MilkType getMilkType(String milkTypeCode) {
        return mapMilkType.get(Integer.parseInt(milkTypeCode));
    }

    @Override
    public List<MemberMilkPurchaseRateBased> fetchRateBased(String code) {
        Optional<MemberMilkPurchaseRate> rate = memberMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }
        return basedRepository.findByMemberMilkPurchaseRate(rate.get());
    }

    @Override
    public MilkRateAndDetailsDto fetchRateAndDetails(String code) {
        Optional<MemberMilkPurchaseRate> rate = memberMilkPurchaseRateRepository.findById(code);
        if (!rate.isPresent()) {
            FieldError error = CommonUtil.getFieldError("MemberMilkPurchaseRate", "code", code, "ratecode.not.valid");
            throw new BusinessValidationFailException(getClass(), error);
        }

        MilkRateAndDetailsDto dto = new MilkRateAndDetailsDto();
        dto.setMemberPurchaseRate(rate.get());

        List<MilkType> milkTypeList = milkTypeRepository.findAll();
        Optional<MilkQualityType> milkQualityType = milkQualityRepository.findById(1);

        Map<String, BigDecimal> map = new HashMap<>();
        for (MilkType type : milkTypeList) {
            List<MemberMilkPurchaseRateDetail> listDetails = dtlRepository
                    .findByMemberMilkPurchaseRateAndMilkTypeAndMilkQualityType(rate.get(), type, milkQualityType.get(),
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
