package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.repository.HamletRepository;
import com.eipl.amcs.master.geo.repository.VillageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HamletServiceImpl implements HamletService {

    @Autowired
    private HamletRepository hamletRepository;
    @Autowired
    private VillageRepository villageRepository;

    private static final Logger log = LoggerFactory.getLogger(HamletServiceImpl.class);

    @Override
    public List<Hamlet> findAll() {
        List<Hamlet> list = hamletRepository.findAll(Sort.by("name"));
        log.info("Hamlet findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public List<Hamlet> findAll(String villageCode) {
        Village village = villageRepository.findById(villageCode)
                .orElseThrow(() -> new EntityNotFoundException(Village.class, "invalid.villagecode"));
        return hamletRepository.findByVillage(village, Sort.by("name"));
    }

}
