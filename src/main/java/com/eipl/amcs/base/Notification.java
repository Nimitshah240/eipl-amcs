package com.eipl.amcs.base;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification")
public class Notification extends BaseModelTxn {

	@Id
	private Integer bulkNotificationId;
	private String societyCode;
	private String unionCode;
	private String mccPlantCode;
	private String bmcCode;
	private String plantCode;
	private String memberCode;
	private String appType;
	private String loginType;
	private LocalDateTime wefDate;
	private String title;
	private String message;
	private String campaignName;
	private Integer receiverType;
	private String contentId;
	private Integer status;
	private LocalDateTime entry;
	private LocalDateTime pickup;
	private LocalDateTime response;
	private String originatingOrgCode;
	private String originatingOrgType;
	private Integer originatingType;
	private LocalDateTime fromDate;		
	private LocalDateTime toDate;
	private Integer fromShift;
	private Integer toShift;
	private Integer NotificationType;
	private String fileName;
	private String filePath;

	@Override
	public String getTableName() {
		return "notification";
	}
}
