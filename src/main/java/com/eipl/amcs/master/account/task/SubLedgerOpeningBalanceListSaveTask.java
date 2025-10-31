package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.master.account.service.SubLedgerOpeningBalanceService;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class SubLedgerOpeningBalanceListSaveTask extends Task<List<SubLedgerOpeningBalance>> {
    private final List<SubLedgerOpeningBalance> dtoList;
    private final boolean fromMigration = false;

    public SubLedgerOpeningBalanceListSaveTask(List<SubLedgerOpeningBalance> dtoList) {
        this.dtoList = dtoList;
    }

    @Override
    protected List<SubLedgerOpeningBalance> call() throws Exception {
        SubLedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(SubLedgerOpeningBalanceService.class);

        if (fromMigration) {
            List<SubLedgerOpeningBalance> listRes = new ArrayList<>();
            List<List<SubLedgerOpeningBalance>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<SubLedgerOpeningBalance> memberDtos : listTemp) {
                try {
                    listRes.addAll(service.importSubLedgerBalance(memberDtos, CommonUtils.setIdentityHeader()));
                    updateMessage("Migration in progress " + current + " of " + listTemp.size());
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return listRes;
        } else {
            try {
                return service.importSubLedgerBalance(dtoList, CommonUtils.setIdentityHeader());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
