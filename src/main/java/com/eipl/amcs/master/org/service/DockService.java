package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.dto.DockDto;
import com.eipl.amcs.master.org.model.Dock;

import java.util.List;
import java.util.Optional;

public interface DockService {

	List<DockDto> findAll();

	Dock save(Dock dock, String identityInfo);
	
	DockDto save(DockDto dockDto, String identityInfo);

	Dock update(Dock dock);
	
	DockDto update(DockDto dockDto, String identityInfo);

	Optional<Dock> findById(String dockNo);
	
	void delete(String dockNo, String identityInfo);

	void delete(Dock dock, String identityInfo);
}
