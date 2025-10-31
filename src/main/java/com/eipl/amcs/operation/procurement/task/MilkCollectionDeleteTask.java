package com.eipl.amcs.operation.procurement.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class MilkCollectionDeleteTask extends Task<Boolean> {
    private final String code;

    public MilkCollectionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);

            Optional<MilkCollection> collectionData = service.findById(code);
            if (collectionData == null || !collectionData.isPresent())
                return null;

            service.delete(collectionData.get().getCode(), CommonUtils.setIdentityHeader());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
