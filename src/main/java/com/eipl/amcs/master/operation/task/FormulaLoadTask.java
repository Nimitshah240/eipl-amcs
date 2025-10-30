package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Formula;
import com.eipl.amcs.master.operation.repository.FormulaRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FormulaLoadTask extends Task<List<Formula>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormulaLoadTask.class);

    @Override
    protected List<Formula> call() throws Exception {
        try {
            FormulaRepository repository = EmcsAppContext.getContext().getBean(FormulaRepository.class);
            return repository.findAll();
        } catch (Exception e) {
            LOGGER.error("Formula fetch", e);
        }
        return null;
    }
}