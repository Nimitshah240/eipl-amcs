package com.eipl.amcs.setting.service;

import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.setting.model.AccountPosting;

import java.util.List;

public interface AccountPostingServiceNew {


    List<Voucher> loadAccountPostingData(AccountPosting accountPosting);

    AccountPosting saveAccountPosting(AccountPosting accountPosting, List<Voucher> voucherList);

}