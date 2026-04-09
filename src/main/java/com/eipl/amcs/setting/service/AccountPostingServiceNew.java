package com.eipl.amcs.setting.service;

import com.eipl.amcs.setting.dto.AccountPostingDtoNew;
import com.eipl.amcs.setting.model.AccountPosting;

import java.util.List;

public interface AccountPostingServiceNew {


    List<AccountPostingDtoNew> loadAccountPostingData(AccountPosting accountPosting);

}