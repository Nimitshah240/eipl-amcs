package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cash_advance")
public class CashAdvance extends BaseModelTxn {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String code;
	private BigDecimal amount;
	private Integer noOfInstallment;
	private LocalDate date;
	private LocalDate installmentDate;
	private String unionCode;
	private String voucherNo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_cash_advance_society_code"))
	@JsonIgnoreProperties(value = {"bank", "branch","union","plant","mcc","bmc","route","state","district","subDistrict","village","hamlet"})
	private Society society;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_cash_advance_society_payment_cycle_code"))
	@JsonIgnoreProperties(value = {"society","fromShift","toShift","milkType"})
	private SocietyPaymentCycle societyPaymentCycle;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_cash_advance_member_code"))
	@JsonIgnoreProperties(value = {"memberType","society","milkType"})
	private Member member;

	@Override
	public String getTableName() {
		return "cash_advance";
	}
}
