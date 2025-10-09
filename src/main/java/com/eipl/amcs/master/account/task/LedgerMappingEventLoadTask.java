package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.EventMappingDto;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.service.EventService;
import com.eipl.amcs.master.account.service.LedgerMappingEventService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LedgerMappingEventLoadTask extends Task<EventMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingEventLoadTask.class);

    @Override
    protected EventMappingDto call() throws Exception {
        try {
            // product group
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.EVENT;
            ResponseEntity<Events[]> response = restTemplate.getForEntity(url, Events[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            List<Events> eventList = new ArrayList<>(Arrays.asList(response.getBody()));

            // ledger
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if (respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;
            // voucherType
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER_TYPE;
            ResponseEntity<VoucherType[]> respVoucherType = restTemplate.getForEntity(url, VoucherType[].class);
            if (respVoucherType == null || respVoucherType.getStatusCode() != HttpStatus.OK)
                return null;

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_EVENT;
            ResponseEntity<LedgerMappingEvent[]> respPgLedMap = restTemplate.getForEntity(url, LedgerMappingEvent[].class);
            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<LedgerMappingEvent> mapping = respPgLedMap.getBody() != null ? Arrays.asList(respPgLedMap.getBody()) : new ArrayList<>();

            List<LedgerMappingEvent> listMapping = new ArrayList<>(mapping);
            for (LedgerMappingEvent mp : listMapping) {
                eventList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getEvents().getCode().toString()));
            }
            for (Events event : eventList) {
                LedgerMappingEvent mp = new LedgerMappingEvent();
                mp.setEvents(event);
                listMapping.add(mp);
            }
            List<Ledger> list = new ArrayList<>(Arrays.asList(respLedger.getBody()));
            list.add(0, new Ledger("None")); //"0",
            return new EventMappingDto(listMapping, list, Arrays.asList(respVoucherType.getBody()));
        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
        }
        return null;
    }
}
