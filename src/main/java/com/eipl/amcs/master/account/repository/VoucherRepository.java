package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Voucher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends BaseRepository<Voucher, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "voucherType"})
    List<Voucher> findAll(Sort sort);


    @Override
    @EntityGraph(attributePaths = {"society", "voucherType"})
    Optional<Voucher> findById(String voucherCode);


    @Query("SELECT v FROM Voucher v WHERE v.xCol5 = :val")
    List<Voucher> findByXCol5(@Param("val") String val);

}
