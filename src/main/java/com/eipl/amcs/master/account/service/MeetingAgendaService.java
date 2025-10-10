package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.model.MomAction;

import java.util.List;
import java.util.Optional;

public interface MeetingAgendaService {
    List<MeetingAgenda> findAll();

    List<Mom> findMom(String code);

    List<MomAction> findMomAction(String code);

    MeetingAgenda save(MeetingAgenda meetingAgenda, String identityInfo);

    Mom saveMom(Mom mom, String identityInfo);

    MomAction saveMomAction(MomAction mom, String identityInfo);

    MeetingAgenda update(MeetingAgenda meetingAgenda, String identityInfo);

    Mom updateMom(Mom mom, String identityInfo);

    MomAction updateMomAction(MomAction momAction, String identityInfo);

    Optional<MeetingAgenda> findById(String meetingAgendaNo);

    void delete(String meetingAgendaNo, String identityInfo);

    void deleteMom(String momCode, String identityInfo);

    void deleteMomAction(String momActionCode, String identityInfo);

    void delete(MeetingAgenda meetingAgenda, String identityInfo);
}
