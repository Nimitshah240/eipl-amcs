package com.eipl.amcs.operation.procurement.repository;


import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
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
        @EntityGraph(attributePaths = {"society", "milkClass", "milkType", "union"})
        List<CouponIssue> findAll();

        CouponIssue findTopByConsumerCodeAndConsumerTypeAndIsDeleteFalseAndCouponIssueNoNotOrderByIssueDateDesc(
                String consumerCode,
                int consumerType,
                String couponIssueNo
        );

        @EntityGraph(attributePaths = {"union", "society", "milkType", "milkClass"})
        CouponIssue findByCouponIssueNo(String couponIssueNo);

        @Query("SELECT SUM(cc.amount) FROM CouponIssue cc " +
                "WHERE cc.isDelete = :isDelete " +
                "AND (cc.consumerType = :type1 OR cc.consumerType = :type2) " +
                "AND cc.consumerCode = :code " +
                "AND cc.issueDate <= :date " +
                "AND cc.milkType = :milkType " +
                "AND cc.milkClass = :milkClass " +
                "AND cc.issueDate BETWEEN :fromDate AND :toDate " +
                "AND cc.couponIssueNo != :couponIssueNo")
        Double sumAmountByMemberExceptCurrent(
                @Param("type1") int type1,
                @Param("type2") int type2,
                @Param("code") String code,
                @Param("date") LocalDate date,
                @Param("isDelete") boolean isDelete,
                @Param("milkType") MilkType milkType,
                @Param("milkClass") MilkClass milkClass,
                @Param("fromDate") LocalDate fromDate,
                @Param("toDate") LocalDate toDate,
                @Param("couponIssueNo") String couponIssueNo
        );

        @Modifying
        @Transactional
        @Query("DELETE FROM CouponIssue sd WHERE sd.couponIssueNo = :couponIssueNo")
        void deleteByCouponIssueNo(@Param("couponIssueNo") String couponIssueNo);
}
