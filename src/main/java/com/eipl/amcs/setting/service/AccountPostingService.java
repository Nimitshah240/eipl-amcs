package com.eipl.amcs.setting.service;

import com.eipl.amcs.setting.dto.AccountPostingDto;
import com.eipl.amcs.setting.model.AccountPosting;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountPostingService {

    AccountPosting save(AccountPosting accountPosting, List<AccountPostingDto> accountPostingDtoList);

    List<AccountPostingDto> loadMilkCollectionAccountPosting(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    List<AccountPostingDto> loadLocalMilkSaleAccountPosting(int type, LocalDateTime fromDateTime, LocalDateTime toDateTime);
}