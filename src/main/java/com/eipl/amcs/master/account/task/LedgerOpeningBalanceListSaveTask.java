package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class LedgerOpeningBalanceListSaveTask extends Task<List<LedgerOpeningBalance>> {
    private final List<LedgerOpeningBalance> dtoList;
    private final boolean fromMigration = false;

    public LedgerOpeningBalanceListSaveTask(List<LedgerOpeningBalance> dtoList) {
        this.dtoList = dtoList;
    }


    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        LedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);

        if (fromMigration) {
            List<LedgerOpeningBalance> listRes = new ArrayList<>();
            List<List<LedgerOpeningBalance>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<LedgerOpeningBalance> memberDtos : listTemp) {
                try {
                    List<LedgerOpeningBalance> list = service.importLedgerBalance(memberDtos, CommonUtil.setIdentityHeader());
                    listRes.addAll(list);
                    updateMessage("Migration in progress " + current + " of " + listTemp.size());
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return listRes;
        } else {
            try {
                List<LedgerOpeningBalance> list = service.importLedgerBalance(dtoList, CommonUtil.setIdentityHeader());
                if (list == null || list.isEmpty())
                    return null;
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
