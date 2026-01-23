package com.eipl.amcs.base.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.IdentityPayload;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.network.RealTimeResponse;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SentBoxDesktopTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SentBoxCountTask.class);
    private final String societyCode;
    List<String> sentBoxUuidList;
    List<Subscribed> subscribedList;

    public SentBoxDesktopTask(String societyCode) {
        this.societyCode = societyCode;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            SubscribedRepository subscribedRepository = EmcsAppContext.getContext().getBean(SubscribedRepository.class);
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.SENT_BOX_DESKTOP_COUNT;

            IdentityPayload payload = new IdentityPayload();
            RealTimeRequest<IdentityPayload> requestPayload = new RealTimeRequest<>(societyCode, MainApp.identityDto.getIdentity().getToken(), payload);
            requestPayload.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
            ResponseEntity<RealTimeResponse> response = null;
            try {
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayload), RealTimeResponse.class);
            } catch (Exception e) {
                LOGGER.error(e.toString());
            }
            if (response == null || (response != null && response.getStatusCode() != HttpStatus.OK))
                return null;

            RealTimeResponse respBody = response.getBody();
            if (!"success".equalsIgnoreCase(respBody.getStatus()))
                return null;


            int count = Integer.parseInt(String.valueOf(response.getBody().getData().get("count")));
            log.info("Sent box count : {}", count);
            if (count != 0) {
                RealTimeRequest<Map<String, List>> requestPayloadForAcknowledgement;

//        checkSentBoxDataAndDownload
                do {
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.SENT_BOX_DESKTOP;
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
                    List codeList = new ArrayList();
                    for (String uuid : sentBoxUuidList) {
                        codeList.add(uuid);
                        System.out.println(uuid);
                    }
                    url = MainApp.getProperty(AppConstant.Props.BASE_URL_REALTIME, null) + AppConstant.UrlPath.SENT_BOX_DESKTOP_ACK;
                    Map<String, List> uuidMap = new HashMap<>();
                    uuidMap.put("uuid", codeList);
                    requestPayloadForAcknowledgement = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), uuidMap);
                    requestPayloadForAcknowledgement.setOrganizationCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                    ResponseEntity<RealTimeResponse> realTimeResponse = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<RealTimeRequest>(requestPayloadForAcknowledgement), RealTimeResponse.class);
                    log.info("Realtimeresponse status : {} ", realTimeResponse.getStatusCode());
                    log.info("Realtimeresponse body : {} ", realTimeResponse.getBody().getData());
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

