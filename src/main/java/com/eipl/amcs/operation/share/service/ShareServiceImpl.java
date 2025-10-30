package com.eipl.amcs.operation.share.service;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.repository.ShareRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    ShareRepository shareRepository;

    @Override
    public List<Share> findAll() {
        return shareRepository.findAll().stream().filter(e -> !e.getCancelled()).collect(Collectors.toList());
    }

    @Override
    public List<Share> findAllData(LocalDate fromDt, LocalDate toDt) {
        return shareRepository.findByIssueDateBetween(fromDt, toDt, Sort.by("issueDate").descending());
    }

    @Override
    public List<Share> findByMember(Member member) {
        List<Share> shareList = shareRepository.findByMember(member);
        for (Share share : shareList) {
            share.setSociety(Hibernate.unproxy(share.getSociety(), Society.class));
        }
        return shareList;
    }

    @Override
    public List<Share> findByShareCode(String code) {
        List<Share> shareList = shareRepository.findByShareCode(code);
        for (Share share : shareList) {
            share.setSociety(Hibernate.unproxy(share.getSociety(), Society.class));
        }
        return shareList;
    }

    @Override
    public Optional<Share> findById(String id) {
        return shareRepository.findById(id);
    }

    @Override
    public Share save(Share share, String identityInfo) {
        if (!share.getCheckMember()) {
            Member m = new Member();
            m.setCode(share.getMember().getCode());
        }

        if (share.getTransferredFrom() != null) {
            List<Share> oldShareList = findByMember(share.getTransferredFrom());
            for (Share oldShare : oldShareList) {
                if (oldShare != null) {
                    share.setTransferDate(LocalDate.now());
                    oldShare.setTransferred(true);
                    oldShare.setNoOfTransferredShare(share.getNoOfShare());
                    shareRepository.customUpdate(oldShare, identityInfo);
                }
            }
        }
        share.setInitData();
        share.setMember(Hibernate.unproxy(share.getMember(), Member.class));
        shareRepository.customSave(share, identityInfo);

        return null;
    }

    @Override
    public Share update(Share share, String identityHeader) {
        share.setupdateData();
        return shareRepository.customUpdate(share, identityHeader);
    }

    @Override
    public Share cancel(Share share) {

        return null;
    }

    @Override
    public Share cancel(String id, String identityInfo) {
        Optional<Share> share = shareRepository.findById(id);
        if (share.isPresent()) {
            share.get().setCancelled(true);
            share.get().setCancelDate(LocalDate.now());
            share.get().setupdateData();
            shareRepository.customUpdate(share.get(), identityInfo);
        }
        return null;
    }

    @Override
    public Share delete(String id, String identityInfo) {
        Optional<Share> share = shareRepository.findById(id);
        if (share.isPresent()) {
            shareRepository.customDelete(share.get(), identityInfo);
        }
        return null;
    }

    @Override
    public Share revert(String id, String identityInfo) {
        Optional<Share> share = shareRepository.findById(id);
        if (share.isPresent()) {
            share.get().setCancelled(false);
            share.get().setCancelDate(null);
            share.get().setupdateData();
            shareRepository.customUpdate(share.get(), identityInfo);
        }
        return null;
    }

    @Override
    public void transferRevert(String code, String identityHeader) {
        Optional<Share> newShare = shareRepository.findById(code);
        Optional<Share> oldShare = shareRepository.findById(newShare.get().getXCol1());
        oldShare.get().setTransferred(false);
        oldShare.get().setNoOfTransferredShare(0);
        shareRepository.customUpdate(oldShare.get(), identityHeader);
        shareRepository.delete(newShare.get());
    }
}
