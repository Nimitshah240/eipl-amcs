package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.LedgerSubLedgerDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.SubLedgerService;
import javafx.concurrent.Task;
import org.springframework.web.client.RestTemplate;

public class LedgerSubLedgerMappingDtoLoadTask extends Task<LedgerSubLedgerDto> {

    private final Ledger ledger;
    private final SubLedger subLedger;

    public LedgerSubLedgerMappingDtoLoadTask(Ledger ledger, SubLedger subLedger) {
        this.ledger = ledger;
        this.subLedger = subLedger;
    }

    @Override
    protected LedgerSubLedgerDto call() throws Exception {
        SubLedgerService subLedgerService = EmcsAppContext.getContext().getBean(SubLedgerService.class);
        LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);

        LedgerSubLedgerDto dto = new LedgerSubLedgerDto();
        if (ledger != null) {
            dto.setSubLedgerList(subLedgerService.findAll());
            dto.setLedgerSubLedgerMappingList(ledgerService.fetchMapping(MainApp.identityDto.getSociety().getCode(), MainApp.identityDto.getSociety().getCode(), null));
            for (SubLedger sbl : dto.getSubLedgerList()) {
                if (dto.getLedgerSubLedgerMappingList().stream()
                        .anyMatch(p -> p.getSubLedger().getCode().equals(sbl.getCode())))
                    sbl.selectedProperty().set(true);
            }
        }

        if (subLedger != null) {
            dto.setLedgerList(ledgerService.findAllByIsActive());
            dto.setLedgerSubLedgerMappingList(ledgerService.fetchMapping(MainApp.identityDto.getSociety().getCode(), null, subLedger.getCode()));

            for (Ledger ldr : dto.getLedgerList()) {
                if (dto.getLedgerSubLedgerMappingList().stream()
                        .anyMatch(p -> p.getLedger().getCode().equals(ldr.getCode())))
                    ldr.selectedProperty().set(true);
            }
        }
        return dto;
    }
}
