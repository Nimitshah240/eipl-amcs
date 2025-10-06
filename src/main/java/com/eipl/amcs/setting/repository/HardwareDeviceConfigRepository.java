package com.eipl.amcs.setting.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HardwareDeviceConfigRepository extends BaseRepository<HardwareDeviceConfig, String> {

	@Override
	@EntityGraph(attributePaths = { "society", "hardwareDevice", "dock" })
	List<HardwareDeviceConfig> findAll();
	
	@Override
	@EntityGraph(attributePaths = { "society", "hardwareDevice", "dock" })
	Optional<HardwareDeviceConfig> findById(String id);

}
