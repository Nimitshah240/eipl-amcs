package com.eipl.amcs.setting.service;

import com.eipl.amcs.setting.dto.MilkCollectionAccountPostingDto;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;

import java.util.List;

public interface MilkCollectionAccountPostingService {

    MilkCollectionAccountPosting save(
            MilkCollectionAccountPosting milkCollectionAccountPosting,
            List<MilkCollectionAccountPostingDto>
                    creditMilkCollectionAccountPostingDto, List<MilkCollectionAccountPostingDto>
                    debitMilkCollectionAccountPostingDto);
}