package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LedgerMappingProductGroupLoadTask extends Task<ProductGroupMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingProductGroupLoadTask.class);

    @Override
    protected ProductGroupMappingDto call() throws Exception {
        try {
            // product group
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.PRODUCT_GROUP;
            ResponseEntity<ProductGroup[]> response = restTemplate.getForEntity(url, ProductGroup[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            List<ProductGroup> productGroupList = new ArrayList<>(Arrays.asList(response.getBody()));

            // ledger
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if (respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_PRODUCT_GROUP;
            ResponseEntity<LedgerMappingProductGroup[]> respPgLedMap = restTemplate.getForEntity(url, LedgerMappingProductGroup[].class);
            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<LedgerMappingProductGroup> mapping = respPgLedMap.getBody() != null ? Arrays.asList(respPgLedMap.getBody()) : new ArrayList<>();

            List<LedgerMappingProductGroup> listMapping = new ArrayList<>(mapping);
            for (LedgerMappingProductGroup mp : listMapping) {
                productGroupList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getProductGroup().getCode().toString()));
            }
            for (ProductGroup productGroup : productGroupList) {
                LedgerMappingProductGroup mp = new LedgerMappingProductGroup();
                mp.setProductGroup(productGroup);
                listMapping.add(mp);
            }
            List<Ledger> list = new ArrayList<>(Arrays.asList(respLedger.getBody()));
            list.add(0, new Ledger("None"));
            return new ProductGroupMappingDto(listMapping, list);

        } catch (Exception e) {
            LOGGER.error("LedgerMappingProductGroup fetch", e);
        }
        return null;
    }
}
