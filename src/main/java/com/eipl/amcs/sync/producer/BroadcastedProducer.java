package com.eipl.amcs.sync.producer;

import com.eipl.amcs.EiplAmcsAppRunner;
import com.eipl.amcs.MainApp;
import com.eipl.amcs.sync.model.*;
import com.eipl.amcs.sync.repository.BroadcastedLogRepository;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import com.eipl.amcs.utils.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class BroadcastedProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedService.class);

    @Autowired
    private BroadcastedRepository broadcastedRepository;
    @Autowired
    private BroadcastedLogRepository logRepository;
    @Autowired
    private RestTemplate restTemplate;

    public void produce(List<Broadcasted> broadcastedList) {
        try {
            String[] strings = restTemplate.postForObject(MainApp.getProperty("syncUrl.realtime", AppConstant.UrlPath.DATA_PROCESSOR), new HttpEntity<>(broadcastedList), String[].class);
            if (strings == null || strings.length == 0) {
                return;
            }
            for (String uuid : strings) {
                broadcastedList.stream().filter(p -> p.getUuid().equals(uuid))
                        .findFirst()
                        .ifPresent(obj -> {
                            BroadcastedLog log = obj.toBroadcatedLog();
                            logRepository.save(log);
                        });
                broadcastedRepository.deleteById(uuid);
            }
        } catch (Exception e) {
            LOGGER.error("Error while making the API call", e);
        }
    }

    public void produceInbox(List<Broadcasted> broadcastedList) {
        List<Inbox> inboxList = new ArrayList<>();
        try {
            if (EiplAmcsAppRunner.identityDto != null) {
                for (Broadcasted broadcasted : broadcastedList) {
                    inboxList.add(broadcasted.toInbox());
                }
                RealTimeRequest<List<Inbox>> realTimeRequest = new RealTimeRequest<>();
                realTimeRequest.setDeviceId("AMUL" + EiplAmcsAppRunner.identityDto.getIdentity().getSocietyCode() + "AMCS");
                realTimeRequest.setIdentityCode(EiplAmcsAppRunner.identityDto.getIdentity().getSocietyRefCode());
                realTimeRequest.setImei("");
                realTimeRequest.setOrganizationCode(EiplAmcsAppRunner.identityDto.getIdentity().getSocietyRefCode());
                realTimeRequest.setOrganizationType("VLC");
                realTimeRequest.setRequestTime(null);
                realTimeRequest.setToken(EiplAmcsAppRunner.identityDto.getIdentity().getToken());
                realTimeRequest.setContent(inboxList);
                RealTimeResponse response = restTemplate.postForObject("http://amulamcsuat.emilkpro.in/androiddpu/v1/master-data/inbox", new HttpEntity<>(realTimeRequest), RealTimeResponse.class);

                for (String successId : String.valueOf(response.getData().get("successId")).split(",")) {
                    broadcastedList.stream().filter(p -> p.getUuid().equals(successId))
                            .findFirst()
                            .ifPresent(obj -> {
                                BroadcastedLog log = obj.toBroadcatedLog();
                                logRepository.save(log);
                            });
                    broadcastedRepository.deleteById(successId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error while making the API call", e);
        }
    }
}


