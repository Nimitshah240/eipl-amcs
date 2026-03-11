package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.repository.MilkCollectionAccountPostingRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class MilkCollectionAccountPostingLoadTask extends Task<List<MilkCollectionAccountPosting>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkCollectionAccountPostingLoadTask.class);

    private LocalDate fromDate;
    private LocalDate toDate;

    public MilkCollectionAccountPostingLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkCollectionAccountPosting> call() throws Exception {
        try {

            MilkCollectionAccountPostingRepository milkCollectionAccountPostingRepository = EmcsAppContext.getContext().getBean(MilkCollectionAccountPostingRepository.class);
            return milkCollectionAccountPostingRepository.findByFromDateGreaterThanEqualAndToDateLessThanEqual(fromDate, toDate);

        } catch (Exception e) {
            LOGGER.error("LedgerMappingEvent fetch", e);
        }
        return null;
    }
}