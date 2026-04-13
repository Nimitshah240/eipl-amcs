package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherRaw;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRawRepository extends BaseRepository<VoucherRaw, String> {


    @Query("SELECT v FROM VoucherRaw v WHERE v.xCol5 = :val")
//    @EntityGraph(attributePaths = {"voucherTransactionRawList", "voucherSubLedgerRawList"})
    List<VoucherRaw> findByXCol5(@Param("val") String val);

//    @Query("SELECT v FROM VoucherRaw v WHERE v.process_reference = :val")
    List<VoucherRaw> findByProcessReference(@Param("val") String val);
}
