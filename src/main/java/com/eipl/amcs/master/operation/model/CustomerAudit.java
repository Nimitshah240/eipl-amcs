package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "customers_audit")
public class CustomerAudit extends BaseModelAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 15)
	private String code;
	@Size(max = 200)
	private String name;
	@Size(max = 15)
	private String nameLocal;
	@Digits(integer = 10, fraction = 2)
	private BigDecimal creditLimit;
	@Size(max = 255)
	private String mobileNo;
	private Integer paymentMode;
	private LocalDate registrationDate;
	@Size(max = 255)
	private String registrationNo;
	private Integer type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Society society;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Union union;

	@Override
	public String getTableName() {
		return "customers_audit";
	}

}
