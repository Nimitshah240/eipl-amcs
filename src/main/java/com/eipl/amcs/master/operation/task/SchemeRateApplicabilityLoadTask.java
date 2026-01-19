package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.repository.SchemeRateApplicabilityRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SchemeRateApplicabilityLoadTask extends Task<List<SchemeRateApplicability>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeRateApplicabilityLoadTask.class);

    @Override
    protected List<SchemeRateApplicability> call() throws Exception {
        try {
            SchemeRateApplicabilityRepository schemeRateApplicabilityRepository = EmcsAppContext.getContext().getBean(SchemeRateApplicabilityRepository.class);
            List<SchemeRateApplicability> list = schemeRateApplicabilityRepository.findByIsActiveTrue(Sort.by("schemeRateAppCode").descending());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("SchemeRate error", e);
        }
        return null;
    }
}