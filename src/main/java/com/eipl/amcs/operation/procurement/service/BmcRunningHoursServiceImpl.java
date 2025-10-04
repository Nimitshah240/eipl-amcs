package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.repository.BmcRunningHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.config.BeanConfig.bmcRunningHoursRepository;
import static com.eipl.amcs.config.BeanConfig.nextCodeService;

@Service
public class BmcRunningHoursServiceImpl implements BmcRunningHoursService {
//    @Autowired
//    private NextCodeService nextCodeService;
//    private final BmcRunningHoursRepository bmcRunningHoursRepository;

//    @Autowired
//    public BmcRunningHoursServiceImpl(BmcRunningHoursRepository repository) {
//        this.bmcRunningHoursRepository = bmcRunningHoursRepository;
//    }

    @Override
    public List<BmcRunningHours> getAllBmcRunningHours() {
        return bmcRunningHoursRepository.findAll();
    }

    @Override
    public List<BmcRunningHours> findAllBetween(LocalDate date, LocalDate date1) {
        return bmcRunningHoursRepository.findAll();
    }

    @Override
    public BmcRunningHours getBmcRunningHoursByCode(Long code) {
        Optional<BmcRunningHours> optional = bmcRunningHoursRepository.findById(code);
        return optional.orElse(null);
    }

    @Override
    public BmcRunningHours createBmcRunningHours(BmcRunningHours bmcRunningHours) {
        return bmcRunningHoursRepository.save(bmcRunningHours);
    }

    @Override
    public BmcRunningHours update(BmcRunningHours bmcRunningHours) {
        bmcRunningHours.setupdateData();
        return bmcRunningHoursRepository.save(bmcRunningHours);
    }

    @Override
    public void deleteBmcRunningHours(Long code) {
        bmcRunningHoursRepository.deleteById(code);
    }


    @Override
    public BmcRunningHours save(BmcRunningHours bmcRunningHours) {
        String txnCode = nextCodeService.getNextCode("BmcRunningHours", "code", bmcRunningHours.getSocietyCode(), 4);
        bmcRunningHours.setCode(Long.valueOf((txnCode)));
        return bmcRunningHoursRepository.save(bmcRunningHours);
    }
}


