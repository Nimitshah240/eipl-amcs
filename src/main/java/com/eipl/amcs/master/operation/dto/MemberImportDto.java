package com.eipl.amcs.master.operation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberImportDto {
    private String code;
    private String operation;
    private String status;
}
