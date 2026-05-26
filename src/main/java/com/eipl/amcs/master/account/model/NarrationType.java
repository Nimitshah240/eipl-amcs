package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_narration_type")
@Getter
@Setter
public class NarrationType implements JsonAndTableBuilder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "narration_type_code")
    private Integer narrationTypeCode;

    @Column(name = "narration_type", nullable = false)
    private String narrationType;

    @Column(name = "narration_type_local")
    private String narrationTypeLocal;

    @OneToMany(mappedBy = "narrationType", fetch = FetchType.LAZY)
    private List<Narration> narrations;

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.narrationType, this.narrationTypeLocal);
    }
}