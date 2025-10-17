package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;

public class MilkCollectionMigrationListSaveTask extends Task<Integer> {
    private final Queue<List<MilkCollection>> dtoList;
    private final int max;

    public MilkCollectionMigrationListSaveTask(Queue<List<MilkCollection>> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            while (!dtoList.isEmpty()) {
                List<MilkCollection> list = dtoList.poll();
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_COLLECTION + "/migrate";
                ResponseEntity<CollectionImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(list), CollectionImportDto[].class);
                updateProgress(max - dtoList.size(), max);
                System.out.println(max - dtoList.size());
            }
            return max;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
