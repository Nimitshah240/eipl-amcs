package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
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

public class SubLedgerLedgerConfigLoadTask extends Task<ProductGroupMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLedgerLedgerConfigLoadTask.class);

    @Override
    protected ProductGroupMappingDto call() throws Exception {
        try {
            // product group
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);

            // ledger
            String  url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if(respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_LEDGER_CONFIG;
            ResponseEntity<SubLedgerLedgerConfig[]> respPgLedMap = restTemplate.getForEntity(url, SubLedgerLedgerConfig[].class);
            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<SubLedgerLedgerConfig> mapping = respPgLedMap.getBody() != null ? Arrays.asList(respPgLedMap.getBody()) : new ArrayList<>();

            List<SubLedgerLedgerConfig> listMapping = new ArrayList<>(mapping);
//            for (SubLedgerLedgerConfig mp : listMapping) {
//                productGroupList.removeIf(p->p.getCode().toString().equalsIgnoreCase(mp.getProductGroup().getCode().toString()));
//            }
//            for (ProductGroup productGroup : productGroupList) {
//                SubLedgerLedgerConfig mp = new SubLedgerLedgerConfig();
//                mp.setProductGroup(productGroup);
//                listMapping.add(mp);
//            }
//            return new ProductGroupMappingDto(listMapping, Arrays.asList(respLedger.getBody()));
        } catch (Exception e) {
            LOGGER.error("SubLedgerLedgerConfig fetch", e);
        }
        return null;
    }
}
