package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class SocietyPaymentCycleLoadByDateTask extends Task<List<SocietyPaymentCycle>> {

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public SocietyPaymentCycleLoadByDateTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }


    @Override
    protected List<SocietyPaymentCycle> call() throws Exception {
        try {
            SocietyPaymentCycleService service = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            LocalDateTime fromDt = LocalDateTime.of(fromDate, LocalTime.MIN);
            LocalDateTime toDt = LocalDateTime.of(toDate, LocalTime.MIN);
            return service.findAll(fromDt, toDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

