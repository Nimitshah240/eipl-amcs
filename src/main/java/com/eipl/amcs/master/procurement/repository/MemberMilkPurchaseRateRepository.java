package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMilkPurchaseRateRepository extends JpaRepository<MemberMilkPurchaseRate, String> {

    @Override
    @EntityGraph(attributePaths = {"shift", "shiftApplicable", "society", "rateType"})
    List<MemberMilkPurchaseRate> findAll(Sort sort);

    @EntityGraph(attributePaths = {"shift", "shiftApplicable", "society", "rateType"})
    Optional<MemberMilkPurchaseRate> findTop1BySocietyAndWefDateGreaterThanOrderByWefDateDesc(Society society, LocalDateTime wefDate);

    @EntityGraph(attributePaths = {"shift", "shiftApplicable", "society", "rateType"})
    Optional<MemberMilkPurchaseRate> findTop1BySocietyAndWefDateGreaterThanEqualOrderByWefDateDesc(Society society, LocalDateTime wefDate);

    @Override
    @EntityGraph(attributePaths = {"shift", "shiftApplicable", "society", "rateType"})
    Optional<MemberMilkPurchaseRate> findById(String code);
}
