package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "voucher_audit")
public class VoucherAudit extends BaseModelTxnAudit {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	private String code;
	private Boolean autoPosted;
	private Boolean cancelled;
	private LocalDate billDate;
	private LocalDate voucherDate;
	private String billNo;
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code",  foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Society society;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucher_type_code",  foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private VoucherType voucherType;
	private String unionCode;
	private String dockCode;
	private String financialYearsCode;

	@Transient
	@JsonIgnore
	private List<VoucherTransaction> voucherTransactions;

	@Override
	public String getTableName() {
		return "voucher_audit";
	}


}
