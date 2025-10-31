package com.eipl.amcs.master.insurance.service;

import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceDetailSummaryRepository;
import com.eipl.amcs.master.insurance.repository.InsuranceMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class InsuranceMasterServiceImpl implements InsuranceMasterService {

    private static final Logger log = LoggerFactory.getLogger(InsuranceMasterServiceImpl.class);
    @Autowired
    private InsuranceMasterRepository insuranceMasterRepository;
    @Autowired
    private InsuranceDetailSummaryRepository insuranceDetailSummaryRepository;
    @Autowired
    private InsuranceDetailRepository insuranceDetailRepository;

    @Override
    public List<InsuranceMaster> findAll() {
        List<InsuranceMaster> list = insuranceMasterRepository.findAll();
        log.info("InsuranceMaster findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<InsuranceDetail> findInsuranceDetailByInsuranceMaster(Integer insuranceMasterCode) {
        return insuranceDetailRepository.findByInsuranceMasterCodeAndIsDelete(insuranceMasterCode, false, Sort.by("memberCode"));
    }

    @Override
    public InsuranceDetailSummary findInsuranceDetailSummaryByInsuranceMaster(Integer insuranceMasterCode) {
        return insuranceDetailSummaryRepository.findByInsuranceMasterCode(insuranceMasterCode);
    }

    @Override
    public InsuranceDetail saveDetails(InsuranceDetail insuranceDetail, String identityInfo) {
        insuranceDetail.setInitData();
        insuranceDetail.setIsDelete(false);
        insuranceDetail.setxCol1(UUID.randomUUID().toString());
        insuranceDetail.setOriginatingOrgType("VLC");
        insuranceDetail.setCreatedBy(insuranceDetail.getDcsCode());
        insuranceDetail.setCreatedAt(LocalDateTime.now());
        insuranceDetail.setUpdatedBy(null);
        return insuranceDetailRepository.customSave(insuranceDetail, identityInfo);
    }

    @Override
    public InsuranceDetail saveDetailsFinalize(InsuranceDetail insuranceDetail, String identityInfo) {
        insuranceDetail.setInitData();
        insuranceDetail.setIsDelete(false);
        insuranceDetail.setxCol1(UUID.randomUUID().toString());
        return insuranceDetailRepository.customSave(insuranceDetail, identityInfo);
    }

    @Override
    public InsuranceDetailSummary saveDetailsSumamry(InsuranceDetailSummary insuranceDetailSummary, String identityInfo) {
        insuranceDetailSummary.setInitData();
        return insuranceDetailSummaryRepository.customSave(insuranceDetailSummary, identityInfo);
    }

    @Override
    public InsuranceMaster saveMaster(InsuranceMaster insuranceMaster, String identityInfo) {
        return insuranceMasterRepository.customSave(insuranceMaster, identityInfo);
    }

    @Override
    public InsuranceDetail updateDetails(InsuranceDetail insuranceDetail, String identityInfo) {
        insuranceDetail.setupdateData();
        insuranceDetail.setIsDelete(false);
        insuranceDetail.setOriginatingOrgType("VLC");
        insuranceDetail.setUpdatedBy(insuranceDetail.getDcsCode());
        insuranceDetail.setSysUpdatedBy("VLC");
        insuranceDetail.setStatus("PUBLISH");
        return insuranceDetailRepository.customUpdate(insuranceDetail, identityInfo);
    }

    @Override
    public InsuranceDetail updateDetailsFinalize(InsuranceDetail insuranceDetail, String identityInfo) {
        insuranceDetail.setupdateData();
        insuranceDetail.setIsDelete(false);
        return insuranceDetailRepository.customUpdate(insuranceDetail, identityInfo);
    }

    @Override
    public InsuranceDetailSummary updateInsuranceSummaryDetails(InsuranceDetailSummary insuranceDetailSummary, String identityInfo) {
        insuranceDetailSummary.setupdateData();
        return insuranceDetailSummaryRepository.customUpdate(insuranceDetailSummary, identityInfo);
    }

    @Override
    public InsuranceDetail deleteDetails(InsuranceDetail insuranceDetail, String identityInfo) {
        insuranceDetail.setupdateData();
        insuranceDetail.setStatus("PARTIAL_FINALIZE");
        insuranceDetail.setOriginatingOrgType("VLC");
        insuranceDetail.setOriginatingOrgCode(insuranceDetail.getDcsCode());
        insuranceDetail.setIsDelete(true);
        return insuranceDetailRepository.customUpdate(insuranceDetail, identityInfo);
    }

    @Override
    public List<InsuranceDetail> findDeletedInsuranceDetailByInsuranceMaster(Integer insuranceMasterCode) {
        return insuranceDetailRepository.findByInsuranceMasterCodeAndIsDelete(insuranceMasterCode, true, Sort.by("memberCode"));
    }


}
