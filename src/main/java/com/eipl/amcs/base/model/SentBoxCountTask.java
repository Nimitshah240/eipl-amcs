package com.eipl.amcs.base.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.IdentityPayload;
import com.eipl.amcs.network.IdentityPayloadForAcknowledgement;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentBoxCountTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentBoxCountTask.class);
    List<String> sentBoxUuidList;
    List<Subscribed> subscribedList;
    private final String societyCode;
    private final String mobileNo;

    public SentBoxCountTask(String societyCode, String mobileNo) {
        this.societyCode = societyCode;
        this.mobileNo = mobileNo;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            SubscribedRepository subscribedRepository = EmcsAppContext.getContext().getBean(SubscribedRepository.class);
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, "") + AppConstant.UrlPath.SENT_BOX_COUNT;
//            String url = "http://192.168.1.74/AMULUAT/webservice/amcs/v1/realtime-services/sentbox-count";
            String url = "http://amulamcsuat.emilkpro.in/webservice/amcs/v1/realtime-services/sentbox-count";

            IdentityPayload payload = new IdentityPayload();
            RealTimeRequest<IdentityPayload> requestPayload = new RealTimeRequest<>(societyCode, MainApp.identityDto.getIdentity().getToken(), payload);
            requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
            ResponseEntity<RealTimeResponse> response = null;
            try {
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            } catch (Exception e) {
            }
            if (response == null || (response != null && response.getStatusCode() != HttpStatus.OK))
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;

//            response.getBody().getData();

            int count = Integer.parseInt(String.valueOf(response.getBody().getData().get("count")));
            if (count != 0) {
                StringBuilder code = new StringBuilder();
                IdentityPayloadForAcknowledgement payloadForAcknowledgement;
                RealTimeRequest<IdentityPayloadForAcknowledgement> requestPayloadForAcknowledgement;

//        checkSentBoxDataAndDownload
                do {
//                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, "") + AppConstant.UrlPath.SENT_BOX_CHECK;
//                    url = "http://192.168.1.74/AMULUAT/webservice/amcs/v1/realtime-services/sentbox";
                    url = "http://amulamcsuat.emilkpro.in/webservice/amcs/v1/realtime-services/sentbox";
                    payload = new IdentityPayload();
                    requestPayload = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), payload);
                    requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                    ResponseEntity<Map> sentBoxResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), Map.class);
                    if (sentBoxResponse.getStatusCode() != HttpStatus.OK)
                        return null;
                    List<Map<String, Object>> list = (List<Map<String, Object>>) sentBoxResponse.getBody().get("data");
                    sentBoxUuidList = new ArrayList<>();
                    subscribedList = new ArrayList<>();
                    for (Map<String, Object> map : list) {
                        Subscribed sentBox = getSentBoxObject(map);
                        sentBoxUuidList.add(sentBox.getUuid());
                        subscribedList.add(sentBox);
                    }

                    subscribedRepository.saveAll(subscribedList);

                    code = new StringBuilder();
                    for (String uuid : sentBoxUuidList) {
                        code.append(uuid);
                        code.append(",");
                        System.out.println(uuid);
                    }
//                    url = "http://192.168.1.74/AMULUAT/webservice/amcs/v1/realtime-services/acknowledgement";
                    url = "http://amulamcsuat.emilkpro.in/webservice/amcs/v1/realtime-services/acknowledgement";
//                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, "") + AppConstant.UrlPath.SENT_BOX_ACK;
                    payloadForAcknowledgement = new IdentityPayloadForAcknowledgement(code.toString());
                    requestPayloadForAcknowledgement = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), payloadForAcknowledgement);
                    requestPayloadForAcknowledgement.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayloadForAcknowledgement), RealTimeResponse.class);
                    count -= 5;
                } while (count >= 0);
            }
        } catch (Exception e) {
            LOGGER.error("Notification", e);
        }
        return null;
    }


    private Subscribed getSentBoxObject(Map<String, Object> map) {
        Subscribed subscribed = new Subscribed();
        subscribed.setUuid((String) map.get("uuid"));
        subscribed.setSourceType((String) map.get("sourceOrgType"));
        subscribed.setSourceCode((String) map.get("sourceOrgId"));
        subscribed.setDestType((String) map.get("destOrgType"));
        subscribed.setDestCode((String) map.get("destOrgId"));
        subscribed.setTableName((String) map.get("tableName"));
        subscribed.setOperation((String) map.get("operation"));
        subscribed.setDataText((String) map.get("jsonText"));
        subscribed.setErrorText((String) map.get("errorLog"));
        subscribed.setSequence(Short.parseShort((String.valueOf(map.get("sequenceNo")))));
        subscribed.setSourceSystemId((String) map.get("sourceDeviceMac"));
        subscribed.setVersion((String) map.get("versionNo"));
        if (map.get("postingTimestamp") != null)
            subscribed.setProcessedAt(LocalDateTime.parse((String) map.get("postingTimestamp"), AppConstant.Formatter4));
        if (map.get("syncTimestamp") != null)
            subscribed.setCreatedAt(LocalDateTime.parse((String) map.get("syncTimestamp"), AppConstant.Formatter4));
        subscribed.setErrorText((String) map.get("errorTimestamp"));
        return subscribed;
    }
}

