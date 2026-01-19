package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import com.eipl.amcs.operation.procurement.repository.DpuIncentiveRequestRepository;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Slf4j
public class DpuIncentiveSaveTask extends Task<DpuIncentiveRequest> {

    private Map<String, Object> mapOfCollectionConfig;

    public DpuIncentiveSaveTask(Map<String, Object> mapOfCollectionConfig) {
        this.mapOfCollectionConfig = mapOfCollectionConfig;
    }

    @Override
    protected DpuIncentiveRequest call() throws Exception {
        try {
            DpuIncentiveRequestRepository dpuIncentiveRequestRepository = EmcsAppContext.getContext().getBean(DpuIncentiveRequestRepository.class);
            DateTimeFormatter dTF1 = DateTimeFormatter.ofPattern("HH:mm");


            DpuIncentiveRequest dpuIncentiveRequest = new DpuIncentiveRequest();
            dpuIncentiveRequest.setIncentiveMasterCode(Long.parseLong(MainApp.identityDto.getSociety().getCode().toString() + "01"));
            dpuIncentiveRequest.setSociety(MainApp.identityDto.getSociety());
            dpuIncentiveRequest.setMstime(LocalTime.parse(this.mapOfCollectionConfig.get("mStartTime").toString(), dTF1));
            dpuIncentiveRequest.setMctime(LocalTime.parse(this.mapOfCollectionConfig.get("mCutoffTime").toString(), dTF1));
            dpuIncentiveRequest.setMltime(LocalTime.parse(this.mapOfCollectionConfig.get("mLockTime").toString(), dTF1));
            dpuIncentiveRequest.setEstime(LocalTime.parse(this.mapOfCollectionConfig.get("eStartTime").toString(), dTF1));
            dpuIncentiveRequest.setEctime(LocalTime.parse(this.mapOfCollectionConfig.get("eCutoffTime").toString(), dTF1));
            dpuIncentiveRequest.setEltime(LocalTime.parse(this.mapOfCollectionConfig.get("eLockTime").toString(), dTF1));
            dpuIncentiveRequest.setUnionCode(MainApp.identityDto.getUnion().getCode());
            dpuIncentiveRequestRepository.save(dpuIncentiveRequest);
        } catch (Exception e) {
            log.error("Dpu incentive save", e);
        }
        return null;
    }
}