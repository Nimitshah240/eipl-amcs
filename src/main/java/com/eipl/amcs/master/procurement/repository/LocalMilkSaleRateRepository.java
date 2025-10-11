package com.eipl.amcs.master.procurement.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocalMilkSaleRateRepository extends BaseRepository<LocalMilkSaleRate, String> {

    @Override
    @EntityGraph(attributePaths = {"milkType", "milkClass", "society"})
    Optional<LocalMilkSaleRate> findById(String id);

    @Override
    @EntityGraph(attributePaths = {"milkType", "milkClass", "society"})
    List<LocalMilkSaleRate> findAll(Sort sort);

    @Query(value = "SELECT wefDate FROM LocalMilkSaleRate lsr WHERE lsr.society = ?1 AND lsr.milkType = ?2 AND lsr.milkClass = ?3 ORDER BY lsr.wefDate desc")
    LocalDate fetchLatestDate(String str1, Integer i1, Integer i2);

    @EntityGraph(attributePaths = {"milkType", "milkClass", "society"})
    Optional<LocalMilkSaleRate> findTop1BySocietyAndMilkTypeAndMilkClassAndWefDateGreaterThan(Society society,
                                                                                              MilkType milktype, MilkClass milkClass, LocalDate wefDate);

    @EntityGraph(attributePaths = {"milkType", "milkClass", "society"})
    LocalMilkSaleRate findTop1RateByWefDateLessThanEqualAndMilkTypeAndMilkClassOrderByWefDate(LocalDate date,
                                                                                              MilkType milkType, MilkClass milkClass);
}