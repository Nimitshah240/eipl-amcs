package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherTransactionRepository extends BaseRepository<VoucherTransaction, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"ledger", "voucher"})
    Optional<VoucherTransaction> findById(String integer);

    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findByVoucher(Voucher voucher);

    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findByVoucherInAndLedgerIn(List<Voucher> voucherList, List<Ledger> ledgers);

    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findByVoucherInAndAutoPostedScreen(List<Voucher> voucherList, Boolean autoPostedScreen);
//
//    @Query(
//            "SELECT new com.eipl.amcs.master.account.dto.ProductSaleTransactionDto( " +
//                    "vt, " +
//                    "p, " +
//                    "vt.ledger.code, " +
//                    "vt.ledger.name, " +
//                    "pt.rate, " +
//                    "pt.quantity, " +
//                    "p.code, " +
//                    "p.name ) " +
//                    "FROM Voucher v " +
//                    "JOIN VoucherTransaction vt ON vt.voucher = v " +
//                    "JOIN ProductSale s ON s.invoiceNo = v.billNo " +
//                    "JOIN ProductSaleTransaction pt ON pt.productSale = s " +
//                    "JOIN pt.product p " +
//                    "WHERE v.processName = 'tbl_product_sale' " +
//                    "AND v.voucherDate = :voucherDate"
//    )
//    List<ProductSaleTransactionDto> findProductSaleTransactions(
//            @Param("voucherDate") LocalDate voucherDate
//    );
}
