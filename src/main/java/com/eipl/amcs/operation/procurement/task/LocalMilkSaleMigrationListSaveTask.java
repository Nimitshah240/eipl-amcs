package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Queue;

public class LocalMilkSaleMigrationListSaveTask extends Task<Integer> {
    private final List<LocalMilkSale> dtoList;
    private int max;

    public LocalMilkSaleMigrationListSaveTask(List<LocalMilkSale> dtoList, int max) {
        this.dtoList = dtoList;
        this.max = max;
    }

    @Override
    protected Integer call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LOCAL_MILK_SALE + "/migrate";
            ResponseEntity<LocalMilkSale[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), LocalMilkSale[].class);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
