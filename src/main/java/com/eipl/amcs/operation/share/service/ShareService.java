package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.share.model.Share;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShareService {
    List<Share> findAll();

    List<Share> findByShareCode(String code);

    Optional<Share> findById(String id);

    Share save(Share share, String identityInfo);

    Share update(Share share, String identityHeader);

    Share cancel(Share share);

    Share cancel(String id, String identityInfo);

    Share delete(String id, String identityInfo);

    List<Share> findAllData(LocalDate fromDt, LocalDate toDt);

    List<Share> findByMember(Member member);

    Share revert(String id, String identityInfo);

    void transferRevert(String code, String identityHeader);
}
