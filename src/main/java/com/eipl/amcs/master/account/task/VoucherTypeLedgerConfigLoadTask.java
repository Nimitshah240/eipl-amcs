package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.model.*;
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

public class VoucherTypeLedgerConfigLoadTask extends Task<VoucherTypeMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTypeLedgerConfigLoadTask.class);

    @Override
    protected VoucherTypeMappingDto call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER_TYPE;
            ResponseEntity<VoucherType[]> response = restTemplate.getForEntity(url, VoucherType[].class);
            if(response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            List<VoucherType> voucherTypeList = new ArrayList<>(Arrays.asList(response.getBody()));

            // ledger
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if(respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;

            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_VOUCHER_TYPE;
            ResponseEntity<VoucherTypeLedgerConfig[]> respPgLedMap = restTemplate.getForEntity(url, VoucherTypeLedgerConfig[].class);
            if (respPgLedMap == null || respPgLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<VoucherTypeLedgerConfig> mapping = respPgLedMap.getBody() != null ? Arrays.asList(respPgLedMap.getBody()) : new ArrayList<>();

            List<VoucherTypeLedgerConfig> listMapping = new ArrayList<>(mapping);
            for (VoucherTypeLedgerConfig mp : listMapping) {
                voucherTypeList.removeIf(p->p.getCode().toString().equalsIgnoreCase(mp.getVoucherType().getCode().toString()));
            }
            for (VoucherType voucherType : voucherTypeList) {
                VoucherTypeLedgerConfig mp = new VoucherTypeLedgerConfig();
                mp.setVoucherType(voucherType);
                listMapping.add(mp);
            }

            List<Ledger> list = new ArrayList<>(Arrays.asList(respLedger.getBody()));
            list.add(0, new Ledger( "None"));//"0",
            return new VoucherTypeMappingDto(listMapping, list);
        } catch (Exception e) {
            LOGGER.error("VoucherTypeLedgerConfig fetch", e);
        }
        return null;
    }
}
