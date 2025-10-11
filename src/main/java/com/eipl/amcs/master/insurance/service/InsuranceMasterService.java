package com.eipl.amcs.master.insurance.service;

import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;

import java.util.List;

public interface InsuranceMasterService {

    List<InsuranceMaster> findAll();

    List<InsuranceDetail> findInsuranceDetailByInsuranceMaster(Integer insuranceMasterCode);

    InsuranceDetailSummary findInsuranceDetailSummaryByInsuranceMaster(Integer insuranceMasterCode);

    InsuranceDetail saveDetails(InsuranceDetail insuranceDetail, String identityInfo);

    InsuranceDetail saveDetailsFinalize(InsuranceDetail insuranceDetail, String identityInfo);

    InsuranceDetailSummary saveDetailsSumamry(InsuranceDetailSummary insuranceDetailSummary, String identityInfo);

    InsuranceMaster saveMaster(InsuranceMaster insuranceMaster, String identityInfo);

    InsuranceDetail updateDetails(InsuranceDetail insuranceDetail, String identityInfo);

    InsuranceDetail updateDetailsFinalize(InsuranceDetail insuranceDetail, String identityInfo);

    InsuranceDetailSummary updateInsuranceSummaryDetails(InsuranceDetailSummary insuranceDetailSummary, String identityInfo);

    InsuranceDetail deleteDetails(InsuranceDetail insuranceDetail, String identityInfo);


    List<InsuranceDetail> findDeletedInsuranceDetailByInsuranceMaster(Integer insuranceMasterCode);

}
