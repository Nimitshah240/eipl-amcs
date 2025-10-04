package com.eipl.amcs.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Permission {
    private Integer code;
    private String name;
    private String type;
    private String description;
    private Integer parentCode;
    private String module;
    private Integer object;
    private String unionCode;
}
