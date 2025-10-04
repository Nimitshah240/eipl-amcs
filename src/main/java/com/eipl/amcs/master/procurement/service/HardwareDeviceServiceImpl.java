package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.repository.HardwareDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eipl.amcs.config.BeanConfig.hardwareDeviceRepository;

@Service
public class HardwareDeviceServiceImpl implements HardwareDeviceService {

//	@Autowired
//	private HardwareDeviceRepository hardwareDeviceRepository;

    private static final Logger log = LoggerFactory.getLogger(HardwareDeviceServiceImpl.class);

    @Override
    public List<HardwareDevice> findAll() {
        List<HardwareDevice> list = hardwareDeviceRepository.findAll();
        log.info("HardwareDevices findAll {} items fetched", list.size());
        return list;
    }
}


