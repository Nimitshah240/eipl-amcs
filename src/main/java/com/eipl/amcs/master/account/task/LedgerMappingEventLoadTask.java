package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.EventMappingDto;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.service.EventService;
import com.eipl.amcs.master.account.service.LedgerMappingEventService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LedgerMappingEventLoadTask extends Task<EventMappingDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LedgerMappingEventLoadTask.class);

    @Override
    protected EventMappingDto call() throws Exception {
        try {
            EventService eventService = EmcsAppContext.getContext().getBean(EventService.class);
            LedgerService ledgerService = EmcsAppContext.getContext().getBean(LedgerService.class);
            VoucherTypeService voucherTypeService = EmcsAppContext.getContext().getBean(VoucherTypeService.class);
            LedgerMappingEventService ledgerMappingEventService = EmcsAppContext.getContext().getBean(LedgerMappingEventService.class);

            // product group
            List<Events> eventList = eventService.findAll();

            // ledger
            List<Ledger> ledgerList = ledgerService.findAllByIsActive();

            // voucherType
            List<VoucherType> voucherTypeList = voucherTypeService.findAll();

            List<LedgerMappingEvent> mapping = ledgerMappingEventService.findAll();

            List<LedgerMappingEvent> listMapping = new ArrayList<>(mapping);
            for (LedgerMappingEvent mp : listMapping) {
                eventList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getEvents().getCode().toString()));
            }
            for (Events event : eventList) {
                LedgerMappingEvent mp = new LedgerMappingEvent();
                mp.setEvents(event);
                listMapping.add(mp);
            }
            List<Ledger> list = new ArrayList<>(ledgerList);
            list.add(0, new Ledger("None")); //"0",
            return new EventMappingDto(listMapping, list, voucherTypeList);
        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
        }
        return null;
    }
}
