package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.model.MomAction;
import com.eipl.amcs.master.account.repository.LedgerTypeRepository;
import com.eipl.amcs.master.account.repository.MeetingAgendaRepository;
import com.eipl.amcs.master.account.repository.MomActionRepository;
import com.eipl.amcs.master.account.repository.MomRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.MainApp.context;

@Service
public class MeetingAgendaServiceImpl implements MeetingAgendaService {

    @Autowired
    private MeetingAgendaRepository meetingAgendaRepository;
    @Autowired
    private MomRepository momRepository;
    @Autowired
    private MomActionRepository momActionRepository;
    @Autowired
    private NextCodeService nextCodeService;

    private static final Logger log = LoggerFactory.getLogger(MeetingAgendaServiceImpl.class);


    @Override
    public List<MeetingAgenda> findAll() {
        List<MeetingAgenda> list = meetingAgendaRepository.findAll();
        for (MeetingAgenda meeting : list) {
            meeting.setSociety(Hibernate.unproxy(meeting.getSociety(), Society.class));
            meeting.setUnion(Hibernate.unproxy(meeting.getUnion(), Union.class));
        }
        log.info("meeting findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<Mom> findMom(String code) {
        Optional<MeetingAgenda> meetingAgenda = meetingAgendaRepository.findById(code);
        List<Mom> list = momRepository.findByMeetingAgenda(meetingAgenda.get());
        for (Mom mom : list) {
            mom.setSociety(Hibernate.unproxy(mom.getSociety(), Society.class));
            mom.setUnion(Hibernate.unproxy(mom.getUnion(), Union.class));
        }
        log.info("mom findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<MomAction> findMomAction(String code) {
        Mom mom = momRepository.findById(code).get();
        List<MomAction> list = momActionRepository.findByMom(mom);
        for (MomAction momAction : list) {
            momAction.setSociety(Hibernate.unproxy(momAction.getSociety(), Society.class));
            momAction.setUnion(Hibernate.unproxy(momAction.getUnion(), Union.class));
            momAction.setMeetingAgenda(Hibernate.unproxy(momAction.getMeetingAgenda(), MeetingAgenda.class));
            momAction.setMom(Hibernate.unproxy(momAction.getMom(), Mom.class));
        }
        log.info("actionc findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public MeetingAgenda save(MeetingAgenda meetingAgenda, String identityInfo) {

        meetingAgenda.setInitData();
        return meetingAgendaRepository.save(meetingAgenda);
    }

    @Override
    public Mom saveMom(Mom mom, String identityInfo) {
        mom.setInitData();
        String code = nextCodeService.getNextCode("Mom", "code", mom.getSociety().getCode(), 2);
        mom.setCode(code);
        momRepository.save(mom);
        return null;

    }

    @Override
    public MomAction saveMomAction(MomAction momAction, String identityInfo) {
        momAction.setInitData();
        String code = nextCodeService.getNextCode("MomAction", "code", momAction.getSociety().getCode(), 2);
        momAction.setCode(code);
        momActionRepository.save(momAction);
        return null;
    }

    @Override
    public MeetingAgenda update(MeetingAgenda meetingAgenda, String identityInfo) {
        meetingAgenda.setupdateData();
        return meetingAgendaRepository.save(meetingAgenda);
    }

    @Override
    public Mom updateMom(Mom mom, String identityInfo) {
        mom.setupdateData();
        return momRepository.save(mom);
    }

    @Override
    public MomAction updateMomAction(MomAction momAction, String identityInfo) {
        momAction.setupdateData();
        momActionRepository.save(momAction);
        return null;
    }

    @Override
    public Optional<MeetingAgenda> findById(String meetingAgendaNo) {
        return Optional.empty();
    }

    @Override
    public void delete(String meetingAgendaNo, String identityInfo) {
        meetingAgendaRepository.deleteById(meetingAgendaNo);
    }

    @Override
    public void deleteMom(String momCode, String identityInfo) {
        momRepository.deleteById(momCode);

    }

    @Override
    public void deleteMomAction(String momActionCode, String identityInfo) {
        momActionRepository.deleteById(momActionCode);
    }

    @Override
    public void delete(MeetingAgenda meetingAgenda, String identityInfo) {
        meetingAgendaRepository.delete(meetingAgenda);
    }

}