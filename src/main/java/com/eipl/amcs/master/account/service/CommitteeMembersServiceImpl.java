package com.eipl.amcs.master.account.service;

import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.committeeMembersRepository;
import static com.eipl.amcs.config.BeanConfig.nextCodeRepository;

@Service
public class CommitteeMembersServiceImpl implements CommitteeMembersService {

//	private CommitteeMembersRepository committeeMembersRepository;
//	private SocietyRepository socRepository;
//	private NextCodeRepository nextCodeRepository;
//	private DesignationRepository designationRepository;


	private static final Logger log = LoggerFactory.getLogger(CommitteeMembersServiceImpl.class);


	@Override
	public List<CommitteeMembers> findAll() {
		List<CommitteeMembers> list = committeeMembersRepository.findAll();
		for (CommitteeMembers committeeMembers : list) {
			committeeMembers.setSociety(Hibernate.unproxy(committeeMembers.getSociety(),Society.class));
			committeeMembers.setDesignation(Hibernate.unproxy(committeeMembers.getDesignation(), Designation.class));
		}
		log.info("CommitteeMembers findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public CommitteeMembers findAllByCommitteeMembers(String memberCode) {


		return null;
	}

	@Override
	public CommitteeMembers save(CommitteeMembers committeeMembers, String identityInfo) {


//		old Desingnation Delete code
//		----------------
//		List<CommitteeMembers> list = findAll();
//		CommitteeMembers committeeMembers1 = list.stream().filter(p->p.getDesignation()==committeeMembers.getDesignation()).findFirst().get();
//		if(committeeMembers1!=null)
//			committeeMembersRepository.delete(committeeMembers1);
//		-----------------
		String code = nextCodeRepository.getNextCode("CommitteeMembers", "code", committeeMembers.getSociety().getCode(),
				0);
		committeeMembers.setCode(code);
		committeeMembers.setInitData();
		CommitteeMembers newdata = committeeMembersRepository.customSave(committeeMembers,identityInfo);
		newdata.setSociety(Hibernate.unproxy(newdata.getSociety(), Society.class));
		newdata.setDesignation(Hibernate.unproxy(newdata.getDesignation(), Designation.class));

		return newdata;
	}

	@Override
	public CommitteeMembers update(CommitteeMembers committeeMembers, String identityInfo) {

		String code = nextCodeRepository.getNextCode("CommitteeMembers", "committeeMembersCode", committeeMembers.getSociety().getCode(),
				0);
		committeeMembers.setupdateData();
		CommitteeMembers newdata = committeeMembersRepository.customSave(committeeMembers,identityInfo);
		newdata.setSociety(Hibernate.unproxy(newdata.getSociety(), Society.class));
		newdata.setDesignation(Hibernate.unproxy(newdata.getDesignation(), Designation.class));
		return null;
	}

	@Override
	public void delete(String memberCode, String identityInfo) {
		CommitteeMembers cm = committeeMembersRepository.findById(memberCode).get();
		committeeMembersRepository.delete(cm);
	}

//	@Override
//	public void delete(CommitteeMembers committeeMembers, String identityInfo) {
//		committeeMembersRepository.customDelete(committeeMembers.getCode(), identityInfo);
//
//	}

}

