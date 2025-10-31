package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;

public class MilkCollectionFromMemberLoadTask extends Task<List<MilkCollection>> {
    private final LocalDateTime date;
    private final String code;

    public MilkCollectionFromMemberLoadTask(LocalDateTime date, String code) {
        this.date = date;
        this.code = code;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {
            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            List<MilkCollection> milkCollections = service.findByMemberAndDate(date, code);
            if (milkCollections == null || milkCollections.isEmpty())
                return null;
            return milkCollections;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
