package com.eipl.amcs.operation.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.procurement.model.AllowDcsManualCollectionRange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AllowDcsManualCollectionRangeRepository extends BaseRepository<AllowDcsManualCollectionRange, Long> {


    List<AllowDcsManualCollectionRange> findAllBySociety(String societyCode);


    @Query("SELECT adr FROM AllowDcsManualCollectionRange adr " +
            "WHERE adr.fromDate <= :fromDate " +
            "AND adr.toDate >= :toDate")
    List<AllowDcsManualCollectionRange> findByFromDateLessThanEqualAndToDateGreaterThanEqual(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT adr FROM AllowDcsManualCollectionRange adr " +
            "WHERE adr.fromDate <= :fromDate " +
            "AND adr.toDate >= :toDate " +
            "AND adr.xCol1 = :type")
    List<AllowDcsManualCollectionRange> findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, @Param("type") String type);

    @Query("SELECT adr FROM AllowDcsManualCollectionRange adr " +
            "WHERE adr.fromDate <= :fromDate " +
            "AND adr.toDate >= :toDate " +
            "AND adr.weightManual = :weightManual " +
            "AND adr.qualityManual = :qualityManual " +
            "AND adr.status <= :status " +
            "AND adr.xCol1 = :type")
    List<AllowDcsManualCollectionRange> findByFromDateLessThanEqualAndToDateGreaterThanEqualAndxCol1AndWeightManualAndQualityManualAndFromShiftAndToShiftAndStatus(LocalDateTime fromDate, LocalDateTime toDate, String type, Boolean qualityManual, Boolean weightManual, Integer status);

    @Query("SELECT adr FROM AllowDcsManualCollectionRange adr " +
            "WHERE :fromDate BETWEEN adr.fromDate AND adr.toDate " +
            "and :toDate BETWEEN adr.fromDate AND adr.toDate " +
            "AND adr.weightManual = :weightManual " +
            "AND adr.qualityManual = :qualityManual " +
            "AND adr.status <= :status " +
            "AND adr.xCol1 = :type")
    List<AllowDcsManualCollectionRange> findByFromDateBetweenAndToDateBetweenAndxCol1AndWeightManualAndQualityManualAndStatus(LocalDateTime fromDate, LocalDateTime toDate, String type, Boolean qualityManual, Boolean weightManual, Integer status);
}
