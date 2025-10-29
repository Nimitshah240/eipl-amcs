package com.eipl.amcs.master.org.service;

import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.DockMilkType;
import com.eipl.amcs.master.org.repository.DockMilkTypeRepository;
import com.eipl.amcs.master.org.repository.DockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DockServiceImpl implements DockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockServiceImpl.class);
    @Autowired
    private DockRepository dockRepository;
    @Autowired
    private DockMilkTypeRepository dockMilkTypeRepository;

    @Override
    public List<DockMilkTypeDto> findAll() {
        List<Dock> list = dockRepository.findAll(Sort.by("dockNo"));
        if (list == null)
            return null;
        LOGGER.info("Docks findAll {} items fetched", list.size());
        List<DockMilkTypeDto> listDto = new ArrayList<>();
        list.forEach(item -> {
            List<DockMilkType> l = dockMilkTypeRepository.findAllByDock(item);
            if (l != null && !l.isEmpty())
                listDto.add(new DockMilkTypeDto(item, l.stream().map(m -> m.getMilkType()).collect(Collectors.toList())));
        });
        return listDto;
    }

    @Override
    public Dock save(Dock dock, String identityInfo) {
        return dockRepository.customSave(dock, identityInfo);
    }

    @Override
    @Transactional
    public DockMilkTypeDto save(DockMilkTypeDto DockMilkTypeDto, String identityInfo) {
        try {
            Dock dock = DockMilkTypeDto.getDock();
            dock.setInitData();
            save(dock, identityInfo);

            DockMilkTypeDto.getMilkTypes().forEach(item -> {
                DockMilkType dockMilkType = new DockMilkType();
                dockMilkType.setCode(dock.getDockNo() + item.getCode());
                dockMilkType.setDock(dock);
                dockMilkType.setMilkType(item);
                dockMilkType.setInitData();
                dockMilkTypeRepository.customSave(dockMilkType, identityInfo);
            });
        } catch (Exception e) {
            LOGGER.error("Dock Save Ex", e);
            return null;
        }
        return DockMilkTypeDto;
    }

    @Override
    @Transactional
    public DockMilkTypeDto update(DockMilkTypeDto DockMilkTypeDto, String identityInfo) {
        try {
            Dock dock = DockMilkTypeDto.getDock();
            dock.setupdateData();
            dockRepository.customUpdate(dock, identityInfo);

            List<DockMilkType> list = dockMilkTypeRepository.findAllByDock(dock);
            if (list != null) {
                list.forEach(item -> {
                    dockMilkTypeRepository.customDelete(item, identityInfo);
                });
            }

            DockMilkTypeDto.getMilkTypes().forEach(item -> {
                DockMilkType dockMilkType = new DockMilkType();
                dockMilkType.setCode(dock.getDockNo() + item.getCode());
                dockMilkType.setDock(dock);
                dockMilkType.setMilkType(item);
                dockMilkType.setInitData();
                dockMilkTypeRepository.customSave(dockMilkType, identityInfo);
            });
        } catch (Exception e) {
            LOGGER.error("Dock update ex", e);
            return null;
        }
        return DockMilkTypeDto;
    }

    @Override
    public Dock update(Dock dock) {
        return dockRepository.save(dock);
    }

    @Override
    public Optional<Dock> findById(String dockNo) {
        return dockRepository.findById(dockNo);
    }

    @Override
    public void delete(String dockNo, String identityInfo) {
        dockRepository.customDelete(dockRepository.findById(dockNo).get(), identityInfo);
    }

    @Override
    @Transactional
    public void delete(Dock dock, String identityInfo) {
//		dockMilkTypeRepository.deleteByDock(dock);
        List<DockMilkType> list = dockMilkTypeRepository.findAllByDock(dock);
        if (list != null) {
            list.forEach(item -> {
                dockMilkTypeRepository.customDelete(item, identityInfo);
            });
        }
        dockRepository.customDelete(dock.getDockNo(), identityInfo);
    }
}
