package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HardwareDeviceRepository extends JpaRepository<HardwareDevice, String> {

    @Override
    Optional<HardwareDevice> findById(String id);
}
