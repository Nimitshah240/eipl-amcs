package com.eipl.amcs.master.account.service;

import com.eipl.amcs.base.repository.NextCodeRepository;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.master.account.repository.StaffSalaryHeadRepository;
import com.eipl.amcs.master.account.repository.StaffSalaryMappingRepository;
import com.eipl.amcs.master.org.model.Society;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.nextCodeService;
import static com.eipl.amcs.config.BeanConfig.staffSalaryMappingRepository;

@Service
public class StaffSalaryMappingServiceImpl implements StaffSalaryMappingService {

//	private StaffSalaryMappingRepository staffSalaryMappingRepository;
//	private NextCodeRepository nextCodeRepository;
//	private NextCodeService nextCodeService;


	private static final Logger log = LoggerFactory.getLogger(StaffSalaryMappingServiceImpl.class);

	@Override
	public List<StaffSalaryMapping> findAll() {
		List<StaffSalaryMapping> list = staffSalaryMappingRepository.findAll();
		for (StaffSalaryMapping staffSalaryMapping : list) {
			staffSalaryMapping.setSociety(Hibernate.unproxy(staffSalaryMapping.getSociety(), Society.class));
			staffSalaryMapping.setStaffSalaryHead(Hibernate.unproxy(staffSalaryMapping.getStaffSalaryHead(), StaffSalaryHead.class));
			staffSalaryMapping.setStaffMember(Hibernate.unproxy(staffSalaryMapping.getStaffMember(), StaffMember.class));
		}
		log.info("StaffSalaryMapping findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public String save(List<StaffSalaryMapping> staffSalaryMappingList, String identityInfo) {
		for (StaffSalaryMapping staffSalaryMapping : staffSalaryMappingList) {
			String c = nextCodeService.getNextCode("StaffSalaryMapping", "code", staffSalaryMapping.getSociety().getCode() , 3);
			staffSalaryMapping.setCode(Integer.valueOf(c));

			staffSalaryMappingRepository.save(staffSalaryMapping);
		}
		return null;

	}

	@Override
	public List<StaffSalaryMapping> update(List<StaffSalaryMapping> staffSalaryMapping, String identityInfo) {
//		staffSalaryMappingRepository.save(staffSalaryMapping);
		return null;
	}

	@Override
	public Optional<StaffSalaryMapping> findById(String staffMemberName) {
		return Optional.empty();
	}

	@Override
	public void delete(String staffMemberName, String identityInfo) {
		staffSalaryMappingRepository.deleteById(Integer.valueOf(staffMemberName));
	}

	@Override
	public void delete(StaffSalaryMapping staffSalaryMapping, String identityInfo) {
      staffSalaryMappingRepository.delete(staffSalaryMapping);
	}

}