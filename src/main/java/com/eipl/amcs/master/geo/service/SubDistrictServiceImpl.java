package com.eipl.amcs.master.geo.service;

import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.repository.DistrictRepository;
import com.eipl.amcs.master.geo.repository.SubDistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubDistrictServiceImpl implements SubDistrictService {

    @Autowired
    private SubDistrictRepository subDistrictRepository;
    @Autowired
    private DistrictRepository districtRepository;


    @Override
    public List<SubDistrict> findAll() {
        return subDistrictRepository.findAll(Sort.by("name"));
    }

    @Override
    public List<SubDistrict> findAll(String districtCode) {
        District district = districtRepository.findById(districtCode)
                .orElseThrow(() -> new EntityNotFoundException(District.class, "invalid.districtcode"));
        return subDistrictRepository.findByDistrict(district, Sort.by("name"));
    }

}
