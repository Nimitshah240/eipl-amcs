package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Ledger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface LedgerRepository extends BaseRepository<Ledger, String> {

    @Override
    @EntityGraph(attributePaths = {"ledgerGroup", "society"})
    List<Ledger> findAll(Sort sort);

    @EntityGraph(attributePaths = {"ledgerGroup", "society", "ledgerGroup.ledgerType"})
    List<Ledger> findAllByActive(Boolean isActive);

    @Query(nativeQuery = true, value = "select * from ledgers order by cast(code AS DECIMAL) ")
    List<Ledger> findAllByCode();

    @Override
    @EntityGraph(attributePaths = {"ledgerGroup", "society"})
    Optional<Ledger> findById(String code);

    @Query(value = "CALL sp_current_stock_for_product(:p_as_on_date,:p_society_code,:p_locale);", nativeQuery = true)
    List<Object[]> fetchCurrentStockByProduct(@Param("p_as_on_date") Date p_as_on_date,
                                              @Param("p_society_code") String p_society_code, @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_current_stock_for_product_with_product(:p_as_on_date,:p_society_code,:p_locale,:p_product_code);", nativeQuery = true)
    List<Object[]> fetchCurrentStockByProductWithProduct(@Param("p_as_on_date") Date p_as_on_date,
                                                         @Param("p_society_code") String p_society_code, @Param("p_locale") String p_locale, @Param("p_product_code") String p_product_code
    );

    @Query(value = "CALL sp_product_sale(:p_from_date,:p_to_date,:p_society_code,:p_locale,:p_product_code);", nativeQuery = true)
    List<Map<String, Object>> fetchSaleProduct(
            @Param("p_from_date") Date p_from_date,
            @Param("p_to_date") Date p_to_date,
            @Param("p_society_code") String p_society_code, @Param("p_locale") String p_locale, @Param("p_product_code") String p_product_code
    );

    @Query(value = "CALL sp_product_purchase(:p_from_date,:p_to_date,:p_society_code,:p_locale,:p_product_code);", nativeQuery = true)
    List<Map<String, Object>> fetchPurchaseProduct(
            @Param("p_from_date") Date p_from_date,
            @Param("p_to_date") Date p_to_date,
            @Param("p_society_code") String p_society_code, @Param("p_locale") String p_locale, @Param("p_product_code") String p_product_code
    );

    @Query(value = "CALL sp_product_receipt_by_desc(:p_product_code,:p_as_on_date);", nativeQuery = true)
    List<Object[]> fetchProductReceipt(@Param("p_product_code") String p_product_code,
                                       @Param("p_as_on_date") Date p_as_on_date
    );


    @Query(value = "CALL sp_rpt_accounting_trading(:p_society_code,:p_from_date,:p_to_date,:p_locale);", nativeQuery = true)
    List<Object[]> fetchTrading(@Param("p_society_code") String p_society_code,
                                @Param("p_from_date") Date p_from_date, @Param("p_to_date") Date p_to_date,
                                @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_rpt_accounting_profit_loss(:p_society_code,:p_from_date,:p_to_date,:p_income_expense,:p_locale);", nativeQuery = true)
    List<Object[]> fetchProfitLoss(@Param("p_society_code") String p_society_code,
                                   @Param("p_from_date") Date p_from_date, @Param("p_to_date") Date p_to_date, @Param("p_income_expense") int p_income_expense,
                                   @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_rpt_accounting_balance_sheet(:p_society_code,:p_from_date,:p_to_date,:p_liability_asset,:p_locale);", nativeQuery = true)
    List<Object[]> fetchBalanceSheet(@Param("p_society_code") String p_society_code,
                                     @Param("p_from_date") Date p_from_date, @Param("p_to_date") Date p_to_date, @Param("p_liability_asset") int p_liability_asset,
                                     @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_accounting_opening_balance(:p_society_code,:p_from_date,:p_to_date,:p_locale);", nativeQuery = true)
    List<Object[]> fetchLedgerClosing(@Param("p_society_code") String p_society_code,
                                      @Param("p_from_date") Date p_from_date, @Param("p_to_date") Date p_to_date,
                                      @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_accounting_subledger_ledger_opening_balance(:p_ledger_code,:p_from_date,:p_to_date,:p_locale);", nativeQuery = true)
    List<Object[]> fetchSubLedgerOpeningBalance(@Param("p_ledger_code") String p_ledger_code,
                                                @Param("p_from_date") Date p_from_date, @Param("p_to_date") Date p_to_date,
                                                @Param("p_locale") String p_locale
    );

    @Query(value = "CALL sp_accounting_sub_ledger_opening_balance(:p_society_code,:p_from_date,:p_to_date,:p_locale);", nativeQuery = true)
    List<Object[]> fetchSubLedgerOpeningBalanceSecond(@Param("p_society_code") String p_society_code,
                                                      @Param("p_from_date") LocalDate p_from_date, @Param("p_to_date") LocalDate p_to_date,
                                                      @Param("p_locale") String p_locale
    );

}
