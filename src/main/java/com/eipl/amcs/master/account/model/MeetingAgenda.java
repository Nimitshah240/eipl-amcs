package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "meeting_agenda")
public class MeetingAgenda extends BaseModelTxn {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Size(max = 10)
	private String code;
	@Size(max = 3000)
	private String detailedAgenda;
	@Size(max = 15)
	private String meetingTime;
	@Size(max = 500)
	private String subjectLine;
	private LocalDate date;
	private LocalDate meetingDate;
	private  short meetingType;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_meeting_agenda_society_code"))
	@JsonIgnoreProperties(value = {"bank", "branch","union","plant","mcc","bmc","route","state","district","subDistrict","village","hamlet"})
	private Society society;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_meeting_agenda_union_code"))
	@JsonIgnoreProperties(value = {"bank", "branch","state","district","subDistrict","village","hamlet"})
	private Union union;

	@Override
	public String getTableName() {
		return "meeting_agenda";
	}
}
