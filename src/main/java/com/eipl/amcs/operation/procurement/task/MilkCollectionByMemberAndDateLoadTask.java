package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.List;

public class MilkCollectionByMemberAndDateLoadTask extends Task<List<MilkCollection>> {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String code;


    public MilkCollectionByMemberAndDateLoadTask(LocalDate fromDate, LocalDate toDate, String code) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.code = code;
    }


    @Override
    protected List<MilkCollection> call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);

            return service.findAllCollectionByMember(CommonUtils.getLocalDateTimeFromDateAndShift(fromDate, new Shift(1, "Morning", "Morning")), CommonUtils.getLocalDateTimeFromDateAndShift(toDate, new Shift(2, "Morning", "Morning")), code);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
