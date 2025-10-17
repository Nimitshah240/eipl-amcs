package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.BillHeadMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.operation.model.BillHead;
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

public class LedgerMappingBillHeadLoadTask extends Task<BillHeadMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingBillHeadLoadTask.class);

    @Override
    protected BillHeadMappingDto call() throws Exception {
        try {
            // Bill head
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BILLHEAD;
            ResponseEntity<BillHead[]> response = restTemplate.getForEntity(url, BillHead[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            List<BillHead> billHeadList = new ArrayList<>(Arrays.asList(response.getBody()));

            // ledger
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if (respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_BILL_HEAD;
            ResponseEntity<LedgerMappingBillHead[]> respPgLedMap = restTemplate.getForEntity(url, LedgerMappingBillHead[].class);
            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<LedgerMappingBillHead> mapping = respPgLedMap.getBody() != null ? Arrays.asList(respPgLedMap.getBody()) : new ArrayList<>();

            List<LedgerMappingBillHead> listMapping = new ArrayList<>(mapping);
            for (LedgerMappingBillHead mp : listMapping) {
                billHeadList.removeIf(p -> p.getCode().equalsIgnoreCase(mp.getBillHead().getCode()));
            }
            for (BillHead billHead : billHeadList) {
                LedgerMappingBillHead mp = new LedgerMappingBillHead();
                mp.setBillHead(billHead);
                listMapping.add(mp);
            }
            List<Ledger> list = new ArrayList<>(Arrays.asList(respLedger.getBody()));
            list.add(0, new Ledger("None"));//"0",
            return new BillHeadMappingDto(listMapping, list);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingBillHead fetch", e);
        }
        return null;
    }
}
