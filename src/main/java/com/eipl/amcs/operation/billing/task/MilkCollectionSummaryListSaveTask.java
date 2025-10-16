package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.MilkSummaryDataEntry;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MilkCollectionSummaryListSaveTask extends Task<List<CollectionImportDto>> {
    private final List<MilkSummaryDataEntry> dtoList;

    public MilkCollectionSummaryListSaveTask(List<MilkSummaryDataEntry> dtoList) {
        this.dtoList = dtoList;
    }

    @Override
    protected List<CollectionImportDto> call() throws Exception {
        try {

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_SUMMARY_DATA_ENTRY + "/import";
            ResponseEntity<CollectionImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), CollectionImportDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
