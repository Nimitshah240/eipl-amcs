package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
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

public class LedgerOpeningBalanceListSaveTask extends Task<List<LedgerOpeningBalance>> {
    private List<LedgerOpeningBalance> dtoList;
    private boolean fromMigration = false;

    public LedgerOpeningBalanceListSaveTask(List<LedgerOpeningBalance> dtoList) {
        this.dtoList = dtoList;
    }


    @Override
    protected List<LedgerOpeningBalance> call() throws Exception {
        if (fromMigration) {
            List<LedgerOpeningBalance> listRes = new ArrayList<>();
            List<List<LedgerOpeningBalance>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<LedgerOpeningBalance> memberDtos : listTemp) {
                try {
                    RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                    String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_OPENING_BALANCE+ "/import";
                    ResponseEntity<LedgerOpeningBalance[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(memberDtos), LedgerOpeningBalance[].class);

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
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.LEDGER_OPENING_BALANCE+"/import";
                ResponseEntity<LedgerOpeningBalance[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), LedgerOpeningBalance[].class);

                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;
                return response.getBody()!=null?Arrays.asList(response.getBody()):null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
