package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.repository.CommitteeMembersRepository;
import com.eipl.amcs.master.account.repository.CommitteeRepository;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class CommitteeSaveTask extends Task<Object> {

    private final Committee dto;
    private final short update;

    public CommitteeSaveTask(Committee dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            CommitteeRepository committeeRepository = EmcsAppContext.getContext().getBean(CommitteeRepository.class);
            CommitteeMembersRepository committeeMemberRepository = EmcsAppContext.getContext().getBean(CommitteeMembersRepository.class);

            List<CommitteeMembers> listMember = new ArrayList<>(dto.getMembers());
            dto.setMembers(null);

            NextCodeRepository nextCodeRepository = EmcsAppContext.getContext().getBean(NextCodeRepository.class);
            Committee committee = committeeRepository.save(dto);
            for (CommitteeMembers committeeMembers : listMember) {
                committeeMembers.setCommittee(committee);
                if (committeeMembers.getCode() == null || committeeMembers.getCode().isBlank())
                    committeeMembers.setCode(nextCodeRepository.getNextCode("CommitteeMembers", "code", MainApp.identityDto.getSociety().getCode(), 0));
                committeeMemberRepository.save(committeeMembers);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
