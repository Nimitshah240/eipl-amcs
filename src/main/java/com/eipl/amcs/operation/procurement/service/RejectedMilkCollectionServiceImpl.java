package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.repository.RejectedMilkCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RejectedMilkCollectionServiceImpl implements RejectedMilkCollectionService {

    @Autowired
    private RejectedMilkCollectionRepository repository;
    
    @Autowired
    private NextCodeService nextCodeService;

    @Override
    public List<RejectedMilkCollection> findAll() {
        return repository.findAll(Sort.by("date"));
    }

    @Override
    public RejectedMilkCollection save(RejectedMilkCollection rejectedMilkCollection) {
        if (rejectedMilkCollection.getMilkCollectionRejectedCode() == null || rejectedMilkCollection.getMilkCollectionRejectedCode().isEmpty()) {
            String societyCode = rejectedMilkCollection.getDock().getSociety().getCode();
            String nextCode = nextCodeService.getNextCode("RejectedMilkCollection", "milkCollectionRejectedCode", societyCode, 0);
            rejectedMilkCollection.setMilkCollectionRejectedCode(nextCode);
        }
        return repository.save(rejectedMilkCollection);
    }

    @Override
    public RejectedMilkCollection update(RejectedMilkCollection rejectedMilkCollection) {
        return repository.save(rejectedMilkCollection);
    }

    @Override
    public Optional<RejectedMilkCollection> findById(String code) {
        return repository.findById(code);
    }

    @Override
    public void delete(RejectedMilkCollection rejectedMilkCollection) {
        repository.delete(rejectedMilkCollection);
    }
}
