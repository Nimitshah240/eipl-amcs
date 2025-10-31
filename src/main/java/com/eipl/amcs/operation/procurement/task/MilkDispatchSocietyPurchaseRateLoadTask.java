package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class MilkDispatchSocietyPurchaseRateLoadTask extends Task<SocietyMilkPurchaseRate> {
    private final LocalDateTime date;
    private final Shift shift;
    private final Society society;

    public MilkDispatchSocietyPurchaseRateLoadTask(LocalDateTime date, Shift shift, Society society) {
        this.date = date;
        this.shift = shift;
        this.society = society;
    }

    @Override
    protected SocietyMilkPurchaseRate call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            return service.fetchPurchaseRateCode(date, shift.getCode(), society.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
