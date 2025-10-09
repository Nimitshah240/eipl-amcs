package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.model.*;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LedgerSubLedgerMappingDtoLoadTask extends Task<LedgerSubLedgerDto> {

    private Ledger ledger;
    private SubLedger subLedger;

    public LedgerSubLedgerMappingDtoLoadTask(Ledger ledger, SubLedger subLedger) {
        this.ledger = ledger;
        this.subLedger = subLedger;
    }

    private SubLedgerService subLedgerService;
    private LedgerService ledgerService;

    @Override
    protected LedgerSubLedgerDto call() throws Exception {
        LedgerSubLedgerDto dto = new LedgerSubLedgerDto();
        RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
        String url = null;
        if (ledger != null) {
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER;
            ResponseEntity<SubLedger[]> response = restTemplate.getForEntity(url, SubLedger[].class);
            if (response.getStatusCode() == HttpStatus.OK)
                dto.setSubLedgerList(Arrays.asList(response.getBody()));


            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_SUB_LEDGER_MAPPING;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", MainApp.identityDto.getSociety().getCode())
                    .queryParam("ledgerCode", ledger.getCode());

            ResponseEntity<LedgerSubLedgerMapping[]> response1 = restTemplate.getForEntity(builder.toUriString(), LedgerSubLedgerMapping[].class);
            if (response1.getStatusCode() == HttpStatus.OK) {
                dto.setLedgerSubLedgerMappingList(Arrays.asList(response1.getBody()));
            }

//            private LedgerService ledgerService;

            for (SubLedger sbl : dto.getSubLedgerList()) {
                if (dto.getLedgerSubLedgerMappingList().stream()
                        .anyMatch(p -> p.getSubLedger().getCode().equals(sbl.getCode())))
                    sbl.selectedProperty().set(true);
            }


//            private SubLedgerService subLedgerService;
//            private LedgerService ledgerService;

//            CompletableFuture<List<SubLedger>> subLedgerListFuture = CompletableFuture.supplyAsync(() -> subLedgerService.findAll());
//            CompletableFuture<List<LedgerSubLedgerMapping>> ledgerSubLedgerMappingListFuture = CompletableFuture.supplyAsync(() -> ledgerService.fetchMapping(societyCode, ledgerCode, subLedgerCode));
//
//            CompletableFuture.allOf(subLedgerListFuture, ledgerSubLedgerMappingListFuture)
//                    .whenCompleteAsync((result, ex) -> {
//                        try {
//
//                            if (!subLedgerListFuture.get().isEmpty())
//                                dto.setSubLedgerList(subLedgerListFuture.get());
//
//                            if (!ledgerSubLedgerMappingListFuture.get().isEmpty())
//                                dto.setLedgerSubLedgerMappingList(ledgerSubLedgerMappingListFuture.get());
//
//                            for (SubLedger sbl : dto.getSubLedgerList()) {
//                                if (dto.getLedgerSubLedgerMappingList().stream()
//                                        .anyMatch(p -> p.getSubLedger().getCode().equals(sbl.getCode())))
//                                    sbl.selectedProperty().set(true);
//                            }
//                        } catch (Exception exs) {
//                            System.out.println(exs);
//                            throw new RuntimeException(exs);
//                        }
//                    });
//

        }

        if (subLedger != null) {
            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER;
            ResponseEntity<Ledger[]> response = restTemplate.getForEntity(url, Ledger[].class);
            if (response.getStatusCode() == HttpStatus.OK)
                dto.setLedgerList(Arrays.asList(response.getBody()));


            url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_SUB_LEDGER_MAPPING;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", MainApp.identityDto.getSociety().getCode())
                    .queryParam("subLedgerCode", subLedger.getCode());

            ResponseEntity<LedgerSubLedgerMapping[]> response1 = restTemplate.getForEntity(builder.toUriString(), LedgerSubLedgerMapping[].class);
            if (response1.getStatusCode() == HttpStatus.OK) {
                dto.setLedgerSubLedgerMappingList(Arrays.asList(response1.getBody()));
            }

            for (Ledger ldr : dto.getLedgerList()) {
                if (dto.getLedgerSubLedgerMappingList().stream()
                        .anyMatch(p -> p.getLedger().getCode().equals(ldr.getCode())))
                    ldr.selectedProperty().set(true);
            }


//            private LedgerService ledgerService;
//
//            CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> ledgerService.findAllByIsActive());
//            CompletableFuture<List<LedgerSubLedgerMapping>> ledgerSubLedgerMappingListFuture = CompletableFuture.supplyAsync(() -> ledgerService.fetchMapping(societyCode, ledgerCode, subLedgerCode));
//
//            CompletableFuture.allOf(ledgerListFuture, ledgerSubLedgerMappingListFuture)
//                    .whenCompleteAsync((result, ex) -> {
//                        try {
//                            if (!ledgerListFuture.get().isEmpty())
//                                dto.setLedgerList(ledgerListFuture.get());
//
//                            if (!ledgerSubLedgerMappingListFuture.get().isEmpty())
//                                dto.setLedgerSubLedgerMappingList(ledgerSubLedgerMappingListFuture.get());
//
//                            for (Ledger ldr : dto.getLedgerList()) {
//                                if (dto.getLedgerSubLedgerMappingList().stream()
//                                        .anyMatch(p -> p.getLedger().getCode().equals(ldr.getCode())))
//                                    ldr.selectedProperty().set(true);
//                            }
//                        } catch (Exception exs) {
//                            System.out.println(exs);
//                            throw new RuntimeException(exs);
//                        }
//                    });

        }
        return dto;


    }
}
