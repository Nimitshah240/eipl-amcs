package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MilkDispatchRepository extends BaseRepository<MilkDispatch, String> {

    @Override
    @EntityGraph(attributePaths = {"fromShift", "toShift", "society", "union"})
    List<MilkDispatch> findAll();

    @Override
    @EntityGraph(attributePaths = {"fromShift", "toShift", "society", "union"})
    List<MilkDispatch> findAll(Sort sort);

    @EntityGraph(attributePaths = {"fromShift", "toShift", "society", "union"})
    List<MilkDispatch> findByFromDateGreaterThanEqualAndToDateLessThanEqual(LocalDateTime fd, LocalDateTime td);

    @Override
    @EntityGraph(attributePaths = {"fromShift", "toShift", "society", "union"})
    Optional<MilkDispatch> findById(String id);

    @Query(nativeQuery = true, value = "select mr.* from milk_dispatch mr join milk_dispatch_transaction mrt on mr.challan_no = mrt.challan_no where \n" +
            "mrt.milk_type_code=3 and mrt.milk_quality_type_code!=3 and mr.from_date<?1 order by mr.from_date desc limit 1;\n")
    Optional<MilkDispatch> findPreviousRecordOfGoodMilkType(LocalDateTime fromDate);

    boolean existsByFromDateAndFromShift(LocalDateTime fromDate, Shift fromShift);

    boolean existsByFromDateAndFromShiftAndChallanNoNot(LocalDateTime fromDate, Shift fromShift, String challanNo);
}