package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.NarrationType;
import com.eipl.amcs.master.account.repository.NarrationTypeRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NarrationTypeLoadTask extends Task<List<NarrationType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NarrationTypeLoadTask.class);

    @Override
    protected List<NarrationType> call() throws Exception {
        try {
            NarrationTypeRepository narrationRepository = EmcsAppContext.getContext().getBean(NarrationTypeRepository.class);
            return narrationRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("FinancialYear fetch", e);
        }
        return null;
    }
}
