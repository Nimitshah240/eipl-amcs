package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.service.IdentityService;
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
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.MainApp.context;

public class SplashController implements MyInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(SplashController.class);
    @FXML
    Label lbl;
    @FXML
    StackPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    private IdentityService identityService;
    private ShiftService shiftService;
    private RateTypeService rateTypeService;
    private MilkTypeService milkTypeService;
    private MilkQualityTypeService milkQualityTypeService;
    private MemberMilkPurchaseRateService memberMilkPurchaseRateService;
    private SocietyMilkPurchaseRateService societyMilkPurchaseRateService;
    private FormulaRepository formulaRepository;

    public SplashController() {
        try {
            formulaRepository = context.getBean(FormulaRepository.class);
            societyMilkPurchaseRateService = context.getBean(SocietyMilkPurchaseRateService.class);
            memberMilkPurchaseRateService = context.getBean(MemberMilkPurchaseRateService.class);
            milkQualityTypeService = context.getBean(MilkQualityTypeService.class);
            milkTypeService = context.getBean(MilkTypeService.class);
            rateTypeService = context.getBean(RateTypeService.class);
            shiftService = context.getBean(ShiftService.class);
            identityService = context.getBean(IdentityService.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var task = new AppInitTask();
        task.setOnSucceeded(e -> {
            try {
                File appProperty = new File("resources/app.properties");
                var resp = task.get();
                if (resp && appProperty.exists()) {
                    initializeIdentity();
                } else {
                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/Activation.fxml")));
                    lbl.setText("An error occurred!");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            } catch (Exception exception) {
            }
        });
        new Thread(task).start();
    }

    private void initializeIdentity() {

// -- MERGING ------------------------------------------
        MainApp.identityDto = identityService.fetchIdentity(MainApp.getProperty(AppConstant.Props.IDENTITY_DOCK, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_SOCIETY, null),
                MainApp.getProperty(AppConstant.Props.IDENTITY_UNION, null));

        if (MainApp.identityDto == null)
            lbl.setText("Society initialization error!");
        else {
//            RateTask rateTask = new RateTask();
//            rateTask.setOnSucceeded(e -> {
//            });
//            rateTask.setOnFailed(e -> {
//            });
//            new Thread(rateTask).start();
//            lbl.textProperty().bind(rateTask.messageProperty());


            try {
                MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));

                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;

                Map<String, String> contentRate = new HashMap<>();
                contentRate.put("rateType", "MEMBER");
                RealTimeRequest<Map<String, String>> requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                        MainApp.identityDto.getIdentity().getToken(), contentRate);
                ResponseEntity<RealTimeResponse> response;
                RealTimeResponse responseRate;

//----------------------------------------------------------------------------------------------------------
//                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//                    // Long-running task
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    }
//                    return "Task Completed!";
//                });

//                future.thenAccept(result -> System.out.println("Result: " + result));
//----------------------------------------------------------------------------------------------------------

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
                int a = 1;
                while (a == 1 || purchaseRate != null) {
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;

                    response = restTemplate.exchange(url, HttpMethod.POST,
                            new HttpEntity<>(requestPayload), RealTimeResponse.class);

                    if (response.getStatusCode() != HttpStatus.OK)
                        return;
                    responseRate = response.getBody();
                    if (responseRate == null || !"success".equalsIgnoreCase(responseRate.getStatus()))
                        return;


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
                        rate.setRateType(purchaseRate.get("rateType") == null ? mapRateType.get(1) : mapRateType.get((int) purchaseRate.get("rateType")));
                        rate.setxCol1("0-0");
                        memberRateDto.setPurchaseRate(rate);

                        // Based
                        List<Map<String, Object>> basedList = (List) data.get("purchaseRateBased");
                        LOGGER.info("Member milk purchase rate based: {}", basedList.size());
                        List<MemberMilkPurchaseRateBased> listMemberRateBased = new ArrayList<>();
                        for (Map<String, Object> map : basedList) {
                            MemberMilkPurchaseRateBased based = new MemberMilkPurchaseRateBased();
                            based.setRateType(1);
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
                            Map<String, String> contentRateDetail = new HashMap<>();
                            contentRateDetail.put("purchaseRateCode", purchaseRate.get("purchaseRateCode").toString());
                            contentRateDetail.put("milkQualityTypeCode", "1");
                            contentRateDetail.put("milkTypeCode", milkType.getCode().toString());
                            contentRateDetail.put("rateType", "MEMBER");
                            contentRateDetail.put("rateClass", "0");


                            requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                    MainApp.identityDto.getIdentity().getToken(), contentRateDetail);
                            ResponseEntity<RealTimeResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                    new HttpEntity<>(requestPayload), RealTimeResponse.class);
                            if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                return;
                            RealTimeResponse respRateDtl = responseRateDtl.getBody();
                            if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                return;
                            Map<String, Object> dataDtl = respRateDtl.getData();
                            List<String> listStr = (List) dataDtl.get("rateDetail");
                            LOGGER.info("Member milk purchase rate detail: {}-{}", milkType.getName(), listStr.size());
                            if (listStr != null && !listStr.isEmpty()) {
                                for (String s : listStr) {
                                    String[] arr = s.split("#");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(arr[0]);
                                    sb.append("#");
                                    sb.append(arr[1]);
                                    sb.append("#");
                                    sb.append(arr[2]);
                                    sb.append("#");
                                    sb.append(milkType.getCode());
                                    sb.append("#");
                                    sb.append("1");
                                    listRateDetails.add(sb.toString());
                                }
                            }
                        }
                        memberRateDto.setListDetail(listRateDetails);

                        // Save member Rate

                        try {
                            String responseRateSave = memberMilkPurchaseRateService.savePurchaseRate(memberRateDto);
                            LOGGER.info("Member milk rate save: {}", responseRateSave);
                            if (responseRateSave.equalsIgnoreCase("Milk Purchase Rate Saved!")) {
                                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                                Map<String, String> contentRateAck = new HashMap<>();
                                contentRateAck.put("rateAppCode", appCode.toString().substring(0, appCode.toString().length() - 1));
                                contentRateAck.put("rateType", "MEMBER");
                                LOGGER.info("Member milk ack for app: {}", contentRateAck.get("rateAppCode"));
                                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), contentRateAck);
                                ResponseEntity<RealTimeResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(requestPayload), RealTimeResponse.class);
                                if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                    return;
                                RealTimeResponse respRateDtl = responseRateDtl.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return;
                                LOGGER.info("Member milk ack success for codes: {}", contentRateAck.get("rateAppCode"));
                            } else {
                            }
                        } catch (Exception e) {
                            if (e.getMessage().contains("wefdate.not.valid")) {
                                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                                Map<String, String> contentRateAck = new HashMap<>();
                                contentRateAck.put("rateAppCode", appCode.toString().substring(0, appCode.toString().length() - 1));
                                contentRateAck.put("rateType", "MEMBER");
                                LOGGER.info("Member milk ack for app: {}", contentRateAck.get("rateAppCode"));
                                requestPayload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), contentRateAck);
                                ResponseEntity<RealTimeResponse> responseRateDtl = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(requestPayload), RealTimeResponse.class);
                                if (responseRateDtl.getStatusCode() != HttpStatus.OK)
                                    return;
                                RealTimeResponse respRateDtl = responseRateDtl.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return;
                                LOGGER.info("Member milk ack success for codes: {}", contentRateAck.get("rateAppCode"));
                            }
                        }
                    }
                }
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
                payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                        MainApp.identityDto.getIdentity().getToken(), content);

                SocietyMilkPurchaseRateDto socRateDto = new SocietyMilkPurchaseRateDto();
                // Rate
                Map<String, Object> sRate = (Map) data.get("purchaseRate");
                int b = 1;
                while (b == 1 || sRate != null) {
                    payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                            MainApp.identityDto.getIdentity().getToken(), content);
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD;
                    socRateResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
                    if (socRateResponse.getStatusCode() != HttpStatus.OK)
                        return;
                    socRateResp = socRateResponse.getBody();
                    if (socRateResp == null || !"success".equalsIgnoreCase(socRateResp.getStatus()))
                        return;
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
                            content.put("purchaseRateCode", sRate.get("purchaseRateCode").toString());
                            content.put("milkQualityTypeCode", "1");
                            content.put("milkTypeCode", milkType.getCode().toString());
                            content.put("rateType", "BMC");
                            content.put("rateClass", "0");


                            payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                    MainApp.identityDto.getIdentity().getToken(), content);
                            socRateResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(payload), RealTimeResponse.class);
                            if (socRateResponse.getStatusCode() != HttpStatus.OK)
                                return;
                            RealTimeResponse respRateDtl = socRateResponse.getBody();
                            if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                return;
                            Map<String, Object> dataDtl = respRateDtl.getData();
                            List<String> listStr = (List) dataDtl.get("rateDetail");
                            LOGGER.info("Society milk purchase rate detail: {}-{}", milkType.getName(), listStr.size());
                            if (listStr != null && !listStr.isEmpty()) {
                                for (String s : listStr) {
                                    String[] arr = s.split("#");
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(arr[0]);
                                    sb.append("#");
                                    sb.append(arr[1]);
                                    sb.append("#");
                                    sb.append(arr[2]);
                                    sb.append("#");
                                    sb.append(milkType.getCode());
                                    sb.append("#");
                                    sb.append("1");
                                    listSocRateDetails.add(sb.toString());
                                }
                            }
                        }
                        socRateDto.setListDetail(listSocRateDetails);

                        try {

                            // Save society Rate
                            String societyRateSave = societyMilkPurchaseRateService.savePurchaseRate(socRateDto);
                            LOGGER.info("Society milk rate save: {}", societyRateSave);
                            if (societyRateSave.equalsIgnoreCase("Milk Purchase Rate Saved!")) {
                                url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                                content = new HashMap<>();
                                content.put("rateAppCode", appCode1.toString().substring(0, appCode1.toString().length() - 1));
                                content.put("rateType", "BMC");
                                LOGGER.info("Society milk ack for app: {}", content.get("rateAppCode"));
                                payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                        MainApp.identityDto.getIdentity().getToken(), content);
                                socRateResponse = restTemplate.exchange(url, HttpMethod.POST,
                                        new HttpEntity<>(payload), RealTimeResponse.class);
                                if (socRateResponse.getStatusCode() != HttpStatus.OK)
                                    return;
                                RealTimeResponse respRateDtl = socRateResponse.getBody();
                                if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                    return;
                                LOGGER.info("Society milk ack success for codes: {}", content.get("rateAppCode"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.RATE_DOWNLOAD_ACK;
                            content = new HashMap<>();
                            content.put("rateAppCode", appCode1.toString().substring(0, appCode1.toString().length() - 1));
                            content.put("rateType", "BMC");
                            LOGGER.info("Society milk ack for app: {}", content.get("rateAppCode"));
                            payload = new RealTimeRequest<>(MainApp.identityDto.getIdentity().getSocietyRefCode(),
                                    MainApp.identityDto.getIdentity().getToken(), content);
                            socRateResponse = restTemplate.exchange(url, HttpMethod.POST,
                                    new HttpEntity<>(payload), RealTimeResponse.class);
                            if (socRateResponse.getStatusCode() != HttpStatus.OK)
                                return;
                            RealTimeResponse respRateDtl = socRateResponse.getBody();
                            if (respRateDtl == null || !"success".equalsIgnoreCase(respRateDtl.getStatus()))
                                return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
// -- MERGING ------------------------------------------


//        var task = new IdentityTask(MainApp.getProperty(AppConstant.Props.IDENTITY_DOCK, null),
//                MainApp.getProperty(AppConstant.Props.IDENTITY_SOCIETY, null),
//                MainApp.getProperty(AppConstant.Props.IDENTITY_UNION, null));

//        task.setOnSucceeded(t -> {
//            try {
//                MainApp.identityDto = task.get();
//                if (MainApp.identityDto == null)
//                    lbl.setText("Society initialization error!");
//                else {
//                    // check rate
//                    RateTask rateTask = new RateTask();
//                    rateTask.setOnSucceeded(e -> {
////                        MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
////                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));
//                    });
//                    rateTask.setOnFailed(e -> {
////                        MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
////                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));
//                    });
//                    new Thread(rateTask).start();
//                    lbl.textProperty().bind(rateTask.messageProperty());
//                    MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
//                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));
//
////                    MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "ABC");
////                    MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));
//
////                    List<String> systemIds = SystemUtils.getAllMac();
////                    LOGGER.info("System Ids {}", systemIds);
////                    if (systemIds.contains(MainApp.getProperty(AppConstant.Props.SYSTEM_ID, ""))) {
////                        MainApp.systemId = MainApp.getProperty(AppConstant.Props.SYSTEM_ID, "");
////                        MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/auth/Login.fxml")));
////                    } else
////                        lbl.setText("Society identity error!");
//                }
//            } catch (InterruptedException | ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
    }

    private void createAndSetLocale() {
    }


    class HealthCheckTask extends Task<String> {
        private String response = null;

        @Override
        protected String call() throws Exception {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + "/home";
            do {
                try {
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                            null, String.class);
                    if (response == null || response.getStatusCode() != HttpStatus.OK)
                        return null;
                    this.response = response.getBody();
                } catch (Exception e) {
                    LOGGER.info("Health check {}", e.getMessage());
                    response = e.getMessage();
                    Thread.sleep(5000);
                }
            } while (!"ok".equalsIgnoreCase(response));
            return response;
        }
    }

    class AppInitTask extends Task<Boolean> {

        @Override
        protected Boolean call() throws Exception {
            try {
//                EmcsAppContext.initializeEmcsAppContext();
//                return EmcsAppContext.getContext() != null;
                return true;
            } catch (Exception e) {
                LOGGER.error("AppInitTask: ", e);
            }
            return null;
        }
    }
}
