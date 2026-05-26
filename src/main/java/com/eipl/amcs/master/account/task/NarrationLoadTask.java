package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Narration;
import com.eipl.amcs.master.account.repository.NarrationRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NarrationLoadTask extends Task<List<Narration>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NarrationLoadTask.class);

    @Override
    protected List<Narration> call() throws Exception {
        try {
            NarrationRepository narrationRepository = EmcsAppContext.getContext().getBean(NarrationRepository.class);
            return narrationRepository.findAll();
        } catch (Exception e) {
            LOGGER.error("FinancialYear fetch", e);
        }
        return null;
    }
}
