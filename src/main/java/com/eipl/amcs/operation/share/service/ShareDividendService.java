package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.exception.BusinessValidationFailException;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.model.ShareDividend;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShareDividendService {
    List<ShareDividend> findAll();

    Optional<ShareDividend> findById(String id);

    ShareDividend save(ShareDividend share, String identityInfo);

    String save(List<ShareDividend> shareList, String identityInfo) throws BusinessValidationFailException;

    ShareDividend update(ShareDividend obj, String identityHeader);

    Share cancel(Share share);

    Share cancel(String id, String identityInfo);

    List<ShareDividend> findAllData(LocalDate fromDt, LocalDate toDt);

    ShareDividend delete(String id, String identityInfo);

    ShareDividend deleteAll(LocalDate fromDt, LocalDate toDt);
}
