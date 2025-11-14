package com.eipl.amcs.base.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JarUpdate {
    private Long jarUpdateCode;
    private String appVersion;
    private Integer appVersionCode;
    private String updateNote;
    private String resourcePath;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Integer skipCount;
    private String societyCode;
    private Boolean isJarAvailable;

    public JarUpdate(String appVersion, String societyCode) {
        this.appVersion = appVersion;
        this.societyCode = societyCode;
    }
}