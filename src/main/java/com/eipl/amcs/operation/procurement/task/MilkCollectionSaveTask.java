package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.network.RealTimeRequest;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class MilkCollectionSaveTask extends Task<Object> {
    private final MilkCollection collection;
    private final short update;
    private final Boolean doubleDock;

    public MilkCollectionSaveTask(MilkCollection collection, short update, Boolean doubleDock) {
        this.collection = collection;
        this.update = update;
        this.doubleDock = doubleDock;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MilkCollection collections = null;
            if (!doubleDock) {
                MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
                if (this.update == 0) {
                    collections = service.save(collection, CommonUtils.setIdentityHeader());
                } else {
                    collections = service.update(collection, CommonUtils.setIdentityHeader());
                }
                return collections;
            } else {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                URI uri = UriComponentsBuilder.fromHttpUrl(MainApp.getProperty(AppConstant.Props.BASE_URL, "") + AppConstant.UrlPath.MILK_COLLECTION)
                        .queryParam("doubleDock", true)
                        .build()
                        .toUri();
                RealTimeRequest<MilkCollection> requestPayload = new RealTimeRequest<>(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getIdentity().getToken(), collections);
                ResponseEntity<MilkCollection> response = restTemplate.exchange(uri, update == 0 ? HttpMethod.POST : HttpMethod.PUT, new HttpEntity<RealTimeRequest>(requestPayload), MilkCollection.class);

                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;

                MilkCollection respBody = response.getBody();
                return respBody;
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
