package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.procurement.model.BmcRecording;
import com.eipl.amcs.operation.procurement.repository.BmcRecordingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.config.BeanConfig.bmcRecordingRepository;
import static com.eipl.amcs.config.BeanConfig.nextCodeService;

@Service
public class BmcRecordingServiceImpl implements BmcRecordingService {

//    private final BmcRecordingRepository bmcRecordingRepository;
//    @Autowired
//    private NextCodeService nextCodeService;

//    @Autowired
//    public BmcRecordingServiceImpl(BmcRecordingRepository bmcRecordingRepository) {
//        this.bmcRecordingRepository = bmcRecordingRepository;
//    }

    @Override
    public List<BmcRecording> getAllBmcRecordings() {
        return bmcRecordingRepository.findAll();
    }

    @Override
    public Optional<BmcRecording> getBmcRecordingByCode(Long code) {
        return bmcRecordingRepository.findById(code);
    }

    @Override
    public BmcRecording save(BmcRecording recording, String identityInfo) {
//       BmcRecording rec = recording.getCode();
        String code = nextCodeService.getNextCode("BmcRecording", "code", recording.getSocietyCode(), 4);
        recording.setCode(Long.valueOf(code));
//        recording.setInitData();
        return bmcRecordingRepository.save(recording);
    }

    public BmcRecording update(BmcRecording recording) {
        recording.setupdateData();
        return bmcRecordingRepository.save(recording);
    }
    @Override
    public BmcRecording updateBmcRecording(Long code, BmcRecording recording) {
        recording.setCode(code);
        return bmcRecordingRepository.save(recording);
    }

    @Override
    public void deleteBmcRecording(Long code) {
        bmcRecordingRepository.deleteById(code);
    }

}
