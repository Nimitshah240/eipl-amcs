package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class LocalSaleByMemberAndDateLoadTask extends Task<List<LocalMilkSale>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalSaleByMemberAndDateLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String memberCode;

    public LocalSaleByMemberAndDateLoadTask(String memberCode, LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.memberCode = memberCode;
    }

    @Override
    protected List<LocalMilkSale> call() throws Exception {
        try {
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            List<LocalMilkSale> list = service.findByMemberAndDate(memberCode, fromDate, toDate);
            if (list == null || list.isEmpty())
                return Collections.emptyList();
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductReceipt fetch", e);
        }
        return Collections.emptyList();
    }
}
