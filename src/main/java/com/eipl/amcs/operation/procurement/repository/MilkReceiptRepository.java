package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.report.dto.DairySaleRegister;
import com.eipl.amcs.report.dto.MilkDispatchChallan;
import com.eipl.amcs.report.dto.SocietyPurchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "CALL rpt_milk_dispatch_challan(:p_society_code, :p_challan_no);", nativeQuery = true)
    List<MilkDispatchChallan> findDispatchChallanReport(@Param("p_society_code") String societyCode, @Param("p_challan_no") String challanno);

    @Query(value = "CALL rpt_dairy_sale_register(:p_society_code,:p_from_date ,:p_to_date , :p_milk_type );", nativeQuery = true)
    List<DairySaleRegister> findDairySaleRegisterData(@Param("p_society_code") String societyCode,
                                                      @Param("p_from_date") LocalDateTime fromDate,
                                                      @Param("p_to_date") LocalDateTime toDate,
                                                      @Param("p_milk_type") Integer milkType);


    @Query(value = "CALL rpt_society_purchase(:p_from_date ,:p_to_date,:p_society_code);", nativeQuery = true)
    List<SocietyPurchase> fndSocietyPurchase(@Param("p_society_code") String societyCode,
                                             @Param("p_from_date") LocalDateTime fromDate,
                                             @Param("p_to_date") LocalDateTime toDate);

    Optional<MilkReceipt> findBySocietyAndFromDateAndFromShift(Society society, LocalDateTime prodate, Shift shift);

    @Query(nativeQuery = true, value = "select mr.* from milk_Receipt mr join milk_receipt_transaction mrt on mr.code = mrt.milk_receipt_code where \n" +
            "mrt.milk_type_code=3 and mrt.milk_quality_type_code!=3 and mr.from_date<?1 order by mr.from_date desc limit 1;\n")
    Optional<MilkReceipt> findPreviousRecordOfGoodMilkType(LocalDateTime fromDate);
}