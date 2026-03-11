package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.service.MilkCollectionAccountPostingService;
import javafx.concurrent.Task;

import java.util.List;

public class MilkCollectionAccountPostingSaveTask extends Task<MilkCollectionAccountPosting> {

    private final MilkCollectionAccountPosting milkCollectionAccountPosting;
    private final List<MilkCollectionAccountPostingDto> creditMilkCollectionAccountPostingDto;
    private final List<MilkCollectionAccountPostingDto> debitMilkCollectionAccountPostingDto;

    public MilkCollectionAccountPostingSaveTask(MilkCollectionAccountPosting milkCollectionAccountPosting, List<MilkCollectionAccountPostingDto> creditMilkCollectionAccountPostingDto, List<MilkCollectionAccountPostingDto> debitMilkCollectionAccountPostingDto) {
        this.milkCollectionAccountPosting = milkCollectionAccountPosting;
        this.creditMilkCollectionAccountPostingDto = creditMilkCollectionAccountPostingDto;
        this.debitMilkCollectionAccountPostingDto = debitMilkCollectionAccountPostingDto;
    }

    @Override
    protected MilkCollectionAccountPosting call() throws Exception {
        try {

            MilkCollectionAccountPostingService milkCollectionAccountPostingService = EmcsAppContext.getContext().getBean(MilkCollectionAccountPostingService.class);
            return milkCollectionAccountPostingService.save(milkCollectionAccountPosting, creditMilkCollectionAccountPostingDto, debitMilkCollectionAccountPostingDto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}