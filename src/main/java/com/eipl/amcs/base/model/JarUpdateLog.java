package com.eipl.amcs.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_jar_update_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JarUpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jar_update_log_code", nullable = false)
    private Long jarUpdateLogCode;

    @Column(name = "dcs_code")
    private String dcsCode;

    @Column(name = "version_no")
    private String versionNo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    public JarUpdateLog(String societyCode, String versionNo) {
        this.dcsCode = societyCode;
        this.versionNo = versionNo;
        this.createdAt = LocalDateTime.now();
        this.createdBy = societyCode;
    }
}