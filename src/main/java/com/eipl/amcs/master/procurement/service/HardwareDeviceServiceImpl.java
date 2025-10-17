package com.eipl.amcs.master.procurement.service;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.eipl.amcs.master.procurement.repository.HardwareDeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HardwareDeviceServiceImpl implements HardwareDeviceService {

    private static final Logger log = LoggerFactory.getLogger(HardwareDeviceServiceImpl.class);
    @Autowired
    private HardwareDeviceRepository hardwareDeviceRepository;

    @Override
    public List<HardwareDevice> findAll() {
        List<HardwareDevice> list = hardwareDeviceRepository.findAll();
        log.info("HardwareDevices findAll {} items fetched", list.size());
        return list;
    }
}


