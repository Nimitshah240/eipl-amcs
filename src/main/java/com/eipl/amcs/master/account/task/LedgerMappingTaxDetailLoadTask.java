package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.*;
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
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingTaxDetail;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.master.account.model.Tax;

public class LedgerMappingTaxDetailLoadTask extends Task<TaxDetailMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingTaxDetailLoadTask.class);

    @Override
    protected TaxDetailMappingDto call() throws Exception {
        try {
            //Tax
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.TAX;
            ResponseEntity<TaxDto[]> response = restTemplate.getForEntity(url, TaxDto[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            List<TaxDto> taxDetailList = new ArrayList<>(Arrays.asList(response.getBody()));


            // ledger
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> respLedger = restTemplate.getForEntity(url, Ledger[].class);
            if (respLedger == null || respLedger.getStatusCode() != HttpStatus.OK)
                return null;

            // mapping
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_MAPPING_TAX_DETAIL;
            ResponseEntity<LedgerMappingTaxDetail[]> respTaxDLedMap = restTemplate.getForEntity(url, LedgerMappingTaxDetail[].class);
            if (respTaxDLedMap == null || respTaxDLedMap.getStatusCode() != HttpStatus.OK)
                return null;
            List<LedgerMappingTaxDetail> mapping = respTaxDLedMap.getBody() != null ? Arrays.asList(respTaxDLedMap.getBody()) : new ArrayList<>();

            List<LedgerMappingTaxDetail> listMapping = new ArrayList<>(mapping);
//            for (LedgerMappingTaxDetail mp : listMapping) {
//                taxDetailList.removeIf(p->p.getTax().getCode().equalsIgnoreCase(mp.getTaxDetail().getCode()));
//            }
            for (TaxDto taxDto : taxDetailList) {
                for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                    LedgerMappingTaxDetail obj = listMapping.stream().filter(p -> p.getTaxDetail().getCode().equalsIgnoreCase(taxDetail.getCode()))
                            .findFirst().orElse(null);

                    if (obj == null) {
                        LedgerMappingTaxDetail mp = new LedgerMappingTaxDetail();
                        mp.setTaxDetail(taxDetail);
                        mp.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                        listMapping.add(mp);
                    } else {
                        obj.setTaxDetail(taxDetail);
                        obj.getTaxDetail().setTax(getTax(taxDetailList, taxDetail.getCode()));
                    }
                }
            }
            List<Ledger> list = new ArrayList<>(Arrays.asList(respLedger.getBody()));
            list.add(0, new Ledger("None")); //"0",
            return new TaxDetailMappingDto(listMapping, list);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingTaxDetail fetch", e);
        }
        return null;
    }

    private Tax getTax(List<TaxDto> taxDetailList, String code) {
        for (TaxDto taxDto : taxDetailList) {
            for (TaxDetail taxDetail : taxDto.getTaxDetails()) {
                if (taxDetail.getCode().equalsIgnoreCase(code))
                    return taxDto.getTax();
            }
        }
        return null;
    }
}
