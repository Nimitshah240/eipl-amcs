package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.service.MilkCollectionAccountPostingService;
import javafx.concurrent.Task;

import java.util.List;

public class MilkCollectionAccountPostingSaveTask extends Task<MilkCollectionAccountPosting> {

    private final MilkCollectionAccountPosting milkCollectionAccountPosting;
    private final List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList;

    public MilkCollectionAccountPostingSaveTask(MilkCollectionAccountPosting milkCollectionAccountPosting, List<MilkCollectionAccountPostingDto> milkCollectionAccountPostingDtoList) {
        this.milkCollectionAccountPosting = milkCollectionAccountPosting;
        this.milkCollectionAccountPostingDtoList = milkCollectionAccountPostingDtoList;
    }

    @Override
    protected MilkCollectionAccountPosting call() throws Exception {
        try {

            MilkCollectionAccountPostingService milkCollectionAccountPostingService = EmcsAppContext.getContext().getBean(MilkCollectionAccountPostingService.class);
            return milkCollectionAccountPostingService.save(milkCollectionAccountPosting, milkCollectionAccountPostingDtoList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}