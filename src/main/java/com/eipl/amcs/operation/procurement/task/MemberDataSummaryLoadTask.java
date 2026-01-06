package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.MilkCollectionSummaryData;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.util.List;

public class MemberDataSummaryLoadTask extends Task<List<MilkCollectionSummaryData>> {

    private int year;
    private int month;
    private Integer milkType;

    public MemberDataSummaryLoadTask( int year, int month, Integer milkType){
        this.year = year;
        this.month = month;
        this.milkType = milkType;
    }

    @Override
    protected List<MilkCollectionSummaryData> call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            return service.findTop10MemberSummaries(year,  month, milkType);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
