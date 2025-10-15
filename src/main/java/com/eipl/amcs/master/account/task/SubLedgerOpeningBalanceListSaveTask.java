package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerOpeningBalance;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.apache.commons.collections4.ListUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubLedgerOpeningBalanceListSaveTask extends Task<List<SubLedgerOpeningBalance>> {
    private List<SubLedgerOpeningBalance> dtoList;
    private boolean fromMigration = false;

    public SubLedgerOpeningBalanceListSaveTask(List<SubLedgerOpeningBalance> dtoList) {
        this.dtoList = dtoList;
    }
//
//    public SubLedgerOpeningBalanceListSaveTask(List<SubLedgerOpeningBalance> dtoList, boolean fromMigration) {
//        this.dtoList = dtoList;
//        this.fromMigration = fromMigration;
//    }

    @Override
    protected List<SubLedgerOpeningBalance> call() throws Exception {
        if (fromMigration) {
            List<SubLedgerOpeningBalance> listRes = new ArrayList<>();
            List<List<SubLedgerOpeningBalance>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<SubLedgerOpeningBalance> memberDtos : listTemp) {
                try {
                    RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                    String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_OPENING_BALANCE+ "/import";
                    ResponseEntity<SubLedgerOpeningBalance[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(memberDtos), SubLedgerOpeningBalance[].class);

                    if (response == null || response.getStatusCode() != HttpStatus.OK)
                        continue;
                    listRes.addAll(Arrays.asList(response.getBody()));
                    updateMessage("Migration in progress " + current + " of " + listTemp.size());
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return listRes;
        } else {
            try {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_OPENING_BALANCE+"/import";
                ResponseEntity<SubLedgerOpeningBalance[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), SubLedgerOpeningBalance[].class);

                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;
                return Arrays.asList(response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
