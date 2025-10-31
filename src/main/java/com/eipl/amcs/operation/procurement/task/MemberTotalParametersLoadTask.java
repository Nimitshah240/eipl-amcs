package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.math.BigDecimal;
import java.util.Map;

public class MemberTotalParametersLoadTask extends Task<Object> {
    private final String code;
    private final String no;
    private final int milkType;

    public MemberTotalParametersLoadTask(String code, String no, int milktype) {
        this.code = code;
        this.no = no;
        this.milkType = milktype;
    }

    @Override
    protected Object call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            Map<String, BigDecimal> fetchTotals = service.findTotals(code, no, milkType);
            if (fetchTotals == null || fetchTotals.isEmpty())
                return null;
            return fetchTotals;

        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
