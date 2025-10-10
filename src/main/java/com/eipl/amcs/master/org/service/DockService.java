package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;

import java.util.List;
import java.util.Optional;

public interface DockService {

	List<DockMilkTypeDto> findAll();

	Dock save(Dock dock, String identityInfo);
	
	DockMilkTypeDto save(DockMilkTypeDto DockMilkTypeDto, String identityInfo);

	Dock update(Dock dock);
	
	DockMilkTypeDto update(DockMilkTypeDto DockMilkTypeDto, String identityInfo);

	Optional<Dock> findById(String dockNo);
	
	void delete(String dockNo, String identityInfo);

	void delete(Dock dock, String identityInfo);
}
