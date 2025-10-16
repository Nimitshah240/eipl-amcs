package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.util.CommonUtil;
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
            List<CollectionImportDto> collectionResultList = service.importCollections(dtoList, CommonUtil.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/import";
//            ResponseEntity<CollectionImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), CollectionImportDto[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            return Arrays.asList(response.getBody());

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
