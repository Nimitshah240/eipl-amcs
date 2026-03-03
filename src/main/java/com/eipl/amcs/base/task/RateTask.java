package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.service.MilkQualityTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.global.service.RateTypeService;
import com.eipl.amcs.master.global.service.ShiftService;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.model.*;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.network.RealTimeMultipleResponse;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.operation.procurement.task.DpuIncentiveSaveTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class RateTask extends Task<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateTask.class);
    @Autowired
    private final SocietyMilkPurchaseRateService societyMilkPurchaseRateService;
    @Autowired
    private final ShiftService shiftService;
    @Autowired
    private final RateTypeService rateTypeService;
    @Autowired
    private final MilkTypeService milkTypeService;
    @Autowired
    private final MilkQualityTypeService milkQualityTypeService;
    @Autowired
    private final FormulaRepository formulaRepository;
    @Autowired
    private final MemberMilkPurchaseRateService memberMilkPurchaseRateService;

    String memberApplicableRate = null;
    String bmcApplicableRate = null;

    public RateTask() {
        formulaRepository = EmcsAppContext.getContext().getBean(FormulaRepository.class);
        milkQualityTypeService = EmcsAppContext.getContext().getBean(MilkQualityTypeService.class);
        milkTypeService = EmcsAppContext.getContext().getBean(MilkTypeService.class);
        rateTypeService = EmcsAppContext.getContext().getBean(RateTypeService.class);
        shiftService = EmcsAppContext.getContext().getBean(ShiftService.class);
        societyMilkPurchaseRateService = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
        memberMilkPurchaseRateService = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
    }


    @Override
    protected Void call() throws Exception {

        Map<String, Object> mapOfStartUp = null;
        updateMessage("Checking Member Rate...");
        RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);


        LOGGER.info("Initiating startup api");
        String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.START_UP;
        LOGGER.info(url);
        RealTimeRequest<Map<String, Object>> requestPayloadStartup = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), null);
        requestPayloadStartup.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
        ResponseEntity<RealTimeResponse> responseStartUp = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayloadStartup), RealTimeResponse.class);
        if (responseStartUp.getStatusCode() != HttpStatus.OK)
            return null;

        RealTimeResponse respBody = responseStartUp.getBody();
        if (!"success".equalsIgnoreCase(respBody.getStatus()))
            return null;
        LOGGER.info("Fetching startup data successful : " + respBody.getData().toString());
        mapOfStartUp = respBody.getData();
        Map<String, Object> mapOfCollectionConfig = (Map<String, Object>) mapOfStartUp.get("collectionConfig");
        var task = new DpuIncentiveSaveTask(mapOfCollectionConfig);
        task.setOnSucceeded(e -> {
            try {
                task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        Map<String, Object> mapOfRate = (Map<String, Object>) mapOfStartUp.get("rate");
        LOGGER.info("Received Rate map :" + mapOfRate);

        memberApplicableRate = mapOfRate.get("memberApplicableRate").toString();
        bmcApplicableRate = mapOfRate.get("bmcApplicableRate").toString();
        LOGGER.info("Received memberApplicableRate :" + memberApplicableRate);
        LOGGER.info("Received bmcApplicableRate :" + bmcApplicableRate);


        RealTimeRequest<Map<String, String>> requestPayload;
        ResponseEntity<RealTimeResponse> response;
        RealTimeResponse responseRate;

        //shift
        List<Shift> shiftList = shiftService.findAll();
        Map<Integer, Shift> mapShift = new HashMap<>();
        for (Shift shift : shiftList) {
            mapShift.put(shift.getCode(), shift);
        }

        // ratetype
        List<RateType> rateTypeList = rateTypeService.findAll();
        Map<Integer, RateType> mapRateType = new HashMap<>();
        for (RateType rateType : rateTypeList) {
            mapRateType.put(rateType.getCode(), rateType);
        }

        // milktype
        List<MilkType> milkTypeList = milkTypeService.findAll();
        Map<Integer, MilkType> mapMilkType = new HashMap<>();
        for (MilkType milkType : milkTypeList) {
            mapMilkType.put(milkType.getCode(), milkType);
        }

        // milk quality type
        List<MilkQualityType> milkQualityTypeList = milkQualityTypeService.findAll();
        Map<Integer, MilkQualityType> mapMilkQuality = new HashMap<>();
        for (MilkQualityType milkQualityType : milkQualityTypeList) {
            mapMilkQuality.put(milkQualityType.getCode(), milkQualityType);
        }

        // Formula
        List<Formula> formulaList = formulaRepository.findAll();
        Map<String, Formula> mapFormula = new HashMap<>();
        for (Formula formula : formulaList) {
            mapFormula.put(formula.getCode(), formula);
        }

        Map<String, Object> data = new HashMap<>();
        MemberMilkPurchaseRateDto memberRateDto = new MemberMilkPurchaseRateDto();
//            // Rate
        Map<String, Object> purchaseRate = new HashMap<>();
        List<String> listOfRate = new ArrayList<>();
        if (memberApplicableRate.contains(","))
            listOfRate = List.of(memberApplicableRate.split(","));
        else
            listOfRate.add(memberApplicableRate);

        for (String rateCode : listOfRate) {
            try {
                Map<String, String> contentRate = new HashMap<>();
                contentRate.put("rateType", "MEMBER");
                contentRate.put("purchaseRateCode", rateCode);
                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                        MainApp.identityDto.getIdentity().getToken(), contentRate);

                int a = 1;
                while (a == 1 || purchaseRate == null) {
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;
                    response = restTemplate.exchange(url, HttpMethod.POST,
                            new HttpEntity<>(requestPayload), RealTimeResponse.class);
                    if (response.getStatusCode() != HttpStatus.OK)
                        return null;
                    responseRate = response.getBody();
                    if (responseRate == null || !"success".equalsIgnoreCase(responseRate.getStatus()))
                        return null;


                    data = responseRate.getData();
                    memberRateDto = new MemberMilkPurchaseRateDto();
                    // Rate
                    purchaseRate = (Map) data.get("purchaseRate");

                    if (purchaseRate != null) {
                        LOGGER.info("Member milk purchase rate download: {}", purchaseRate.get("purchaseRateCode"));
                        MemberMilkPurchaseRate rate = new MemberMilkPurchaseRate();
                        rate.setDescription(purchaseRate.get("description").toString());
                        rate.setRateGenMethodCode((short) 1);
                        rate.setShift(mapShift.get((int) purchaseRate.get("shiftId")));
                        rate.setShiftApplicable(mapShift.get((int) purchaseRate.get("shiftApplicability")));
                        rate.setUnionCode(MainApp.identityDto.getUnion().getCode());
                        rate.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(LocalDate.parse(purchaseRate.get("wefDate").toString().split(" ")[0]), rate.getShift()));
                        rate.setSociety(MainApp.identityDto.getSociety());
                        rate.setxCol1("0-0");


                        // Based
                        List<Map<String, Object>> basedList = (List) data.get("purchaseRateBased");
                        LOGGER.info("Member milk purchase rate based: {}", basedList.size());
                        List<MemberMilkPurchaseRateBased> listMemberRateBased = new ArrayList<>();
                        for (Map<String, Object> map : basedList) {
                            MemberMilkPurchaseRateBased based = new MemberMilkPurchaseRateBased();
                            based.setRateType(map.get("rateTypeCode") == null ? 1 : (int) map.get("rateTypeCode"));
                            based.setQualityParam((int) map.get("qualityParamCode"));
                            based.setStartVal(new BigDecimal(map.get("startRange").toString()));
                            based.setEndVal(new BigDecimal(map.get("endRange").toString()));
                            based.setKgRate(new BigDecimal(map.get("kgRate").toString()));
                            based.setDeductionType((int) map.get("deductionType"));
                            based.setRefType((int) map.get("refType"));
                            based.setVal(new BigDecimal(map.get("value").toString()));
                            based.setFixedPoint(new BigDecimal(map.get("fixedPoint").toString()));
                            based.setStep((int) map.get("step"));
                            based.setFormula(map.get("formulaCode") == null ? null : mapFormula.get(map.get("formulaCode").toString()));
                            based.setMilkType(mapMilkType.get((int) map.get("milkTypeCode")));
                            based.setMilkQualityType(mapMilkQuality.get(1));
                            listMemberRateBased.add(based);
                        }
                        memberRateDto.setListRateBased(listMemberRateBased);

                        if (!listMemberRateBased.isEmpty()) {
                            rate.setRateType(mapRateType.get(listMemberRateBased.get(0).getRateType()));
                        }
                        memberRateDto.setPurchaseRate(rate);

                        // Applicability
                        List<Map<String, Object>> appList = (List) data.get("purchaseRateApplicabilityMultiple");
                        LOGGER.info("Member milk purchase rate applicability: {}", appList.size());
                        List<MemberMilkPurchaseRateApplicability> applicabilityList = new ArrayList<>();
                        StringBuilder appCode = new StringBuilder();
                        for (Map<String, Object> map : appList) {
                            MemberMilkPurchaseRateApplicability app = new MemberMilkPurchaseRateApplicability();
                            app.setShift(mapShift.get((int) map.get("shiftCode")));
                            app.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(
                                    LocalDate.parse(map.get("wefDate").toString().split(" ")[0]), app.getShift()));
                            app.setUnionCode(MainApp.identityDto.getUnion().getCode());
                            app.setSociety(MainApp.identityDto.getSociety());
                            applicabilityList.add(app);
                            appCode.append(map.get("rateAppCode").toString() + ",");
                        }
                        memberRateDto.setListApplicability(applicabilityList);

                        // Rate detail download

                        url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DETAIL_DOWNLOAD;
                        List<String> listRateDetails = new ArrayList<>();
                        for (MilkType milkType : milkTypeList) {
                            try {
                                Map<String, String> contentRateDetail = new HashMap<>();
                                contentRateDetail.put("purchaseRateCode", purchaseRate.get("purchaseRateCode").toString());
                                contentRateDetail.put("milkQualityTypeCode", "1");
                                contentRateDetail.put("milkTypeCode", milkType.getCode().toString());
                                contentRateDetail.put("rateType", "MEMBER");
//                            contentRateDetail.put("rateClass", "0");
                                LOGGER.info("purchaseRateCode : {}, milkQualityTypeCode : {}, milkTypeCode : {}, rateType : {}", purchaseRate.get("purchaseRateCode").toString(), "1", milkType.getCode().toString(), "MEMBER");
                                updateMessage("Download rate " + purchaseRate.get("purchaseRateCode").toString() + "(" + milkType + ")");

                                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), contentRateDetail);
                                requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                                ResponseEntity<RealTimeMultipleResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(requestPayload), RealTimeMultipleResponse.class);
                                if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                    return null;
                                RealTimeMultipleResponse respRateDtl = responseRateDtl.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return null;
//                            Map<String, Object> dataDtl = respRateDtl.getData();
                                List<String> listStr = respRateDtl.getData();
                                LOGGER.info("Member milk purchase rate detail: {}-{}", milkType.getName(), listStr.size());
                                if (listStr != null && !listStr.isEmpty()) {
                                    for (String s : listStr) {
                                        String[] arr = s.split("#");
                                        String sb = arr[0] +
                                                "#" +
                                                arr[1] +
                                                "#" +
                                                arr[2] +
                                                "#" +
                                                milkType.getCode() +
                                                "#" +
                                                "1";
                                        listRateDetails.add(sb);
                                    }
                                }
                            } catch (Exception e) {
                                LOGGER.error("DETAIL ERROR : "+ e.getMessage());
                            }
                        }
                        memberRateDto.setListDetail(listRateDetails);

                        // Save member Rate

                        try {
                            String responseRateSave = memberMilkPurchaseRateService.savePurchaseRate(memberRateDto);
                            if (responseRateSave == null)
                                return null;

                            LOGGER.info("Member milk rate save: {}", responseRateSave);
                            if (responseRateSave.equalsIgnoreCase("Milk Purchase Rate Saved!")) {
                                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                                Map<String, String> contentRateAck = new HashMap<>();
                                contentRateAck.put("rateAppCode", appCode.substring(0, appCode.toString().length() - 1));
                                contentRateAck.put("rateType", "MEMBER");
                                LOGGER.info("Member milk ack for app: {}", contentRateAck.get("rateAppCode"));
                                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), contentRateAck);
                                requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                                ResponseEntity<RealTimeResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(requestPayload), RealTimeResponse.class);
                                if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                    return null;
                                RealTimeResponse respRateDtl = responseRateDtl.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return null;
                                LOGGER.info("Member milk ack success for codes: {}", contentRateAck.get("rateAppCode"));
                                updateMessage("Member rate saved successfully");
                            } else {
                            }
                        } catch (Exception ex) {
                            if (ex.getMessage().contains("wefdate.not.valid")) {
                                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                                Map<String, String> contentRateAck = new HashMap<>();
                                contentRateAck.put("rateAppCode", appCode.substring(0, appCode.toString().length() - 1));
                                contentRateAck.put("rateType", "MEMBER");
                                LOGGER.info("Member milk ack for app: {}", contentRateAck.get("rateAppCode"));
                                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), contentRateAck);
                                requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                                ResponseEntity<RealTimeResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(requestPayload), RealTimeResponse.class);
                                if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                    return null;
                                RealTimeResponse respRateDtl = responseRateDtl.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return null;
                                LOGGER.info("Member milk ack success for codes: {}", contentRateAck.get("rateAppCode"));
                            }
                        }
                    } else {
                        LOGGER.info("No Member Rate Found");
                        break;
                    }
                    a++;
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        try {
            // **********************************
            // Society Rate Download and start
            // **********************************
            Map<String, String> content = null;
            RealTimeRequest<Map<String, String>> payload = null;
            ResponseEntity<RealTimeResponse> socRateResponse = null;
            RealTimeResponse socRateResp = null;

            url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;

            content = new HashMap<>();
            content.put("rateType", "BMC");
            content.put("purchaseRateCode", bmcApplicableRate);
            payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                    MainApp.identityDto.getIdentity().getToken(), content);
            updateMessage("Society rate check...");
            SocietyMilkPurchaseRateDto socRateDto = new SocietyMilkPurchaseRateDto();
            // Rate
            Map<String, Object> sRate = new HashMap<>();
            int b = 1;
            while (b == 1 || sRate == null) {
                payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                        MainApp.identityDto.getIdentity().getToken(), content);
                payload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;
                socRateResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
                if (socRateResponse.getStatusCode() != HttpStatus.OK)
                    return null;
                socRateResp = socRateResponse.getBody();
                if (socRateResp == null || !"success".equalsIgnoreCase(socRateResp.getStatus()))
                    return null;
                data = socRateResp.getData();
                socRateDto = new SocietyMilkPurchaseRateDto();
                // Rate
                sRate = (Map) data.get("purchaseRate");
                if (sRate != null) {
                    LOGGER.info("Society milk purchase rate download: {}", sRate.get("purchaseRateCode"));
                    SocietyMilkPurchaseRate rate1 = new SocietyMilkPurchaseRate();
                    rate1.setDescription(sRate.get("description").toString());
                    rate1.setRateGenMethodCode(((Integer) sRate.get("rateGenMethodCode")).shortValue());
                    rate1.setShift(mapShift.get((int) sRate.get("shiftId")));
                    rate1.setShiftApplicable(mapShift.get((int) sRate.get("shiftApplicability")));
                    rate1.setUnionCode(MainApp.identityDto.getUnion().getCode());
                    rate1.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(LocalDate.parse(sRate.get("wefDate").toString().split(" ")[0]), rate1.getShift()));
                    rate1.setRateType(sRate.get("rateType") == null ? mapRateType.get(2) : mapRateType.get((int) sRate.get("rateType")));
                    socRateDto.setPurchaseRate(rate1);

                    // Based
                    List<Map<String, Object>> basedList1 = (List) data.get("purchaseRateBased");
                    LOGGER.info("Society milk purchase rate based: {}", basedList1.size());
                    List<SocietyMilkPurchaseRateBased> listMemberRateBased1 = new ArrayList<>();
                    for (Map<String, Object> map : basedList1) {
                        SocietyMilkPurchaseRateBased based = new SocietyMilkPurchaseRateBased();
                        based.setRateType(rate1.getRateType().getCode());
                        based.setQualityParam((int) map.get("qualityParamCode"));
                        based.setStartVal(new BigDecimal(map.get("startRange").toString()));
                        based.setEndVal(new BigDecimal(map.get("endRange").toString()));
                        based.setKgRate(new BigDecimal(map.get("kgRate").toString()));
                        based.setDeductionType((int) map.get("deductionType"));
                        based.setRefType((int) map.get("refType"));
                        based.setVal(new BigDecimal(map.get("value").toString()));
                        based.setFixedPoint(new BigDecimal(map.get("fixedPoint").toString()));
                        based.setStep((int) map.get("step"));
                        based.setFormula(map.get("formulaCode") == null ? null : mapFormula.get(map.get("formulaCode").toString()));
                        based.setMilkType(mapMilkType.get((int) map.get("milkTypeCode")));
                        based.setMilkQualityType(mapMilkQuality.get((int) map.get("milkQualityTypeCode")));
                        listMemberRateBased1.add(based);
                    }
                    socRateDto.setListRateBased(listMemberRateBased1);

                    // Applicability
                    List<Map<String, Object>> appList1 = (List) data.get("purchaseRateApplicabilityMultiple");
                    LOGGER.info("Society milk purchase rate applicability: {}", appList1.size());
                    List<SocietyMilkPurchaseRateApplicability> applicabilityList1 = new ArrayList<>();
                    StringBuilder appCode1 = new StringBuilder();
                    for (Map<String, Object> map : appList1) {
                        SocietyMilkPurchaseRateApplicability app = new SocietyMilkPurchaseRateApplicability();
                        app.setShift(mapShift.get((int) map.get("shiftCode")));
                        app.setWefDate(CommonUtils.getLocalDateTimeFromDateAndShift(
                                LocalDate.parse(map.get("wefDate").toString().split(" ")[0]), app.getShift()));
                        app.setUnionCode(MainApp.identityDto.getUnion().getCode());
                        app.setSociety(MainApp.identityDto.getSociety());
                        applicabilityList1.add(app);
                        appCode1.append(map.get("rateAppCode").toString() + ",");
                    }
                    socRateDto.setListApplicability(applicabilityList1);

                    // Details
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DETAIL_DOWNLOAD;
                    List<String> listSocRateDetails = new ArrayList<>();
                    for (MilkType milkType : milkTypeList) {
                        content = new HashMap<>();
//                        content.put("purchaseRateCode", sRate.get("purchaseRateCode").toString());
                        content.put("purchaseRateCode", bmcApplicableRate);
                        content.put("milkQualityTypeCode", "1");
                        content.put("milkTypeCode", milkType.getCode().toString());
                        content.put("rateType", "BMC");
//                        content.put("rateClass", "0");

                        updateMessage("Download rate " + sRate.get("purchaseRateCode").toString() + "(" + milkType.toString() + ")");

                        payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                MainApp.identityDto.getIdentity().getToken(), content);
                        payload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                        socRateResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
                        if (socRateResponse.getStatusCode() != HttpStatus.OK)
                            return null;
                        RealTimeResponse respRateDtl = socRateResponse.getBody();
                        if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                            return null;
                        Map<String, Object> dataDtl = respRateDtl.getData();
                        List<String> listStr = (List) dataDtl.get("rateDetail");
                        LOGGER.info("Society milk purchase rate detail: {}-{}", milkType.getName(), listStr.size());
                        if (listStr != null && !listStr.isEmpty()) {
                            for (String s : listStr) {
                                String[] arr = s.split("#");
                                String sb = arr[0] +
                                        "#" +
                                        arr[1] +
                                        "#" +
                                        arr[2] +
                                        "#" +
                                        milkType.getCode() +
                                        "#" +
                                        "1";
                                listSocRateDetails.add(sb);
                            }
                        }
                    }
                    socRateDto.setListDetail(listSocRateDetails);

                    try {

                        // Save society Rate
                        String societyRateSave = societyMilkPurchaseRateService.savePurchaseRate(socRateDto);
                        if (societyRateSave == null)
                            return null;
                        LOGGER.info("Society milk rate save: {}", societyRateSave);
                        if (societyRateSave.equalsIgnoreCase("Milk Purchase Rate Saved!")) {
                            url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                            content = new HashMap<>();
                            content.put("rateAppCode", appCode1.substring(0, appCode1.toString().length() - 1));
                            content.put("rateType", "BMC");
                            LOGGER.info("Society milk ack for app: {}", content.get("rateAppCode"));
                            payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                    MainApp.identityDto.getIdentity().getToken(), content);
                            payload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());

                            socRateResponse = restTemplate.exchange(url, HttpMethod.POST,
                                    new HttpEntity<>(payload), RealTimeResponse.class);
                            if (socRateResponse.getStatusCode() != HttpStatus.OK)
                                return null;
                            RealTimeResponse respRateDtl = socRateResponse.getBody();
                            if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                return null;
                            LOGGER.info("Society milk ack success for codes: {}", content.get("rateAppCode"));
                            updateMessage("Society Rate saved successfully");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                        content = new HashMap<>();
                        content.put("rateAppCode", appCode1.substring(0, appCode1.toString().length() - 1));
                        content.put("rateType", "BMC");
                        LOGGER.info("Society milk ack for app: {}", content.get("rateAppCode"));
                        payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                MainApp.identityDto.getIdentity().getToken(), content);
                        payload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                        socRateResponse = restTemplate.exchange(url, HttpMethod.POST,
                                new HttpEntity<>(payload), RealTimeResponse.class);
                        if (socRateResponse.getStatusCode() != HttpStatus.OK)
                            return null;
                        RealTimeResponse respRateDtl = socRateResponse.getBody();
                        if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                            return null;
                    }

                } else {
                    LOGGER.info("No Society Rate Found");
                    break;
                }

            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}