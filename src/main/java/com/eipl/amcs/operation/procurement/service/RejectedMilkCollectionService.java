package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RejectedMilkCollectionService {
    List<RejectedMilkCollection> findAll();

    RejectedMilkCollection save(RejectedMilkCollection rejectedMilkCollection);

    RejectedMilkCollection update(RejectedMilkCollection rejectedMilkCollection);

    Optional<RejectedMilkCollection> findById(String code);

    void delete(RejectedMilkCollection rejectedMilkCollection);

    List<RejectedMilkCollection> findAllByFilter(LocalDateTime fromDate, LocalDateTime toDate);
}
