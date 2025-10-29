package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.dto.MilkCollectionPreReqDto;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MilkCollectionPreRequisiteTask extends Task<MilkCollectionPreReqDto> {
    private final LocalDateTime date;
    private final Shift shift;
    private final Society society;

    public MilkCollectionPreRequisiteTask(LocalDateTime date, Shift shift, Society society) {
        this.date = date;
        this.shift = shift;
        this.society = society;
    }

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected MilkCollectionPreReqDto call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            LocalDateTime dt = LocalDateTime.parse(date.format(DATE_TIME_FMT), DATE_TIME_FMT);
            return service.fetchPreRequsite(dt, shift.getCode(), society.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
