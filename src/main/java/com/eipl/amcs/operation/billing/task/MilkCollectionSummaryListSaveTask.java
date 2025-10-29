package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.List;

public class MilkCollectionSummaryListSaveTask extends Task<List<CollectionImportDto>> {
    private final List<MilkCollectionSummaryData> dtoList;

    public MilkCollectionSummaryListSaveTask(List<MilkCollectionSummaryData> dtoList) {
        this.dtoList = dtoList;
    }

    @Override
    protected List<CollectionImportDto> call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<CollectionImportDto> list = service.importCollectionSummaryData(dtoList, CommonUtils.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_SUMMARY_DATA_ENTRY + "/import";
//            ResponseEntity<CollectionImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), CollectionImportDto[].class);
            if (list == null || list.isEmpty())
                return null;
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
