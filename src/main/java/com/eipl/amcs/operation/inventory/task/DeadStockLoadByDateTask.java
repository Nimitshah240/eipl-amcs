package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.service.DeadStockService;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class DeadStockLoadByDateTask extends Task<List<DeadStock>> {

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public DeadStockLoadByDateTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<DeadStock> call() throws Exception {
        try {
            DeadStockService service = EmcsAppContext.getContext().getBean(DeadStockService.class);
            return service.findByPurchaseDateBetween(fromDate, toDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
