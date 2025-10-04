package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductSaleInstallmentRepository extends BaseRepository<ProductSaleInstallment, String> {

	@Override
	@EntityGraph(attributePaths = { "societyPaymentCycle", "member" })
	Optional<ProductSaleInstallment> findById(String id);

	@Override
	@EntityGraph(attributePaths = { "societyPaymentCycle", "member" })
	List<ProductSaleInstallment> findAll(Sort sort);

	@Query("SELECT psm FROM ProductSaleInstallment psm WHERE psm.invoiceNo = ?1 AND psm.billing = ?2")
	List<ProductSaleInstallment> fetchInstallmentIsBilled(String str, boolean b);

	@EntityGraph(attributePaths = { "societyPaymentCycle", "member" })
	List<ProductSaleInstallment> findByMemberAndBillingFalse(Member member);

	@EntityGraph(attributePaths = { "societyPaymentCycle", "member" })
	List<ProductSaleInstallment> findByMemberAndBillingFalseAndType(Member member,Integer type);

	void deleteByInvoiceNo(String invoiceNo);

	@EntityGraph(attributePaths = { "societyPaymentCycle", "member" })
	List<ProductSaleInstallment> findByInvoiceNo(String invoiceNo);

	@EntityGraph(attributePaths = {"member"})
	List<ProductSaleInstallment> findBySocietyPaymentCycle(SocietyPaymentCycle spc);

}
