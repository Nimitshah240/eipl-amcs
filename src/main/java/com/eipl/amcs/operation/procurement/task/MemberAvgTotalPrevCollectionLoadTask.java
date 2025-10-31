package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MemberWiseCollectionDto;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDate;

public class MemberAvgTotalPrevCollectionLoadTask extends Task<Object> {
    private final String code;
    private final String no;
    private final String paymentCycleCode;
    private final String milktype;
    private final LocalDate date;
    private final int shiftCode;

    public MemberAvgTotalPrevCollectionLoadTask(String code, String no, String milktype, LocalDate date,
                                                int shiftCode, String paymentCycleCode) {
        this.code = code;
        this.no = no;
        this.milktype = milktype;
        this.date = date;
        this.shiftCode = shiftCode;
        this.paymentCycleCode = paymentCycleCode;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            MemberWiseCollectionDto dtoResult = service.findAllInOne(code, Integer.parseInt(no), milktype, date, shiftCode, paymentCycleCode);

            return dtoResult;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
