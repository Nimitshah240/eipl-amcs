package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.repository.MilkCollectionRepository;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MilkCollectionDashboardLoadTask extends Task<List<Map<String, Object>>> {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public MilkCollectionDashboardLoadTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        try {
            MilkCollectionRepository service = EmcsAppContext.getContext().getBean(MilkCollectionRepository.class);
            return service.findDataForGraph(fromDate, toDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
