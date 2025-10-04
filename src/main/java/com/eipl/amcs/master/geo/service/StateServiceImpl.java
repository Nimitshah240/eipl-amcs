package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.repository.HamletRepository;
import com.eipl.amcs.master.geo.repository.StateRepository;
import com.eipl.amcs.master.geo.repository.VillageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.config.BeanConfig.stateRepository;

@Service
public class StateServiceImpl implements StateService {

//	private StateRepository stateRepository;

	private static final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);

	@Override
	public List<State> findAll() {
		List<State> list = stateRepository.findAll(Sort.by("name"));
		log.info("States findAll {} items fetched", list.size());
		return list;
	}

}
