package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.operation.share.model.ShareRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShareRateService {
    List<ShareRate> findAll();

    Optional<ShareRate> findById(String id);

    ShareRate save(ShareRate shareRate, String identityInfo);

    ShareRate update(ShareRate shareRate, String identityHeader);

    void delete(ShareRate shareRate, String identityInfo);

    void delete(String id, String identityInfo);

    ShareRate fetchRate(LocalDate dt);
}
