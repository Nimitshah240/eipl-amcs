package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.BmcRecording;
import org.springframework.stereotype.Repository;

@Repository
public interface BmcRecordingRepository extends BaseRepository<BmcRecording, Long> {

//    List<BmcRecording> findAll(Integer code);
}
