package com.eipl.amcs.operation.administartion.dto;
import com.eipl.amcs.base.model.BaseModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffSalary extends BaseModel {

    private String Code;

    private LocalDate wefDate;

    private int effectiveWorkingDays;

   private LocalDate month;

    private int typeOfHead;

   private double value;
   private String voucherNo;

    private String societyCode;

    private String staffMemberCode;

    private String subCenterCode;

    private String unionCode;

    private boolean is_active;

    private LocalDate created_at;

    private String created_by;

    private LocalDate updated_at;


}
