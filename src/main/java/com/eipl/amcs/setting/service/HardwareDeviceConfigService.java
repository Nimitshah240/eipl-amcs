package com.eipl.amcs.setting.service;

import com.eipl.amcs.setting.model.HardwareDeviceConfig;

import java.util.List;
import java.util.Optional;

public interface HardwareDeviceConfigService {

	List<HardwareDeviceConfig> findAll();

	HardwareDeviceConfig save(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo);

	HardwareDeviceConfig update(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo);

	Optional<HardwareDeviceConfig> findById(String hardwareDeviceConfig);
	
	void delete(String code, String identityInfo);

	void delete(HardwareDeviceConfig hardwareDeviceConfig, String identityInfo);

	String saveUpdate(List<HardwareDeviceConfig> list, String identityInfo);
}
