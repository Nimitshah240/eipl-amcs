package com.eipl.amcs.operation.procurement.service;


import com.eipl.amcs.operation.procurement.model.BmcRecording;

import java.util.List;
import java.util.Optional;

public interface BmcRecordingService {
    List<BmcRecording> getAllBmcRecordings();

    Optional<BmcRecording> getBmcRecordingByCode(Long code);

    BmcRecording save(BmcRecording recording, String identityInfo);

    BmcRecording update(BmcRecording recording);

//    BmcRecording createBmcRecording(BmcRecording recording);

    BmcRecording updateBmcRecording(Long code, BmcRecording recording);

    void deleteBmcRecording(Long code);


}

