package com.eipl.amcs.auth.service;

import com.eipl.amcs.EiplAmcsAppRunner;
import com.eipl.amcs.auth.dto.IdentityDto;
import com.eipl.amcs.base.repository.IdentityRepository;
import com.eipl.amcs.master.org.repository.DockRepository;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import com.eipl.amcs.master.org.repository.UnionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    DockRepository dockRepository;
    @Autowired
    SocietyRepository societyRepository;
    @Autowired
    UnionRepository unionRepository;
    @Autowired
    IdentityRepository identityRepository;



    @Override
    public IdentityDto fetchIdentity(String dockNumber, String societyCode, String unionCode) {
        IdentityDto dto = new IdentityDto();
        dto.setUnion(unionRepository.findById(unionCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Union Code")));
        dto.setSociety(societyRepository.findById(societyCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Society Code")));
        dto.setDock(dockRepository.findById(dockNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Dock Number")));
        dto.setIdentity(identityRepository.findById("1")
                .orElseThrow(() -> new IllegalArgumentException("Invalid Identity Number")));
        EiplAmcsAppRunner.identityDto = dto;
        return dto;
    }

}
