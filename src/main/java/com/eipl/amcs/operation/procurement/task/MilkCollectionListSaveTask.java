package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class MilkCollectionListSaveTask extends Task<List<CollectionImportDto>> {
    private final List<MilkCollection> dtoList;

    public MilkCollectionListSaveTask(List<MilkCollection> dtoList) {
        this.dtoList = dtoList;
    }

    @Override
    protected List<CollectionImportDto> call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<CollectionImportDto> collectionResultList = service.importCollections(dtoList, CommonUtils.setIdentityHeader());

            if (collectionResultList == null || collectionResultList.isEmpty()) {
                return null;
            }
            return collectionResultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
