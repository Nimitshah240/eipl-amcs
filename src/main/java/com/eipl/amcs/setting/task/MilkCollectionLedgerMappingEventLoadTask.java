package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.service.LedgerMappingEventService;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MilkCollectionLedgerMappingEventLoadTask extends Task<List<MilkCollectionAccountPostingDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionLedgerMappingEventLoadTask.class);

    private int type;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;

    public  MilkCollectionLedgerMappingEventLoadTask(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        this.type = type;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
    }

    @Override
    protected List<MilkCollectionAccountPostingDto> call() throws Exception {
        try {

            LedgerMappingEventService ledgerMappingEventService = EmcsAppContext.getContext().getBean(LedgerMappingEventService.class);
            MilkCollectionService milkCollectionService = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> milkCollectionList = milkCollectionService.findAllBetween(fromDateTime, toDateTime);

            List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList = new ArrayList<>();
            MilkCollectionAccountPostingDto milkCollectionAccountPostingDto = null;

            if (type == 1) { // consolidate

                milkCollectionAccountPostingDto = new MilkCollectionAccountPostingDto();

                milkCollectionAccountPostingDto.setDate(LocalDate.now());
                milkCollectionAccountPostingDto.setAmount(milkCollectionList.stream()
                        .map(MilkCollection::getAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                milkCollectionAccountPostingDtoList.add(milkCollectionAccountPostingDto);
            } else if (type == 2) {// for day wise

                Map<LocalDate, BigDecimal> dailyTotals = milkCollectionList.stream()
                        .filter(item -> !item.getCollectionDate().isBefore(fromDateTime)
                                && !item.getCollectionDate().isAfter(toDateTime))
                        .collect(Collectors.groupingBy(
                                item -> item.getCollectionDate().toLocalDate(),
                                TreeMap::new,
                                Collectors.reducing(BigDecimal.ZERO, MilkCollection::getAmount, BigDecimal::add)
                        ));

                for (LocalDate date : dailyTotals.keySet()) {
                    milkCollectionAccountPostingDto = new MilkCollectionAccountPostingDto();
                    milkCollectionAccountPostingDto.setDate(date);
//                    milkCollectionAccountPostingDto.setLedgerMappingEvent(ledgerMappingEvent);
                    milkCollectionAccountPostingDto.setAmount(dailyTotals.get(date));
                    milkCollectionAccountPostingDtoList.add(milkCollectionAccountPostingDto);
                }
            }

            return milkCollectionAccountPostingDtoList;

        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
        }
        return null;
    }
}