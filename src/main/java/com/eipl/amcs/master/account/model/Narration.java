package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tbl_narration")
@Getter
@Setter
public class Narration extends BaseModel {

    @Id
    @Column(name = "narration_code", length = 25)
    private String narrationCode;

    @Lob
    @Column(name = "narration", nullable = false, columnDefinition = "LONGTEXT")
    private String narration;

    @Lob
    @Column(name = "narration_local", columnDefinition = "LONGTEXT")
    private String narrationLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dcs_code", referencedColumnName = "code")
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "narration_type_code")
    private NarrationType narrationType;

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.narration, this.narrationLocal);
    }
}