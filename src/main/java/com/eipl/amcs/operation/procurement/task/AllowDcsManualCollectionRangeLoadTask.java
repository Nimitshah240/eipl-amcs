package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import com.eipl.amcs.operation.procurement.repository.AllowDcsManualCollectionRangeRepository;
import javafx.concurrent.Task;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.util.List;

public class AllowDcsManualCollectionRangeLoadTask extends Task<List<AllowDcsManualCollectionRange>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllowDcsManualCollectionRangeLoadTask.class);

    @Override
    protected List<AllowDcsManualCollectionRange> call() throws Exception {
        try {
            AllowDcsManualCollectionRangeRepository repository = EmcsAppContext.getContext().getBean(AllowDcsManualCollectionRangeRepository.class);

            List<AllowDcsManualCollectionRange> list = repository.findAll(Sort.by("createdAt").descending());
            for (AllowDcsManualCollectionRange request : list) {
                request.setFromShift(Hibernate.unproxy(request.getFromShift(), Shift.class));
                request.setToShift(Hibernate.unproxy(request.getToShift(), Shift.class));
                request.setSociety(Hibernate.unproxy(request.getSociety(), Society.class));
            }
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("ManualRequest fetch", e);
        }
        return null;
    }
}
