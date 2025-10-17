package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MilkCollectionSaveTask extends Task<Object> {
    private final MilkCollection collection;
    private final short update;

    public MilkCollectionSaveTask(MilkCollection collection, short update) {
        this.collection = collection;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MilkCollection collections;

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            if (this.update == 0) {
                collections = service.save(collection, CommonUtil.setIdentityHeader());
            } else {
                collections = service.update(collection, CommonUtil.setIdentityHeader());
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION;
//
//            ResponseEntity<MilkCollection> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(collection), MilkCollection.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(collection), MilkCollection.class);
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getBody();
            return collections;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
