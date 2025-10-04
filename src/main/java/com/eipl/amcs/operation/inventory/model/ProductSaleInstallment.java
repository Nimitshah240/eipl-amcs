package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_sale_installment")
public class ProductSaleInstallment extends BaseModelTxn {
	@Id
	@Size(max = 40)
	private String code;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal actualInstallment;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal installmentAmount;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal previousPendingAmount;
	@Column(name = "is_billing")
	private Boolean billing;
	@Size(max = 35)
	private String invoiceNo;
	private LocalDate deductionDate;
	@Size(max = 10)
	private String unionCode;
	@Size(max = 10)
	private String societyCode;
	private Integer type; // 1-product, 2-service, 3- cash adv, 4-farmer bill head

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_product_sale_member_installment_society_payment_cycle_code"))
	@JsonIgnoreProperties(value = {"society","fromShift","toShift","milkType"})
	private SocietyPaymentCycle societyPaymentCycle;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_product_sale_member_installment_member_code"))
	@JsonIgnoreProperties(value = {"memberType","society","milkType"})
	private Member member;

	@Override
	public String getTableName() {
		return "product_sale_installment";
	}

	@Override
	public Object getId() {
		return this.getCode();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		ProductSaleInstallmentAudit audit = new ProductSaleInstallmentAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setCode(this.getCode());
		audit.setActualInstallment(this.getActualInstallment());
		audit.setInstallmentAmount(this.getInstallmentAmount());
		audit.setPreviousPendingAmount(this.getPreviousPendingAmount());
		audit.setBilling(this.getBilling());
		audit.setInvoiceNo(this.getInvoiceNo());
		audit.setDeductionDate(this.getDeductionDate());
		audit.setUnionCode(this.getUnionCode());
		audit.setSocietyCode(this.getSocietyCode());
		audit.setType(this.getType());
		audit.setSocietyPaymentCycle(this.getSocietyPaymentCycle());
		audit.setMember(this.getMember());

		audit.setCreatedAt(this.getCreatedAt());
		audit.setCreatedBy(this.getCreatedBy());
		audit.setUpdatedAt(this.getUpdatedAt());
		audit.setUpdatedBy(this.getUpdatedBy());
		audit.setXCol1(this.getXCol1());
		audit.setXCol2(this.getXCol2());
		audit.setXCol3(this.getXCol3());

		return audit;
	}
}
