package com.eipl.amcs.sync.producer;

import com.eipl.amcs.EiplAmcsAppRunner;
import com.eipl.amcs.sync.model.*;
import com.eipl.amcs.sync.repository.BroadcastedLogRepository;
import com.eipl.amcs.sync.repository.BroadcastedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.eipl.amcs.config.BeanConfig.*;

@Component
public class BroadcastedProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastedService.class);
    //
//    @Autowired
//    private KafkaTemplate<String, Broadcasted> kafkaTemplate;
//    @Autowired
//    private BroadcastedRepository broadcastedRepository;
//    @Autowired
//    private BroadcastedLogRepository logRepository;
//    @Autowired
//    private RestTemplate restTemplate;

    @Value(value = "${sync.url}")
    private String syncUrl;
    @Value(value = "${inbox.url}")
    private String inboxUrl;

//    public void produce(Broadcasted broadcated) {
//		ListenableFuture<SendResult<String, Broadcasted>> future = kafkaTemplate.send(topic, broadcated);
//		future.addCallback(new ListenableFutureCallback<SendResult<String, Broadcasted>>() {
//			@Override
//			public void onSuccess(SendResult<String, Broadcasted> result) {
//				LOGGER.info("Sent message [{}] with offset {}", result, result.getRecordMetadata().offset());
//				BroadcastedLog log = broadcated.toBroadcatedLog();
//				try {
//					logRepository.save(log);
//					broadcastedRepository.delete(broadcated);
//				} catch (Exception e) {
//					LOGGER.error("Broadcasted produce", e);
//				}
//			}
//
//			@Override
//			public void onFailure(Throwable ex) {
//				LOGGER.error("Error in sent {}", ex.getMessage());
//			}
//		});
//    }

    public void produce(List<Broadcasted> broadcastedList) {
        try {
            String[] strings = restTemplate.postForObject(syncUrl, new HttpEntity<>(broadcastedList), String[].class);
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
//                RealTimeResponse response = restTemplate.postForObject(inboxUrl, new HttpEntity<>(realTimeRequest), RealTimeResponse.class);
                RealTimeResponse response = restTemplate.postForObject("http://amulamcsuat.emilkpro.in/androiddpu/v1/master-data/inbox", new HttpEntity<>(realTimeRequest), RealTimeResponse.class);
//                RealTimeResponse response = restTemplate.postForObject("http://192.168.1.74/AMULUAT/androiddpu/v1/master-data/inbox", new HttpEntity<>(realTimeRequest), RealTimeResponse.class);
//                RealTimeResponse response = restTemplate.postForObject("https://amulamcs.yamatech.app/androiddpu/v1/master-data/inbox", new HttpEntity<>(realTimeRequest), RealTimeResponse.class);

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


