package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MilkReceiptTransactionRepository extends BaseRepository<MilkReceiptTransaction, String> {

    @EntityGraph(attributePaths = {"milkReceipt", "milkQualityType", "milkType"})
    List<MilkReceiptTransaction> findByMilkReceipt(MilkReceipt milkReceipt);

    @Override
    @EntityGraph(attributePaths = {"milkReceipt", "milkQualityType", "milkType"})
    Optional<MilkReceiptTransaction> findById(String id);

    Optional<MilkReceiptTransaction> findByMilkReceiptAndMilkTypeAndMilkQualityTypeAndQty(MilkReceipt milkReceipt, MilkType mtype, MilkQualityType mcat, BigDecimal weight);
}
