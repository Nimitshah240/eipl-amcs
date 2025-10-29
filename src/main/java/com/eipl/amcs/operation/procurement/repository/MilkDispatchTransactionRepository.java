package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MilkDispatchTransactionRepository extends BaseRepository<MilkDispatchTransaction, String> {

    @EntityGraph(attributePaths = {"milkDispatch", "milkQualityType", "milkType"})
    List<MilkDispatchTransaction> findByMilkDispatch(MilkDispatch milkDispatch);

    @EntityGraph(attributePaths = {"milkDispatch", "milkQualityType", "milkType"})
    void deleteByMilkDispatch(MilkDispatch challanNo);

    @Override
    @EntityGraph(attributePaths = {"milkDispatch", "milkQualityType", "milkType"})
    Optional<MilkDispatchTransaction> findById(String id);

}
