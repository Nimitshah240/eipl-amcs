package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.operation.repository.VendorRepository;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private SocietyRepository socRepository;

    @Autowired
    private VendorRepository repository;


    @Override
    public List<Vendor> findAllBySociety(String societyCode) {
        Society society = socRepository.findById(societyCode)
                .orElseThrow(() -> new EntityNotFoundException(Society.class, "societycode", "invalid.society"));
        return repository.findAllBySociety(society, Sort.by("code"));
    }
}
