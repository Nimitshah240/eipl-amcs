package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.dto.MemberDto;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
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

public class MemberListSaveTask extends Task<List<MemberImportDto>> {
    private List<MemberDto> dtoList;
    private boolean fromMigration = false;

    public MemberListSaveTask(List<MemberDto> dtoList) {
        this.dtoList = dtoList;
    }

    public MemberListSaveTask(List<MemberDto> dtoList, boolean fromMigration) {
        this.dtoList = dtoList;
        this.fromMigration = fromMigration;
    }

    @Override
    protected List<MemberImportDto> call() throws Exception {
        if (fromMigration) {
            List<MemberImportDto> listRes = new ArrayList<>();
            List<List<MemberDto>> listTemp = ListUtils.partition(dtoList, AppConstant.MIGRATION_LIST_SIZE);
            int current = 1;
            for (List<MemberDto> memberDtos : listTemp) {
                try {
                    RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                    String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER + "/import";
                    ResponseEntity<MemberImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(memberDtos), MemberImportDto[].class);

                    if (response == null || response.getStatusCode() != HttpStatus.OK)
                        continue;
                    listRes.addAll(Arrays.asList(response.getBody()));
                    updateMessage("Processing " + current * AppConstant.MIGRATION_LIST_SIZE + " of " + listTemp.size() * AppConstant.MIGRATION_LIST_SIZE);
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return listRes;
        } else {
            try {
                RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
                String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER + "/import";
                ResponseEntity<MemberImportDto[]> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoList), MemberImportDto[].class);

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
