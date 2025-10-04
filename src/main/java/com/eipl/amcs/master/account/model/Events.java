package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor @Table(name = "event_master")
public class Events extends BaseModel {

	@Id
	private Integer code;
	@Size(max = 200)
	private String eventName;
	private Integer eventCode;
	private String description;
	private Boolean ledgerDebit;
	private Boolean ledgerCredit;
	private Boolean subLedgerCredit;
	private Boolean subLedgerDebit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_event_society_code"))
	@JsonIgnoreProperties(value = {"bank", "branch","union","plant","mcc","bmc","route","state","district","subDistrict","village","hamlet"})
	private Society society;
	private String unionCode;

	@Override
	public String getTableName() {
		return "event";
	}


	@Override
	public Object getId() {
		return this.getCode();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		EventsAudit audit = new EventsAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setCode(this.getCode());
		audit.setLedgerCredit(this.getLedgerCredit());
		audit.setLedgerDebit(this.getLedgerDebit());
		audit.setEventName(this.getEventName());
		audit.setDescription(this.getDescription());
		audit.setSociety(this.getSociety());
		audit.setUnionCode(this.getUnionCode());
		audit.setSubLedgerCredit(this.getSubLedgerCredit());
		audit.setSubLedgerDebit(this.getSubLedgerDebit());

		audit.setCreatedAt(this.getCreatedAt());
		audit.setCreatedBy(this.getCreatedBy());
		audit.setUpdatedAt(this.getUpdatedAt());
		audit.setUpdatedBy(this.getUpdatedBy());
		audit.setActive(this.isActive());
		audit.setXCol1(this.getXCol1());
		audit.setXCol2(this.getXCol2());
		audit.setXCol3(this.getXCol3());

		return audit;
	}
}
