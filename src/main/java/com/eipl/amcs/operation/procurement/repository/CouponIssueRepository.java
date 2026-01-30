package com.eipl.amcs.operation.procurement.repository;


import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CouponIssueRepository extends BaseRepository<CouponIssue, String> {


        @Override
        @EntityGraph(attributePaths = {"society", "milkType", "union"})
        List<CouponIssue> findAll(Sort sort);

        CouponIssue findTopByConsumerCodeAndConsumerTypeAndIsDeleteFalseAndCodeNotOrderByIssueDateDesc(
                String consumerCode,
                int consumerType,
                String couponIssueNo
        );

        @EntityGraph(attributePaths = {"union", "society", "milkType"})
        CouponIssue findByCode(String couponIssueNo);


        @Modifying
        @Transactional
        @Query("DELETE FROM CouponIssue sd WHERE sd.code = :couponIssueNo")
        void deleteByCode(@Param("couponIssueNo") String code);
}
