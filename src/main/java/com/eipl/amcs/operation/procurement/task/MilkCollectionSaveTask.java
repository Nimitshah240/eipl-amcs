package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
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
                collections = service.save(collection, CommonUtils.setIdentityHeader());
            } else {
                collections = service.update(collection, CommonUtils.setIdentityHeader());
            }
            return collections;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
