package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dock_audit")
public class DockAudit extends BaseModelAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dockNo;
    private String unionCode;
    private Short isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;

    @Override
    public String getTableName() {
        return "dock_audit";
    }
}
