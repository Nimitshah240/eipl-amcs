package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MilkReceiptRepository extends BaseRepository<MilkReceipt, String> {

    @Override
    @EntityGraph(attributePaths = {"milkDispatch", "fromShift", "toShift", "society", "union"})
    List<MilkReceipt> findAll();

    @Override
    @EntityGraph(attributePaths = {"milkDispatch", "fromShift", "toShift", "society", "union"})
    Optional<MilkReceipt> findById(String id);

    Optional<MilkReceipt> findBySocietyAndFromDateAndFromShift(Society society, LocalDateTime prodate, Shift shift);

    @Query(nativeQuery = true, value = "select mr.* from milk_Receipt mr join milk_receipt_transaction mrt on mr.code = mrt.milk_receipt_code where \n" +
            "mrt.milk_type_code=3 and mrt.milk_quality_type_code!=3 and mr.from_date<?1 order by mr.from_date desc limit 1;\n")
    Optional<MilkReceipt> findPreviousRecordOfGoodMilkType(LocalDateTime fromDate);
}